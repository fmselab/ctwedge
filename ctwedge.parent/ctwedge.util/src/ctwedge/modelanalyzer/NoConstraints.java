package ctwedge.modelanalyzer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ctwedge.ctWedge.AndExpression;
import ctwedge.ctWedge.AtomicPredicate;
import ctwedge.ctWedge.CitModel;
import ctwedge.ctWedge.Constraint;
import ctwedge.ctWedge.EqualExpression;
import ctwedge.ctWedge.Expression;
import ctwedge.ctWedge.NotExpression;
import ctwedge.ctWedge.Operators;
import ctwedge.ctWedge.OrExpression;

// constraints are forbidden tuples
public class NoConstraints extends CTWedgeModelAnalyzer{
	
	
	static public NoConstraints eInstance = new  NoConstraints();
	
	@Override
	public boolean process(CitModel model) {
		return model.getConstraints().stream().count() == 0 || model.getConstraints() == null;
	}
}
