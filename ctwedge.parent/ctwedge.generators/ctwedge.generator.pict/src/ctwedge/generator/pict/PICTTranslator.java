package ctwedge.generator.pict;

import ctwedge.ctWedge.CitModel;
import ctwedge.ctWedge.Constraint;
import ctwedge.ctWedge.Parameter;
import ctwedge.generator.util.ParameterElementsGetterAsStrings;

public class PICTTranslator{
	
	public String paramToPictCode(CitModel citModel) {
		String s = "";
		for (Parameter param : citModel.getParameters()) {
			s += param.getName() + " : ";
			for (String p : ParameterElementsGetterAsStrings.instance.doSwitch(param))
				s += p + ", ";
			s = s.substring(0, s.length()-2);
			s += "\n";
		}
		s += "\n";
		return s;
	}
	
	public String constraintToPictCode(CitModel citModel) {
		String s = "";
		PICTConstraintTranslator constraintTranslator = new PICTConstraintTranslator(citModel);
		for (Constraint constr : citModel.getConstraints()) {
			s += constraintTranslator.doSwitch(constr) + ";\n";
		}
		return s;
	}
	
	

}
