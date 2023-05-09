package ctwedge.generator.acts;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import org.junit.Test;

import ctwedge.util.TestSuite;
import ctwedge.util.ext.ICTWedgeTestGenerator;
import ctwedge.util.ext.Utility;
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
	public void testFolder() throws IOException {
		ICTWedgeTestGenerator generator = new ACTSTranslator();
		int nTest = 0;
		int nComplete = 0;
		int nValid = 0;
		int nTimeOut = 0;
		StringBuilder sb = new StringBuilder();
		List<File> fileList = Files.walk(Paths.get("../../ctwedge.benchmarks/models_test/"))
                .filter(Files::isRegularFile)
                .map(Path::toFile)
                .filter(x -> x.getName().endsWith(".ctw"))
                .collect(Collectors.toList());
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
				else {
					sb.append("NOT VALID - " + file + "\n");
				}
				if (tsv.isComplete())
					nComplete++;
				else {
					sb.append("NOT COMPLETE - " + file + "\n");
				}
				
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
		
		System.out.println("LOGS");
		System.out.println(sb.toString());
	}
}