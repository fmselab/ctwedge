package ctwedge.benchmarks;

import java.io.File;

import ctwedge.ctWedge.CitModel;
import ctwedge.util.ModelUtils;
import ctwedge.util.Test;
import ctwedge.util.ext.Utility;
import ctwedge.util.validator.RuleEvaluator;

/** Standalone validator for CTWedge */
public class CtWedgeStandaloneValidator {
	public static void main(String[] args) {
		try {
			File modelFile = new File("model.ctw");
			CitModel model = Utility.loadModelFromPath(modelFile.getAbsolutePath());
			Test test = new Test();
			int i=0;
			for (String p : (new ModelUtils(model)).paramValues.keySet()) test.put(p, args[i++]);
			System.out.println(new RuleEvaluator(test).doSwitch(model));
		} catch (Exception e) {
			System.out.println("Error. It takes the CTWedge model file path as first parameter, then the values assigned to each variable in the model");
			e.printStackTrace();
		}
	}
}
