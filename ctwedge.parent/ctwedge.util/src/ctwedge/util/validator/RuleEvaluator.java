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
/*
 * 
 */
package ctwedge.util.validator;

import ctwedge.ctWedge.AndExpression;
import ctwedge.ctWedge.AtomicPredicate;
import ctwedge.ctWedge.CitModel;
import ctwedge.ctWedge.Constraint;
import ctwedge.ctWedge.EqualExpression;
import ctwedge.ctWedge.Expression;
import ctwedge.ctWedge.ImpliesExpression;
import ctwedge.ctWedge.ImpliesOperator;
import ctwedge.ctWedge.ModMultDiv;
import ctwedge.ctWedge.NotExpression;
import ctwedge.ctWedge.OrExpression;
import ctwedge.ctWedge.PlusMinus;
import ctwedge.ctWedge.PlusMinusOperators;
import ctwedge.ctWedge.RelationalExpression;
import ctwedge.ctWedge.util.CtWedgeSwitch;
import ctwedge.util.SimpleExpressionToString;
import ctwedge.util.Test;

/**
 * Evaluated if a rule makes a seed invalid, it generate a boolean output. The
 * output is a true/false string.
 * 
 * @author garganti vava radavelli bombarda
 */
public class RuleEvaluator {

	/** The seed. */
	Test seed;

	public RuleEvaluator(Test t) {
		assert t != null;
		this.seed = t;
	}

	ConstraintEvaluator eval = new ConstraintEvaluator();

	public Boolean evaluateConstraint(Constraint r) {
		assert seed != null;
		assert r.eContainer() instanceof CitModel;
		return (Boolean) eval.doSwitch(r);
	}

	// itis valid for every constraint in the model
	public Boolean evaluateModel(CitModel r) {
		return (Boolean) eval.doSwitch(r);
	}

	class ConstraintEvaluator extends CtWedgeSwitch<Object> {

		@Override
		public Boolean caseCitModel(CitModel m) {
			int i = 0;
			for (Constraint c : m.getConstraints()) {				
				//System.out.println("eval " + i++ + SimpleExpressionToString.toStringCoverter.doSwitch(c));
				if (!(Boolean) this.doSwitch(c))
					return false;
			}
			return true;
		}

		@Override
		public Boolean caseNotExpression(NotExpression not) {
			if (not == null || not.getPredicate() == null)
				return null;
			return !(Boolean) this.doSwitch(not.getPredicate());
		}

		@Override
		public Object caseAtomicPredicate(AtomicPredicate x) {
			//System.out.println("AtomicPredicate: " + x.getName() + " " + x.getBoolConst() + " "
			// + new ModelUtils() "+utils.getType(x)
			//		+ " " + getValueInSeed(x));
			return getValueInSeed(x);
		}

		@Override
		public Boolean caseImpliesExpression(ImpliesExpression ruleExpr) {
			if (ruleExpr == null || ruleExpr.getLeft() == null | ruleExpr.getRight() == null) {
				return null;
			}
			Boolean leftVal = (Boolean) this.doSwitch(ruleExpr.getLeft());
			// if leftval is false and implication
			if (!leftVal && ruleExpr.getOp() == ImpliesOperator.IMPL)
				return true;
			Boolean rightVal = (Boolean) this.doSwitch(ruleExpr.getRight());
			switch (ruleExpr.getOp()) {
			case IMPL:
				return rightVal;
			case IFF:
				return leftVal == rightVal;
			}
			throw new RuntimeException("Operator not found in seed");

		}

		@Override
		public Boolean caseOrExpression(OrExpression orExpr) {
			assert ! (orExpr == null || orExpr.getLeft() == null || orExpr.getRight() == null);
			Boolean leftVal = (Boolean) this.doSwitch(orExpr.getLeft());
			// if the first one is true, exit
			if (leftVal)
				return true;
			Object doSwitch = this.doSwitch(orExpr.getRight());
			assert doSwitch instanceof Boolean : doSwitch.toString();
			return (Boolean) doSwitch;
		}

