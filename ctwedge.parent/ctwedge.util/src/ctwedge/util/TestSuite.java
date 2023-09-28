package ctwedge.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ctwedge.ctWedge.CitModel;
import ctwedge.ctWedge.Parameter;

/** class for memorizing a test suite */
public class TestSuite {
	// string as CSV output
	private String ts;
	float generatorTime;
	String generatorName;
	int strength;
	CitModel model;
	List<Test> tests;

	/**
	 * @param ts    test in csv format (first line param names, other lines param
	 *              values)
	 * @param model the model
	 */
	public TestSuite(String ts, CitModel model) {
		this(ts,model,";");
	}		

	/**
	 * @param ts        test in csv format (first line param names, other lines
	 *                  param values)
	 * @param model     the model
	 * @param delimiter the {@link String} to be used to split the test cases of the
	 *                  ts parameter
	 */
	public TestSuite(String ts, CitModel model, String delimiter) {
		this.ts = ts;
		this.model = model;
		populateTestSuite(delimiter);
		generatorTime = Float.NaN;
	}

	/**
	 * Instantiates a new test suite.
	 *
	 * @param model     the model
	 * @param testSuite the test suite
	 */
	public TestSuite(CitModel model, List<Map<Parameter, ?>> testSuite) {
		this.model = model;
		tests = new ArrayList<>();
		if (testSuite != null) {
			for (Map<Parameter, ?> test : testSuite) {
				// all the paramters ar ein the model
				assert model.getParameters().containsAll(test.keySet());
				// convert <Parameter,?> to <String,String>
				Map<String, String> as = test.entrySet().stream()
						.collect(Collectors.toMap(e -> e.getKey().getName(), e -> e.getValue().toString()));
				tests.add(new Test(as));
			}
		}
		// TODO String ts is to be build
	}

	@Override
	public String toString() {
		return ts;
	}

	public void setGeneratorTime(float f) {
		this.generatorTime = f;
	}

	public void setGeneratorName(String generatorName) {
		this.generatorName = generatorName;
	}

	public void setStrength(int nWise) {
		this.strength = nWise;
	}

	public int getStrength() {
		return strength;
	}

	public String getGeneratorName() {
		return generatorName;
	}

	public void setModel(CitModel model) {
		this.model = model;
	}

	public CitModel getModel() {
		return model;
	}

	public List<Test> getTests() {
		return tests;
	}

	public void setTests(List<Test> tests) {
		this.tests = tests;
	}

	public float getGeneratorTime() {
		return generatorTime;
	}

	/**
	 * Populate the {@link List} of {@link Test} of this test suite from the
	 * {@link String} field ts of the current {@link TestSuite} class.
	 * 
	 * @param delimiter the {@link String} to be used for delimiting elements in the
	 *                  {@link String} containing the test suite
	 * 
	 */
	public void populateTestSuite(String delimiter) {
		setModel(model);
		List<Parameter> params = new ArrayList<>();
		List<Test> tests = new ArrayList<>();
		String[] st = ts.split("\n");
		for (int i = 0; i < st.length; i++) {
			String[] ps = st[i].split(delimiter);
			// sometimes when we read a file, it adds '\r' to the last value
			// -> in that case we need to delete it.
			if (ps[ps.length - 1].charAt(ps[ps.length - 1].length() - 1) == '\r') {
				ps[ps.length - 1] = ps[ps.length - 1].substring(0, ps[ps.length - 1].length() - 1);
			}
			if (i == 0) {
				for (String p : ps)
					params.add(getParamFromName(model, p));
			} else {
//				List<Assignment> as = new ArrayList<>();
//				for (int j=0; j<ps.length; j++) as.add(new Assignment(params.get(j), ps[j]));
//				tests.add(new Test(as));
				Map<String, String> as = new HashMap<>();
				for (int j = 0; j < ps.length; j++) {
					if (params.get(j) != null)
						as.put(params.get(j).getName(), ps[j]);
				}
				tests.add(new Test(as));
			}
		}
		setTests(tests);
	}

	public static Parameter getParamFromName(CitModel model, String parameterName) {
		for (int i = 0; i < model.getParameters().size(); i++) {
			if (parameterName.equals(model.getParameters().get(i).getName()))
				return model.getParameters().get(i);
		}
		return null;
	}

}