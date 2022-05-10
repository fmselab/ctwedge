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
import ctwedge.ctWedge.Expression;
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
	static boolean PRINT = false;
	
	// the constraint to be translated;
	Set<String> elements = new HashSet<>();
	
	public ACTSConstraintTranslator(CitModel citModel) {
		for (Parameter p : citModel.getParameters()) {
			if (p instanceof Enumerative) for (Element e : ((Enumerative)p).getElements()) elements.add(e.getName());
		}
	}

	@Override
	public String caseConstraint(Constraint x) {
		if (PRINT) System.out.println("CaseRule: "+x);
		if (PRINT) System.out.println("Dump: "+dump(x,""));
		return this.doSwitch(x);
	}
	
	@Override
	public String caseNotExpression(NotExpression x) {
		if (PRINT) System.out.println("caseNotExpression: "+x);
		return "! ("+doSwitch(x.getPredicate())+")";
	}
		
	@Override
	public String caseAtomicPredicate(AtomicPredicate x) {
		// if not null, it does not contain !
		assert x.getName() == null || !x.getName().contains("!");
		if (PRINT) System.out.println("Case AtomicPredicate: "+x);
		
		// AB: Is it to the left?
		// Normally we have the parameter name to the left of the Equal expression
		if (x.eContainer() instanceof EqualExpression) {
			EqualExpression container = (EqualExpression) x.eContainer();
			if (container.getLeft().equals(x) && x.getName() != null) {
				return x.getName();
			}
		}
		
		return x.getName() != null ? (elements.contains(x.getName()) && !StaticUtils.INSTANCE.isNumber(x.getName())
				? "\"" + x.getName() + "\""
				: x.getName()) : (x.getBoolConst() != null ? x.getBoolConst() : "");
	}
	
	@Override
	public String caseEqualExpression(EqualExpression x) {
		if (PRINT) System.out.println("caseEqualExpression: "+x);
		switch (x.getOp()) {
		case EQ:
			return parathesis(x.getLeft(), ACTS_OP_EQ, x.getRight());
		case NE:
			return parathesis(x.getLeft(), ACTS_OP_NEQ, x.getRight());
		default: throw new RuntimeException("Operator not found in constraint");
		}
	}

	@Override
	public String caseImpliesExpression(ImpliesExpression ruleExpr) {
		if (PRINT) System.out.println("caseImpliesExpression: "+ruleExpr);
		switch (ruleExpr.getOp()) {
		case IMPL:
			return parathesis(ruleExpr.getLeft(), ACTS_OP_IMPLIES, ruleExpr.getRight());
		case IFF:
			return parathesis(parathesis(ruleExpr.getLeft(), ACTS_OP_IMPLIES, ruleExpr.getRight()),
					ACTS_OP_AND, parathesis(ruleExpr.getRight(), ACTS_OP_IMPLIES, ruleExpr.getLeft()));
		}
		throw new RuntimeException("Operator not found in seed");
	}
	private String parathesis(Expression left, String op, Expression right) {
		String leftVal = this.doSwitch(left);
		String rightVal = this.doSwitch(right);
		String result = "";
		if (left instanceof AtomicPredicate ) 			result += leftVal;
		else result += "(" + leftVal + ")";
		result += " " + op + " ";
		if (right instanceof AtomicPredicate ) 			result += rightVal;
		else result += "(" + rightVal + ")";
		return result;
		
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
		return parathesis(orExpr.getLeft(), ACTS_OP_OR, orExpr.getRight());
	}

	@Override
	public String caseAndExpression(AndExpression andExpr) {
		return parathesis(andExpr.getLeft(), ACTS_OP_AND, andExpr.getRight());
	}

	@Override
	public String caseRelationalExpression(RelationalExpression ineqExpr) {
		switch (ineqExpr.getOp()) {
		case GE:
			return parathesis(ineqExpr.getLeft(), ">=", ineqExpr.getRight());
		case GT:
			return parathesis(ineqExpr.getLeft(), ">", ineqExpr.getRight());
		case LE:
			return parathesis(ineqExpr.getLeft(), "<=", ineqExpr.getRight());
		case LT:
			return parathesis(ineqExpr.getLeft(), "<", ineqExpr.getRight());
		default: throw new RuntimeException("Operator not found in constraint");

		}
	}

	@Override
	public String casePlusMinus(PlusMinus pm) {
		if (pm.getOp() == PlusMinusOperators.MINUS)
			return parathesis(pm.getLeft(), "-", pm.getRight());
		else
			return parathesis(pm.getLeft(), "+", pm.getRight());
	}

	@Override
	public String caseModMultDiv(ModMultDiv md) {
		switch (md.getOp()) {
		case DIV:
			return parathesis(md.getLeft(), "/", md.getRight());
		case MULT:
			return parathesis(md.getLeft(), "*", md.getRight());
		case MOD:
			return parathesis(md.getLeft(), "%", md.getRight());
		}
		throw new RuntimeException("Operator not found");
	}
	
	static String dump(EObject mod_, String indent) {
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
