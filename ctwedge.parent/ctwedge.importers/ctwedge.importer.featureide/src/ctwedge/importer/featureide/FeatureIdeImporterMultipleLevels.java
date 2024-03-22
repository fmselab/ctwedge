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

import org.apache.log4j.Logger;
import org.eclipse.xtext.EcoreUtil2;
//import org.sat4j.specs.TimeoutException;

import ctwedge.ctWedge.CitModel;
import ctwedge.ctWedge.CtWedgeFactory;
import ctwedge.ctWedge.Element;
import ctwedge.ctWedge.Enumerative;
import ctwedge.ctWedge.EqualExpression;
import ctwedge.ctWedge.Expression;
import ctwedge.ctWedge.ImpliesOperator;
import ctwedge.ctWedge.Operators;
import ctwedge.ctWedge.Parameter;
import ctwedge.util.Pair;
import de.ovgu.featureide.fm.core.base.IFeature;
import de.ovgu.featureide.fm.core.base.IFeatureStructure;

/**
 * general importer for Feature Models by using the code from featureide. It
 * uses the approach presented in IWCT13 - uses enumerations
 *
 * @author garganti
 *
 */
public abstract class FeatureIdeImporterMultipleLevels extends FeatureIdeImporter{


	private static final String NONE = "NONE";

	static final Logger logger = Logger.getLogger(FeatureIdeImporterMultipleLevels.class);


	public FeatureIdeImporterMultipleLevels() {}


	protected void addConstraintForAlternativeChild(IFeature currentNode, CitModel result) {
		assert currentNode.getStructure().getParent().isAlternative();
		if (currentNode.getStructure().isAlternative()) {
			String currentNodeChoose = choosenExprString.get(currentNode).getSecond();
			Expression chooseCurrent = choosenExprString.get(currentNode).getFirst();
			// parent alternative and current alternative
			// currentNode != NONE
			Pair<EqualExpression, String> notchosen = getNotChoosen(currentNode,result);
			// currentNode != NONE <=> choose(currentNode)		
			addImpliesConstraint(EcoreUtil2.cloneIfContained(notchosen.getFirst()), ImpliesOperator.IFF,
					EcoreUtil2.cloneIfContained(chooseCurrent), result);
			addImpliesConstraint(notchosen.getSecond(), currentNodeChoose, ImpliesOperator.IFF);
		} else {
			// the constraints are guranteed by the enumerative
			logger.debug("skipping constraint for " + normalize(currentNode.getName()));
		}
	}
	
	protected Pair<EqualExpression, String> getNotChoosen(IFeature currentNode, CitModel result) {
		Parameter pNode = getEnumerativeByName(result, normalize(currentNode.getName()));
		assert pNode != null;
		EqualExpression enumAssign = createEqExpression(pNode, Operators.NE, NONE);
		String currentNodeIsTaken = currentNode + "!=" + NONE;
		return new Pair<>(enumAssign, currentNodeIsTaken);
	}


	protected void addConstraintForAlternativeParent(IFeature currentNode, CitModel result) {
		// DO nothing, since the alternative is already implicit in the enumeration
	}


	/**
	 * FASE 1: per ogni nodo aggiungi un termine grammatica citlab
	 *
	 * @param currentNode
	 * @param result
	 */
	@Override
	protected void addParameterFor(IFeature currentFeature, CitModel result) {
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
				setChosen(currentNode.getFeature(), eq, normalize(currentNode.getFeature().getName()) + " !=" + NONE);
			}
			// get nephews
			for (IFeatureStructure sonStr : currentNode.getChildren()) {
				// update choosen for son ???
				EqualExpression eq = createEqExpression(en, Operators.EQ, sonStr.getFeature().getName());
				setChosen(sonStr.getFeature(), eq, normalize(currentNode.getFeature().getName()) + " = " + normalize(sonStr.getFeature().getName()));
				// if son is an alternative, add parameters for it
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
			addCurrentNodeAsBoolean(result, currentNode);
		}
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
}
