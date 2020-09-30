package ctwedge.util.validator;

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

import java.util.Map;

import org.apache.log4j.Logger;

import ctwedge.ctWedge.*;
import ctwedge.ctWedge.util.CtWedgeSwitch;
import ctwedge.util.Assignment;

import com.sun.jna.Pointer;

/**
 * translates a constraints in yices expressions
 * 
 * @author garganti
 * 
 */
public class YicesConstraintTranslator extends CtWedgeSwitch<Pointer> {

	private static final boolean AVOID_BOOLASSIGN = true;

	private static final Logger logger = Logger
			.getLogger(YicesConstraintTranslator.class);

	YicesLibrary yices = null;

	Pointer ctx;
	Map<Parameter, Pointer> variables;
	Map<String, String> declaredElements;

	public YicesConstraintTranslator(Pointer ctx, Map<Parameter, Pointer> variables,
			Map<String, String> declaredElements2, YicesLibrary yices) {
		// ctx = yices.yices_mk_context();
		this.ctx = ctx;
		this.variables = variables;
		this.declaredElements = declaredElements2;
		this.yices=yices;
	}

	@Override
	public Pointer caseNotExpression(NotExpression not) {
		Pointer predicate = this.doSwitch(not.getPredicate());
		return yices.yices_mk_not(ctx, predicate);
	}

	@Override
	public Pointer caseImpliesExpression(ImpliesExpression ruleExpr) {
		Pointer leftVal = this.doSwitch(ruleExpr.getLeft());
		Pointer rightVal = this.doSwitch(ruleExpr.getRight());
		switch (ruleExpr.getOp()) {
		case IMPL:
			// left => right ===> ! left or right
			Pointer nl = yices.yices_mk_not(ctx, leftVal);
			return yices.yices_mk_or(ctx, new Pointer[] { nl, rightVal }, 2);
		case IFF:
			return yices.yices_mk_eq(ctx, leftVal, rightVal);
		}
		throw new RuntimeException("Operator not found");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * combinatorial.comb.comB.util.CmbSwitch#caseOr(combinatorial.comb.comB.Or)
	 */
	@Override
	public Pointer caseOrExpression(OrExpression orExpr) {
		Pointer leftVal = this.doSwitch(orExpr.getLeft());
		Pointer rightVal = this.doSwitch(orExpr.getRight());
		return yices.yices_mk_or(ctx, new Pointer[] { leftVal, rightVal }, 2);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * combinatorial.comb.comB.util.CmbSwitch#caseAnd(combinatorial.comb.comB
	 * .And)
	 */
	@Override
	public Pointer caseAndExpression(AndExpression andExpr) {
		Pointer leftVal = this.doSwitch(andExpr.getLeft());
		Pointer rightVal = this.doSwitch(andExpr.getRight());
		return yices.yices_mk_and(ctx, new Pointer[] { leftVal, rightVal }, 2);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * combinatorial.comb.comB.util.CmbSwitch#caseInequality(combinatorial.comb
	 * .comB.Inequality )
	 */
	@Override
	public Pointer caseRelationalExpression(RelationalExpression ineqExpr) {
		logger.debug("Parsing left");
		Pointer leftVal = this.doSwitch(ineqExpr.getLeft());
		logger.debug(leftVal);
		logger.debug("Parsing Right");
		Pointer rightVal = this.doSwitch(ineqExpr.getRight());
		logger.debug(rightVal);
		Pointer p = null;
		switch (ineqExpr.getOp()) {
		case EQ:
			p = yices.yices_mk_eq(ctx, leftVal, rightVal);
			break;
		case GE:
			p = yices.yices_mk_ge(ctx, leftVal, rightVal);
			break;
		case GT:
			p = yices.yices_mk_gt(ctx, leftVal, rightVal);
			break;
		case LE:
			p = yices.yices_mk_le(ctx, leftVal, rightVal);
			break;
		case LT:
			p = yices.yices_mk_lt(ctx, leftVal, rightVal);
			break;
		case NE:
			p = yices.yices_mk_diseq(ctx, leftVal, rightVal);
			break;
		}
		if (p == null)
			throw new RuntimeException("Operator not found in constraint");
		return p;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * combinatorial.comb.comB.util.CmbSwitch#casePlusMinus(combinatorial.comb
	 * .comB.PlusMinus)
	 */
	@Override
	public Pointer casePlusMinus(PlusMinus pm) {
		Pointer leftVal = this.doSwitch(pm.getLeft());
		Pointer rightVal = this.doSwitch(pm.getRight());
		if (pm.getOp() == PlusMinusOperators.MINUS)
			return yices.yices_mk_sub(ctx, new Pointer[] { leftVal, rightVal },
					2);
		else
			return yices.yices_mk_sum(ctx, new Pointer[] { leftVal, rightVal },
					2);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * combinatorial.comb.comB.util.CmbSwitch#caseModMultDiv(combinatorial.comb
	 * .comB.ModMultDiv)
	 */
	@Override
	public Pointer caseModMultDiv(ModMultDiv md) {
		Pointer leftVal = this.doSwitch(md.getLeft());
		Pointer rightVal = this.doSwitch(md.getRight());
		switch (md.getOp()) {
		case DIV: {
			Pointer div = yices.yices_mk_var_from_decl(ctx,
					yices.yices_get_var_decl_from_name(ctx, "div"));
			return yices.yices_mk_app(ctx, div, new Pointer[] { leftVal,
					rightVal }, 2);
		}
		case MULT:
			return yices.yices_mk_mul(ctx, new Pointer[] { leftVal, rightVal },
					2);
		case MOD: {
			Pointer mod = yices.yices_mk_var_from_decl(ctx,
					yices.yices_get_var_decl_from_name(ctx, "mod"));
			return yices.yices_mk_app(ctx, mod, new Pointer[] { leftVal,
					rightVal }, 2);
		}

		}
		throw new RuntimeException("Operator not found");
	}

}
