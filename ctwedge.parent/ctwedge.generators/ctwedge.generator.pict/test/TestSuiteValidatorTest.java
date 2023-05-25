import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.Test;
import org.sosy_lab.common.configuration.InvalidConfigurationException;
import org.sosy_lab.java_smt.api.SolverException;

import ctwedge.generator.pict.PICTGenerator;
import ctwedge.util.TestSuite;
import ctwedge.util.ext.ICTWedgeTestGenerator;
import ctwedge.util.ext.Utility;
import ctwedge.util.validator.SMTTestSuiteValidator;
import ctwedge.util.validator.ValidatorException;

class GeneratorExec implements Callable<TestSuite> {
	String model;
	ICTWedgeTestGenerator generator;

    public GeneratorExec(String model, ICTWedgeTestGenerator generator) {
		super();
		this.model = model;
		this.generator = generator;
	}

	@Override
    public TestSuite call() throws Exception {
        return (generator.getTestSuite(Utility.loadModel(model), 2, false));
    }
}

public class TestSuiteValidatorTest {


	@Test
	public void simpleBooleanTestModel() throws SolverException, InterruptedException, InvalidConfigurationException, ValidatorException {

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

		SMTTestSuiteValidator tsv = new SMTTestSuiteValidator(ts);
		//tsv.setTestSuite(ts);
		assertEquals(ts.getTests().size(), 5);
		assertTrue(tsv.howManyTestAreValid() == ts.getTests().size());
		assertEquals(tsv.howManyTuplesCovers(), 11);
		assertTrue(tsv.isValid());
		assertTrue(tsv.isComplete());

		System.out.println("***** Now remove the first test from test suite! *****\n"
				+ "The size should decrease and the test suite must be not complete");

		ts.getTests().remove(0);
		//tsv.setTestSuite(ts);
		assertEquals(ts.getTests().size(), 4);
		assertTrue(tsv.howManyTestAreValid() == ts.getTests().size());
		assertEquals(tsv.howManyTuplesCovers(), 10);
		assertTrue(tsv.isValid());
		assertFalse(tsv.isComplete());

	}

	@Test
	public void complexBooleanTestModel() throws SolverException, InterruptedException, InvalidConfigurationException, ValidatorException {

		TestSuite ts = null;

		try {
			Path path = Paths.get("../../ctwedge.benchmarks/models_test/ctwedge/SmartHome.ctw");
			PICTGenerator generator = new PICTGenerator();
			ts = generator.getTestSuite(Utility.loadModelFromPath(path.toString()), 2, false);
		} catch (Exception e) {
			e.printStackTrace();
		}

		SMTTestSuiteValidator tsv = new SMTTestSuiteValidator(ts);
		//tsv.setTestSuite(ts);
		assertEquals(ts.getTests().size(), 14);
		assertTrue(tsv.howManyTestAreValid() == ts.getTests().size());
		assertEquals(tsv.howManyTuplesCovers(), 1858);
		assertTrue(tsv.isValid());
		assertTrue(tsv.isComplete());

		System.out.println("***** Now remove the first test from test suite! *****\n"
				+ "The size should decrease and the test suite must be not complete");

		ts.getTests().remove(0);
		//tsv.setTestSuite(ts);
		assertEquals(ts.getTests().size(), 13);
		assertTrue(tsv.howManyTestAreValid() == ts.getTests().size());
		assertEquals(tsv.howManyTuplesCovers(), 1849);
		assertTrue(tsv.isValid());
		assertFalse(tsv.isComplete());

	}

	@Test
	public void simpleRangeTestModel() throws SolverException, InterruptedException, InvalidConfigurationException, ValidatorException {

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

		SMTTestSuiteValidator tsv = new SMTTestSuiteValidator(ts);
		//tsv.setTestSuite(ts);
		assertEquals(ts.getTests().size(), 6);
		assertTrue(tsv.howManyTestAreValid() == ts.getTests().size());
		assertEquals(tsv.howManyTuplesCovers(), 6);
		assertTrue(tsv.isValid());
		assertTrue(tsv.isComplete());

		System.out.println("***** Now remove the first test from test suite! *****\n"
				+ "The size should decrease and the test suite must be not complete");

		ts.getTests().remove(0);
		//tsv.setTestSuite(ts);
		assertEquals(ts.getTests().size(), 5);
		assertTrue(tsv.howManyTestAreValid() == ts.getTests().size());
		assertEquals(tsv.howManyTuplesCovers(), 5);
		assertTrue(tsv.isValid());
		assertFalse(tsv.isComplete());
	}

