package featuremodels.specificity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

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
import net.sf.javabdd.BDD;
import net.sf.javabdd.BDD.AllSatIterator;
import pMedici.util.Pair;

/**
 * This generator generates combinatorial test cases specific to test the
 * evolution from an old feature model to its new version
 */
public class SpecificCITTestGenerator extends BDDCITTestGenerator {

	private IFeatureModel oldFm;	
	private ArrayList<BDD> specificTests;
	private ArrayList<BDD> nonSpecificTests;

	/**
	 * Builds a new SpecificCITTestGenerator
	 * 
	 * @param oldFm    the old feature model
	 * @param newFm    the new feature model
	 * @param strength the strength for CIT generation
	 */
	SpecificCITTestGenerator(IFeatureModel oldFm, IFeatureModel newFm, int strength) {
		super(newFm, strength);
		this.oldFm = oldFm;
		this.specificTests = new ArrayList<BDD>();
		this.nonSpecificTests = new ArrayList<BDD>();
	}

	/**
	 * Generates a test suite which maximizes the specificity of the test cases
	 * 
	 * @return a specific test suite
	 * @throws IOException
	 */
	@Override
	public TestSuite generateTestSuite() throws IOException {
		// Get all the tuples for this feature model
		Iterator<List<Pair<String, Integer>>> tg = getTuplesFromFM(fm, strength);
		// The set of features is the union between those from the old model and those
		// from the new one
		LinkedHashSet<String> featureSet = fm.getFeatures().stream().map(t -> t.getName())
				.collect(Collectors.toCollection(LinkedHashSet::new));
		featureSet.addAll(oldFm.getFeatures().stream().map(t -> t.getName()).toList());
		List<String> featureList = new ArrayList<String>(featureSet);

		long initialTime = System.currentTimeMillis();

		// BDD Builder. It must be used for creating all BDDs in order to maintain the
		// same origin structure
		FMToBDD bdd_builder = new FMToBDD(featureList);
		// Convert the two FMs into their corresponding BDDs
		BDD bddNew = getBDDFromFM(fm, bdd_builder);
		BDD bddOld = getBDDFromFM(oldFm, bdd_builder);

		// Set in bddNew the deleted features
		bddNew = setDeletedFeatures(bddNew, fm, featureList, bdd_builder);

		// Just for debug purposes, print the list of satisfying products
		printBDDSat(featureList, bddNew, true, "BDDNew");
		printBDDSat(featureList, bddOld, true, "BDDOld");

		// Initial BDD. All possible assignments satisfying this BDD are those that are
		// SPECIFIC for the test evolution
		BDD bddInitial = bddOld.not();
		printBDDSat(featureList, bddInitial, true, "BDDOldWithNot");
		bddInitial = bddInitial.and(bddNew);
		printBDDSat(featureList, bddInitial, true, "BDDInitial");
		boolean skipSpecific = (bddInitial.satCount() == 0);
		if (skipSpecific)
			logger.debug("Skipping specific tests check");

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
			if (!skipSpecific) {
				// Try to cover the tuple with specific tests
				if (tryToCover(bddTuple, specificTests, bddInitial))
					continue;
			}
			// Cover the tuple with non specific tests
			tryToCover(bddTuple, nonSpecificTests, bddNew);
		}

		// Return the test suite
		return getTestSuiteFromTests(featureList, new FeatureIdeImporterBoolean().importModel(fm),
				System.currentTimeMillis() - initialTime);
	}

	/**
	 * Removes the features deleted in the new Feature model (if any)
	 * 
	 * @param bddNew      the bdd of the new fm
	 * @param fm          the feature model
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
	 * Returns a TestSuite object starting from the two sets of specific and non
	 * specific tests
	 * 
	 * @param featureList      the list of features
	 * @param modelNew         the new CITModel
	 * @param time             the time required for test generation
	 * @return
	 */
	private TestSuite getTestSuiteFromTests(List<String> featureList, CitModel modelNew, long time) {
		// Header
		String ts = featureList.stream().collect(Collectors.joining(";")) + ";\n";
		// Specific tests
		logger.info("Generated " + specificTests.size() + " specific BDDs");
		for (BDD st : specificTests) {
			AllSatIterator it = st.allsat();
			if (it.hasNext()) {
				byte[] thisSat = (byte[]) it.next();
				for (int i = 0; i < featureList.size(); i++) {
					ts = ts + (thisSat[i] == 1 ? "true" : (thisSat[i] == 0 ? "false" : "*")) + ";";
				}
				ts = ts + "\n";
			}
		}
		// Not specific tests
		logger.info("Generated " + nonSpecificTests.size() + " non specific BDDs");
		for (BDD st : nonSpecificTests) {
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
		TestSuite res = new TestSuite(ts, modelNew, ";");
		res.setGeneratorName("SPECIFICITY_FM");
		res.setGeneratorTime(time);
		return res;
	}

}
