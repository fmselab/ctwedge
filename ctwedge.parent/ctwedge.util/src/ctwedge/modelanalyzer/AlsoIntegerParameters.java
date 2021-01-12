package ctwedge.modelanalyzer;

import java.util.Arrays;

import ctwedge.ctWedge.CitModel;
import ctwedge.ctWedge.Element;
import ctwedge.ctWedge.Enumerative;
import ctwedge.ctWedge.Parameter;
import ctwedge.ctWedge.Range;

// boolean prameters (bool or enum with 2 values can be converted to bool)
public class AlsoIntegerParameters extends CTWedgeModelAnalyzer{
	
	
	static public AlsoIntegerParameters eInstance = new  AlsoIntegerParameters();
	
	@Override
	public boolean process(CitModel model) {
		for (Parameter p: model.getParameters()) {
			if (p instanceof Range) return true;
			if (p instanceof Enumerative) {				
				if (Arrays.stream(((Enumerative)p).getElements().toArray()).allMatch(x -> isInteger((Element)x)))
					return true;
			}
		}		
		return false;
	} 
	
	private boolean isInteger(Element e) {
		try {
			Double.parseDouble(e.getName());
			return true;
		} catch(NumberFormatException ex) {
			return false;
		}
	}
}
