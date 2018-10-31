package ctwedge.generator.casa;

import ctwedge.ctWedge.AndExpression;
import ctwedge.ctWedge.AtomicPredicate;
import ctwedge.ctWedge.EqualExpression;
import ctwedge.ctWedge.ImpliesExpression;
import ctwedge.ctWedge.NotExpression;
import ctwedge.ctWedge.OrExpression;
import ctwedge.ctWedge.Range;
import ctwedge.ctWedge.RelationalExpression;
import ctwedge.ctWedge.util.CtWedgeSwitch;

public class CASASwitchVerifier extends CtWedgeSwitch<Boolean> {
	@Override
	public Boolean caseRange(Range object) {
		return true;
	}
	
	@Override
	public Boolean caseRelationalExpression(RelationalExpression object) {
		return true;
	}
	
	@Override
	public Boolean caseAtomicPredicate(AtomicPredicate object) {
		return false;
	}
	
	@Override
	public Boolean caseEqualExpression(EqualExpression object) {
		Boolean a = doSwitch(object.getLeft());
		Boolean b = doSwitch(object.getRight());
		return a || b;
	}
	
	@Override
	public Boolean caseAndExpression(AndExpression object) {
		Boolean a = doSwitch(object.getLeft());
		Boolean b = doSwitch(object.getRight());
		return a || b;
	}
	
	@Override
	public Boolean caseOrExpression(OrExpression object) {
		Boolean a = doSwitch(object.getLeft());
		Boolean b = doSwitch(object.getRight());
		return a || b;
	}
	
	@Override
	public Boolean caseImpliesExpression(ImpliesExpression object) {
		Boolean a = doSwitch(object.getLeft());
		Boolean b = doSwitch(object.getRight());
		return a || b;
	}
	
	@Override
	public Boolean caseNotExpression(NotExpression object) {
		return doSwitch(object.getPredicate());
	}
}
