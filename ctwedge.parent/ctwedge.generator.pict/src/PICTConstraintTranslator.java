import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;

import ctwedge.ctWedge.AndExpression;
import ctwedge.ctWedge.AtomicPredicate;
import ctwedge.ctWedge.Bool;
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


public class PICTConstraintTranslator extends CtWedgeSwitch<String> {

	
	private static final String ACTS_OP_NEQ = "<>";
	private static final String ACTS_OP_AND = " AND ";
	private static final String ACTS_OP_OR = " OR ";
	private static final String ACTS_OP_EQ = "=";

	Set<String> elements = new HashSet<>();
	final Map<String,Parameter> params = new HashMap<>();
	
	public PICTConstraintTranslator(CitModel citModel) {
		for (Parameter p : citModel.getParameters()) {
			params.put(p.getName(), p);
			if (p instanceof Enumerative) for (Element e : ((Enumerative)p).getElements()) elements.add(e.getName());
		}
	}

	@Override
	public String caseConstraint(Constraint x) {
		//System.out.println("CaseRule: "+x);
		//System.out.println("Dump: "+dump(x,""));
		return this.doSwitch(x);
	}
	
	@Override
	public String caseNotExpression(NotExpression x) {
		//System.out.println("caseNotExpression: "+x);
		return "NOT ("+doSwitch(x.getPredicate())+")";
	}
	
	@Override
	public String caseAtomicPredicate(AtomicPredicate x) {
		//System.out.println("Case AtomicPredicate: "+x);
		//	caso boolean
		String name = x.getName();
		if (name!=null && params.get(name) instanceof Bool)
			return squareBrackets(x.getName()) + " = \"true\"";
		if (params.containsKey(name))
			return squareBrackets(name);
		String res = x.getName()!=null ? (elements.contains(x.getName()) && !StaticUtils.INSTANCE.isNumber(x.getName()) ? "\""+x.getName()+"\"" : x.getName()) : (x.getBoolConst()!=null ? "\"" + x.getBoolConst() + "\"" : "");
		return res;
	}
	
	@Override
	public String caseEqualExpression(EqualExpression x) {
		String left = this.doSwitch(x.getLeft());
		String right = this.doSwitch(x.getRight());
		if (right.equals("\"true\"") || right.equals("\"false\""))
			left = left.substring(0, left.indexOf("]")+1);
		switch (x.getOp()) {
		case EQ:
			return left + ACTS_OP_EQ + right;
		case NE:
			return left + ACTS_OP_NEQ + right;
		default: throw new RuntimeException("Operator not found in constraint");
		}
	}

	@Override
	public String caseImpliesExpression(ImpliesExpression ruleExpr) {
		String leftVal = this.doSwitch(ruleExpr.getLeft());
		String rightVal = this.doSwitch(ruleExpr.getRight());
		switch (ruleExpr.getOp()) {
		case IMPL:
			return "IF " + parathesis(leftVal, " THEN ", rightVal);
		case IFF:
			return "IF " + parathesis(leftVal, " THEN ", rightVal) + ";\n" +
					"IF " + parathesis(rightVal, " THEN ", leftVal);
		}
		throw new RuntimeException("Operator not found in seed");

	}

	private String parathesis(String leftVal, String op, String rightVal) {
		return "(" + leftVal + ")" + op + "(" + rightVal + ")";
	}
	
	private String squareBrackets(String s) {
		return "[" + s + "]";
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
			return numerical + " >= " + value2;
		case GT:
			return numerical + ">" + value2;
		case LE:
			return numerical + "<=" + value2;
		case LT:
			return numerical + "<" + value2;
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
