package ctwedge.util.validator;

import java.io.File;

import ctwedge.benchmarks.Benchmarks;
import ctwedge.ctWedge.CitModel;
import ctwedge.util.ModelUtils;
import ctwedge.util.Test;

/** Standalone validator for CTWedge */
public class CtWedgeStandaloneValidator {
	public static void main(String[] args) {
		try {
			File modelFile = new File(args[0]);
			CitModel model = Benchmarks.loadModel(Benchmarks.readFromFile(modelFile));
			Test test = new Test();
			int i=1;
			for (String p : (new ModelUtils(model)).paramValues.keySet()) test.put(p, args[i++]);
			System.out.println(new RuleEvaluator(test).doSwitch(model));
		} catch (Exception e) {
			System.out.println("Error. It takes the CTWedge model file path as first parameter, then the values assigned to each variable in the model");
			e.printStackTrace();
		}
	}
}
