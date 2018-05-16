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
package ctwedge.validation;

import ctwedge.ctWedge.Expression;
import ctwedge.ctWedge.ModMultDiv;
import ctwedge.ctWedge.PlusMinus;
import ctwedge.ctWedge.PlusMinusOperators;
import ctwedge.ctWedge.util.CtWedgeSwitch;

public class ArithmeticEvaluator extends CtWedgeSwitch<Double> {

	/**
	 * Evaluates arithmetic rules giving each arithmetic rule a double output.
	 *
	 * @param r the r
	 * @return the double
	 */
	public Double evaluateArith(Expression r) {
		return this.doSwitch(r);
	}

	/* (non-Javadoc)
	 * @see combinatorial.comb.comB.util.CmbSwitch#casePlusMinus(combinatorial.comb.comB.PlusMinus)
	 */
	@Override
	public Double casePlusMinus(PlusMinus pm) {
		Double leftVal = this.doSwitch(pm.getLeft());
		Double rightVal = this.doSwitch(pm.getRight());
		if (pm.getOp() == PlusMinusOperators.MINUS)
			return (leftVal - rightVal);
		else
			return (leftVal + rightVal);

	}

	/* (non-Javadoc)
	 * @see combinatorial.comb.comB.util.CmbSwitch#caseModMultDiv(combinatorial.comb.comB.ModMultDiv)
	 */
	@Override
	public Double caseModMultDiv(ModMultDiv md) {
		Double leftVal = this.doSwitch(md.getLeft());
		Double rightVal = this.doSwitch(md.getRight());
		switch (md.getOp()) {
		case DIV:
			return (leftVal / rightVal);
		case MULT:
			return (leftVal * rightVal);
		case MOD:
			return (leftVal % rightVal);
		}
		throw new RuntimeException("Operator not found");
	}

}
