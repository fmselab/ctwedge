package ctwedge.util.smt;

/*******************************************************************************
 * Copyright (c) 2020 University of Bergamo - Italy
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *   Andrea Bombarda - initial API and implementation
 ******************************************************************************/

import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.sosy_lab.java_smt.api.BooleanFormula;
import org.sosy_lab.java_smt.api.BooleanFormulaManager;
import org.sosy_lab.java_smt.api.Formula;
import org.sosy_lab.java_smt.api.FormulaManager;
import org.sosy_lab.java_smt.api.IntegerFormulaManager;
import org.sosy_lab.java_smt.api.NumeralFormula;
import org.sosy_lab.java_smt.api.NumeralFormula.IntegerFormula;
import org.sosy_lab.java_smt.api.NumeralFormula.RationalFormula;
import org.sosy_lab.java_smt.api.RationalFormulaManager;
import org.sosy_lab.java_smt.api.SolverContext;

import ctwedge.ctWedge.AndExpression;
import ctwedge.ctWedge.AtomicPredicate;
import ctwedge.ctWedge.EqualExpression;
import ctwedge.ctWedge.Expression;
import ctwedge.ctWedge.ImpliesExpression;
import ctwedge.ctWedge.ModMultDiv;
import ctwedge.ctWedge.NotExpression;
import ctwedge.ctWedge.OrExpression;
import ctwedge.ctWedge.Parameter;
import ctwedge.ctWedge.PlusMinus;
import ctwedge.ctWedge.PlusMinusOperators;
import ctwedge.ctWedge.RelationalExpression;
import ctwedge.ctWedge.impl.ExpressionImpl;
import ctwedge.ctWedge.util.CtWedgeSwitch;

/**
 * translates a constraints in SMT expressions
 * 
 * @author bombarda_andrea
 * 
 */
public class SMTConstraintTranslator extends CtWedgeSwitch<Formula> {

	private static final Logger logger = Logger.getLogger(SMTConstraintTranslator.class);

	SolverContext ctx;
	Map<Parameter, Formula> variables;
	Map<String, String> declaredElements;

	public SMTConstraintTranslator(SolverContext ctx, Map<Parameter, Formula> variables,
			Map<String, String> declaredElements) {
		this.ctx = ctx;
		this.variables = variables;
		this.declaredElements = declaredElements;
	}

	@Override
	public Formula caseNotExpression(NotExpression not) {
		FormulaManager fmgr = ctx.getFormulaManager();
		BooleanFormulaManager bmgr = fmgr.getBooleanFormulaManager();
		
		Formula predicate = this.doSwitch(not.getPredicate());
		
		assert predicate instanceof BooleanFormula : "NotExpression predicate must be a boolean term";
		
		return bmgr.not((BooleanFormula) predicate);
	}

	@Override
	public Formula caseImpliesExpression(ImpliesExpression ruleExpr) {
		FormulaManager fmgr = ctx.getFormulaManager();
		BooleanFormulaManager bmgr = fmgr.getBooleanFormulaManager();
		
		Formula leftVal = this.doSwitch(ruleExpr.getLeft());
		Formula rightVal = this.doSwitch(ruleExpr.getRight());

		assert leftVal instanceof BooleanFormula && rightVal instanceof BooleanFormula: "ImpliesExpression terms must be boolean";
		
		switch (ruleExpr.getOp()) {
		case IMPL:
			// left => right
			return bmgr.implication((BooleanFormula)leftVal, (BooleanFormula)rightVal);
		case IFF:
			return bmgr.equivalence((BooleanFormula)leftVal, (BooleanFormula)rightVal);
		}
		throw new RuntimeException("Operator not found");
	}

	@Override
	public Formula caseOrExpression(OrExpression orExpr) {
		FormulaManager fmgr = ctx.getFormulaManager();
		BooleanFormulaManager bmgr = fmgr.getBooleanFormulaManager();
		
		Formula leftVal = this.doSwitch(orExpr.getLeft());
		Formula rightVal = this.doSwitch(orExpr.getRight());
		
		assert leftVal instanceof BooleanFormula && rightVal instanceof BooleanFormula: "OrExpression terms must be boolean";
		
		return bmgr.or((BooleanFormula)leftVal, (BooleanFormula)rightVal);
	}

	@Override
	public Formula caseAndExpression(AndExpression andExpr) {
		FormulaManager fmgr = ctx.getFormulaManager();
		BooleanFormulaManager bmgr = fmgr.getBooleanFormulaManager();
		
		Formula leftVal = this.doSwitch(andExpr.getLeft());
		Formula rightVal = this.doSwitch(andExpr.getRight());
		
		assert leftVal instanceof BooleanFormula && rightVal instanceof BooleanFormula: "AndExpression terms must be boolean";
		
		return bmgr.and((BooleanFormula)leftVal, (BooleanFormula)rightVal);
	}
	
	private Formula areEqual(Formula leftVal, Formula rightVal) {
		FormulaManager fmgr = ctx.getFormulaManager();
		BooleanFormulaManager bmgr = fmgr.getBooleanFormulaManager();
		IntegerFormulaManager imgr = fmgr.getIntegerFormulaManager();
		RationalFormulaManager rfmgr = fmgr.getRationalFormulaManager();
		
		if (leftVal == null || rightVal==null)
			return bmgr.makeBoolean(false);
			
		// Different classed -> Cannot be equals
		if (!leftVal.getClass().equals(rightVal.getClass()))
			return bmgr.makeBoolean(false);
		
		if (leftVal instanceof BooleanFormula && rightVal instanceof BooleanFormula) {
			return bmgr.equivalence((BooleanFormula)leftVal, (BooleanFormula)rightVal);
		}
		
		if (leftVal instanceof IntegerFormula && rightVal instanceof IntegerFormula) {
			return imgr.equal((IntegerFormula)leftVal, (IntegerFormula)rightVal);
		}
		
		if (leftVal instanceof RationalFormula && rightVal instanceof RationalFormula) {
			return rfmgr.equal((RationalFormula)leftVal, (RationalFormula)rightVal);
		}
				
		// TODO: Other possibilities?		
		
		// None of the previous applicable -> Cannot be equals
		return bmgr.makeBoolean(false);
	}

