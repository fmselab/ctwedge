package ctwedge.generator;

import org.junit.Test;

import ctwedge.util.ext.ICTWedgeTestGenerator;
import ctwedge.util.ext.Utility;

public class GetTestSuiteFromUtilityTest {

	@Test
	public void test1() throws Exception {
		ICTWedgeTestGenerator generator = DiscoverGenerators.getGenerator("ACTSGenerator");
		System.out.println("ACTS: " + Utility.getTestSuite(
				"Model prova\nParameters:\n a: Boolean; b: Boolean; c: Boolean;\nConstraints:\n # a -> b #\n", generator,
				2, false, null));
	}
		
	@Test
	public void test2() throws Exception {
		ICTWedgeTestGenerator generator = DiscoverGenerators.getGenerator("CASAGenerator");
		System.out.println("CASA: " + Utility.getTestSuite(
				"Model prova\nParameters:\n a: Boolean; b: Boolean; c: Boolean;\nConstraints:\n # a -> b #\n", generator,
				2, false, null));
	}
	
	@Test
	public void test3() throws Exception {
		ICTWedgeTestGenerator generator = DiscoverGenerators.getGenerator("PICTGenerator");
		System.out.println("PICT: " + generator.getTestSuite(Utility.loadModel(
				"Model prova\nParameters:\n a: Boolean; b: Boolean; c: Boolean;\nConstraints:\n # a -> b #\n"),
				2, false));
	}
	
}
