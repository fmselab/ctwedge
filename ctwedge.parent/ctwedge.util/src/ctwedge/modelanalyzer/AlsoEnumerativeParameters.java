package ctwedge.modelanalyzer;

import ctwedge.ctWedge.CitModel;
import ctwedge.ctWedge.Enumerative;
import ctwedge.ctWedge.Parameter;

// boolean prameters (bool or enum with 2 values can be converted to bool)
public class AlsoEnumerativeParameters extends CTWedgeModelAnalyzer{
	
	
	static public AlsoEnumerativeParameters eInstance = new  AlsoEnumerativeParameters();
	
	@Override
	public boolean process(CitModel model) {
		for (Parameter p: model.getParameters()) {
			if (p instanceof Enumerative && ((Enumerative)p).getElements().size() != 2) continue;
			return false;
		}		
		return true;
	} 
}
