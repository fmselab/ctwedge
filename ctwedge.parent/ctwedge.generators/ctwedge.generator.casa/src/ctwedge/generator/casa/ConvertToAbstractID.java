package ctwedge.generator.casa;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.xbase.lib.Pair;

import ctwedge.ctWedge.AndExpression;
import ctwedge.ctWedge.AtomicPredicate;
import ctwedge.ctWedge.Bool;
import ctwedge.ctWedge.CitModel;
import ctwedge.ctWedge.Constraint;
import ctwedge.ctWedge.EqualExpression;
import ctwedge.ctWedge.Expression;
import ctwedge.ctWedge.NotExpression;
import ctwedge.ctWedge.OrExpression;
import ctwedge.ctWedge.Parameter;
import ctwedge.ctWedge.util.CtWedgeSwitch;
import ctwedge.generator.casa.CNF.Clause;
import ctwedge.generator.util.ParameterElementsGetterAsStrings;
import ctwedge.util.ParameterValuesToInt;

/** it converts every expression to a list of numbers (as strings)
 * 
 */
public class ConvertToAbstractID extends CtWedgeSwitch<List<String>> {

	private CitModel model;
	private ParameterValuesToInt valConverter;
	
	static Logger logger = Logger.getLogger(ConvertToAbstractID.class); 

	public ConvertToAbstractID(CitModel citModel) {
		valConverter = new ParameterValuesToInt(citModel);
		model = citModel;
	}


	@Override
	public List<String> caseAndExpression(AndExpression and) {
		List<String> res = new ArrayList<>();
		res.addAll(doSwitch(and.getLeft()));
		res.addAll(doSwitch(and.getRight()));
		return res;
	}

	@Override
	public List<String> caseOrExpression(OrExpression or) {
		List<String> left=doSwitch(or.getLeft()), right=doSwitch(or.getRight());
		List<String> res = new ArrayList<>();
		for (String a : left) for (String b : right) res.add(a+" "+b);
		return res;
	}

	@Override
	public List<String> caseEqualExpression(EqualExpression x) {
		if (x.getLeft() instanceof AtomicPredicate && x.getRight() instanceof AtomicPredicate) {
			return Collections.singletonList(valConverter.eqToInt((AtomicPredicate)x.getLeft(), x.getOp(), (AtomicPredicate)x.getRight()));
		} else 
			throw new RuntimeException("Not all constraints are supported in CASA : " + x.getLeft().getClass() + "=" + x.getRight().getClass());
	}


	
	@Override
	public List<String> caseAtomicPredicate(AtomicPredicate x) {
		// in case the predicate is not in an EqualExpression
		String name = x.getName();
		if (valConverter.getParamByName(name) instanceof Bool) {
			int base = valConverter.get(name);
			int value = base + ParameterElementsGetterAsStrings.instance.doSwitch(valConverter.getParamByName(name)).indexOf("true");
			List<String> list = new ArrayList<>();
			list.add("+ "+value);
			return list;
		}
		return super.caseAtomicPredicate(x);
	}
	
	@Override
	public List<String> caseNotExpression(NotExpression x) {
		// in case the predicate is not in an EqualExpression
		if (x.getPredicate() instanceof AtomicPredicate) {
			String name = ((AtomicPredicate) x.getPredicate()).getName();
			if (valConverter.getParamByName(name) instanceof Bool) {
				int base = valConverter.get(name);
				int value = base + ParameterElementsGetterAsStrings.instance.doSwitch(valConverter.getParamByName(name)).indexOf("false");
				List<String> list = new ArrayList<>();
				list.add("+ "+value);
				return list;
			}
		}
		return super.caseNotExpression(x);
	}
	
	@Override
	public List<String> caseConstraint(Constraint x) {
		return doSwitch(x);
	}
	
	public StringBuffer translateConstraints() throws Exception {
		int counter = 0;
		StringBuffer clauses = new StringBuffer();
		StringBuffer buffer = new StringBuffer();
		EList<Constraint> constraints = model.getConstraints();
		// [number of disjunctive clauses] \
		// TODO: fix it is the sum

		for (Constraint r : constraints) {
			CNF cnf = new CNFConverter().convertToCNF((Expression)r);
			logger.info("CNF: "+cnf);

			for (Clause cl : cnf.clauses) {
				int size=cl.literals.size();
				clauses.append(size + "\n");
				for (Expression e : cl.literals) clauses.append(doSwitch(e).get(0) + " ");
				clauses.append("\n");
				counter++;
			}

		}
		buffer.append(counter + "\n");
		buffer.append(clauses);
		return buffer;
	}

	@Deprecated
	public StringBuffer translateConstraintsWithoutCNFConversion() {
		int counter = 0;
		StringBuffer clauses = new StringBuffer();
		StringBuffer buffer = new StringBuffer();
		EList<Constraint> constraints = model.getConstraints();
		// [number of disjunctive clauses] \
		// TODO: fix it is the sum

		for (Constraint r : constraints) {
			List<String> res = doSwitch(r);
			System.out.println(res);

			for (String c : res) {
				int size=0;
				for (int i=0; i<c.length(); i++) if (c.charAt(i)=='+' || c.charAt(i)=='-') size++;
				clauses.append(size + "\n" + c + "\n");
				counter++;
			}

		}
		buffer.append(counter + "\n");
		buffer.append(clauses);
		return buffer;
	}

	//back from int to paramter and string
	public Pair<Parameter, String> convertInt(int parseInt) {
		return valConverter.convertInt(parseInt);
	}

}
