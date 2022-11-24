package ctwedge.importer.featureide;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.xtext.EcoreUtil2;

import ctwedge.ctWedge.AtomicPredicate;
import ctwedge.ctWedge.Bool;
import ctwedge.ctWedge.CitModel;
import ctwedge.ctWedge.CtWedgeFactory;
import ctwedge.ctWedge.EqualExpression;
import ctwedge.ctWedge.Expression;
import ctwedge.ctWedge.ImpliesExpression;
import ctwedge.ctWedge.ImpliesOperator;
import ctwedge.ctWedge.Operators;
import ctwedge.ctWedge.OrExpression;
import ctwedge.ctWedge.Parameter;
import ctwedge.ctWedge.impl.CtWedgePackageImpl;
import ctwedge.util.Pair;
import ctwedge.util.ext.NotImportableException;
import de.ovgu.featureide.fm.core.ConstraintAttribute;
import de.ovgu.featureide.fm.core.base.FeatureUtils;
import de.ovgu.featureide.fm.core.base.IConstraint;
import de.ovgu.featureide.fm.core.base.IFeature;
import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.base.IFeatureStructure;
import de.ovgu.featureide.fm.core.base.impl.FeatureModel;
import de.ovgu.featureide.fm.core.init.FMCoreLibrary;
import de.ovgu.featureide.fm.core.init.LibraryManager;
import de.ovgu.featureide.fm.core.io.UnsupportedModelException;
import de.ovgu.featureide.fm.core.io.manager.FeatureModelManager;

abstract public class FeatureIdeImporter extends ctwedge.util.ext.ICTWedgeImporter {

	static {
		LibraryManager.registerLibrary(FMCoreLibrary.getInstance());
	}

	static final Logger logger = Logger.getLogger(FeatureIdeImporter.class);

	
	// at every feature maps its constraint that means that that feature is chosen
	// it depends how the feature are represented
	protected Map<IFeature, Pair<Expression, String>> choosenExprString = new HashMap<>();
	
	// all the constraints as strings
	List<String> constraintStrings = new ArrayList<>();


	@Override
	public CitModel importModel(String path) throws NotImportableException {
			FeatureModel fm = null;
			try {
				fm = readModel(Paths.get(path));
				//TODO Analyze the model
	//			FeatureModelAnalyzer featureModelAnalyzer = fm.getAnalyser();
	//			if (!featureModelAnalyzer.isValid())
	//				throw new NotImportableException(path + "\n NOT VALID MODEL");
	//			// analyze the model in order to find problems, useless features and
	//			// so on
	//			featureModelAnalyzer.analyzeFeatureModel(new NullProgressMonitor());
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new NotImportableException(path + "\n FILE NOT FOUND");
			} catch (UnsupportedModelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new NotImportableException(path + "\n UNSUPPORTED EXCEPTION");
	//		} catch (TimeoutException e) {
	//			// TODO Auto-generated catch block
	//			e.printStackTrace();
	//			throw new NotImportableException(path + "\n TIMEOUT");
			}
			// transform to citlab
			File file = new File(path);
			String newname = normalize(file.getName().substring(0, file.getName().indexOf(".")));
			CitModel transformed = transform(fm);
			if (startsWithANumber(newname))
				transformed.setName("M" + newname);
			else
				transformed.setName("M" + newname);
			return transformed;
		}

