package ctwedge.modelanalyzer;

import ctwedge.ctWedge.Bool;
import ctwedge.ctWedge.CitModel;
import ctwedge.ctWedge.Parameter;

public class BooleanOnlyParameters extends CTWedgeModelAnalyzer{
	
	
	static public BooleanOnlyParameters eInstance = new  BooleanOnlyParameters();
	
	@Override
	public boolean process(CitModel model) {
		for (Parameter p: model.getParameters()) {
			if (! (p instanceof Bool)) return false;
		}		
		return true;
	} 
}
