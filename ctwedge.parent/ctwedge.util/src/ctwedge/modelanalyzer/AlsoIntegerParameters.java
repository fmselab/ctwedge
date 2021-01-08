package ctwedge.modelanalyzer;

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
			if (p instanceof Range) continue;
			if (p instanceof Enumerative) {
				for (Element e : ((Enumerative)p).getElements())
					try {
						Double.parseDouble(e.getName());
					} catch(NumberFormatException ex) {
						return false;
					}
				continue;
			}
			return false;
		}		
		return true;
	} 
}
