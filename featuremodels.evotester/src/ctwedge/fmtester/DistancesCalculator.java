package ctwedge.fmtester;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import ctwedge.ctWedge.CitModel;
import ctwedge.generator.acts.ACTSTranslator;
import ctwedge.util.ext.Utility;
import ctwedge.importer.featureide.FeatureIdeImporterBoolean;
import ctwedge.util.Test;
import ctwedge.util.TestSuite;

/**
 * Calculate the distance between:
 * <ul>
 * <li>test suites</li>
 * <li>test cases</li>
 * <li>feature values</li>
 * </ul>
 * 
 * @author Luca Parimbelli
 * 
 */
public class DistancesCalculator {

	// when this field is set to "true", debug info are printed
	// during the execution of this class
	public static boolean PRINT_DEBUG = false;

	/*
	 * Info about implementation: - TestSuite is a List of <Test> - each Test is a
	 * Map<key,value> where - Map<feature,value> in particular Map<key,value> is
	 * implemented as TreeMap<String,String>
	 * 
	 */

	/**
	 * Get the greedy distance as percentage between the boolean combinatorial test
	 * suites of the CTWedge Models <i>ctwModelPath</i> and <i>ctwpModelPath</i>.
	 * This method imports the test suites (.csv) and the related CTWedge Models
	 * (.xml) and calculates the distance between the two test suites.
	 * 
	 * @param ctwModelPath  the path to the first CTWedge Model (.ctw)
	 * @param tsPath        the path to the test suite of the first model (.csv)
	 * @param tsDelimiter   the delimiter {@link String} used to separe values in
	 *                      the .csv file of the first test suite
	 * @param ctwpModelPath the path to the second CTWedge Model (.ctw)
	 * @param tspPath       the path to the test suite of the second model (.csv)
	 * @param tspDelimiter  the delimiter {@link String} used to separe values in
	 *                      the .csv file of the second test suite
	 * 
	 * @return % distance between the test suites of fm and fmp
	 * @throws IOException
	 */
	public static float percTestSuitesDist_FromTestSuites_CTW(String ctwModelPath, String tsPath, String tsDelimiter,
			String ctwpModelPath, String tspPath, String tspDelimiter) throws IOException {

		// Disabling console printing
		ConsoleManager cm = new ConsoleManager(System.out);
		cm.consolePrintingOff();

		// Populating the test suite ts (=TS) of the first model
		// Importing the CTWedge model
		CitModel model = Utility.loadModelFromPath(ctwModelPath);
		// Importing the test suite
		String importedTs = new String(Files.readAllBytes(Paths.get(tsPath)), StandardCharsets.UTF_8);
		// Populating the test suite of the model
		TestSuite ts = new TestSuite(importedTs, model, tsDelimiter);

		// Populating the test suite tsp (=TS') of the second model
		CitModel modelp = Utility.loadModelFromPath(ctwpModelPath);
		String importedTsp = new String(Files.readAllBytes(Paths.get(tspPath)), StandardCharsets.UTF_8);
		TestSuite tsp = new TestSuite(importedTsp, modelp, tspDelimiter);

		// Enabling console printing
		cm.consolePrintingOn();

		return DistancesCalculator.percTestSuitesDist(ts, tsp);

	}

	/**
	 * Get the greedy distance as percentage between the boolean combinatorial test
	 * suites of the Feature Models <i>fmPath</i> and <i>fmpPath</i>. This method
	 * imports the test suites (.csv) and the related Feature Models (.xml) and
	 * calculates the distance between the two test suites.
	 * 
	 * @param fmPath       the path to the first Feature Model (.xml)
	 * @param tsPath       the path to the test suite of the first model (.csv)
	 * @param tsDelimiter  the delimiter {@link String} used to separe values in the
	 *                     .csv file of the first test suite
	 * @param fmpPath      the path to the second Feature Model (.xml)
	 * @param tspPath      the path to the test suite of the second model (.csv)
	 * @param tspDelimiter the delimiter {@link String} used to separe values in the
	 *                     .csv file of the second test suite
	 * 
	 * @return % distance between the test suites of fm and fmp
	 * @throws IOException
	 */
	public static float percTestSuitesDist_FromTestSuites(String fmPath, String tsPath, String tsDelimiter,
			String fmpPath, String tspPath, String tspDelimiter) throws IOException {

		// Disabling console printing
		ConsoleManager cm = new ConsoleManager(System.out);
		cm.consolePrintingOff();

		// Populating the test suite ts (=TS) of the first model
		// Importing the feature model
		FeatureIdeImporterBoolean importer = new FeatureIdeImporterBoolean();
		CitModel model = importer.importModel(fmPath);
		// Importing the test suite
		String importedTs = new String(Files.readAllBytes(Paths.get(tsPath)), StandardCharsets.UTF_8);
		// Populating the test suite of the model
		TestSuite ts = new TestSuite(importedTs, model, tsDelimiter);

		// Populating the test suite tsp (=TS') of the second model
		FeatureIdeImporterBoolean importerp = new FeatureIdeImporterBoolean();
		CitModel modelp = importerp.importModel(fmpPath);
		String importedTsp = new String(Files.readAllBytes(Paths.get(tspPath)), StandardCharsets.UTF_8);
		TestSuite tsp = new TestSuite(importedTsp, modelp, tspDelimiter);

		// Enabling console printing
		cm.consolePrintingOn();

		return DistancesCalculator.percTestSuitesDist(ts, tsp);

	}

