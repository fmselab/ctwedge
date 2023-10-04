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

	public TestSuite generateTestSuite(ConfigurationGenerator gen) throws FileNotFoundException {
		String string = "tempts.txt";
		String[] args = { "-a", "incling", "-fm", fmmodel, "-o", string };
		CitModel model = new FeatureIdeImporterBoolean().importModel(fmPath);
		gen.run(Arrays.asList(args));
		File text = new File(string);

		// Creating Scanner instance to read File in Java
		Scanner scnr = new Scanner(text);
		String testSuiteCSV = "";
		while (scnr.hasNextLine()) {
			String line = scnr.nextLine();
			line = line.substring(line.indexOf(";") + 1);
			line = line.replaceAll("\\+", "true");
			line = line.replaceAll("\\-", "false");
			System.out.println("line " + " :" + line);
			testSuiteCSV += line + "\n";
		}
		scnr.close();
		return testSuiteCSV;
	}

}
