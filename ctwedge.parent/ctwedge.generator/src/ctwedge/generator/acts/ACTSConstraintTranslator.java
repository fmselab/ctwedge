package ctwedge.generator.acts;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;

import ctwedge.ctWedge.AndExpression;
import ctwedge.ctWedge.AtomicPredicate;
import ctwedge.ctWedge.CitModel;
import ctwedge.ctWedge.Constraint;
import ctwedge.ctWedge.Element;
import ctwedge.ctWedge.Enumerative;
import ctwedge.ctWedge.EqualExpression;
import ctwedge.ctWedge.ImpliesExpression;
import ctwedge.ctWedge.ModMultDiv;
import ctwedge.ctWedge.NotExpression;
import ctwedge.ctWedge.OrExpression;
import ctwedge.ctWedge.Parameter;
import ctwedge.ctWedge.PlusMinus;
import ctwedge.ctWedge.PlusMinusOperators;
import ctwedge.ctWedge.RelationalExpression;
import ctwedge.ctWedge.util.CtWedgeSwitch;
import ctwedge.util.StaticUtils;

/**
 * translates a constraints in the ACTS language
 * 
 * @author garganti
 * 
 */
public class ACTSConstraintTranslator extends CtWedgeSwitch<String> {

	private static final String ACTS_OP_NEQ = "!=";
	private static final String ACTS_OP_AND = "&&";
	private static final String ACTS_OP_OR = "||";
	private static final String ACTS_OP_IMPLIES = "=>";
	@SuppressWarnings("unused")
	private static final String ACTS_OP_IFF = "=";
	private static final String ACTS_OP_EQ = "=";
	
	// the constraint to be translated;
	Set<String> elements = new HashSet<>();
	
	public ACTSConstraintTranslator(CitModel citModel) {
		for (Parameter p : citModel.getParameters()) {
			if (p instanceof Enumerative) for (Element e : ((Enumerative)p).getElements()) elements.add(e.getName());
		}
	}

	@Override
	public String caseConstraint(Constraint x) {
		System.out.println("CaseRule: "+x);
		System.out.println("Dump: "+dump(x,""));
		return this.doSwitch(x);
	}
	
	@Override
	public String caseNotExpression(NotExpression x) {
		System.out.println("caseNotExpression: "+x);
		return "! ("+doSwitch(x.getPredicate())+")";
	}
	
	/*@Override
	public String caseNotExpression(NotExpression not) {
		System.out.println("caseNot: "+not+" ->"+not.getPredicate());
		assert not.getPredicate()!=null : "Predicate null";
		String predicate = this.doSwitch(not.getPredicate());
		return "! (" + predicate + ")";
	}*/
	
	@Override
	public String caseAtomicPredicate(AtomicPredicate x) {
		System.out.println("Case AtomicPredicate: "+x);
		return x.getName()!=null ? (elements.contains(x.getName()) && !StaticUtils.INSTANCE.isNumber(x.getName()) ? "\""+x.getName()+"\"" : x.getName()) : (x.getBoolConst()!=null ? x.getBoolConst() : "");
	}
	
	@Override
	public String caseEqualExpression(EqualExpression x) {
		String left = this.doSwitch(x.getLeft());
		String right = this.doSwitch(x.getRight());
		switch (x.getOp()) {
		case EQ:
			return parathesis(left, ACTS_OP_EQ, right);
		case NE:
			return parathesis(left, ACTS_OP_NEQ, right);
		default: throw new RuntimeException("Operator not found in constraint");
		}
	}

	@Override
	public String caseImpliesExpression(ImpliesExpression ruleExpr) {
		String leftVal = this.doSwitch(ruleExpr.getLeft());
		String rightVal = this.doSwitch(ruleExpr.getRight());
		switch (ruleExpr.getOp()) {
		case IMPL:
			return parathesis(leftVal, ACTS_OP_IMPLIES, rightVal);
		case IFF:
			return parathesis(parathesis(leftVal, ACTS_OP_IMPLIES, rightVal),
					ACTS_OP_AND, parathesis(rightVal, ACTS_OP_IMPLIES, leftVal));
		}
		throw new RuntimeException("Operator not found in seed");

	}

	private String parathesis(String leftVal, String op, String rightVal) {
		// choco does not accept negative integers
		// commentato sembra funzionare. Forse nelle operazioni? AG
		//if (rightVal.matches("-\\d+"))
		//	throw new RuntimeException("choco does not accept constraints with negative numbers");
		return "(" + leftVal + ")" + op + "(" + rightVal + ")";
	}

	@Override
	public String caseOrExpression(OrExpression orExpr) {
		String leftVal = this.doSwitch(orExpr.getLeft());
		String rightVal = this.doSwitch(orExpr.getRight());
		return parathesis(leftVal, ACTS_OP_OR, rightVal);
	}

	@Override
	public String caseAndExpression(AndExpression andExpr) {
		String leftVal = this.doSwitch(andExpr.getLeft());
		String rightVal = this.doSwitch(andExpr.getRight());
		return parathesis(leftVal, ACTS_OP_AND, rightVal);
	}

	@Override
	public String caseRelationalExpression(RelationalExpression ineqExpr) {
		String numerical = this.doSwitch(ineqExpr.getLeft());
		String value2 = this.doSwitch(ineqExpr.getRight());
		switch (ineqExpr.getOp()) {
		case GE:
			return parathesis(numerical, ">=", value2);
		case GT:
			return parathesis(numerical, ">", value2);
		case LE:
			return parathesis(numerical, "<=", value2);
		case LT:
			return parathesis(numerical, "<", value2);
		default: throw new RuntimeException("Operator not found in constraint");

		}
	}

	@Override
	public String casePlusMinus(PlusMinus pm) {
		String leftVal = this.doSwitch(pm.getLeft());
		String rightVal = this.doSwitch(pm.getRight());
		if (pm.getOp() == PlusMinusOperators.MINUS)
			return parathesis(leftVal, "-", rightVal);
		else
			return parathesis(leftVal, "+", rightVal);
	}

	@Override
	public String caseModMultDiv(ModMultDiv md) {
		String leftVal = this.doSwitch(md.getLeft());
		String rightVal = this.doSwitch(md.getRight());
		switch (md.getOp()) {
		case DIV:
			return parathesis(leftVal, "/", rightVal);
		case MULT:
			return parathesis(leftVal, "*", rightVal);
		case MOD:
			return parathesis(leftVal, "%", rightVal);
		}
		throw new RuntimeException("Operator not found");
	}
	
	public static String dump(EObject mod_, String indent) {
	    String res = indent + mod_.toString().replaceFirst(".*[.]impl[.](.*)Impl[^(]*", "$1 ");

	    for (EObject a :mod_.eCrossReferences())
	        res += " ->" + a.toString().replaceFirst(".*[.]impl[.](.*)Impl[^(]*", "$1 ");
	    res += "\n";
	    for (EObject f :mod_.eContents()) {
	        res += dump(f, indent+"    ");
	    }
	    return res;
	}

}
