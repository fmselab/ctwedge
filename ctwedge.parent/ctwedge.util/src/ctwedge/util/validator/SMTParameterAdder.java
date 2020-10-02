 package ctwedge.util.validator;

/*******************************************************************************
 * Copyright (c) 2020 University of Bergamo - Italy
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *   Andrea Bombarda - initial API and implementation
 *   
 * add a parameter (and return the parameter added) with the right type
 *  
 ******************************************************************************/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.sosy_lab.java_smt.api.ArrayFormula;
import org.sosy_lab.java_smt.api.ArrayFormulaManager;
import org.sosy_lab.java_smt.api.BooleanFormula;
import org.sosy_lab.java_smt.api.Formula;
import org.sosy_lab.java_smt.api.FormulaType;
import org.sosy_lab.java_smt.api.NumeralFormula.IntegerFormula;
import org.sosy_lab.java_smt.api.SolverContext;

import ctwedge.ctWedge.*;
import ctwedge.ctWedge.util.CtWedgeSwitch;

import com.sun.jna.Pointer;

public class SMTParameterAdder extends CtWedgeSwitch<Formula> {
	
	private static final Logger logger = Logger.getLogger(SMTParameterAdder.class);

	private SolverContext ctx;
	private Map<Enumerative, Formula> declaredTypes = new HashMap<>();
	private Map<String, String> declaredElements;

	SMTParameterAdder(SolverContext ctx, Map<String, String> declaredElements) {
		this.ctx = ctx;
		this.declaredElements = declaredElements;
	}

	@Override
	public Formula caseEnumerative(Enumerative enumerative) {
		Enumerative type = enumerative;
		
		// Check if the enumerative type is already declared
		Formula enumType = declaredTypes.get(type);		
		if (enumType != null)
			return enumType;
		
		// The enumerative type is a new declared type		
		String elements = "";
		String enumName = enumerative.getName();
		
		// Create the list of elements
		for (Element e : type.getElements()) {
			String typeName = "";
			typeName = type.getName();
			declaredElements.put(e.getName().concat(enumName), typeName);
			elements += " " + e.getName().concat(enumName);
		}
		
		// add the type
		enumType = addEnumType(type, enumName,elements);
		declaredTypes.put(type, enumType);
				
		// Return the name of the new created variable
		return enumType;
	}
	/**
	 * 
	 * @param type
	 * @param enumName nome dell'enumerativo (necessary in case of anonymous types)
	 * @param elements2 
	 * @return
	 */
	private Formula addEnumType(Enumerative type, String enumName, String elements) {
		// Create the new EnumType
		Formula enumType = ctx.getFormulaManager().getIntegerFormulaManager().makeVariable(enumName);
		return enumType;
	}

	@Override
	public Formula caseBool(Bool boolParam) {
		return ctx.getFormulaManager().getBooleanFormulaManager().makeVariable(boolParam.getName());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Formula caseRange(Range range) {
		// The Range object can be seen as an array of values => Get the list of all possible values
		ArrayList<String> values = new ArrayList<String>(ParameterElementsGetterAsStrings.eInstance.caseRange(range));
		if (values.size() >= 1) {
			for (String v : values) {
				declaredElements.put(v.concat(range.getName()),v);
			}
		} else
			throw new RuntimeException("Not valid");
		
		return ctx.getFormulaManager().getIntegerFormulaManager().makeVariable(range.getName());
	}

}
