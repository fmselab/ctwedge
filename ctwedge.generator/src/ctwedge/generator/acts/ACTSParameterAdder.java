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
package ctwedge.generator.acts;

import java.util.ArrayList;

import ctwedge.ctWedge.Bool;
import ctwedge.ctWedge.Enumerative;
import ctwedge.ctWedge.Range;
import ctwedge.ctWedge.util.CtWedgeSwitch;
import ctwedge.generator.util.ParameterElementsGetterAsStrings;
import ctwedge.generator.util.Utility;
import edu.uta.cse.fireeye.common.Parameter;
import edu.uta.cse.fireeye.common.SUT;

/** add a parameter (and return the parameter added) with the right type 
 * 
 * @author garganti
 *
 */
public class ACTSParameterAdder extends CtWedgeSwitch<Parameter>{
	
	// add parameters to this citModel;
	SUT sut;
		
	ACTSParameterAdder(SUT s){
		sut = s;
	}
	
	@Override
	public Parameter caseEnumerative(Enumerative enumerative) {
		Parameter p = sut.addParam(enumerative.getName());
		p.setType(isAllNumbers(enumerative) ? Parameter.PARAM_TYPE_INT : Parameter.PARAM_TYPE_ENUM);
		ArrayList<String> arrayList = new ArrayList<String>(ParameterElementsGetterAsStrings.instance.caseEnumerative(enumerative));
		System.out.println(arrayList);
		p.setValues(arrayList);
		return p;
	}
	
	boolean isAllNumbers(Enumerative enumerative) {
		ArrayList<String> arrayList = new ArrayList<String>(ParameterElementsGetterAsStrings.instance.caseEnumerative(enumerative));
		for (String s : arrayList) if (!Utility.INSTANCE.isNumber(s)) return false;
		return true;
	}

	@Override
	public Parameter caseBool(Bool boolParam) {
		Parameter p = sut.addParam(boolParam.getName());
		p.setType(Parameter.PARAM_TYPE_BOOL);
		// order is important
		p.addValue("true");
		p.addValue("false");
		return p;
	}

	@Override
	public Parameter caseRange(Range range) {
		Parameter p = sut.addParam(range.getName());
		p.setType(Parameter.PARAM_TYPE_INT);
		p.setValues(new ArrayList<String>(ParameterElementsGetterAsStrings.instance.caseRange(range)));
		return p;
	}
}
