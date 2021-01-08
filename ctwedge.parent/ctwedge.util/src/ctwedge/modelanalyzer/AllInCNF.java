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
			
			
			
			
			return splitAnd.stream().allMatch(x -> isSimpleEqual(x, Operators.EQ));
		}
		// A!=A1 or B!=B2 ...
		List<Expression> splitor = splitOr(e);
		return splitor.stream().allMatch(x -> isSimpleEqual(x, Operators.NE));
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
	// equal or not equal expression
	private boolean isSimpleEqual(Expression e, Operators operator) {
		if (e instanceof EqualExpression) {
			EqualExpression eqExpression = (EqualExpression) e;
			if (eqExpression.getOp()==operator) {
				return (eqExpression.getLeft() instanceof AtomicPredicate) &&
						(eqExpression.getRight() instanceof AtomicPredicate);
			}
		}
		return false;
	}
}
