package featuremodels.specificity;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

import ctwedge.ctWedge.CitModel;
import ctwedge.importer.featureide.FeatureIdeImporterBoolean;
import ctwedge.util.TestSuite;
import de.ovgu.featureide.fm.core.cli.ConfigurationGenerator;

/**
 * This class generates combinatorial test suite exploiting the INCLING
 * generator embedded in feature ide
 */
public class FeatureIdeTestGenerator {

	/**
	 * The name of the temporary file containing the test suite
	 */
	private static final String TEMPTS_TXT = "tempts.txt";
	/**
	 * The path of the feature model which we want to generate the test suite for
	 */
	String fmPath;

	/**
	 * Builds a new Test generator
	 * 
	 * @param fmPath the path of the feature model
	 */
	public FeatureIdeTestGenerator(String fmPath) {
		this.fmPath = fmPath;
	}

	/**
	 * Generates the test suites
	 * 
	 * @return the test suite
	 * @throws FileNotFoundException
	 */
	public TestSuite generateTestSuite() throws FileNotFoundException {
		long startTime = System.currentTimeMillis();

		// Generate the test suite and store it into a file
		ConfigurationGenerator gen = new ConfigurationGenerator();
		String[] args = { "-a", "incling", "-fm", fmPath, "-o", TEMPTS_TXT };
		CitModel model = new FeatureIdeImporterBoolean().importModel(fmPath);
		gen.run(Arrays.asList(args));

		// Generation time
		startTime = System.currentTimeMillis() - startTime;

		// Read the test suite into a CSV string
		File text = new File(TEMPTS_TXT);
		Scanner scnr = new Scanner(text);
		String testSuiteCSV = "";
		while (scnr.hasNextLine()) {
			String line = scnr.nextLine();
			line = line.substring(line.indexOf(";") + 1);
			line = line.replaceAll("\\+", "true");
			line = line.replaceAll("\\-", "false");
			testSuiteCSV += line + "\n";
		}
		scnr.close();

		// Convert the test suite into a CTWedge Test suite
		TestSuite res = new TestSuite(testSuiteCSV, model, ";");
		res.setGeneratorName("INCLING");
		res.setGeneratorTime(startTime);
		return res;
	}

}
