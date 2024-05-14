package featuremodels.specificity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.prop4j.And;
import org.prop4j.FMToBDD;
import org.prop4j.Literal;
import org.prop4j.Node;
import org.prop4j.Not;

import ctwedge.ctWedge.CitModel;
import ctwedge.importer.featureide.FeatureIdeImporterBoolean;
import ctwedge.util.TestSuite;
import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.editing.NodeCreator;
import de.ovgu.featureide.fm.core.init.FMCoreLibrary;
import net.sf.javabdd.BDD;
import net.sf.javabdd.BDD.AllSatIterator;
import pMedici.util.Pair;
import pMedici.util.TupleGenerator;

/**
 * This generator generates combinatorial test cases from a feature model
 */
public class BDDCITTestGenerator {

	/**
	 * The feature model
	 */
	protected IFeatureModel fm;

	/**
	 * The strength for test generation
	 */
	protected int strength;

	/**
	 * Builds a new BDDCITTestGenerator
	 * 
	 * @param fm       the feature model for which tests have to be generated
	 * @param strength the strength
	 */
	public BDDCITTestGenerator(IFeatureModel fm, int strength) {
		FMCoreLibrary.getInstance().install();
		this.fm = fm;
		this.strength = strength;
	}

	/**
	 * The logger used during test generation
	 */
	protected Logger logger = Logger.getLogger(BDDCITTestGenerator.class);

	/**
	 * Given the feature model, it returns the iterator over all the tuples
	 * 
	 * @param fm the feature model
	 * @return the iterator over all tuples derived from the feature model newFM2
	 */
	protected Iterator<List<Pair<String, Integer>>> getTuplesFromFM(IFeatureModel fm, int strength) {
		List<String> features = fm.getFeatures().stream().map(t -> t.getName()).toList();
		HashMap<String, List<Integer>> values = new HashMap<>();
		ArrayList<Integer> featureValues = new ArrayList<Integer>();
		featureValues.add(0);
		featureValues.add(1);
		for (String s : features) {
			values.put(s, featureValues);
		}
		return TupleGenerator.getAllKWiseCombination(values, strength);
	}

	/**
	 * Given the FM of interest, the method returns the corresponding BDD
	 * 
	 * @param fm          the feature model
	 * @param bdd_builder the bdd builder
	 * @return the bdd
	 */
	protected BDD getBDDFromFM(IFeatureModel fm, FMToBDD bdd_builder) {
		// Convert the FM into the corresponding BDD
		return bdd_builder.nodeToBDD(NodeCreator.createNodes(fm));
	}

	/**
	 * Tries to cover a tuple with one of the test in the test set, if possible, or
	 * creates a new one
	 * 
	 * @param tp      the BDD of the tuple
	 * @param testSet the test set
	 * @param bddNoTp the bdd without any tuple commited
	 * @return
	 */
	protected boolean tryToCover(BDD tp, ArrayList<BDD> testSet, BDD bddNoTp) {
		for (int i = 0; i < testSet.size(); i++) {
			// If the tuple can be covered with the considered test-BDD
			if (testSet.get(i).and(tp).satCount() > 0) {
				testSet.set(i, testSet.get(i).andWith(tp));
				return true;
			}
		}
		// Try to cover the tuple with a new BDD
		if (bddNoTp.and(tp).satCount() > 0) {
			testSet.add(bddNoTp.and(tp));
			return true;
		}
		// The tuple cannot be covered
		return false;
	}

	/**
	 * Prints the assignments satisfying the BDD
	 * 
	 * @param featureList the list of features
	 * @param bdd         the bdd
	 * @param useLogger   use the logger or not (i.e., standard output)
	 * @param prefix      the text to be printed before each BDD
	 */
	protected void printBDDSat(List<String> featureList, BDD bdd, boolean useLogger, String prefix) {
		AllSatIterator it = bdd.allsat();
		while (it.hasNext()) {
			printString(useLogger, prefix);

			byte[] thisSat = (byte[]) it.next();
			for (int i = 0; i < featureList.size(); i++) {
				printString(useLogger,
						featureList.get(i) + " -> " + (thisSat[i] == 1 ? "true" : (thisSat[i] == 0 ? "false" : "*")));
			}
			printString(useLogger, "---------");
		}
	}

