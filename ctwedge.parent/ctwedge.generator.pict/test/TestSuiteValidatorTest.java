import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.sosy_lab.common.configuration.InvalidConfigurationException;
import org.sosy_lab.java_smt.api.SolverException;

import ctwedge.generator.acts.ACTSTranslator;
import ctwedge.generator.pict.PICTGenerator;
import ctwedge.generator.util.Utility;
import ctwedge.util.TestSuite;
import ctwedge.util.validator.SMTTestSuiteValidator;

public class TestSuiteValidatorTest {

	public static String readFromFile(File f) throws Exception {
		StringBuilder sb = new StringBuilder();
		BufferedReader fin = new BufferedReader(new FileReader(f));
		String s = "";
		while ((s = fin.readLine()) != null)
			sb.append(s + "\n");
		fin.close();
		return sb.toString();
	}
	
	@Test
	public void simpleBooleanTestModel() throws SolverException, InterruptedException, InvalidConfigurationException {

		TestSuite ts = null;

		try {
			String model = "Model Concurrency\r\n" + 
						   "\r\n" + 
						   "Parameters:\r\n" + 
						   "p4: Boolean;\r\n" + 
						   "p5: Boolean;\r\n" +
						   "p2: Boolean;\r\n" + 
						   "\r\n" + 
						   "Constraints:\r\n" + 
						   "	# p2!=true OR p5 #";
			PICTGenerator generator = new PICTGenerator();
			ts = generator.getTestSuite(Utility.loadModel(model), 2, false);
		} catch (Exception e) {
			e.printStackTrace();
		}

		SMTTestSuiteValidator tsv = new SMTTestSuiteValidator();
		tsv.setTestSuite(ts);
		assertEquals(ts.getTests().size(), 5);
		assertTrue(tsv.howManyTestAreValid() == ts.getTests().size());
		assertEquals(tsv.howManyTuplesCovers(), 11);
		assertTrue(tsv.isValid());
		assertTrue(tsv.isComplete());
		
		System.out.println("***** Now remove the first test from test suite! *****\n"
				+ "The size should decrease and the test suite must be not complete");
		
		ts.getTests().remove(0);
		tsv.setTestSuite(ts);
		assertEquals(ts.getTests().size(), 4);
		assertTrue(tsv.howManyTestAreValid() == ts.getTests().size());
		assertEquals(tsv.howManyTuplesCovers(), 10);
		assertTrue(tsv.isValid());
		assertFalse(tsv.isComplete());
		
	}
	
	@Test
	public void complexBooleanTestModel() throws SolverException, InterruptedException, InvalidConfigurationException {

		TestSuite ts = null;

		try {
			String model = readFromFile(new File("models/SmartHome.ctw"));
			PICTGenerator generator = new PICTGenerator();
			ts = generator.getTestSuite(Utility.loadModel(model), 2, false);
		} catch (Exception e) {
			e.printStackTrace();
		}

		SMTTestSuiteValidator tsv = new SMTTestSuiteValidator();
		tsv.setTestSuite(ts);
		assertEquals(ts.getTests().size(), 14);
		assertTrue(tsv.howManyTestAreValid() == ts.getTests().size());
		assertEquals(tsv.howManyTuplesCovers(), 1858);
		assertTrue(tsv.isValid());
		assertTrue(tsv.isComplete());
		
		System.out.println("***** Now remove the first test from test suite! *****\n"
				+ "The size should decrease and the test suite must be not complete");
		
		ts.getTests().remove(0);
		tsv.setTestSuite(ts);
		assertEquals(ts.getTests().size(), 13);
		assertTrue(tsv.howManyTestAreValid() == ts.getTests().size());
		assertEquals(tsv.howManyTuplesCovers(), 1849);
		assertTrue(tsv.isValid());
		assertFalse(tsv.isComplete());
		
	}
	
