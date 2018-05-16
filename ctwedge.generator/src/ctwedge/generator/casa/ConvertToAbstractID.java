package ctwedge.generator.casa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import ctwedge.ctWedge.Operators;
import ctwedge.ctWedge.OrExpression;
import ctwedge.ctWedge.Parameter;
import ctwedge.ctWedge.util.CtWedgeSwitch;
import ctwedge.generator.casa.CNF.Clause;
import ctwedge.generator.util.IndexOutOfBoundException;
import ctwedge.generator.util.ParameterElementsGetterAsStrings;
import ctwedge.generator.util.ParameterSize;
import ctwedge.util.Utils;

public class ConvertToAbstractID extends CtWedgeSwitch<List<String>> {

	Map<Parameter, Integer> offsets = new HashMap<>();
	CitModel model;
	
	final Map<String,Parameter> params;
	
	final Utils u;

	public ConvertToAbstractID(CitModel citModel) {
		params = new HashMap<>();
		
		model = citModel;
		// init offsets
		int offset = 0;
		for (Parameter p : citModel.getParameters()) {
			offsets.put(p, offset);
			int size = ParameterSize.eInstance.doSwitch(p);
			offset += size;
			params.put(p.getName(), p);
		}
		
		u = new Utils(citModel);
	}

	// convert an integer to a couple parameter + its value
	// example offsets 0,10,12
	// 1 -> first offset 0
	public Pair<Parameter, String> convertInt(int n) {
		// get the first offset that is suitable
		// the greatest offset between the minors or equals to n
		// init to the first one
		Parameter selectedPara = null;
		Integer selectedOffset = -1;
		// search among all
		for (Entry<Parameter, Integer> po : offsets.entrySet()) {
			// if this one is best, take this
			Integer currentOffset = po.getValue();
			// currentOffset must minor or equals n
			if (currentOffset <= n && currentOffset > selectedOffset) {
				// po is a good candidate and it is better than the current one
				selectedPara = po.getKey();
				selectedOffset = currentOffset;
			}
		}
		assert selectedPara != null;
		int singleOffset = n - selectedOffset;//
		assert singleOffset >= 0;
		List<String> doSwitch = ParameterElementsGetterAsStrings.instance.doSwitch(selectedPara);
		if (singleOffset >= doSwitch.size()) {
			throw new IndexOutOfBoundException(n, offsets);
		}
		String string = doSwitch.get(singleOffset);
		return Pair.of(selectedPara, string);
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
			assert (x.getOp()==Operators.EQ || x.getOp()==Operators.NE);
			String left = ((AtomicPredicate)x.getLeft()).getName();
			String right = ((AtomicPredicate)x.getRight()).getName();
			
			int value=-1;
			
			if (u.enums.containsKey(left) && u.elems.contains(right)) {
				int base = offsets.get(params.get(left));
				value = base + ParameterElementsGetterAsStrings.instance.doSwitch(params.get(left)).indexOf(right);
			} 
			else if (u.enums.containsKey(right) && u.elems.contains(left)) {
				int base = offsets.get(params.get(right));
				value = base + ParameterElementsGetterAsStrings.instance.doSwitch(params.get(right)).indexOf(left);
			} 
			else if (params.get(left) instanceof Bool && ((AtomicPredicate)x.getRight()).getBoolConst()!=null) {
				int base = offsets.get(params.get(left));
				value = base + ParameterElementsGetterAsStrings.instance.doSwitch(params.get(left)).indexOf(((AtomicPredicate)x.getRight()).getBoolConst());
			}
			else if (((AtomicPredicate)x.getLeft()).getBoolConst()!=null && params.get(right) instanceof Bool) {
				int base = offsets.get(params.get(right));
				value = base + ParameterElementsGetterAsStrings.instance.doSwitch(params.get(right)).indexOf(((AtomicPredicate)x.getLeft()).getBoolConst());
			}
			else if (u.ranges.containsKey(left)) {
				int base = offsets.get(params.get(left));
				value = base + Integer.parseInt(right) - u.ranges.get(left)[0];
			}
			else if (u.ranges.containsKey(right)) {
				int base = offsets.get(params.get(right));
				value = base + Integer.parseInt(left) - u.ranges.get(right)[0];
			}
			else 
				throw new RuntimeException("equalExpression not supported!");
			
			String sign = x.getOp()==Operators.EQ ? "+" : "-";
			List<String> res = new ArrayList<>();
			res.add(sign + " " + value);
			return res;
		} else throw new RuntimeException("Not all constraints are supported in CASA");
	}
	
	@Override
	public List<String> caseAtomicPredicate(AtomicPredicate x) {
		// in case the predicate is not in an EqualExpression
		String name = x.getName();
		if (params.get(name) instanceof Bool) {
			int base = offsets.get(params.get(name));
			int value = base + ParameterElementsGetterAsStrings.instance.doSwitch(params.get(name)).indexOf("true");
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
			if (params.get(name) instanceof Bool) {
				int base = offsets.get(params.get(name));
				int value = base + ParameterElementsGetterAsStrings.instance.doSwitch(params.get(name)).indexOf("false");
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
			System.out.println("CNF: "+cnf);

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

}