	/**
	 * Get the greedy distance as percentage between the boolean combinatorial test
	 * suites of the feature models fm and fmp. This method generates the test
	 * suites from the imported models (parameters) before calculating the distance.
	 * 
	 * @param fmPath  the path to the first feature model (.xml)
	 * @param fmpPath the path to the second feature model (.xml)
	 * 
	 * @return % distance between the test suites of fm and fmp
	 */
	public static float percTestSuitesDist_FromModels(String fmPath, String fmpPath) {

		// Disabling console printing
		ConsoleManager cm = new ConsoleManager(System.out);
		cm.consolePrintingOff();

		// Getting the test suite ts of the first feature model
		FeatureIdeImporterBoolean importer = new FeatureIdeImporterBoolean();
		ACTSTranslator acts = new ACTSTranslator();
		CitModel model = importer.importModel(fmPath);
		TestSuite ts = acts.getTestSuite(model, 2, false);

		// Getting the test suite tsp of the second feature model
		FeatureIdeImporterBoolean importerp = new FeatureIdeImporterBoolean();
		ACTSTranslator actsp = new ACTSTranslator();
		model = importerp.importModel(fmpPath);
		TestSuite tsp = actsp.getTestSuite(model, 2, false);

		// Enabling console printing
		cm.consolePrintingOn();

		return DistancesCalculator.percTestSuitesDist(ts, tsp);

	}

	/**
	 * Get the greedy distance as percentage between the test suites ts and tsp
	 * 
	 * @param ts  the first test suite
	 * @param tsp the second test suite
	 * 
	 * @return distance% between ts and tsp
	 */
	public static float percTestSuitesDist(TestSuite ts, TestSuite tsp) {
		final int cardTs = ts.getTests().size();
		final int cardTsp = tsp.getTests().size();
		final int nFeatTs = ts.getTests().get(0).keySet().size();
		final int nFeatTsp = tsp.getTests().get(0).keySet().size();

		final float wortsTestSuitesDist = cardTs * nFeatTs + cardTsp * nFeatTsp;
		final float greedyTestSuiteDist = DistancesCalculator.testSuitesDist(ts, tsp);

		/* Debug printing of various distance info */
		if (PRINT_DEBUG) {
			final float ratioDist = (greedyTestSuiteDist / wortsTestSuitesDist);
			System.out.println("Worst dist: " + wortsTestSuitesDist);
			System.out.println("Greedy dist: " + greedyTestSuiteDist);
			System.out.println("Perc dist: " + ratioDist);
		}

		return (greedyTestSuiteDist / wortsTestSuitesDist) * 100;
	}

