package ctwedge.util.validator;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import ctwedge.ctWedge.CitModel;
import ctwedge.util.Test;
import ctwedge.util.TestSuite;
import ctwedge.util.ext.Utility;
import ctwedge.util.smt.SMTParameterAdder.EnumTreatment;
import ctwedge.util.smt.SMTTestSuiteValidator;

public class CTWedgeV {

	public static void main(String[] args) {

		CommandLineOptions options = new CommandLineOptions();
		CmdLineParser parser = new CmdLineParser(options);

		try {
			Long initialTime = System.currentTimeMillis();
			parser.parseArgument(args);

			if ((options.isCompleteness() || options.isMinimality()) && options.getStrength() == null) {
				throw new IllegalArgumentException(
						"Coverage strength (--t) must be specified for completeness or minimality checks.");
			}

			// Load model
			CitModel model = Utility.loadModelFromPath(options.getModelPath());

			if (model != null) {

				// Read the entire content of the test suite file into a string
				String testSuiteStr = Files.readString(Path.of(options.getTestSuitePath()));

				// Load test suite
				TestSuite testSuite = new TestSuite(testSuiteStr, model);
				testSuite.populateTestSuite(",");
				testSuite.setStrength(options.getStrength());

				SMTTestSuiteValidator validator = new SMTTestSuiteValidator(testSuite);

				if (options.isValidity()) {
					List<Test> notValidTests = validator.notValidTests();
					if (notValidTests.size() == 0) {
						System.out.println("-- the provided test suite is valid");
					} else {
						System.out.println("-- the provided test suite is not valid");
						String notValidTestsStr = notValidTests.stream().map(Test::toString)
								.reduce((a, b) -> a + ", " + b).orElse("");
						System.out.println("\t--> INVALID TEST CASES: " + notValidTestsStr);
					}
				}

				if (options.isCompleteness()) {
					String nonCoveredTuples = validator.nonCoveredTuples();
					if (nonCoveredTuples.length() == 0) {
						System.out.println("-- the provided test suite is complete");
					} else {
						System.out.println("-- the provided test suite is not complete");
						System.out.println("\t--> UNCOVERED TUPLES: " + nonCoveredTuples);
					}
				}

				if (options.isMinimality()) {
					List<Test> uselessTests = validator.getUselessTests();
					if (uselessTests.size() == 0) {
						System.out.println("-- the provided test suite is minimal");
					} else {
						System.out.println("-- the provided test suite is not minimal");
						String uselessTestsStr = uselessTests.stream().map(Test::toString)
								.reduce((a, b) -> a + ", " + b).orElse("");
						System.out.println("\t--> POTENTIALLY USELESS TESTS: " + uselessTestsStr);
					}
				}

				System.out.println("Validation time: " + (System.currentTimeMillis() - initialTime) + " ms");
			} else {
				System.err.println("Failed to load model from: " + options.getModelPath());
				System.exit(1);
			}

		} catch (CmdLineException e) {
			System.err.println("Argument error: " + e.getMessage());
			parser.printUsage(System.err);
			System.exit(1);

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

}