	@SuppressWarnings("incomplete-switch")
	@Override
	public Formula caseEqualExpression(EqualExpression object) {
		FormulaManager fmgr = ctx.getFormulaManager();
		BooleanFormulaManager bmgr = fmgr.getBooleanFormulaManager();
		
		logger.debug("Parsing left");
		Formula leftVal = this.doSwitch(object.getLeft());
		logger.debug(leftVal);
		
		logger.debug("Parsing Right");
		Formula rightVal = this.doSwitch(object.getRight());
		logger.debug(rightVal);
		
		switch (object.getOp()) {
		case EQ:
			return areEqual(leftVal, rightVal);
		case NE:
			return bmgr.not((BooleanFormula) areEqual(leftVal, rightVal));
		}
		
		throw new RuntimeException("Operator not found in constraint");
	}

	@SuppressWarnings("incomplete-switch")
	@Override
	public Formula caseRelationalExpression(RelationalExpression ineqExpr) {
		FormulaManager fmgr = ctx.getFormulaManager();
		RationalFormulaManager nmgr = fmgr.getRationalFormulaManager();
		
		logger.debug("Parsing left");
		Formula leftVal = this.doSwitch(ineqExpr.getLeft());
		logger.debug(leftVal);
		
		logger.debug("Parsing Right");
		Formula rightVal = this.doSwitch(ineqExpr.getRight());
		logger.debug(rightVal);
		
		switch (ineqExpr.getOp()) {
		case GE:
			return nmgr.greaterOrEquals((NumeralFormula)leftVal, (NumeralFormula)rightVal);
		case GT:			
			return nmgr.greaterThan((NumeralFormula)leftVal, (NumeralFormula)rightVal);
		case LE:
			return nmgr.lessOrEquals((NumeralFormula)leftVal, (NumeralFormula)rightVal);
		case LT:
			return nmgr.lessThan((NumeralFormula)leftVal, (NumeralFormula)rightVal);
		case EQ:
		case NE:
			throw new RuntimeException("This should never happen");
		}
		
		throw new RuntimeException("Operator not found in constraint");
	}

	@Override
	public Formula casePlusMinus(PlusMinus pm) {
		FormulaManager fmgr = ctx.getFormulaManager();
		RationalFormulaManager rmgr = fmgr.getRationalFormulaManager();

		Formula leftVal = this.doSwitch(pm.getLeft());
		Formula rightVal = this.doSwitch(pm.getRight());

		assert leftVal instanceof RationalFormula && rightVal instanceof RationalFormula
				: "PlusMinus terms must be rationals";

		if (pm.getOp() == PlusMinusOperators.MINUS)
			return rmgr.subtract((RationalFormula) leftVal, (RationalFormula) rightVal);
		else
			return rmgr.add((RationalFormula) leftVal, (RationalFormula) rightVal);
	}

	@Override
	public Formula caseModMultDiv(ModMultDiv md) {
		FormulaManager fmgr = ctx.getFormulaManager();
		RationalFormulaManager rmgr = fmgr.getRationalFormulaManager();
		IntegerFormulaManager imgr = fmgr.getIntegerFormulaManager();

		Formula leftVal = this.doSwitch(md.getLeft());
		Formula rightVal = this.doSwitch(md.getRight());

		assert leftVal instanceof RationalFormula && rightVal instanceof RationalFormula
				: "ModMultDiv terms must be rationals";

		switch (md.getOp()) {
		case DIV: {
			return rmgr.divide((RationalFormula) leftVal, (RationalFormula) rightVal);
		}
		case MULT:
			return rmgr.multiply((RationalFormula) leftVal, (RationalFormula) rightVal);
		case MOD: {
			return imgr.subtract((IntegerFormula) leftVal, imgr.multiply(
					imgr.divide((IntegerFormula) leftVal, (IntegerFormula) rightVal), (IntegerFormula) rightVal));
		}
		}
		throw new RuntimeException("Operator not found");
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Formula caseAtomicPredicate(AtomicPredicate atom) {
		FormulaManager fmgr = ctx.getFormulaManager();
		BooleanFormulaManager bmgr = fmgr.getBooleanFormulaManager();
		RationalFormulaManager rfmgr = fmgr.getRationalFormulaManager();
		IntegerFormulaManager ifmgr = fmgr.getIntegerFormulaManager();

		// Boolean value
		if (atom.getBoolConst() != null)
			return bmgr.makeBoolean(atom.getBoolConst().equalsIgnoreCase("true") ? true : false);
		
		// Enumerative value
		String varName = atom.getName().replace("\"", "");
		int counter = 0;
		for (Entry<String, String> p: declaredElements.entrySet()) {
			Expression e = (ExpressionImpl) atom.eContainer();
			if (e instanceof EqualExpression) {
				if (p.getKey().equals(varName))
					return ifmgr.makeNumber(counter);
			}	
			counter++;
		}
		
		// Numeric value
		try {
			double num = Double.parseDouble(atom.getName().toString());
			return rfmgr.makeNumber(num);
		} catch (NumberFormatException ex) {}
		
		// Variable name
		for (Entry p : variables.entrySet())
			if (((Parameter)p.getKey()).getName().toString().equalsIgnoreCase(varName)) {
				return (Formula)p.getValue();
			}
		
		return null;
	}

}
