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

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
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
import de.ovgu.featureide.fm.core.base.IFeature;
import de.ovgu.featureide.fm.core.base.IFeatureStructure;
import de.ovgu.featureide.fm.core.init.FMCoreLibrary;
import de.ovgu.featureide.fm.core.init.LibraryManager;

/**
 * general importer for Feature Models by using the code from featureide. It
 * uses the approach presented in IWCT13.
 *
 * @author garganti
 *
 */
public abstract class FeatureIdeImporterMultipleLevels extends FeatureIdeImporter{

	static {
		LibraryManager.registerLibrary(FMCoreLibrary.getInstance());
	}

	private static final String NONE = "NONE";

	private static final Logger logger = Logger.getLogger(FeatureIdeImporterMultipleLevels.class);

	// all the constraints
	List<String> constraintStrings = new ArrayList<>();

	public FeatureIdeImporterMultipleLevels() {}


	/**
	 * add the constraints due to the translation
	 *
	 * @param currentNode
	 * @param result
	 */
	@Override
	protected void addConstraints(IFeature currentNode, CitModel result) {
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

				// NOT POSSIBLE OPTIMIZATION; se currentNodeChoose � gi� nei
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
		ArrayList<Expression> bList = new ArrayList<>();
		for (IFeatureStructure f : list) {
			bList.add(EcoreUtil2.cloneIfContained(choosenExpr.get(f.getFeature())));
		}
		return createOrExpression(bList);
	}

	private void setChosen(IFeature iFeature, EqualExpression enumAssign) {
		assert choosenString.get(iFeature) == null
				: "current value " + choosenString.get(iFeature) + "->" + enumAssign.toString();
		choosenExpr.put(iFeature, enumAssign);
	}

	private void setChosen(IFeature currentNode, String string) {
		assert choosenString.get(currentNode) == null
				: "current value " + choosenString.get(currentNode) + "->" + string;
		choosenString.put(currentNode, string);
	}
}
