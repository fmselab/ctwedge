package ctwedge.generator.medici;

import ctwedge.ctWedge.CitModel;
import ctwedge.generator.util.Benchmarkable;
import ctwedge.util.TestSuite;
import ctwedge.util.ext.ICTWedgeTestGenerator;

public class MediciCITGenerator extends ICTWedgeTestGenerator implements Benchmarkable{
	
	public MediciCITGenerator() {
		super("Medici");
	}

	@Override
	public TestSuite getTestSuite(CitModel loadModel, int t, boolean ignoreC) throws Exception {
		// TODO
		// convert to medici
		// run the tool
		// read the output
		return null;
	}

	@Override
	public TestSuite benchmark_run(CitModel model) {
		// TODO 
		// benchmark
		return null;
	}

	@Override
	public void destroyProcess() {}

}
