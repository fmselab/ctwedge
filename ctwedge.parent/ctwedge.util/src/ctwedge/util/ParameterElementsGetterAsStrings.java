package ctwedge.util;

import java.util.ArrayList;
import java.util.List;

import ctwedge.ctWedge.Bool;
import ctwedge.ctWedge.Element;
import ctwedge.ctWedge.Enumerative;
import ctwedge.ctWedge.Parameter;
import ctwedge.ctWedge.Range;
import ctwedge.ctWedge.util.CtWedgeSwitch;


/** @Deprecated for constraints, because there is no reference anymore */
public class ParameterElementsGetterAsStrings extends CtWedgeSwitch<List<String>> {
	
	public static final String TRUE_AS_STRING = "true";
	public static final String FALSE_AS_STRING = "false";
	
	public static ParameterElementsGetterAsStrings instance = new ParameterElementsGetterAsStrings();
		
	@Override
	public List<String> caseParameter(Parameter parameter) {
		return this.doSwitch(parameter);
	}

	@Override
	public List<String> caseEnumerative(Enumerative enumerative) {
		ArrayList<String> elements = new ArrayList<String>();
		for (Element e : enumerative.getElements()) {
			elements.add(e.getName());
		}
		return elements;

	}

	@Override
	public List<String> caseBool(Bool bool) {
		ArrayList<String> boolValues = new ArrayList<String>() {
			// in case of boolean values, it must be consider the case (ignoring it)
			@Override
			public int indexOf(Object o) {
				if (o instanceof String) {
					return super.indexOf(((String)o).toLowerCase());
				}
				return super.indexOf(o);
			}
			@Override
			public boolean contains(Object o) {
				throw new RuntimeException("not implemented yet");
			}
		};
		boolValues.add(FALSE_AS_STRING);			
		boolValues.add(TRUE_AS_STRING);
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
