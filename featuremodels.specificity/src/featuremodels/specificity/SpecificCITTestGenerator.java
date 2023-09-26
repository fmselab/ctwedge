package featuremodels.specificity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.prop4j.FMToBDD;

import ctwedge.fmtester.experiments.TestSimpleExampleForPaper;
import ctwedge.util.TestSuite;
import ctwedge.util.ext.Utility;
import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.editing.NodeCreator;
import net.sf.javabdd.BDD;
import pMedici.util.Operations;
import pMedici.util.Order;
import pMedici.util.Pair;
import pMedici.util.TupleGenerator;

// it generates specific tests if possible
public class SpecificCITTestGenerator {

	private IFeatureModel oldFm;
	private IFeatureModel newFm;
	// TODO do we really need it?
	private boolean useEnum;
	private int strength;
	private ArrayList<BDD> specificTests;
	private ArrayList<BDD> nonSpecificTests;

	SpecificCITTestGenerator(IFeatureModel oldFm, IFeatureModel newFm, boolean useEnum, int strength) {
		this.oldFm = oldFm;
		this.newFm = newFm;
		this.useEnum = useEnum;
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
		Iterator<List<Pair<Integer, Integer>>> tg = getTuplesFromFM(newFm, strength);
		// Convert the two FMs into their corresponding BDDs
		BDD bddNew = getBDDFromFM(newFm);
		BDD bddOld = getBDDFromFM(oldFm);

		// Initial BDD. All possible assignments satisfying this BDD are those that are
		// SPECIFIC for the test evolution
		BDD bddInitial = bddOld.not().and(bddNew);
		boolean skipSpecific = (bddInitial.satCount() == 0);

		// List of specific and non specific tests
		this.specificTests = new ArrayList<BDD>();
		this.nonSpecificTests = new ArrayList<BDD>();

		// Fetch all tuples
		while (tg.hasNext()) {
			List<Pair<Integer, Integer>> tp = tg.next();
			// TODO how to map tuples (i.e., pairs of integers) into a BDD? It depends on
			// the structure of the BDD itself
			BDD bddTuple = null;
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
		return getTestSuiteFromTests(specificTests, nonSpecificTests);
	}

	private TestSuite getTestSuiteFromTests(ArrayList<BDD> specificTests2, ArrayList<BDD> nonSpecificTests2) {
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
		for(BDD bdd : testSet) {
			// If the tuple can be covered with the considered test-BDD
			if (bdd.and(bddNoTp).satCount() > 0) {
				bdd = bdd.and(bddNoTp);
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
	 * @param fm the feature model
	 * @return the bdd
	 */
	private BDD getBDDFromFM(IFeatureModel fm) {
		// Create an FMToBDD object based on the list of features of the model
		FMToBDD bdd_builder = new FMToBDD(fm.getFeatures().stream().map(t -> t.getName()).toList());
		// Convert the FM into the corresponding BDD
		return bdd_builder.nodeToBDD(NodeCreator.createNodes(fm));
	}

	/**
	 * Given the feature model, it returns the iterator over all the tuples
	 * 
	 * @param newFm2 the feature model
	 * @return the iterator over all tuples derived from the feature model newFM2
	 * @throws IOException
	 */
	private Iterator<List<Pair<Integer, Integer>>> getTuplesFromFM(IFeatureModel newFm2, int strength)
			throws IOException {
		TestSimpleExampleForPaper tester = new TestSimpleExampleForPaper();
		String oldModel = tester.convertModelFromFMToCTW(newFm2, "temp");
		LinkedHashMap<Integer, List<Integer>> elements = Operations.getElementsMap(Utility.loadModelFromPath(oldModel),
				Order.AS_DECLARED);
		return TupleGenerator.getAllKWiseCombination(elements, strength);
	}

}
