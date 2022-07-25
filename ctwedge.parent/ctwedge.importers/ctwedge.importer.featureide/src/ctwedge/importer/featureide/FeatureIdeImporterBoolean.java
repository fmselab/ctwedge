package ctwedge.importer.featureide;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.xtext.EcoreUtil2;

import ctwedge.ctWedge.AtomicPredicate;
import ctwedge.ctWedge.CitModel;
import ctwedge.ctWedge.CtWedgeFactory;
import ctwedge.ctWedge.Expression;
import ctwedge.ctWedge.ImpliesOperator;
import ctwedge.ctWedge.NotExpression;
import ctwedge.ctWedge.Parameter;
import ctwedge.util.Pair;
import de.ovgu.featureide.fm.core.base.IFeature;
import de.ovgu.featureide.fm.core.base.IFeatureStructure;

/** this transforms the model as pure boolean model
 *
 * @author garganti
 */
public class FeatureIdeImporterBoolean extends FeatureIdeImporter {

	static final Logger logger = Logger.getLogger(FeatureIdeImporterBoolean.class);
	
	@Override
	protected void addParameterFor(IFeature root, CitModel result) {
		// add as boolean
		addCurrentNodeAsBoolean(result, root.getStructure());
	}

	@Override
	protected void addConstraintForAlternativeChild(IFeature currentNode, CitModel result) {
		assert currentNode.getStructure().getParent().isAlternative();
		String currentNodeChoose = choosenExprString.get(currentNode).getSecond();
		Expression chooseCurrent = choosenExprString.get(currentNode).getFirst();
		IFeature parent = currentNode.getStructure().getParent().getFeature();
		Pair<Expression, String> parentChosen = choosenExprString.get(parent);
		// add current => not the others and true for parent
		List<IFeatureStructure> siblings = currentNode.getStructure().getParent().getChildren();
		for(IFeatureStructure sib: siblings) {
			// skip itself
			if (sib == currentNode.getStructure()) continue;
			// current => ! sib 
			Pair<Expression, String> notChosen = getNotChoosen(sib.getFeature(),result);
			addImpliesConstraint(EcoreUtil2.cloneIfContained(chooseCurrent),
					ImpliesOperator.IMPL, notChosen.getFirst(), result);
			addImpliesConstraint(currentNodeChoose,notChosen.getSecond(),ImpliesOperator.IMPL);
			logger.debug(chooseCurrent + " implies " + notChosen.getFirst());
			logger.debug(currentNodeChoose + " implies " + notChosen.getFirst());
			// current => parent			
			addImpliesConstraint(EcoreUtil2.cloneIfContained(chooseCurrent),
					ImpliesOperator.IMPL, EcoreUtil2.cloneIfContained(parentChosen.getFirst()), result);
			addImpliesConstraint(currentNodeChoose,parentChosen.getSecond(),ImpliesOperator.IMPL);
			logger.debug(chooseCurrent + " implies " + parentChosen.getFirst());
			logger.debug(currentNodeChoose + " implies " + parentChosen.getFirst());
		}		
	}

	private Pair<Expression, String> getNotChoosen(IFeature feature, CitModel result) {
		Parameter pNode = getParamByName(result, normalize(feature.getName()));
		assert pNode != null;
		// assert pNode is a boolean
		AtomicPredicate paramExpr = CtWedgeFactory.eINSTANCE.createAtomicPredicate();
		paramExpr.setName(pNode.getName());
		NotExpression not = CtWedgeFactory.eINSTANCE.createNotExpression();
		not.setPredicate(paramExpr);
		String currentNodeIsNotTaken = "!"+ feature.getName();
		return new Pair<Expression, String>(not, currentNodeIsNotTaken);
	}

	private Parameter getParamByName(CitModel result, String name) {
		for (Parameter parameter : result.getParameters()) {
			if (normalize(parameter.getName()).equals(normalize(name))) {
				return parameter;
			}
		}
		throw new RuntimeException(name + " not found");
	}

	
	
	@Override
	protected void addConstraintForAlternativeParent(IFeature currentNode, CitModel result) {
		assert currentNode.getStructure().isAlternative();
		String currentNodeChoose = choosenExprString.get(currentNode).getSecond();
		Expression chooseCurrent = choosenExprString.get(currentNode).getFirst();
		// add current => one of the children
		List<IFeatureStructure> children = currentNode.getStructure().getChildren();
		Pair<Expression, String> or = getOr(children);
		// current => ! OR sibs 
		addImpliesConstraint(EcoreUtil2.cloneIfContained(chooseCurrent),
					ImpliesOperator.IMPL, or.getFirst(), result);
		addImpliesConstraint(currentNodeChoose,or.getSecond(),ImpliesOperator.IMPL);
	}


}