	/**
	 * It prints a string on a logger or on the standard output
	 * 
	 * @param useLogger use the logger?
	 * @param str       the string to print
	 */
	private void printString(boolean useLogger, String str) {
		if (useLogger)
			logger.debug(str);
		else
			System.out.println(str);
	}

	/**
	 * Prints the assignments satisfying the BDD
	 * 
	 * @param featureList the list of features
	 * @param bdd         the bdd
	 * @param useLogger   use the logger or not (i.e., standard output)
	 */
	protected void printBDDSat(List<String> featureList, BDD bdd, boolean useLogger) {
		printBDDSat(featureList, bdd, useLogger, "");
	}

	/**
	 * Generates a test suite for the feature model under analysis
	 * 
	 * @return a test suite with the desired combinatorial coverage
	 * @throws IOException
	 */
	public TestSuite generateTestSuite() throws IOException {
		// Get all the tuples for this feature model
		Iterator<List<Pair<String, Integer>>> tg = getTuplesFromFM(fm, strength);
		// The set of features is the union between those from the old model and those
		// from the new one
		List<String> featureList = fm.getFeatures().stream().map(t -> t.getName()).collect(Collectors.toList());

		long initialTime = System.currentTimeMillis();

		// BDD Builder. It must be used for creating all BDDs in order to maintain the
		// same origin structure
		FMToBDD bdd_builder = new FMToBDD(featureList);
		// Convert the FM into its corresponding BDD
		BDD bddNew = getBDDFromFM(fm, bdd_builder);

		// Just for debug purposes, print the list of satisfying products
		printBDDSat(featureList, bddNew, true, "BDD");

		// List of tests
		ArrayList<BDD> tests = new ArrayList<BDD>();

		// Fetch all tuples
		while (tg.hasNext()) {
			// Convert the FM into its corresponding BDD
			bddNew = getBDDFromFM(fm, bdd_builder);
			
			List<Pair<String, Integer>> tp = tg.next();
			String tpAsString = tp.stream().map(x -> "[" + x.getFirst() + "," + x.getSecond() + "]")
					.collect(Collectors.joining(","));
			logger.debug("Checking " + tpAsString);

			// Build the node for the tuple under consideration
			Node newBDDNode = NodeCreator.createNodes(fm);
			for (Pair<String, Integer> elem : tp) {
				newBDDNode = new And(
						(elem.getSecond() == 1 ? new Literal(elem.getFirst()) : new Not(new Literal(elem.getFirst()))),
						newBDDNode);
			}
			BDD bddTuple = bdd_builder.nodeToBDD(newBDDNode);

			// The tuple cannot be covered
			if (bddNew.and(bddTuple).satCount() == 0)
				continue;
			// Cover the tuple with non specific tests
			tryToCover(bddTuple, tests, bddNew);
		}

		// Return the test suite
		return getTestSuiteFromTests(tests, featureList, new FeatureIdeImporterBoolean().importModel(fm),
				System.currentTimeMillis() - initialTime);
	}

	/**
	 * Returns a TestSuite object starting from the set tests
	 * 
	 * @param tests       the list of tests
	 * @param featureList the list of features
	 * @param model       the CITModel
	 * @param time        the time required for test generation
	 * @return
	 */
	private TestSuite getTestSuiteFromTests(List<BDD> tests, List<String> featureList, CitModel model, long time) {
		// Header
		String ts = featureList.stream().collect(Collectors.joining(";")) + ";\n";
		// Specific tests
		logger.info("Generated " + tests.size() + " BDDs");
		for (BDD st : tests) {
			AllSatIterator it = st.allsat();
			if (it.hasNext()) {
				byte[] thisSat = (byte[]) it.next();
				for (int i = 0; i < featureList.size(); i++) {
					ts = ts + (thisSat[i] == 1 ? "true" : (thisSat[i] == 0 ? "false" : "*")) + ";";
				}
				ts = ts + "\n";
			}
		}

		// Return a real test suite
		TestSuite res = new TestSuite(ts, model, ";");
		res.setGeneratorName("BDD_FM");
		res.setGeneratorTime(time);
		return res;
	}
}
