package ctwedge.modelanalyzer;

import ctwedge.ctWedge.Bool;
import ctwedge.ctWedge.CitModel;
import ctwedge.ctWedge.Enumerative;
import ctwedge.ctWedge.Parameter;

// boolean prameters (bool or enum with 2 values can be converted to bool)
public class BooleanOnlyParameters extends CTWedgeModelAnalyzer{
	
	
	static public BooleanOnlyParameters eInstance = new  BooleanOnlyParameters();
	
	@Override
	public boolean process(CitModel model) {
		for (Parameter p: model.getParameters()) {
			if (p instanceof Bool) continue;
			if (p instanceof Enumerative && ((Enumerative)p).getElements().size() == 2) continue;
			return false;
		}		
		return true;
	} 
}
