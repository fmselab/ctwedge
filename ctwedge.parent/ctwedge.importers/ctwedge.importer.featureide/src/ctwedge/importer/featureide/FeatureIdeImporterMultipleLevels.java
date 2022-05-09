/*******************************************************************************
 * Copyright (c) 2013 University of Bergamo - Italy
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *   Paolo Vavassori - initial API and implementation
 *   Angelo Gargantini - utils and architecture
 ******************************************************************************/
package ctwedge.importer.featureide;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.xtext.EcoreUtil2;
//import org.sat4j.specs.TimeoutException;

import ctwedge.ctWedge.AtomicPredicate;
import ctwedge.ctWedge.Bool;
import ctwedge.ctWedge.CitModel;
import ctwedge.ctWedge.CtWedgeFactory;
import ctwedge.ctWedge.Element;
import ctwedge.ctWedge.Enumerative;
import ctwedge.ctWedge.EqualExpression;
import ctwedge.ctWedge.Expression;
import ctwedge.ctWedge.ImpliesExpression;
import ctwedge.ctWedge.ImpliesOperator;
import ctwedge.ctWedge.Operators;
import ctwedge.ctWedge.OrExpression;
import ctwedge.ctWedge.Parameter;
import ctwedge.ctWedge.impl.CtWedgePackageImpl;
import ctwedge.services.CTWedgeGrammarAccess.BoolConstElements;
import ctwedge.util.ModelUtils;
import ctwedge.util.ext.NotImportableException;
//import de.ovgu.featureide.fm.core.Constraint;
import de.ovgu.featureide.fm.core.ConstraintAttribute;
import de.ovgu.featureide.fm.core.base.FeatureUtils;
import de.ovgu.featureide.fm.core.base.IConstraint;
import de.ovgu.featureide.fm.core.base.IFeature;
import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.base.IFeatureStructure;
import de.ovgu.featureide.fm.core.base.impl.DefaultFeatureModelFactory;
import de.ovgu.featureide.fm.core.base.impl.Feature;
import de.ovgu.featureide.fm.core.base.impl.FeatureModel;
import de.ovgu.featureide.fm.core.init.FMCoreLibrary;
import de.ovgu.featureide.fm.core.init.LibraryManager;
//import de.ovgu.featureide.fm.core.FeatureModel;
import de.ovgu.featureide.fm.core.FeatureModelAnalyzer;
//import de.ovgu.featureide.fm.core.io.AbstractFeatureModelReader;
//import de.ovgu.featureide.fm.core.io.FeatureModelReaderIFileWrapper;
import de.ovgu.featureide.fm.core.io.UnsupportedModelException;
import de.ovgu.featureide.fm.core.io.manager.FeatureModelManager;
import de.ovgu.featureide.fm.core.io.manager.IFeatureModelManager;
//import de.ovgu.featureide.fm.core.io.guidsl.GuidslReader;

/**
 * general importer for Feature Models by using the code from featureide. It
 * uses the approach presented in IWCT13.
 * 
 * @author garganti
 * 
 */
public abstract class FeatureIdeImporterMultipleLevels extends ctwedge.util.ext.ICTWedgeImporter {

	static {
		LibraryManager.registerLibrary(FMCoreLibrary.getInstance());
	}
	
	private static final String NONE = "NONE";

	private static final Logger logger = Logger.getLogger(FeatureIdeImporterMultipleLevels.class);

	Map<IFeature, Expression> choosenExpr = new HashMap<IFeature, Expression>();
	// at every feature add a constraint that means that that feature is choosen
	Map<IFeature, String> choosenString = new HashMap<IFeature, String>();
	// all the constraints
	List<String> constraintStrings = new ArrayList<String>();

	public FeatureIdeImporterMultipleLevels() {}

	// subclasses can introduce their own reader for feature models (FeatureModel)
	// changed the way models are supported
	// abstract protected FeatureModelManager getFeatureModelReader(FeatureModel fm);

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

	private static boolean startsWithANumber(String s) {
		return !(s.charAt(0) >= 'a' || s.charAt(0) >= 'A');
	}

