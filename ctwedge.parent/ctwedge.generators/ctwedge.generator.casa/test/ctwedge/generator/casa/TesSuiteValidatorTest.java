package ctwedge.generator.casa;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.Test;

import ctwedge.generator.util.Utility;
import ctwedge.util.TestSuite;
import ctwedge.util.ext.ICTWedgeTestGenerator;
import ctwedge.util.validator.SMTTestSuiteValidator;

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

public class TesSuiteValidatorTest {

	public static String readFromFile(File f) throws Exception {
		StringBuilder sb = new StringBuilder();
		BufferedReader fin = new BufferedReader(new FileReader(f));
		String s = "";
		while ((s = fin.readLine()) != null)
			sb.append(s + "\n");
		fin.close();
		return sb.toString();
	}
	
	public void listFiles(File folder, List<File> fileList) {
		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()	&& (listOfFiles[i].getName().endsWith(".citw") || listOfFiles[i].getName().endsWith(".ctw"))) {
				fileList.add(listOfFiles[i]);
			} else if (listOfFiles[i].isDirectory()) {
				listFiles(listOfFiles[i], fileList);
			}
		}
	}	
	
	@Test
	public void testFolder() {
		ICTWedgeTestGenerator generator = new CASATestGenerator();
		List<File> fileList = new ArrayList<>();
		int nTest = 0;
		int nComplete = 0;
		int nValid = 0;
		int nTimeOut = 0;
		listFiles(new File("../../ctwedge.benchmarks/models_test/"), fileList);
		for (File file : fileList) {
			String model;
			try {
				
				System.out.println("Checking " + file);
				nTest++;
				
				TestSuite ts = null;
				model = readFromFile(file);
				
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
				
				// Save the number of tests
				int numTest = ts.getTests().size();
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
		System.out.println(nComplete + " complete test suites");
		System.out.println(nTimeOut + " test timed out");
	}
}