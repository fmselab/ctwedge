package ctwedge.util.validator;

import java.util.ArrayList;
import java.util.List;

import ctwedge.ctWedge.*;
import ctwedge.ctWedge.util.CtWedgeSwitch;
import ctwedge.util.ModelUtils;

/**
 * given a parameter returns the list of elements as strings
 * 
 * @author garganti
 * 
 */
public class ParameterElementsGetterAsStrings extends CtWedgeSwitch<List<String>> {

	// standard order among booleans (false, true)
	public static ParameterElementsGetterAsStrings eInstance = new ParameterElementsGetterAsStrings(false, null);

	// order for CASA (true,false)
	public static ParameterElementsGetterAsStrings eInstanceCasa = new ParameterElementsGetterAsStrings(true, null);

	boolean casaOrder; 
	CitModel model;
	
	private ParameterElementsGetterAsStrings(boolean casaOrder, CitModel model) {
		this.casaOrder = casaOrder;
		this.model = model;
	}

	@Override
	public List<String> caseParameter(Parameter parameter) {
		return this.doSwitch(parameter);
	}

	@Override
	public List<String> caseEnumerative(Enumerative enumerative) {
		ArrayList<String> elements = new ArrayList<String>();
		ModelUtils mu = new ModelUtils(model);	
		
		for (String e : mu.enums.get(enumerative.getName())) {
			elements.add(e);
		}
		return elements;

	}

	public List<String> caseBoolean(Bool bool) {
		ArrayList<String> boolValues = new ArrayList<String>();
		if (casaOrder){
			// useful when using Casa Like translation (true = 0, false = 1)
			boolValues.add("true");			
			boolValues.add("false");
		} else{
			// use false , true (as declared in the grammar)
			boolValues.add("false");
			boolValues.add("true");			
		}
		return boolValues;
	}

	@Override
	public List<String> caseRange(Range range) {
		ArrayList<String> rangeValues = new ArrayList<String>();
		int step;
		if (range.getStep() == 0)
			step = 1;
		else
			step = range.getStep();
		for (int i = range.getBegin(); i <= range.getEnd(); i += step) {
				rangeValues.add(Integer.toString(i));
		} 
		return rangeValues;

	}

}