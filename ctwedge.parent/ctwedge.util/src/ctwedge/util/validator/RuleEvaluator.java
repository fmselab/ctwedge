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
import ctwedge.ctWedge.ImpliesExpression;
import ctwedge.ctWedge.ImpliesOperator;
import ctwedge.ctWedge.ModMultDiv;
import ctwedge.ctWedge.NotExpression;
import ctwedge.ctWedge.OrExpression;
import ctwedge.ctWedge.PlusMinus;
import ctwedge.ctWedge.PlusMinusOperators;
import ctwedge.ctWedge.RelationalExpression;
import ctwedge.ctWedge.util.CtWedgeSwitch;
import ctwedge.util.Test;

/**
 * Evaluated if a rule makes a seed invalid, it generate a boolean output.
 * The output is a true/false string.
 * 
 * @author garganti vava radavelli
 */
public class RuleEvaluator extends CtWedgeSwitch<String> {

	/** The seed. */
	Test seed;
	
	public RuleEvaluator(Test t) {
		this.seed = t;
	}
	
	public String evaluateConstraint(Constraint r) {
		assert seed != null;
		return this.doSwitch(r);
	}
	
	@Override
	public String caseCitModel(CitModel m) {
		for (Constraint c : m.getConstraints()) if (!this.doSwitch(c).equals("true")) return "false";
		return "true";
	}

	@Override
	public String caseNotExpression(NotExpression not) {
		if (not == null || not.getPredicate() == null)
			return null;
		String predicate = this.doSwitch(not.getPredicate());
		return "true".equals(predicate) ? "false" : "true";
	}
	
	@Override
	public String caseAtomicPredicate(AtomicPredicate x) {
		//System.out.println("AtomicPredicate: "+x.getName()+" "+x.getBoolConst()+" "+utils.getType(x)+" "+getValueInSeed(x));
		return getValueInSeed(x);
	}

	@Override
	public String caseImpliesExpression(ImpliesExpression ruleExpr) {
		if (ruleExpr == null || ruleExpr.getLeft() == null | ruleExpr.getRight() == null) {
			return null;
		}
		String leftVal = this.doSwitch(ruleExpr.getLeft());
		// if leftval is false and implication
		if (leftVal.equals("false") && ruleExpr.getOp() == ImpliesOperator.IMPL)
			return "true";
		String rightVal = this.doSwitch(ruleExpr.getRight());
		switch (ruleExpr.getOp()) {
		case IMPL:
			return rightVal;
		case IFF:
			return leftVal.equals(rightVal)+"";
		}
		throw new RuntimeException("Operator not found in seed");

	}

	@Override
	public String caseOrExpression(OrExpression orExpr) {
		if (orExpr == null || orExpr.getLeft() == null || orExpr.getRight() == null) {
			return null;
		}
		String leftVal = this.doSwitch(orExpr.getLeft());
		// if the first one is true, exit
		if ("true".equals(leftVal))
			return "true";
		String rightVal = this.doSwitch(orExpr.getRight());
		return rightVal;
	}

	@Override
	public String caseAndExpression(AndExpression andExpr) {
		if (andExpr == null || andExpr.getLeft() == null || andExpr.getRight() == null) {
			return null;
		}
		String leftVal = this.doSwitch(andExpr.getLeft());
		// simulate short circuit - first is false
		if ("false".equals(leftVal))
			return "false";
		String rightVal = this.doSwitch(andExpr.getRight());
		return rightVal;
	}
	
	@Override
	public String caseEqualExpression(EqualExpression x) {
		String left = this.doSwitch(x.getLeft());
		String right = this.doSwitch(x.getRight());
		switch (x.getOp()) {
		case EQ:
			return ""+left.equals(right);
		case NE:
			return ""+!left.equals(right);
		default: throw new RuntimeException("Operator not found in constraint");
		}
	}
	
	@Override
	public String casePlusMinus(PlusMinus pm) {
		Double leftVal = Double.parseDouble(this.doSwitch(pm.getLeft()));
		Double rightVal = Double.parseDouble(this.doSwitch(pm.getRight()));
		if (pm.getOp() == PlusMinusOperators.MINUS)
			return ""+(leftVal - rightVal);
		else
			return ""+(leftVal + rightVal);
	}

	@Override
	public String caseModMultDiv(ModMultDiv md) {
		Double leftVal = Double.parseDouble(this.doSwitch(md.getLeft()));
		Double rightVal = Double.parseDouble(this.doSwitch(md.getRight()));
		switch (md.getOp()) {
		case DIV:
			return ""+(leftVal / rightVal);
		case MULT:
			return ""+(leftVal * rightVal);
		case MOD:
			return ""+(leftVal % rightVal);
		}
		throw new RuntimeException("Operator not found");
	}

	@Override
	public String caseRelationalExpression(RelationalExpression x) {
		if (x == null || x.getLeft() == null || x.getRight() == null) {
			return null;
		}
		String left = this.doSwitch(x.getLeft());
		String right = this.doSwitch(x.getRight());
		switch (x.getOp()) {
		case EQ:
			return left.equals(right)+"";
		case GE:
			return (Double.parseDouble(left) >= Double.parseDouble(right))+"";
		case GT:
			return (Double.parseDouble(left) > Double.parseDouble(right))+"";
		case LE:
			return (Double.parseDouble(left) <= Double.parseDouble(right))+"";
		case LT:
			return (Double.parseDouble(left) < Double.parseDouble(right))+"";
		case NE:
			return (!(left.equals(right))+"");
		}
		throw new RuntimeException("Operator not found in seed");
	}
	
	private String getValueInSeed(AtomicPredicate element) {
		if (element == null) 
			return null;
		if (element.getBoolConst()!=null)
			return element.getBoolConst();
		if (seed.containsKey(element.getName()))
			return seed.get(element.getName());
		return element.getName();
//		for (Assignment a : seed.getAssignments()) {
//			if (a.getParameter().getName().equals(element.getName())) {
//				return a.getValue();
//			}
//		}
//		// it reach this point only when i'm typing my seed, when it is incomplete.
//		return null;
	}
}
