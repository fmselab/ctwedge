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
import ctwedge.ctWedge.OrExpression;
import ctwedge.ctWedge.RelationalExpression;

// constraints are forbidden tuples
public class AllInCNF extends CTWedgeModelAnalyzer{
	
	
	static public AllInCNF eInstance = new  AllInCNF();
	
	@Override
	public boolean process(CitModel model) {
		return model.getConstraints().stream().allMatch( c -> isCNF(c));
	} 
	
	private boolean isCNF(Constraint c) {
		// (...) AND (...)
		Expression e = (Expression) c;
		if (c instanceof AndExpression) {
			List<Expression> splitAnd = splitAnd(e);		
			return splitAnd.stream().allMatch(x -> notContainsAnd(x));
		}
		// If it is not an AND, it should not contain an AND both in right or and
		return notContainsAnd((Expression) c);
//		else if (c instanceof NotExpression) {
//			// !((...) OR (....))
//			List<Expression> splitor = splitOr(((NotExpression) c).getPredicate());
//			return splitor.stream().allMatch(x -> isCNF((Expression) x));
//		}
		//return false;
		
	}
	
	// if e is e1 and e2 and ... en returns [e1,..en] 
	private List<Expression> splitAnd(Expression e){
		if (e instanceof AndExpression) {
			List<Expression> results = new ArrayList<>();
			results.addAll(splitAnd(((AndExpression) e).getLeft()));			
			results.addAll(splitAnd(((AndExpression) e).getRight()));
			return results;
		} else {
			return Collections.singletonList(e);
		}
	}
	
	// the same for OR
	private List<Expression> splitOr(Expression e){
		if (e instanceof OrExpression) {
			List<Expression> results = new ArrayList<>();
			results.addAll(splitAnd(((OrExpression) e).getLeft()));			
			results.addAll(splitAnd(((OrExpression) e).getRight()));
			return results;
		} else {
			return Collections.singletonList(e);
		}
	}
	
	// Check if the expression does not contain another and
	private boolean notContainsAnd(Expression e) {
		// CNF is composed by a set of OR expressions
		if (e instanceof OrExpression) {
			OrExpression orExpression = (OrExpression) e;
			// Verify that on the right and on the left, the expression does not contain an AND
			return notContainsAnd(orExpression.getLeft()) && notContainsAnd(orExpression.getRight());
		}
		
		// CNF can be seen as an AND of Atomic Predicates
		if (e instanceof AtomicPredicate || e instanceof EqualExpression || e instanceof RelationalExpression) 
			return true;
		
		if (e instanceof NotExpression)
			return notContainsAnd(((NotExpression)e).getPredicate());
		
		// It is not a CNF form
		return false;
	}
}
