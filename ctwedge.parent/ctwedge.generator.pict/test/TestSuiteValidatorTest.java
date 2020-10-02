import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.junit.Test;
import org.sosy_lab.common.configuration.InvalidConfigurationException;
import org.sosy_lab.java_smt.api.SolverException;

import ctwedge.generator.pict.PICTGenerator;
import ctwedge.generator.util.Utility;
import ctwedge.util.TestSuite;
import ctwedge.util.validator.SMTTestSuiteValidator;
import ctwedge.util.validator.TestSuiteValidator;

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
		tsv.setTestSuite(ts);
		assertEquals(ts.getTests().size(), 5);
		assertTrue(tsv.howManyTestAreValid() == ts.getTests().size());
		assertEquals(tsv.howManyTuplesCovers(), 5);
		assertTrue(tsv.isValid());
		assertFalse(tsv.isComplete());
	}
}