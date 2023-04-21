package ctwedge.util.validator;

import java.util.List;
import java.util.Map.Entry;

import ctwedge.ctWedge.CitModel;
import ctwedge.ctWedge.Parameter;
import ctwedge.util.Pair;
import ctwedge.util.Test;

public class ParametersEvaluator {
	ParameterSwitchToPairStrings evaluator;
	Test test;
	List<List<Pair<Parameter, String>>> values;
	
	public ParametersEvaluator(CitModel model, Test test) {
		this.test = test;
		values = ParameterSwitchToPairStrings.getListPairsParameterValues(model);
	}
	
	public boolean isTestOk() {
		for (Entry<String, String> t : test.entrySet()) {
			boolean found = false;
			for (List<Pair<Parameter, String>> pairList : values) {
				for(Pair<Parameter, String> pair : pairList) {
					if (pair.getFirst().getName().equals(t.getKey()) && pair.getSecond().equals(t.getValue()))
						found = true;
				}
			}
			if (!found)
				return false;
		}
		return true;
	}
}