		@Override
		public Boolean caseAndExpression(AndExpression andExpr) {
			if (andExpr == null || andExpr.getLeft() == null || andExpr.getRight() == null) {
				return null;
			}
			Boolean leftVal = (Boolean) this.doSwitch(andExpr.getLeft());
			// simulate short circuit - first is false
			if (!leftVal)
				return false;
			return (Boolean) this.doSwitch(andExpr.getRight());
		}

		@Override
		public Boolean caseEqualExpression(EqualExpression x) {
			Expression leftExpr = x.getLeft();
			assert leftExpr != null; 
			Object left = this.doSwitch(leftExpr);
			Object right = this.doSwitch(x.getRight());
			switch (x.getOp()) {
			case EQ:
				return left.equals(right);
			case NE:
				return !left.equals(right);
			default:
				throw new RuntimeException("Operator not found in constraint");
			}
		}

		@Override
		public Boolean caseRelationalExpression(RelationalExpression x) {
			if (x == null || x.getLeft() == null || x.getRight() == null) {
				return null;
			}
			Object left = this.doSwitch(x.getLeft());
			Object right = this.doSwitch(x.getRight());
			switch (x.getOp()) {
			case EQ:
				return left.equals(right);
			case GE:
				return Double.compare((Double) left, (Double) right) >= 0;
			case GT:
				return Double.compare((Double) left, (Double) right) > 0;
			case LE:
				return Double.compare((Double) left, (Double) right) <= 0;
			case LT:
				return Double.compare((Double) left, (Double) right) < 0;
			case NE:
				return !left.equals(right);
			}
			throw new RuntimeException("Operator not found in seed");
		}

		@Override
		public Double casePlusMinus(PlusMinus pm) {
			Double leftVal = (Double) this.doSwitch(pm.getLeft());
			Double rightVal = (Double) this.doSwitch(pm.getRight());
			if (pm.getOp() == PlusMinusOperators.MINUS)
				return (leftVal - rightVal);
			else
				return (leftVal + rightVal);
		}

		@Override
		public Double caseModMultDiv(ModMultDiv md) {
			Double leftVal = (Double) this.doSwitch(md.getLeft());
			Double rightVal = (Double) this.doSwitch(md.getRight());
			switch (md.getOp()) {
			case DIV:
				return leftVal / rightVal;
			case MULT:
				return leftVal * rightVal;
			case MOD:
				return leftVal % rightVal;
			}
			throw new RuntimeException("Operator not found");
		}
	}

	private Object getValueInSeed(AtomicPredicate element) {

		if (element == null)
			return null;
		if (element.getBoolConst() != null)
			return Boolean.parseBoolean(element.getBoolConst());

		if (element.eContainer() instanceof EqualExpression) {
			// If the parent is an EqualExpression, and the element is on the right, then
			// we should consider the enumerative value instead of the parameter name
			EqualExpression eq = (EqualExpression) element.eContainer();
			if (eq.getRight().equals(element)) {
				try {
					return Double.parseDouble(element.getName());
				} catch (NumberFormatException ex) {
				}
				return element.getName();
			}
		}

		String val = seed.get(element.getName());

		// Enumerative -> Return a string
		if (seed.containsKey(element.getName())) {
			// If val is double...
			try {
				return Double.parseDouble(val);
			} catch (NumberFormatException ex) {
			}
			// Otherwise, it can be a boolean
			if (val.equalsIgnoreCase("true"))
				return true;
			if (val.equalsIgnoreCase("false"))
				return false;
			// NAN and not boolean
			return val;
		}

		// Numeric constant
		try {
			return Double.parseDouble(element.getName());
		} catch (NumberFormatException ex) {
		}
		return element.getName().replace("\"", "");

//		for (Assignment a : seed.getAssignments()) {
//			if (a.getParameter().getName().equals(element.getName())) {
//				return a.getValue();
//			}
//		}
//		// it reach this point only when i'm typing my seed, when it is incomplete.
//		return null;*/
	}
}