	@Test
	public void simpleRangeTestModel() throws SolverException, InterruptedException, InvalidConfigurationException {

		TestSuite ts = null;

		try {
			String model = "Model Phone\r\n"
						 + " Parameters:\r\n"
						 + "   emailViewer : Boolean\r\n"
						 + "   textLines:  [ 25 .. 30 ]\r\n"
						 + "\r\n"
						 + " Constraints:\r\n"
						 + "   # emailViewer => textLines > 28 #\r\n"
						 + "   # !emailViewer => textLines < 29#";
			PICTGenerator generator = new PICTGenerator();
			ts = generator.getTestSuite(Utility.loadModel(model), 2, false);
		} catch (Exception e) {
			e.printStackTrace();
		}

		SMTTestSuiteValidator tsv = new SMTTestSuiteValidator();
		tsv.setTestSuite(ts);
		assertEquals(ts.getTests().size(), 6);
		assertTrue(tsv.howManyTestAreValid() == ts.getTests().size());
		assertEquals(tsv.howManyTuplesCovers(), 6);
		assertTrue(tsv.isValid());
		assertTrue(tsv.isComplete());
		
		System.out.println("***** Now remove the first test from test suite! *****\n"
				+ "The size should decrease and the test suite must be not complete");
		
		ts.getTests().remove(0);
		tsv.setTestSuite(ts);
		assertEquals(ts.getTests().size(), 5);
		assertTrue(tsv.howManyTestAreValid() == ts.getTests().size());
		assertEquals(tsv.howManyTuplesCovers(), 5);
		assertTrue(tsv.isValid());
		assertFalse(tsv.isComplete());
	}
	
