package ctwedge.generator.cagen;

import ctwedge.ctWedge.CitModel;
import ctwedge.ctWedge.Constraint;
import ctwedge.ctWedge.Parameter;
import ctwedge.util.ParameterElementsGetterAsStrings;

public class CAgenTranslator {
	
	public String paramToCAgenCode(CitModel citModel) {
		String s = "";
		for (Parameter param : citModel.getParameters()) {
			s += param.getName() + "(enum): ";
			for (String p : ParameterElementsGetterAsStrings.instance.doSwitch(param))
				s += p + ", ";
			s = s.substring(0, s.length()-2);
			s += "\n";
		}
		s += "\n";
		return s;
	}
	
	public String constraintToCAgenCode(CitModel citModel) {
		String s = "";
		CAgenConstraintTranslator constraintTranslator = new CAgenConstraintTranslator(citModel);
		for (Constraint constr : citModel.getConstraints()) {
			s += constraintTranslator.doSwitch(constr) + "\n";
		}
		return s;
	}
	

}
