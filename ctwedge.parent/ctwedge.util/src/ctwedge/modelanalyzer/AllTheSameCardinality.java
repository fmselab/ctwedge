package ctwedge.modelanalyzer;

import java.util.ArrayList;
import java.util.List;

import ctwedge.ctWedge.Bool;
import ctwedge.ctWedge.CitModel;
import ctwedge.ctWedge.Enumerative;
import ctwedge.ctWedge.Parameter;
import ctwedge.ctWedge.Range;

// boolean prameters (bool or enum with 2 values can be converted to bool)
public class AllTheSameCardinality extends CTWedgeModelAnalyzer{
	
	
	static public AllTheSameCardinality eInstance = new  AllTheSameCardinality();
	
	@Override
	public boolean process(CitModel model) {
		List<Integer> cardinalities = new ArrayList<Integer>();
		for (Parameter p: model.getParameters()) {
			if (p instanceof Bool) cardinalities.add(2);
			if (p instanceof Enumerative) cardinalities.add(((Enumerative)p).getElements().size());
			if (p instanceof Range) cardinalities.add((((Range)p).getEnd()-((Range)p).getBegin())/(((Range)p).getStep()));
		}		
		return allTheSameCardinality(cardinalities);
	} 
	
	private boolean allTheSameCardinality(List<Integer> cardinalities) {
		for (int i=1; i<cardinalities.size(); i++)
			if (cardinalities.get(i) != cardinalities.get(i-1))
				return false;
		return true;
	}
}