	/**
	 * Get the greedy distance between the test suites ts and tsp
	 */
	public static int testSuitesDist(TestSuite ts, TestSuite tsp) {

		int[][] tcDist = DistancesCalculator.get_tcDist(ts, tsp);
		final int maxValue = DistancesCalculator.get_maxValue(tcDist) + 1;

		final int cardTs = ts.getTests().size();
		final int cardTsp = tsp.getTests().size();
		final int nRows = tcDist.length; // tcDist rows number
		final int nCols = tcDist[0].length; // tcDist cols number

		int testSuitesDist = 0;

		// the number of features in each TS is computed from the number
		// of keys (=features) in any test case (Test class) of the
		// corresponding test suite
		if (cardTs >= cardTsp)
			testSuitesDist = (nCols - nRows) * ts.getTests().get(0).keySet().size();
		else
			testSuitesDist = (nCols - nRows) * tsp.getTests().get(0).keySet().size();

		/* Debug printing of tcDist as table and other info */
		if (PRINT_DEBUG) {
			System.out.println("Before loop - tcDist:\n" + Arrays.deepToString(tcDist).replace("], ", "]\n"));
		}

		for (int i = 0; i < nRows; i++) {
			int minValue = maxValue;
			int minIndex = -1;
			// seeking for the minimum value in the i-row
			for (int j = 0; j < nCols; j++) {
				if (tcDist[i][j] < minValue) {
					minValue = tcDist[i][j];
					minIndex = j;
				}
			}
			testSuitesDist += minValue;
			// updating all the values of the rows following the current
			// i-row in the minIndex column so that the values tcDist[i][minIndex]
			// of that column won't be chosen in the following iterations
			for (int k = i + 1; k < nRows; k++)
				tcDist[k][minIndex] = maxValue;

			/* Debug printing of tcDist as table and other info */
			if (PRINT_DEBUG) {
				System.out.println("MaxValue: " + maxValue);
				System.out.println("Iteration: " + (i + 1));
				System.out.println("minValue: " + minValue + ", minIndex: " + minIndex);
				System.out.println("tcDist:\n" + Arrays.deepToString(tcDist).replace("], ", "]\n"));
			}

		}
		return testSuitesDist;
	}

	/**
	 * Get the maximum value that can be found in tcDist matrix
	 */
	public static int get_maxValue(int[][] tcDist) {
		int max = -1;

		final int nRows = tcDist.length; // tcDist rows number
		final int nCols = tcDist[0].length; // tcDist cols number

		for (int i = 0; i < nRows; i++) {
			for (int j = 0; j < nCols; j++) {
				if (tcDist[i][j] > max)
					max = tcDist[i][j];
			}
		}

		return max;
	}

	/**
	 * Create tcDist matrix for test suites ts and tsp
	 * 
	 * @param ts  the first test suite
	 * @param tsp the second test suite
	 * @return the tcDist matrix with distance values between all the test cases
	 *         from ts and tsp
	 */
	public static int[][] get_tcDist(TestSuite ts, TestSuite tsp) {

		final int cardTs = ts.getTests().size();
		final int cardTsp = tsp.getTests().size();
		int[][] tcDist;

		// |TS|>=|TS'|
		if (cardTs >= cardTsp)
			tcDist = new int[cardTsp][cardTs]; // ts=cols, tsp=rows
		// |TS|<|TS'|
		else
			tcDist = new int[cardTs][cardTsp]; // ts=rows, tsp=cols

		final int nRows = tcDist.length; // tcDist rows number
		final int nCols = tcDist[0].length; // tcDist cols number

		for (int i = 0; i < nRows; i++) {
			for (int j = 0; j < nCols; j++) {
				// |TS|>=|TS'|
				if (cardTs >= cardTsp) // i=tsp=rows, j=ts=cols
					tcDist[i][j] = DistancesCalculator.testCasesDist(tsp.getTests().get(i), ts.getTests().get(j));
				// |TS|<|TS'|
				else // i=ts=rows, j=tsp=cols
					tcDist[i][j] = DistancesCalculator.testCasesDist(ts.getTests().get(i), tsp.getTests().get(j));
			}
		}

		return tcDist;
	}

	/**
	 * Compute the distance between the test cases t and tp
	 * 
	 * @param t  the first test case
	 * @param tp the second test case
	 * @return the value of the distance between the t and tp
	 */
	public static int testCasesDist(Test t, Test tp) {

		// Set guarantee that there won't be duplicates values in "features" set
		Set<String> features = new TreeSet<String>();
		features.addAll(t.keySet());
		features.addAll(tp.keySet());

		int testCasesDist = 0;

		for (String s : features) {
			// get returns the value to which the specified key is mapped
			// or null if this map contains no mapping for the key
			testCasesDist += DistancesCalculator.featDist(t.get(s), tp.get(s));
		}

		return testCasesDist;
	};

	/**
	 * Compares the values of feature f with the value of feature fp
	 * 
	 * @param f  the value of the feature f belonging to T
	 * @param fp the value of the feature f' belonging to T'
	 * @return 0 if the values of f and fp are the same, 1 otherwise
	 */
	public static int featDist(String f, String fp) {
		if (f == null || fp == null || !f.equals(fp))
			return 1;
		else
			return 0;
	}

}