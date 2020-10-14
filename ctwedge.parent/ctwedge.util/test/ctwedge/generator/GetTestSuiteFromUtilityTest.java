package ctwedge.generator;

import static org.junit.Assert.*;

import org.junit.Test;

import ctwedge.generator.util.Utility;

public class GetTestSuiteFromUtilityTest {

	@Test
	public void test1() throws Exception {
		System.out.println("ACTS: " + Utility.getTestSuite(
				"Model prova\nParameters:\n a: Boolean; b: Boolean; c: Boolean;\nConstraints:\n # a -> b #\n", DiscoverGenerators.getGenerator("ACTSGenerator"),
				2, false, null));
	}
		
	@Test
	public void test2() throws Exception {
		System.out.println("CASA: " + Utility.getTestSuite(
				"Model prova\nParameters:\n a: Boolean; b: Boolean; c: Boolean;\nConstraints:\n # a -> b #\n", DiscoverGenerators.getGenerator("CASAGenerator"),
				2, false, null));
	}
	
	@Test
	public void test3() throws Exception {
		System.out.println("PICT: " + Utility.getTestSuite(
				"Model prova\nParameters:\n a: Boolean; b: Boolean; c: Boolean;\nConstraints:\n # a -> b #\n", DiscoverGenerators.getGenerator("PICTGenerator"),
				2, false, null));
	}
	
}
