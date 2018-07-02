package ctwedge.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ctwedge.ctWedge.CitModel;
import ctwedge.ctWedge.Parameter;

public class TestSuite {
	String ts;
	float generatorTime;
	String generatorName;
	int strength;
	CitModel model;
	List<Test> tests;

	public TestSuite(String ts, CitModel model) {
		this.ts = ts;
		this.model = model;
		populateTestSuite(model);
	}

	@Override
	public String toString() {
		return ts;
	}

	public void setGeneratorTime(float f) {
		this.generatorTime=f;
	}

	public void setGeneratorName(String generatorName) {	
		this.generatorName=generatorName;
	}

	public void setStrength(int nWise) {
		this.strength=nWise;
	}
	
	public int getStrength() {
		return strength;
	}

	public String getGeneratorName() {
		return generatorName;
	}
	
	public void setModel(CitModel model) {
		this.model=model;
	}

	public CitModel getModel() {
		return model;
	}

	public List<Test> getTests() {
		return tests;
	}
	
	public void setTests(List<Test> tests) {
		this.tests=tests;
	}

	public float getGeneratorTime() {
		return generatorTime;
	}
	
	public void populateTestSuite(CitModel model) {
		setModel(model);
		List<Parameter> params = new ArrayList<>();
		List<Test> tests = new ArrayList<>();
		String[] st = ts.split("\n");
		for (int i=0; i<st.length; i++) {
			String[] ps = st[i].split(";");
			if (i==0) {
				for (String p : ps) params.add(getParamFromName(model, p));
			} else {
//				List<Assignment> as = new ArrayList<>();
//				for (int j=0; j<ps.length; j++) as.add(new Assignment(params.get(j), ps[j]));
//				tests.add(new Test(as));
				Map<String,String> as = new HashMap<>();
				for (int j=0; j<ps.length; j++) as.put(params.get(j).getName(), ps[j]);
				tests.add(new Test(as));
			}
		}
		setTests(tests);
	}
	
	public static Parameter getParamFromName(CitModel model, String parameterName) {
		for (int i=0; i<model.getParameters().size(); i++) {
			if (parameterName.equals(model.getParameters().get(i).getName())) return model.getParameters().get(i);
		}
		return null;
	}
}