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
 *   
 * add a parameter (and return the parameter added) with the right type
 *  
 ******************************************************************************/

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.management.RuntimeErrorException;

import org.apache.log4j.Logger;
import org.sosy_lab.java_smt.api.EnumerationFormulaManager;
import org.sosy_lab.java_smt.api.Formula;
import org.sosy_lab.java_smt.api.FormulaType.EnumerationFormulaType;
import org.sosy_lab.java_smt.api.SolverContext;

import ctwedge.ctWedge.Bool;
import ctwedge.ctWedge.Element;
import ctwedge.ctWedge.Enumerative;
import ctwedge.ctWedge.Range;
import ctwedge.ctWedge.util.CtWedgeSwitch;
import ctwedge.util.ParameterElementsGetterAsStrings;

/**
 * return the set of variable for the parameter (can be more than one)
 */
public class SMTParameterAdder extends CtWedgeSwitch<List<Formula>> {
	
	private static final Logger logger = Logger.getLogger(SMTParameterAdder.class);

	private SolverContext ctx;
	private Map<Enumerative, List<Formula>> declaredTypes = new HashMap<>();
	private Map<String, List<String>> declaredElements;

	public enum EnumTreatment{
		INTEGER, BOOLEAN, ENUM;
	}
	
	static  EnumTreatment enumTreatment = EnumTreatment.INTEGER;
	
	/**
	 * Instantiates a new SMT parameter adder.
	 *
	 * @param ctx the ctx
	 * @param declaredElements the declared elements param ---> values that it can take (for enum and integers)
	 */
	SMTParameterAdder(SolverContext ctx, Map<String, List<String>> declaredElements) {
		this.ctx = ctx;
		this.declaredElements = declaredElements;
	}
	
	private void addElement(String prameter, String element) {
		// under the assumption that every element is unique
		// otherwise atomic predicate for example won't work
		assert ! declaredElements.keySet().contains(element);
		List<String> map;
		if (declaredElements.containsKey(prameter)) {
			map = declaredElements.get(prameter);
		} else {
			map = new ArrayList<>();
			declaredElements.put(prameter, map);
		}
		map.add(element);
	}
	
	

	@Override
	public List<Formula> caseEnumerative(Enumerative enumerative) {
		Enumerative type = enumerative;
		
		// Check if the enumerative type is already declared
		List<Formula> enumType = declaredTypes.get(type);		
		if (enumType != null)
			return enumType;
		
		// The enumerative type is a new declared type		
		Set<String> elements = new HashSet<>();
		String enumName = enumerative.getName();
		
		// Create the list of elements
		for (Element e : type.getElements()) {
			String typeName = type.getName();
			addElement(typeName, e.getName());
			// the name of the element is  ELEMENT NAME + ENUM NAME
			elements.add(e.getName().concat(enumName));
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
	 * @param elements 
	 * @param elements2 
	 * @return
	 */
	private List<Formula> addEnumType(Enumerative type, String enumName, Set<String> elements) {		
		switch (enumTreatment) {
		// Create the new EnumType
		case INTEGER:
			return Collections.singletonList(ctx.getFormulaManager().getIntegerFormulaManager().makeVariable(enumName));
		case ENUM:
			EnumerationFormulaManager fm = ctx.getFormulaManager().getEnumerationFormulaManager();
			EnumerationFormulaType enumeration = fm.declareEnumeration(enumName, elements);
			return Collections.singletonList(fm.makeVariable(enumName, enumeration));
		case BOOLEAN:
			// make a variable for every enum
			List<Formula> boolVars = new ArrayList<>();
			// Create the list of elements
			for (String e : elements) {
				boolVars.add(ctx.getFormulaManager().getBooleanFormulaManager().makeVariable(e));
			}
			return boolVars;
		default:
			throw new RuntimeException();
		} 
	}

	@Override
	public List<Formula> caseBool(Bool boolParam) {
		return Collections.singletonList(ctx.getFormulaManager().getBooleanFormulaManager().makeVariable(boolParam.getName()));
	}

	@Override
	public List<Formula> caseRange(Range range) {
		// The Range object can be seen as an array of values => Get the list of all possible values
		ArrayList<String> values = new ArrayList<String>(ParameterElementsGetterAsStrings.instance.caseRange(range));
		if (values.size() >= 1) {
			for (String v : values) {
				addElement(v, range.getName());
			}
		} else
			throw new RuntimeException("Not valid");
		
		return Collections.singletonList(ctx.getFormulaManager().getIntegerFormulaManager().makeVariable(range.getName()));
	}

}