	@Test
	public void simpleEnumTestModel() throws SolverException, InterruptedException, InvalidConfigurationException {
		
		TestSuite ts = null;
		
		try {
			String model = "Model Concurrency\r\n" + 
					"\r\n" + 
					"Parameters:\r\n" + 
					"p1: { v1 v2 };\r\n" + 
					"p2: { v1 v2 };\r\n" + 
					"p3: { v1 v2 };\r\n" + 
					"p4: Boolean;\r\n" + 
					"p5: Boolean;\r\n" + 
					"\r\n" + 
					"Constraints:\r\n" + 
					"	# ( p3!=v1 OR p2!=v1 OR p5 OR p4 OR p1!=v1) #\r\n" + 
					"	# ( p1!=v2 OR p5!=true) #\r\n" + 
					"	# ( p2!=v1 OR p5 OR p4!=true OR p3!=v2 OR p1!=v1) #\r\n" + 
					"	# ( p5!=true OR p2!=v2) #\r\n" + 
					"	# ( p4 OR p3!=v2 OR p1!=v1) #\r\n" + 
					"	# ( p4!=true OR p1!=v2) #\r\n" + 
					"	# ( p3!=v1 OR p4!=true) #";
			PICTGenerator generator = new PICTGenerator();
			ts = generator.getTestSuite(Utility.loadModel(model), 2, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		SMTTestSuiteValidator tsv = new SMTTestSuiteValidator();
		tsv.setTestSuite(ts);
		assertEquals(ts.getTests().size(), 6);
		assertTrue(tsv.howManyTestAreValid() == ts.getTests().size());
		assertEquals(tsv.howManyTuplesCovers(), 36);
		assertTrue(tsv.isValid());
		assertTrue(tsv.isComplete());
		
		System.out.println("***** Now remove the first test from test suite! *****\n"
				+ "The size should decrease and the test suite must be not complete");
		
		ts.getTests().remove(0);
		ts.getTests().remove(0);
		tsv.setTestSuite(ts);
		assertEquals(ts.getTests().size(), 4);
		assertTrue(tsv.howManyTestAreValid() == ts.getTests().size());
		assertEquals(tsv.howManyTuplesCovers(), 32);
		assertTrue(tsv.isValid());
		assertFalse(tsv.isComplete());
	}
	
	@Test
	public void fileTest() throws SolverException, InterruptedException, InvalidConfigurationException {

		TestSuite ts = null;

		try {
			String model = readFromFile(new File("models/benchmark_27.ctw"));
			ACTSTranslator generator = new ACTSTranslator();
			ts = generator.getTestSuite(Utility.loadModel(model), 2, false);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Define the validator
		SMTTestSuiteValidator tsv = new SMTTestSuiteValidator();
		tsv.setTestSuite(ts);
		
		// Save the number of tests
		int numTest = ts.getTests().size();
		// Save the number of covered tuples
		int covTuples = tsv.howManyTuplesCovers();
		
		// Check all the tests are valid
		assertTrue(tsv.howManyTestAreValid() == ts.getTests().size());
		
		// The test suite must be valid and complete
		assertTrue(tsv.isValid());
		assertTrue(tsv.isComplete());
		
		// Now remove tests until the covered tuples decreases
		while (ts.getTests().size() > 0) {
			ts.getTests().remove(0);
			tsv.setTestSuite(ts);
			
			if (tsv.howManyTuplesCovers() < covTuples)
				break;
		}
		
		// If we still have tests
		if (ts.getTests().size() > 0) {
			// Check all the tests are valid
			assertTrue(tsv.howManyTestAreValid() == ts.getTests().size());
			// The test suite must be valid but not complete
			assertTrue(tsv.isValid());
			assertFalse(tsv.isComplete());
		}	
		
	}
	
	@Test
	public void testFolder() {
		PICTGenerator generator = new PICTGenerator();
		List<File> fileList = new ArrayList<>();
		new PictTest().listFiles(new File("models/"), fileList);
		for (File file : fileList) {
			String model;
			try {
				
				// With ACTS we have -1 as weight (for what?)
				if (file.toString().contains("grep_v0_1_small.ctw")) continue;
				
				// It seems that the error is due to PICT. Generating the test suite with ACTS there are no problems
				if (file.toString().contains("SystemMgmt.ctw")) continue;
				if (file.toString().contains("benchmark_15.ctw")) continue;
				if (file.toString().contains("benchmark_27.ctw")) continue;
				if (file.toString().contains("benchmark_3.ctw")) continue;
				if (file.toString().contains("benchmark_4.ctw")) continue;
				if (file.toString().contains("benchmark_8.ctw")) continue;			
				if (file.toString().contains("CommProtocol.ctw")) continue; 
				if (file.toString().contains("gzip_v0_small.ctw")) continue;
				if (file.toString().contains("Healthcare1.ctw")) continue;
				if (file.toString().contains("Healthcare3.ctw")) continue;
				if (file.toString().contains("Healthcare4.ctw")) continue;
				if (file.toString().contains("NetworkMgmt.ctw")) continue;
				if (file.toString().contains("sed_v0_2_small.ctw")) continue;
				if (file.toString().contains("Services.ctw")) continue;
				if (file.toString().contains("Storage1.ctw")) continue;
				if (file.toString().contains("Storage3.ctw")) continue;
				if (file.toString().contains("Storage5.ctw")) continue;
				
				System.out.println("Checking " + file);
				
				TestSuite ts = null;
				model = readFromFile(file);
				
				// Generate test suite
				ts = generator.getTestSuite(Utility.loadModel(model), 2, false);
				
				// Define the validator
				SMTTestSuiteValidator tsv = new SMTTestSuiteValidator();
				tsv.setTestSuite(ts);
				
				// Save the number of tests
				int numTest = ts.getTests().size();
				// Save the number of covered tuples
				int covTuples = tsv.howManyTuplesCovers();
				
				// Check all the tests are valid
				assertTrue(tsv.howManyTestAreValid() == ts.getTests().size());
				
				// The test suite must be valid and complete
				assertTrue(tsv.isValid());
				assertTrue(tsv.isComplete());
				
				// Now remove tests until the covered tuples decreases
				while (ts.getTests().size() > 0) {
					ts.getTests().remove(0);
					tsv.setTestSuite(ts);
					
					if (tsv.howManyTuplesCovers() < covTuples)
						break;
				}
				
				// If we still have tests
				if (ts.getTests().size() > 0) {
					// Check all the tests are valid
					assertTrue(tsv.howManyTestAreValid() == ts.getTests().size());
					// The test suite must be valid but not complete
					assertTrue(tsv.isValid());
					assertFalse(tsv.isComplete());
				}				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}