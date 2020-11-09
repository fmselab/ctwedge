package ctwedge.generator.medici;

import ctwedge.ctWedge.AndExpression;
import ctwedge.ctWedge.AtomicPredicate;
import ctwedge.ctWedge.Bool;
import ctwedge.ctWedge.CitModel;
import ctwedge.ctWedge.Constraint;
import ctwedge.ctWedge.EqualExpression;
import ctwedge.ctWedge.NotExpression;
import ctwedge.ctWedge.OrExpression;
import ctwedge.ctWedge.util.CtWedgeSwitch;
import ctwedge.generator.util.ParameterElementsGetterAsStrings;
import ctwedge.util.ParameterValuesToInt;

/** 
 * 
 * it converts every expression to a list of numbers (as strings) using the medici syntax
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
			String eqToInt = valConverter.eqToInt((AtomicPredicate)x.getLeft(), x.getOp(), (AtomicPredicate)x.getRight());
			// can be + n or - n
			assert eqToInt.startsWith("+ ") || eqToInt.startsWith("- ");
			//
			if (eqToInt.startsWith("+ ")) return eqToInt.substring(2);
			else return eqToInt.substring(2) + " -";
		} else 
			throw new RuntimeException("Not all constraints are supported in medici : " + x.getLeft().getClass() + "=" + x.getRight().getClass());
	}


	
	@Override
	public String caseAtomicPredicate(AtomicPredicate x) {
		// in case the predicate is not in an EqualExpression
		String name = x.getName();		
		if (valConverter.getParamByName(name) instanceof Bool) {
			int base = valConverter.get(name);
			int value = base + ParameterElementsGetterAsStrings.instance.doSwitch(valConverter.getParamByName(name)).indexOf("true");
			return Integer.toString(value);
		}
		return super.caseAtomicPredicate(x);
	}
	
	@Override
	public String caseNotExpression(NotExpression x) {
		return doSwitch(x.getPredicate()) + " -";
	}
	
	@Override
	public String caseConstraint(Constraint x) {
		return doSwitch(x);
	}
}
