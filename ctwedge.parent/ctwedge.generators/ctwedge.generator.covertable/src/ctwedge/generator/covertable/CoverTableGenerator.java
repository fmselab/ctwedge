package ctwedge.generator.covertable;

import ctwedge.ctWedge.CitModel;
import ctwedge.generator.util.Benchmarkable;
import ctwedge.util.TestSuite;
import ctwedge.util.ext.ICTWedgeTestGenerator;

public class CoverTableGenerator extends ICTWedgeTestGenerator implements Benchmarkable {

	@Override
	public TestSuite getTestSuite(CitModel loadModel, int t, boolean ignoreC) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TestSuite benchmark_run(CitModel model) {
		System.out.println("benchmark covertable");
		return null;
	}
	
	

}
