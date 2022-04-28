package ctwedge.generator.medici;

import org.eclipse.xtext.EcoreUtil2;

import ctwedge.ctWedge.AndExpression;
import ctwedge.ctWedge.AtomicPredicate;
import ctwedge.ctWedge.Bool;
import ctwedge.ctWedge.CitModel;
import ctwedge.ctWedge.Constraint;
import ctwedge.ctWedge.CtWedgeFactory;
import ctwedge.ctWedge.EqualExpression;
import ctwedge.ctWedge.Expression;
import ctwedge.ctWedge.ImpliesExpression;
import ctwedge.ctWedge.ImpliesOperator;
import ctwedge.ctWedge.NotExpression;
import ctwedge.ctWedge.Operators;
import ctwedge.ctWedge.OrExpression;
import ctwedge.ctWedge.Parameter;
import ctwedge.ctWedge.RelationalExpression;
import ctwedge.ctWedge.util.CtWedgeSwitch;
import ctwedge.generator.util.ParameterElementsGetterAsStrings;
import ctwedge.util.Pair;
import ctwedge.util.ParameterValuesToInt;
import ctwedge.util.ext.NotConvertableModel;

/**
 * 
 * it converts every expression to a list of numbers (as strings) using the
 * medici syntax
 * 
 */
public class ConstraintToMediciIds extends CtWedgeSwitch<String> {

	private ParameterValuesToInt valConverter;

	public ConstraintToMediciIds(CitModel citModel) {
		valConverter = new ParameterValuesToInt(citModel);
	}

	@Override
	public String caseAndExpression(AndExpression and) {
		return doSwitch(and.getLeft()) + " " + doSwitch(and.getRight()) + " *";
	}

	@Override
	public String caseOrExpression(OrExpression or) {
		return doSwitch(or.getLeft()) + " " + doSwitch(or.getRight()) + " +";
	}

	@Override
	public String caseEqualExpression(EqualExpression x) {
		if (x.getLeft() instanceof AtomicPredicate && x.getRight() instanceof AtomicPredicate) {
			Pair<Character, Integer> eqToInt = valConverter.eqToInt((AtomicPredicate) x.getLeft(), x.getOp(),
					(AtomicPredicate) x.getRight());
			// can be + n or - n
			assert eqToInt.getFirst() == '+' || eqToInt.getFirst() == '-';
			//
			if (eqToInt.getFirst() == '+')
				// ignore the +
				return eqToInt.getSecond().toString();
			else
				// postpone the not operator
				return eqToInt.getSecond().toString() + " -";
		} else {
			if (x.getOp() != Operators.EQ) throw new RuntimeException("equal expected"); 
			// If they are not atomic predicates, it means that the equal has been derived
			// from a double implication a <=> b
			// Let's convert it as (a and b) or (not a and not b)
			OrExpression orE = CtWedgeFactory.eINSTANCE.createOrExpression();
			AndExpression andER = CtWedgeFactory.eINSTANCE.createAndExpression();
			AndExpression andEL = CtWedgeFactory.eINSTANCE.createAndExpression();
			NotExpression notA = CtWedgeFactory.eINSTANCE.createNotExpression();
			NotExpression notB = CtWedgeFactory.eINSTANCE.createNotExpression();
			Expression left = EcoreUtil2.clone(x.getLeft());
			Expression right = EcoreUtil2.clone(x.getRight());
			Expression left2 = EcoreUtil2.clone(left);
			Expression right2 = EcoreUtil2.clone(right);

			andER.setLeft(left);
			andER.setRight(right);
			notA.setPredicate(left2);
			notB.setPredicate(right2);
			andEL.setLeft(notA);
			andEL.setRight(notB);
			orE.setLeft(andEL);
			orE.setRight(andER);
			return doSwitch(orE);
			/*
			 * throw new RuntimeException("Not all constraints are supported in medici : " +
			 * x.getLeft().getClass() + "=" + x.getRight().getClass());
			 */
		}
	}

	@Override
	public String caseAtomicPredicate(AtomicPredicate x) {
		// in case the predicate is not in an EqualExpression
		String name = x.getName();
		Parameter paramByName = valConverter.getParamByName(name);
		if (paramByName instanceof Bool) {
			int base = valConverter.get(name);
			int value = base + ParameterElementsGetterAsStrings.instance.doSwitch(paramByName).indexOf(ParameterElementsGetterAsStrings.TRUE_AS_STRING);
			assert value != -1;
			return Integer.toString(value);
		}
		return super.caseAtomicPredicate(x);
	}

	@Override
	public String caseNotExpression(NotExpression x) {
		return doSwitch(x.getPredicate()) + " -";
	}

	@Override
	public String caseImpliesExpression(ImpliesExpression impl) {
		if (impl.getOp() == ImpliesOperator.IMPL) {
			// convert to not A or B
			NotExpression notL = CtWedgeFactory.eINSTANCE.createNotExpression();
			notL.setPredicate(impl.getLeft());
			OrExpression or = CtWedgeFactory.eINSTANCE.createOrExpression();
			or.setLeft(notL);
			or.setRight(impl.getRight());
			return doSwitch(or);
		} else {
			// convert to equals 
			EqualExpression eqE = CtWedgeFactory.eINSTANCE.createEqualExpression();
			eqE.setLeft(impl.getLeft());
			eqE.setOp(Operators.EQ);
			eqE.setRight(impl.getRight());
			return doSwitch(eqE);
		}
	}
	
	@Override
	public String caseRelationalExpression(RelationalExpression object) {
		throw new NotConvertableModel("relational expression are not supported");
	}
	

	@Override
	public String caseConstraint(Constraint x) {
		return doSwitch(x);
	}
}