	//static PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:*.xml");
	// load the feature model from file
	private FeatureModel readModel(Path path) throws FileNotFoundException, UnsupportedModelException {
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

	private String normalize(String x) {
		return Normalizer.normalize(x, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").replaceAll(",", "y")
				.replaceAll("-", "_");
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
		ConstraintConverter converter = new ConstraintConverter(choosenExpr);
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

	/**
	 * add the constraints due to the translation
	 * 
	 * @param currentNode
	 * @param result
	 */
	private void addConstraints(IFeature currentNode, CitModel result) {
		// aggiungo i constraints
		IFeatureStructure parentS = currentNode.getStructure().getParent();
		String currentNodeChoose = choosenString.get(currentNode);
		Expression chooseCurrent = choosenExpr.get(currentNode);
		if (parentS == null) {
			// assume that currentNode is the root
			result.getConstraints().add(chooseCurrent);
			constraintStrings.add(currentNodeChoose);
		} else {
			IFeature parentF = parentS.getFeature();
			if (parentS.isAlternative()) {
				if (currentNode.getStructure().isAlternative()) {
					// parent alternative and current alternative
					// currentNode != NONE
					Parameter pNode = getEnumerativeByName(result, normalize(currentNode.getName()));
					assert pNode != null;					
					EqualExpression enumAssign = createEqExpression(pNode, Operators.NE, NONE);									
					//
					String currentNodeIsTaken = currentNode + "!=" + NONE;
					// currentNode != NONE <=> choose(currentNode)
					addImpliesConstraint(EcoreUtil2.cloneIfContained(enumAssign), ImpliesOperator.IFF,
							EcoreUtil2.cloneIfContained(chooseCurrent), result);
					addImpliesConstraint(currentNodeIsTaken, currentNodeChoose, ImpliesOperator.IFF);
				} else {
					// the constraints are guranteed by the enumerative
					logger.debug("skipping constraint for " + normalize(currentNode.getName()));
				}
			} else if (currentNode.getStructure().isMandatory() && !parentS.isOr()) {
				// mandatory (but leaf in not OR)
				// choose current IFF choose parent
				addImpliesConstraint(EcoreUtil2.cloneIfContained(chooseCurrent), ImpliesOperator.IFF,
						EcoreUtil2.cloneIfContained(choosenExpr.get(parentF)), result);
				addImpliesConstraint(currentNodeChoose, choosenString.get(parentF), ImpliesOperator.IFF);
			} else {
				// not mandatory of not in or => OPTIONAL
				// son => parent
				addImpliesConstraint(EcoreUtil2.cloneIfContained(chooseCurrent), ImpliesOperator.IMPL,
						EcoreUtil2.cloneIfContained(choosenExpr.get(parentF)), result);
				addImpliesConstraint(currentNodeChoose, choosenString.get(parentF), ImpliesOperator.IMPL);

				// NOT POSSIBLE OPTIMIZATION; se currentNodeChoose ï¿½ giï¿½ nei
				// constraints posso otttimizzare.
				// if constraints.contains(currentNodeChoose) add only
				// choosen.get(parent)
			}
		}
		// if current Node is an OrNode even if it is root
		if (currentNode.getStructure().isOr()) {
			// add constraints
			// parent => or dei figli
			addImpliesConstraint(EcoreUtil2.cloneIfContained(chooseCurrent), ImpliesOperator.IMPL,
					EcoreUtil2.cloneIfContained(getOrExp(currentNode.getStructure().getChildren())), result);
			addImpliesConstraint(currentNodeChoose, getOr(currentNode.getStructure().getChildren()), ImpliesOperator.IMPL);
			// it will added that son => parent for every son
		}
		// recurse down the children
		addConstraints(currentNode.getStructure().getChildren(), result);
	}

	private void addConstraints(List<IFeatureStructure> list, CitModel result) {
		for (IFeatureStructure f : list)
			addConstraints(f.getFeature(), result);
	}

	private void addImpliesConstraint(Expression left, ImpliesOperator impl, Expression right, CitModel result) {
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

	// add the constraint ifPart op thenPart, where op is => or IFF
	private void addImpliesConstraint(String ifPart, String thenPart, ImpliesOperator impl) {
		if (impl == ImpliesOperator.IMPL) {
			constraintStrings.add(ifPart + " => " + thenPart);
		} else {
			assert (impl == ImpliesOperator.IFF);
			constraintStrings.add(ifPart + " IFF " + thenPart);
		}
	}

	/**
	 * FASE 1: per ogni nodo aggiungi un termine grammatica citlab
	 * 
	 * @param currentNode
	 * @param result
	 */
	private void addParameterFor(IFeature currentFeature, CitModel result) {
		// compute the type for father
		IFeatureStructure currentNode = currentFeature.getStructure();
		// System.out.println("adding parameter for " + currentNode);
		if (currentNode.isAlternative()) {
			// aggiungi come enumerativo
			Enumerative en = getEnumerative(currentFeature);
			result.getParameters().add(en);
			// update chosen unless is an alternative and already set
			if (currentNode.getParent() != null && currentNode.getParent().isAlternative()) {
				logger.debug("skipping choose for " + currentNode.getFeature().getName());
			} else {
				// set chosen for the currentNode
				EqualExpression eq = createEqExpression(en, Operators.NE, NONE);				
				setChosen(currentNode.getFeature(), eq);
				setChosen(currentNode.getFeature(), normalize(currentNode.getFeature().getName()) + " !=" + NONE);

			}
			// get newphews
			for (IFeatureStructure sonStr : currentNode.getChildren()) {
				// update choosen for son ???
				EqualExpression eq = createEqExpression(en, Operators.EQ, sonStr.getFeature().getName());
				setChosen(sonStr.getFeature(), eq);
				setChosen(sonStr.getFeature(), normalize(currentNode.getFeature().getName()) + " = " + normalize(sonStr.getFeature().getName()));
				// if son is an alternative, add paramters for it
				if (sonStr.isAlternative())
					addParameterFor(sonStr.getFeature(), result);
				else {
					// if son is not an alternative, skip it (traslated already as element)
					for (IFeatureStructure nephew : sonStr.getChildren()) {
						addParameterFor(nephew.getFeature(), result);
					}
				}
			}
		} else {
			// current node is not an alternative
			Bool bool = CtWedgeFactory.eINSTANCE.createBool();
			bool.setName(normalize(currentNode.getFeature().getName()));
			result.getParameters().add(bool);
			// 
			EqualExpression eq = createEqExpression(bool, Operators.EQ, "TRUE");
			setChosen(currentNode.getFeature(), eq);
			setChosen(currentNode.getFeature(), normalize(currentNode.getFeature().getName()) + " = true");

			logger.debug("boolean " + currentNode);
			for (IFeatureStructure son : currentNode.getChildren()) {
				addParameterFor(son.getFeature(), result);
			}
		}
	}

	// traslate the expression en (enumrative) = (or !=) name of the parameter
	// any paremter = 
	private EqualExpression createEqExpression(Parameter en, Operators eq, String name) {
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

	private Expression createOrExpression(ArrayList<Expression> bList) {
		assert bList.size() >= 1;
		Expression result = null;
		for (Expression boola : bList) {
			// the first one
			if (result == null) {
				result = boola;
			} else {
				// the seond and so on
				OrExpression resultP = CtWedgeFactory.eINSTANCE.createOrExpression();
				resultP.setLeft(result);
				// resultP.setOp(OrOperators.OR);
				resultP.setRight(boola);
				result = resultP;
			}
		}

		return result;
	}

	Enumerative getEnumerative(IFeature root) {
		Enumerative enume = CtWedgeFactory.eINSTANCE.createEnumerative();
		enume.setName(normalize(root.getName()));
		for (IFeatureStructure nephew : root.getStructure().getChildren()) {
			Element e = CtWedgeFactory.eINSTANCE.createElement();
			e.setName(normalize(nephew.getFeature().getName()));
			enume.getElements().add(e);
		}
		// add also none for no element is selected 
		Element e = CtWedgeFactory.eINSTANCE.createElement();
		e.setName(NONE);
		enume.getElements().add(e);
		return enume;
	}

	private Parameter getEnumerativeByName(CitModel result, String name) {
		for (Parameter parameter : result.getParameters()) {
			if (normalize(parameter.getName()).equals(normalize(name)) && (parameter instanceof Enumerative)) {
				return parameter;
			}
		}
		throw new RuntimeException(name + " not found");
	}

	private String getOr(List<IFeatureStructure> list) {
		String result = "";
		for (IFeatureStructure f : list) {
			result += " OR " + choosenString.get(f.getFeature());
		}
		return result;
	}

	// or among features
	private Expression getOrExp(List<IFeatureStructure> list) {
		ArrayList<Expression> bList = new ArrayList<Expression>();
		for (IFeatureStructure f : list) {
			bList.add(EcoreUtil2.cloneIfContained(choosenExpr.get(f.getFeature())));
		}
		return createOrExpression(bList);
	}

	private void setChosen(IFeature iFeature, EqualExpression enumAssign) {
		assert choosenString.get(iFeature) == null
				: "current value " + choosenString.get(iFeature) + "->" + enumAssign.toString();
		choosenExpr.put(iFeature, enumAssign);
	};

	private void setChosen(IFeature currentNode, String string) {
		assert choosenString.get(currentNode) == null
				: "current value " + choosenString.get(currentNode) + "->" + string;
		choosenString.put(currentNode, string);
	}
}
