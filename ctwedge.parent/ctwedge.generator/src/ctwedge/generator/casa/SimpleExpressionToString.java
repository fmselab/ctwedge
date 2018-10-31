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
package ctwedge.generator.casa;

import org.eclipse.emf.ecore.EObject;

import ctwedge.ctWedge.AndExpression;
import ctwedge.ctWedge.AtomicPredicate;
import ctwedge.ctWedge.Element;
import ctwedge.ctWedge.Enumerative;
import ctwedge.ctWedge.EqualExpression;
import ctwedge.ctWedge.Expression;
import ctwedge.ctWedge.ImpliesExpression;
import ctwedge.ctWedge.NotExpression;
import ctwedge.ctWedge.OrExpression;
import ctwedge.ctWedge.Range;
import ctwedge.ctWedge.RelationalExpression;
import ctwedge.ctWedge.util.CtWedgeSwitch;

/**
 * convert an expression to string without using the serializer
 * 
 * @author garganti
 * 
 */
public class SimpleExpressionToString extends CtWedgeSwitch<String> {

	public static SimpleExpressionToString toStringCoverter = new SimpleExpressionToString();

	SimpleExpressionToString() {
	}

	@Override
	public String caseAndExpression(AndExpression and) {
		return "(" + this.doSwitch(and.getLeft()) + ") and ("
				+ this.doSwitch(and.getRight()) + ")";
	}

	@Override
	public String caseOrExpression(OrExpression or) {
		Expression right = or.getRight();
		//assert right != null : or.getRight().getClass();
		String doSwitchRx = this.doSwitch(right);
		//assert doSwitchRx != null;
		Expression left = or.getLeft();
		//assert left != null;
		return "(" + this.doSwitch(left) + ") or (" + doSwitchRx + ")";
	}

	@Override
	public String caseImpliesExpression(ImpliesExpression imp) {
		return "(" + this.doSwitch(imp.getLeft()) + ") "
				+ imp.getOp().getLiteral() + " ("
				+ this.doSwitch(imp.getRight()) + ")";
	}

	@Override
	public String caseNotExpression(NotExpression not) {
		return "not (" + this.doSwitch(not.getPredicate())+ ")";
	}

	@Override
	public String caseRelationalExpression(RelationalExpression a) {
		Expression right = a.getRight();
		assert right != null : a.getRight().getClass();
		return "(" + this.doSwitch(a.getLeft()) + ") " + a.getOp().getLiteral()
				+ " (" + this.doSwitch(right) + ")";
	}
	
	@Override
	public String caseEqualExpression(EqualExpression a) {
		Expression right = a.getRight();
		assert right != null : a.getRight().getClass();
		return "(" + this.doSwitch(a.getLeft()) + ") " + a.getOp().getLiteral()
				+ " (" + this.doSwitch(right) + ")";
	}

	@Override
	public String caseAtomicPredicate(AtomicPredicate object) {
		if (object.getBoolConst()!=null) return object.getBoolConst();
		return object.getName();
	}
	
	@Override
	public String caseEnumerative(Enumerative object) {
		return object.getName();
	}

	@Override
	public String caseRange(Range object) {
		return object.getName();
	}

	@Override
	public String caseElement(Element object) {
		return object.getName();
	}
	
	@Override
	public String doSwitch(EObject eObject) {
		assert eObject != null;
		String res = super.doSwitch(eObject);
		assert res != null : eObject.toString() + " class " + eObject.getClass();
		return res;
	}
	
}