	@Test
	public void simpleEnumTestModel() throws SolverException, InterruptedException, InvalidConfigurationException, ValidatorException {

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

		SMTTestSuiteValidator tsv = new SMTTestSuiteValidator(ts);
		//tsv.setTestSuite(ts);
		assertEquals(ts.getTests().size(), 6);
		assertTrue(tsv.howManyTestAreValid() == ts.getTests().size());
		assertEquals(tsv.howManyTuplesCovers(), 36);
		assertTrue(tsv.isValid());
		assertTrue(tsv.isComplete());

		System.out.println("***** Now remove the first test from test suite! *****\n"
				+ "The size should decrease and the test suite must be not complete");

		ts.getTests().remove(0);
		ts.getTests().remove(0);
		//tsv.setTestSuite(ts);
		assertEquals(ts.getTests().size(), 4);
		assertTrue(tsv.howManyTestAreValid() == ts.getTests().size());
		assertEquals(tsv.howManyTuplesCovers(), 32);
		assertTrue(tsv.isValid());
		assertFalse(tsv.isComplete());
	}

	@Test
	public void fileTest() throws SolverException, InterruptedException, InvalidConfigurationException, ValidatorException {
		generateAndValidate("../../ctwedge.benchmarks/models_test/ctwedge/Storage3.ctw");
	}
	
	@Test
	public void fileTest2() throws SolverException, InterruptedException, InvalidConfigurationException, ValidatorException {
		generateAndValidate("examples/ADD_BOOLC_1.ctw");
	}
	
	@Test
	public void fileTest3() throws SolverException, InterruptedException, InvalidConfigurationException, ValidatorException {
		generateAndValidate("examples/ADD_BOOLC_0.ctw");
	}
	
	@Test
	public void testAllFilesInCTComp() throws IOException {
		Path path = Paths.get("examples/CTComp/");
		Files.walk(path).filter(Files::isRegularFile).map(Path::toFile).filter(x -> x.getName().endsWith(".ctw"))
				.forEach(x -> {
					System.err.println(x.getAbsolutePath());
					try {
						generateAndValidate(x.getAbsolutePath());
					} catch (InterruptedException | SolverException | InvalidConfigurationException | ValidatorException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
	}

	private void generateAndValidate(String pathString) throws SolverException, InterruptedException, InvalidConfigurationException, ValidatorException {
		TestSuite ts = null;

		try {
			Path path = Paths.get(pathString);
			PICTGenerator generator = new PICTGenerator();
			ts = generator.getTestSuite(Utility.loadModelFromPath(path.toString()), 2, false);
			ts.setModel(Utility.loadModelFromPath(path.toString()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		

		// Define the validator
		SMTTestSuiteValidator tsv = new SMTTestSuiteValidator(ts);
		//tsv.setTestSuite(ts);

		// Check all the tests are valid
		assertTrue(tsv.howManyTestAreValid() == ts.getTests().size());

		// The test suite must be valid and complete
		assertTrue(tsv.isValid());
		assertTrue(tsv.isComplete());

	}

	@Test
	public void testFolder() {
		ICTWedgeTestGenerator generator = new PICTGenerator();
		List<File> fileList = new ArrayList<>();
		int nTest = 0;
		int nComplete = 0;
		int nValid = 0;
		int nTimeOut = 0;
		new PictTest().listFiles(new File("../../ctwedge.benchmarks/models_test/"), fileList);
		for (File file : fileList) {
			String model;
			try {

				System.out.println("Checking " + file);
				nTest++;

				TestSuite ts = null;
				model = Files.readString(file.toPath());

				// Generate test suite

				ExecutorService executor = Executors.newSingleThreadExecutor();
				Future<TestSuite> ts_future = executor.submit(new GeneratorExec(model, generator));
				TestSuite result = null;
				try {
					result = ts_future.get(150, TimeUnit.SECONDS);
				} catch (TimeoutException ex) {
					nTimeOut++;
					continue;
		        }

				ts = generator.getTestSuite(Utility.loadModel(model), 2, false);

				// Define the validator
				SMTTestSuiteValidator tsv = new SMTTestSuiteValidator(ts);
				//tsv.setTestSuite(ts);
				// Save the number of covered tuples
				int covTuples = tsv.howManyTuplesCovers();

				// Check all the tests are valid
				// assertTrue(tsv.howManyTestAreValid() == ts.getTests().size());

				// The test suite must be valid and complete
				if (tsv.isValid()) {
					nValid++;
					assertTrue(tsv.howManyTestAreValid() == ts.getTests().size());
				}
				if (tsv.isComplete())
					nComplete++;

				// assertTrue(tsv.isValid());
				// assertTrue(tsv.isComplete());

				// Now remove tests until the covered tuples decreases
				while (ts.getTests().size() > 0) {
					ts.getTests().remove(0);
					//tsv.setTestSuite(ts);

					if (tsv.howManyTuplesCovers() < covTuples)
						break;
				}

				// If we still have tests
				if (ts.getTests().size() > 0 && tsv.isValid()) {
					// Check all the tests are valid
					assertTrue(tsv.howManyTestAreValid() == ts.getTests().size());
					// The test suite must be valid but not complete
					assertTrue(tsv.isValid());
					//assertFalse(tsv.isComplete());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("Checked " + nTest + " test");
		System.out.println(nValid + " valid test suites");
		System.out.println(nComplete + " test suites");
		System.out.println(nTimeOut + " test timed out");
	}
}
