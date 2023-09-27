package featuremodels.specificity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.prop4j.And;
import org.prop4j.FMToBDD;
import org.prop4j.Literal;
import org.prop4j.Node;
import org.prop4j.Not;

import ctwedge.util.TestSuite;
import de.ovgu.featureide.fm.core.base.IFeature;
import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.editing.NodeCreator;
import de.ovgu.featureide.fm.core.init.FMCoreLibrary;
import net.sf.javabdd.BDD;
import net.sf.javabdd.BDD.AllSatIterator;
import pMedici.util.Pair;
import pMedici.util.TupleGenerator;

// it generates specific tests if possible
public class SpecificCITTestGenerator {

	private Logger logger = Logger.getLogger(SpecificCITTestGenerator.class);
	private IFeatureModel oldFm;
	private IFeatureModel newFm;
	private int strength;
	private ArrayList<BDD> specificTests;
	private ArrayList<BDD> nonSpecificTests;

	SpecificCITTestGenerator(IFeatureModel oldFm, IFeatureModel newFm, int strength) {
		FMCoreLibrary.getInstance().install();
		this.oldFm = oldFm;
		this.newFm = newFm;
		this.strength = strength;
		this.specificTests = new ArrayList<BDD>();
		this.nonSpecificTests = new ArrayList<BDD>();
	}

	/**
	 * Generates a test suite which maximizes the specificity of the test cases
	 * 
	 * @return a specific test suite
	 * @throws IOException
	 */
	public TestSuite generateSpecificTestSuite() throws IOException {
		// Get all the tuples for this feature model
		Iterator<List<Pair<String, Integer>>> tg = getTuplesFromFM(newFm, strength);
		// The set of features is the union between those from the old model and those
		// from the new one
		LinkedHashSet<String> featureSet = newFm.getFeatures().stream().map(t -> t.getName())
				.collect(Collectors.toCollection(LinkedHashSet::new));
		featureSet.addAll(oldFm.getFeatures().stream().map(t -> t.getName()).toList());
		List<String> featureList = new ArrayList<String>(featureSet);
		// BDD Builder. It must be used for creating all BDDs in order to maintain the
		// same origin structure
		FMToBDD bdd_builder = new FMToBDD(featureList);
		// Convert the two FMs into their corresponding BDDs
		BDD bddNew = getBDDFromFM(newFm, bdd_builder);
		BDD bddOld = getBDDFromFM(oldFm, bdd_builder);

		// Set in bddNew the deleted features
		bddNew = setDeletedFeatures(bddNew, newFm, featureList, bdd_builder);

		// Just for debug purposes, print the list of satisfying products
		printBDDSat(featureList, bddNew);
		printBDDSat(featureList, bddOld);

		// Initial BDD. All possible assignments satisfying this BDD are those that are
		// SPECIFIC for the test evolution
		BDD bddInitial = bddOld.not();
		bddInitial = bddInitial.and(bddNew);
		printBDDSat(featureList, bddInitial);
		boolean skipSpecific = (bddInitial.satCount() == 0);

		// List of specific and non specific tests
		this.specificTests = new ArrayList<BDD>();
		this.nonSpecificTests = new ArrayList<BDD>();

		// Fetch all tuples
		while (tg.hasNext()) {
			List<Pair<String, Integer>> tp = tg.next();
			String tpAsString = tp.stream().map(x -> "[" + x.getFirst() + "," + x.getSecond() + "]")
					.collect(Collectors.joining(","));
			logger.debug("Checking " + tpAsString);

			// Build the node for the tuple under consideration
			Node newBDDNode = NodeCreator.createNodes(newFm);
			for (Pair<String, Integer> elem : tp) {
				IFeature feature = newFm.getFeature(elem.getFirst());
				newBDDNode = new And((elem.getSecond() == 1 ? new Literal(feature) : new Not(new Literal(feature))),
						newBDDNode);
			}
			BDD bddTuple = bdd_builder.nodeToBDD(newBDDNode);

			// The tuple cannot be covered
			if (bddNew.and(bddTuple).satCount() == 0)
				continue;
			if (!skipSpecific) {
				// Try to cover the tuple with specific tests
				if (tryToCover(bddTuple, specificTests, bddInitial))
					continue;
			}
			// Cover the tuple with non specific tests
			tryToCover(bddTuple, nonSpecificTests, bddNew);
		}

		// Return the test suite
		return getTestSuiteFromTests(specificTests, nonSpecificTests, featureList);
	}

	/**
	 * Removes the features deleted in the new Feature model (if any)
	 * 
	 * @param bddNew the bdd of the new fm
	 * @param fm the feature model
	 * @param featureList the list of all the features
	 * @param bdd_builder the BDD Builder
	 * @return the updated bdd
	 */
	private BDD setDeletedFeatures(BDD bddNew, IFeatureModel fm, List<String> featureList, FMToBDD bdd_builder) {
		for (String feature : featureList) {
			if (fm.getFeature(feature) == null) {
				bddNew.andWith(bdd_builder.nodeToBDD(new Not(new Literal(feature))));
			}
		}
		return bddNew;
	}

	/**
	 * Prints the assignments satisfying the BDD
	 * 
	 * @param featureList the list of features
	 * @param bdd         the bdd
	 */
	private void printBDDSat(List<String> featureList, BDD bdd) {
		AllSatIterator it = bdd.allsat();
		while (it.hasNext()) {
			byte[] thisSat = (byte[]) it.next();
			for (int i = 0; i < featureList.size(); i++) {
				logger.debug(
						featureList.get(i) + " -> " + (thisSat[i] == 1 ? "true" : (thisSat[i] == 0 ? "false" : "*")));
			}
			logger.debug("---------");
		}
	}

	private TestSuite getTestSuiteFromTests(ArrayList<BDD> specificTests, ArrayList<BDD> nonSpecificTests,
			List<String> featureList) {
		// Header
		String ts = featureList.stream().collect(Collectors.joining(";"));
		// Specific tests
		logger.debug("Generated " + specificTests.size() + " specific tests");
		logger.debug("Generated " + nonSpecificTests.size() + " non specific tests");
		// TODO Return a real test suite
		return null;
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
	private boolean tryToCover(BDD tp, ArrayList<BDD> testSet, BDD bddNoTp) {
		for (int i = 0; i < testSet.size(); i++) {
			// If the tuple can be covered with the considered test-BDD
			if (testSet.get(i).and(bddNoTp).satCount() > 0) {
				testSet.set(i, testSet.get(i).and(bddNoTp));
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
	 * Given the FM of interest, the method returns the corresponding BDD
	 * 
	 * @param fm          the feature model
	 * @param bdd_builder the bdd builder
	 * @return the bdd
	 */
	private BDD getBDDFromFM(IFeatureModel fm, FMToBDD bdd_builder) {
		// Convert the FM into the corresponding BDD
		return bdd_builder.nodeToBDD(NodeCreator.createNodes(fm));
	}

	/**
	 * Given the feature model, it returns the iterator over all the tuples
	 * 
	 * @param fm the feature model
	 * @return the iterator over all tuples derived from the feature model newFM2
	 */
	private Iterator<List<Pair<String, Integer>>> getTuplesFromFM(IFeatureModel fm, int strength) {
		List<String> features = newFm.getFeatures().stream().map(t -> t.getName()).toList();
		HashMap<String, List<Integer>> values = new HashMap<>();
		ArrayList<Integer> featureValues = new ArrayList<Integer>();
		featureValues.add(0);
		featureValues.add(1);
		for (String s : features) {
			values.put(s, featureValues);
		}
		return TupleGenerator.getAllKWiseCombination(values, strength);
	}

}
