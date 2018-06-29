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
package ctwedge.util.simplifier;

import ctwedge.ctWedge.AndExpression;
import ctwedge.ctWedge.ImpliesExpression;
import ctwedge.ctWedge.NotExpression;
import ctwedge.ctWedge.OrExpression;
import ctwedge.ctWedge.RelationalExpression;
import ctwedge.ctWedge.util.CtWedgeSwitch;

/** discovers if an object is in a rule/constraint */
public class InRule extends CtWedgeSwitch<Boolean> {
	
	@Override
	public Boolean caseAndExpression(AndExpression object) {
		return this.doSwitch(object.getLeft()) || this.doSwitch(object.getRight()) ;
	}
	
	@Override
	public Boolean caseOrExpression(OrExpression object) {
		Boolean doSwitchLeft = this.doSwitch(object.getLeft());
		assert doSwitchLeft != null;
		Boolean doSwitchRight = this.doSwitch(object.getRight());
		assert doSwitchRight != null : object.getRight().getClass();
		return doSwitchLeft || doSwitchRight ;
	}
	
	@Override
	public Boolean caseImpliesExpression(ImpliesExpression object) {
		return this.doSwitch(object.getLeft()) || this.doSwitch(object.getRight()) ;
	}
	
	@Override
	public Boolean caseNotExpression(NotExpression object) {
		return this.doSwitch(object.getPredicate());
	}

	@Override
	public Boolean caseRelationalExpression(RelationalExpression object) {
		Boolean doSwitchLeft = this.doSwitch(object.getLeft());
		assert doSwitchLeft != null : object.getLeft().toString();
		Boolean doSwitchRight = this.doSwitch(object.getRight());
		assert doSwitchRight != null : object.getRight().getClass();
		return doSwitchLeft || doSwitchRight ;
	}
	
}
