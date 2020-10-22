package ctwedge.generator.util;

import ctwedge.ctWedge.CitModel;
import ctwedge.util.TestSuite;

public interface Benchmarkable {
	
	public String getGeneratorName();
	
	/** 
	 * @param the citModel
	 * @return the test suite
	 * @return the millisecond of execution
	 */
	public TestSuite benchmark_run(CitModel model);
	
	


}