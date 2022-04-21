package ctwedge.modelanalyzer;

import ctwedge.ctWedge.CitModel;

// constraints are forbidden tuples
public class NoConstraints extends CTWedgeModelAnalyzer{
	
	
	static public NoConstraints eInstance = new  NoConstraints();
	
	@Override
	public boolean process(CitModel model) {
		return model.getConstraints().stream().count() == 0 || model.getConstraints() == null;
	}
}
