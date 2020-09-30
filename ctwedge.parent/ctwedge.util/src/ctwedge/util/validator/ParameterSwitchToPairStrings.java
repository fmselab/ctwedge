package ctwedge.util.validator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.util.EList;

import ctwedge.ctWedge.*;
import ctwedge.ctWedge.util.CtWedgeSwitch;
import ctwedge.util.ModelUtils;
import ctwedge.util.Pair;

/**
 * Given a parameter returns the list of pairs <paramter,value> with values as
 * strings
 * 
 * @author garganti
 * 
 */
public class ParameterSwitchToPairStrings extends
		CtWedgeSwitch<List<Pair<Parameter, String>>> {

	private static ParameterSwitchToPairStrings eInstance = new ParameterSwitchToPairStrings();

	private ParameterSwitchToPairStrings() {

	}

	@Override
	public List<Pair<Parameter, String>> caseParameter(Parameter parameter) {
		return buildList(parameter);
	}

	private List<Pair<Parameter, String>> buildList(Parameter parameter) {
		List<Pair<Parameter, String>> elements = new ArrayList<Pair<Parameter, String>>();
		List<String> values = ParameterElementsGetterAsStrings.eInstance
				.doSwitch(parameter);
		for (String v : values) {
			elements.add(new Pair<Parameter, String>(parameter, v));
		}
		return elements;

	}

	/**
	 * return the list of the list of parameters and values (As strings). the
	 * order in which the parameters are taken is not set.
	 * 
	 * @param citModel
	 * @return
	 */
	static public List<List<Pair<Parameter, String>>> getListPairsParameterValues(
			CitModel citModel) {
		List<List<Pair<Parameter, String>>> elements = new ArrayList<List<Pair<Parameter, String>>>();
		EList<Parameter> vars = citModel.getParameters();
		ParameterSwitchToPairStrings switchP = ParameterSwitchToPairStrings.eInstance;
		for (Parameter p : vars) {
			elements.add(switchP.doSwitch(p));
		}
		return elements;
	}

	/**
	 * 
	 * @param citModel
	 * @param nWise
	 * @return
	 */
	static public Iterator<List<Pair<Parameter, String>>> getTuples(CitModel citModel, int nWise) {
		List<List<Pair<Parameter, String>>> elements = getListPairsParameterValues(citModel);
		return ModelUtils.getAllKWiseCombination(elements, nWise);
	}
}