	// normalize the strings in case they contain strange characters
	public static String normalize(String x) {
		if (Character.isDigit(x.charAt(0))) {
			x = "_" + x;
		}
		return Normalizer.normalize(x, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").replaceAll(",", "y")
				.replaceAll("-", "_");
	}
	// subclasses can introduce their own reader for feature models (FeatureModel)
	// changed the way models are supported
	// abstract protected FeatureModelManager getFeatureModelReader(FeatureModel fm);
	FeatureModel readModel(Path path) throws FileNotFoundException, UnsupportedModelException {
			if (path.toString().toLowerCase().endsWith(".xml")) {
				//FeatureModelManager reader = getFeatureModelReader(fm);
				IFeatureModel root = FeatureModelManager.load(path);
				assert root != null : "errors reading " + path;
				System.out.println(root);
				System.out.println(root.getFeatureOrderList());
				return (FeatureModel) root;
			} else {
	//			GuidslReader guidslReader = new GuidslReader(fm);
	//			FeatureModelReaderIFileWrapper reader = new FeatureModelReaderIFileWrapper(guidslReader);
	//			reader.readFromFile(new File(path));
				throw new RuntimeException("only .xml files are supported by this importer");
			}
		}
	/**
	 *
	 * @param fm
	 * @return an equivalent CitModel
	 */
	private CitModel transform(IFeatureModel fm) {
		CtWedgePackageImpl.init();
		CitModel result = CtWedgeFactory.eINSTANCE.createCitModel();
		IFeature root = fm.getStructure().getRoot().getFeature();
		result.setName(normalize(normalize(root.getName())));
		// add parameters and set choose
		addParameterFor(root, result);
		// add the constraints (due to the translation)
		addConstraints(root, result);
		// add the extra constraints contained in the original model
		ConstraintConverter converter = new ConstraintConverter(choosenExprString);
		for (IConstraint c : fm.getConstraints()) {
			// if the constraint is useless, skip it
			// TODO questo potrebbe non funzionare perchè adesso è deprecato
			ConstraintAttribute attribute = FeatureUtils.getConstraintAttribute(c);
			if ((attribute == ConstraintAttribute.REDUNDANT) || (attribute == ConstraintAttribute.DEAD)
					|| (attribute == ConstraintAttribute.TAUTOLOGY))
				continue;
			Expression expr = converter.visit(c.getNode());
			result.getConstraints().add(expr);
		}
		// System.out.println(constraints);
		return result;
	}

	protected abstract void addParameterFor(IFeature root, CitModel result);
	
	// add current node as boolean and add its subnodes
	protected void addCurrentNodeAsBoolean(CitModel result, IFeatureStructure currentNode) {
		// current node is not an alternative
		Bool bool = CtWedgeFactory.eINSTANCE.createBool();
		bool.setName(normalize(currentNode.getFeature().getName()));
		result.getParameters().add(bool);
		//
		EqualExpression eq = createEqExpression(bool, Operators.EQ, "TRUE");
		setChosen(currentNode.getFeature(), eq, normalize(currentNode.getFeature().getName()) + " = true");
	
		logger.debug("boolean " + currentNode);
		for (IFeatureStructure son : currentNode.getChildren()) {
			addParameterFor(son.getFeature(), result);
		}
	}
	// set the expression to be used when the iFeature is selected chosen 
	protected void setChosen(IFeature iFeature, EqualExpression eq, String string) {
		assert choosenExprString.get(iFeature) == null
				: "current value " + choosenExprString.get(iFeature).getSecond() + "->" + eq.toString();
		choosenExprString.put(iFeature, new Pair<Expression, String>(eq,string));
	}

	protected EqualExpression createEqExpression(Parameter en, Operators eq, String name) {
		EqualExpression enumAssign = CtWedgeFactory.eINSTANCE.createEqualExpression();
		AtomicPredicate paramExpr = CtWedgeFactory.eINSTANCE.createAtomicPredicate();
		paramExpr.setName(en.getName());
		enumAssign.setLeft(paramExpr);
		// 06/05/2022 Andrea - It is wrong to always assign != if the operator is given as parameter
		//enumAssign.setOp(Operators.NE);
		enumAssign.setOp(eq);
		AtomicPredicate rExpr = CtWedgeFactory.eINSTANCE.createAtomicPredicate();
		rExpr.setName(name);
		enumAssign.setRight(rExpr);
		return enumAssign;
	}

	/**
	 * add the constraints due to the translation
	 *
	 * @param currentNode
	 * @param result
	 */
	protected void addConstraints(IFeature currentNode, CitModel result) {
		// aggiungo i constraints
		IFeatureStructure parentS = currentNode.getStructure().getParent();
		if (parentS == null) {
			// assume that currentNode is the root
			addConstraintIsChosen(currentNode,result);
		} else {
			IFeature parentF = parentS.getFeature();
			if (parentS.isAlternative()) {
				addConstraintForAlternativeChild(currentNode, result);
			} else if (currentNode.getStructure().isMandatory() && !parentS.isOr()) {
				// mandatory (but leaf in not OR)
				// choose current IFF choose parent
				addImpliesConstraint(currentNode, parentF,ImpliesOperator.IFF, result);
			} else {
				// not mandatory of not in or => OPTIONAL
				// son => parent
				addImpliesConstraint(currentNode, parentF,ImpliesOperator.IMPL, result);	
				// NOT POSSIBLE OPTIMIZATION; se currentNodeChoose ï¿½ giï¿½ nei
				// constraints posso otttimizzare.
				// if constraints.contains(currentNodeChoose) add only
				// choosen.get(parent)
			}
		}
		// moreover is if it is an alternative (even a root)
		if (currentNode.getStructure().isAlternative()) {
			addConstraintForAlternativeParent(currentNode, result);
		}
		// if current Node is an OrNode even if it is root
		if (currentNode.getStructure().isOr()) {
			// add constraints
			// parent => or dei figli
			String currentNodeChoose = choosenExprString.get(currentNode).getSecond();
			Expression chooseCurrent = choosenExprString.get(currentNode).getFirst();
			Pair<Expression, String> or = getOr(currentNode.getStructure().getChildren());
			addImpliesConstraint(EcoreUtil2.cloneIfContained(chooseCurrent), ImpliesOperator.IMPL,
					EcoreUtil2.cloneIfContained(or.getFirst()), result);
			addImpliesConstraint(currentNodeChoose,or.getSecond(), ImpliesOperator.IMPL);
		}
		// recurse down the children
		addConstraints(currentNode.getStructure().getChildren(), result);
	}
	// currentNode implies its parent 
	private void addImpliesConstraint(IFeature currentNode, IFeature parentF, ImpliesOperator iff, CitModel result) {
		String currentNodeChoose = choosenExprString.get(currentNode).getSecond();
		Expression chooseCurrent = choosenExprString.get(currentNode).getFirst();

		String parentNodeChoose = choosenExprString.get(parentF).getSecond();
		Expression chooseParent = choosenExprString.get(parentF).getFirst();		
		addImpliesConstraint(EcoreUtil2.cloneIfContained(chooseCurrent), iff,
				EcoreUtil2.cloneIfContained(chooseParent), result);
		addImpliesConstraint(currentNodeChoose, parentNodeChoose, iff);
	}

	// add the constraint: is current is chosen
	private void addConstraintIsChosen(IFeature currentNode, CitModel result) {
		String currentNodeChoose = choosenExprString.get(currentNode).getSecond();
		Expression chooseCurrent = choosenExprString.get(currentNode).getFirst();
		result.getConstraints().add(chooseCurrent);
		constraintStrings.add(currentNodeChoose);		
	}

	// current node is a parent in an alternative
	protected abstract void addConstraintForAlternativeParent(IFeature currentNode, CitModel result);

	// current node is a child in an alternative
	protected abstract void addConstraintForAlternativeChild(IFeature currentNode, CitModel result);

	private static boolean startsWithANumber(String s) {
		return !(s.charAt(0) >= 'a' || s.charAt(0) >= 'A');
	}
	
	// add the constraint ifPart op thenPart, where op is => or IFF AS EXPRESSION
	protected void addImpliesConstraint(Expression left, ImpliesOperator impl, Expression right, CitModel result) {
		if (impl == ImpliesOperator.IMPL) {
			ImpliesExpression implies = CtWedgeFactory.eINSTANCE.createImpliesExpression();
			implies.setRight(right);
			implies.setLeft(left);
			implies.setOp(ImpliesOperator.IMPL);
			result.getConstraints().add(implies);
		} else {
			assert (impl == ImpliesOperator.IFF);
			ImpliesExpression iffs = CtWedgeFactory.eINSTANCE.createImpliesExpression();
			iffs.setRight(right);
			iffs.setLeft(left);
			iffs.setOp(ImpliesOperator.IFF);
			result.getConstraints().add(iffs);
		}
	}
	// add the constraint ifPart op thenPart, where op is => or IFF AS STRING
	protected void addImpliesConstraint(String ifPart, String thenPart, ImpliesOperator impl) {
		if (impl == ImpliesOperator.IMPL) {
			constraintStrings.add(ifPart + " => " + thenPart);
		} else {
			assert (impl == ImpliesOperator.IFF);
			constraintStrings.add(ifPart + " IFF " + thenPart);
		}
	}


	
	protected  Pair<Expression,String> getOr(List<IFeatureStructure> list) {
		ArrayList<Expression> bList = new ArrayList<>();
		String result = "";
		for (IFeatureStructure f : list) {
			bList.add(EcoreUtil2.cloneIfContained(choosenExprString.get(f.getFeature()).getFirst()));
			result += " OR " + choosenExprString.get(f.getFeature()).getSecond();
		}
		return new Pair<>(createOrExpression(bList),result);
	}

	private void addConstraints(List<IFeatureStructure> list, CitModel result) {
		for (IFeatureStructure f : list)
			addConstraints(f.getFeature(), result);
	}

	private Expression createOrExpression(ArrayList<Expression> bList) {
		assert bList.size() >= 1;
		Expression result = null;
		for (Expression boola : bList) {
			// the first one
			if (result == null) {
				result = boola;
			} else {
				// the second and so on
				OrExpression resultP = CtWedgeFactory.eINSTANCE.createOrExpression();
				resultP.setLeft(result);
				// resultP.setOp(OrOperators.OR);
				resultP.setRight(boola);
				result = resultP;
			}
		}
	
		return result;
	}

}
