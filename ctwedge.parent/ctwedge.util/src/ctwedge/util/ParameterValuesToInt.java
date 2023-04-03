package ctwedge.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ctwedge.ctWedge.AtomicPredicate;
import ctwedge.ctWedge.Bool;
import ctwedge.ctWedge.CitModel;
import ctwedge.ctWedge.Operators;
import ctwedge.ctWedge.Parameter;
import ctwedge.generator.util.IndexOutOfBoundException;
import ctwedge.generator.util.ParameterElementsGetterAsStrings;
import ctwedge.generator.util.ParameterSize;
import ctwedge.util.ext.NotConvertableModel;

/** converts any value of a parameter to unique integers */
public class ParameterValuesToInt {
	
	private Map<Parameter, Integer> offsets = new HashMap<>();
	private final ModelUtils u;
	// string -> parameter
	private final Map<String,Parameter> params;
	
	public ParameterValuesToInt(CitModel citModel) {
		params = new HashMap<>();
		// init offsets
		int offset = 0;
		for (Parameter p : citModel.getParameters()) {
			offsets.put(p, offset);
			int size = ParameterSize.eInstance.doSwitch(p);
			offset += size;
			params.put(p.getName(), p);
		}
		u = new ModelUtils(citModel);
	}	
	
	
	// convert an integer to a couple parameter + its value
	// example offsets 0,10,12
	// 1 -> first offset 0
	public ctwedge.util.Pair<Parameter, String> convertInt(int n) {
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
		return new Pair<Parameter, String>(selectedPara, string);
	}

	// parameter -> int
	public int get(Parameter parameter) {
		return offsets.get(parameter);
	}
	// parameter name -> int
	public int get(String parameter) {		
		return offsets.get(getParamByName(parameter));
	}
	
	public Parameter getParamByName(String pn) {
		assert params.containsKey(pn) : pn;
		return params.get(pn);
	}
	
	
	// convert the equal expression to an sign + integer
	// char + o -
	// integer: tha value
	public Pair<Character,Integer> eqToInt(AtomicPredicate leftPred, Operators op, AtomicPredicate rightPred) {
		assert (op==Operators.EQ || op ==Operators.NE);
		// attenzione potrebbero essere dei booleani
		String left = leftPred.getName()  != null? leftPred.getName().replaceAll("\"", "") : null;
		String right = rightPred.getName() != null? rightPred.getName().replaceAll("\"", "") : null;
		
		int value = -1;
		// enum1 OP enum2
		if (left!= null && right != null && u.enums.containsKey(left) && u.elems.contains(right)) {
			int base = offsets.get(params.get(left));
			List<String> doSwitch = ParameterElementsGetterAsStrings.instance.doSwitch(params.get(left));
			value = base + doSwitch.indexOf(right);
		} 
		else if (left!= null && right != null && u.enums.containsKey(right) && u.elems.contains(left)) {
			int base = offsets.get(params.get(right));
			value = base + ParameterElementsGetterAsStrings.instance.doSwitch(params.get(right)).indexOf(left);
		}
		// a = true/false
		else if (left != null && params.get(left) instanceof Bool && rightPred.getBoolConst()!=null) {
			int base = offsets.get(params.get(left));
			List<String> doSwitch = ParameterElementsGetterAsStrings.instance.doSwitch(params.get(left));
			value = base + doSwitch.indexOf(rightPred.getBoolConst());
		}
		else if (right!= null && leftPred.getBoolConst()!=null && params.get(right) instanceof Bool) {
			int base = offsets.get(params.get(right));
			value = base + ParameterElementsGetterAsStrings.instance.doSwitch(params.get(right)).indexOf(leftPred.getBoolConst());
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
			throw new NotConvertableModel("equalExpression : " + left + " " + op.getName() + " " + right + " not supported!");
		assert value != -1 : " left = " + left + " right = " + right + " map: " + params;
		// now builde the String for the expression
		char sign = op == Operators.EQ ? '+' : '-';
		return new Pair<>(sign, value);
	}
}
