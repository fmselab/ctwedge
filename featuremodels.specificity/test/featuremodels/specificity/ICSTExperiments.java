package featuremodels.specificity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Test;

import ctwedge.ctWedge.CitModel;
import ctwedge.fmtester.experiments.MutationScore;
import ctwedge.fmtester.experiments.TestSimpleExampleForPaper;
import ctwedge.fmtester.experiments.MutationScore.MissingFeatureTreatment;
import ctwedge.generator.acts.ACTSTranslator;
import ctwedge.importer.featureide.FeatureIdeImporter;
import ctwedge.importer.featureide.FeatureIdeImporterBoolean;
import ctwedge.util.TestSuite;
import ctwedge.util.validator.MinimalityTestSuiteValidator;
import de.ovgu.featureide.fm.core.ExtensionManager.NoSuchExtensionException;
import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.init.FMCoreLibrary;
import de.ovgu.featureide.fm.core.io.UnsupportedModelException;
import de.ovgu.featureide.fm.core.io.manager.FeatureModelManager;
import fmautorepair.mutationoperators.FMMutation;
import fmautorepair.mutationoperators.FMMutator;
import fmautorepair.mutationprocess.FMMutationProcess;

public class ICSTExperiments {

	public static int N_REP = 10;
	public static int MAX_MUTATIONS = 10;

	static {
		FMCoreLibrary.getInstance().install();
	}

	@Test
	public void testExperiments() throws IOException, UnsupportedModelException, NoSuchExtensionException {
		String output_file = "resultsSPECIFICITY.csv";
		BufferedWriter fw = new BufferedWriter(new FileWriter(new File(output_file), false));
		fw.write("OldFM;NewFM;Generator;Size;Time;NSpecificTests;NNotSpecificTests;Specificity;FaultDetection\n");
		fw.close();

		for (int i = 0; i < N_REP; i++) {
			Logger.getLogger(BDDCITTestGenerator.class).setLevel(Level.OFF);
			testEvo(TestSimpleExampleForPaper.EV_ALIV);
			testEvo(TestSimpleExampleForPaper.EV_PPU);
			testEvo(TestSimpleExampleForPaper.EV_AUTOM);
			testEvo(TestSimpleExampleForPaper.EV_BOING);
			testEvo(TestSimpleExampleForPaper.EV_CARBODY);
			testEvo(TestSimpleExampleForPaper.EV_LINUX);
			testEvo(TestSimpleExampleForPaper.EV_PARKING);
			testEvo(TestSimpleExampleForPaper.EV_BCS);
			testEvo(TestSimpleExampleForPaper.EV_ERP);
			testEvo(TestSimpleExampleForPaper.EV_HSYS);
			testEvo(TestSimpleExampleForPaper.EV_MOBMEDIA);
			testEvo(TestSimpleExampleForPaper.EV_SHOME);
			testEvo(TestSimpleExampleForPaper.EV_SMARTH);
			testEvo(TestSimpleExampleForPaper.EV_SMARTW);
			testEvo(TestSimpleExampleForPaper.EV_WSTAT);
		}
	}

	private void testEvo(String[] evo) throws IOException, UnsupportedModelException, NoSuchExtensionException {
		for (int i = 1; i < evo.length - 1; i++) {
			for (int j = 1; j < evo.length - 1; j++)
				if (i != j) {
					// Execute the experiments on the original model
					executeTest("../featuremodels.evotester/" + evo[0] + "/" + evo[i] + ".xml",
							"../featuremodels.evotester/" + evo[0] + "/" + evo[j] + ".xml");
				}
		}
	}

	private void executeTest(String oldFm, String newFm)
			throws IOException, UnsupportedModelException, NoSuchExtensionException {
		// Output files
		String output_file = "resultsSPECIFICITY.csv";
		BufferedWriter fw = new BufferedWriter(new FileWriter(new File(output_file), true));

		// Load the two feature models
		Path oldFMPath = Path.of(oldFm);
		IFeatureModel oldFM = FeatureModelManager.load(oldFMPath);
		Path newFMPath = Path.of(newFm);
		IFeatureModel newFM = FeatureModelManager.load(newFMPath);
		FeatureIdeImporter importer = new FeatureIdeImporterBoolean();
		CitModel result = importer.importModel(newFMPath.toString());
		SpecificityChecker spcheck = new SpecificityChecker(oldFM, newFM, false);
		
		// Generate using SPECIFICITY
		generateWithSpecificity(oldFm, newFm, fw, oldFM, newFMPath, newFM, spcheck);

		// Generate with ACTS
		generateWithACTS(oldFm, newFm, fw, newFMPath, result, spcheck);

		// Generate with BDD without specificity
		generateWithBDDs(oldFm, newFm, fw, oldFM, newFMPath, newFM);

		fw.close();
	}

	private void generateWithSpecificity(String oldFm, String newFm, BufferedWriter fw, IFeatureModel oldFM,
			Path newFMPath, IFeatureModel newFM, SpecificityChecker spcheck)
			throws IOException, FileNotFoundException, UnsupportedModelException, NoSuchExtensionException {
		SpecificCITTestGenerator gen = new SpecificCITTestGenerator(oldFM, newFM, 2);
		TestSuite ts = gen.generateTestSuite();
		ts.setStrength(2);
		MinimalityTestSuiteValidator minimality = new MinimalityTestSuiteValidator(ts);
		TestSuite tsReduced = minimality.reduceSize();
		int countSpec = 0;
		int countNotSpec = 0;
		for (ctwedge.util.Test t : tsReduced.getTests()) {
			if (spcheck.isSpecific(t))
				countSpec++;
			else
				countNotSpec++;
		}

		// SPECIFICITY output
		fw.write(new File(oldFm).getName() + ";" + new File(newFm).getName() + ";" + tsReduced.getGeneratorName() + ";"
				+ tsReduced.getTests().size() + ";" + tsReduced.getGeneratorTime() + ";" + countSpec + ";"
				+ countNotSpec + ";" + (float) countSpec / tsReduced.getTests().size() + ";"
				+ computeFaultDetectionCapability(newFMPath, tsReduced) + "\n");
	}

	private void generateWithBDDs(String oldFm, String newFm, BufferedWriter fw, IFeatureModel oldFM, Path newFMPath,
			IFeatureModel newFM)
			throws IOException, FileNotFoundException, UnsupportedModelException, NoSuchExtensionException {
		TestSuite ts;
		MinimalityTestSuiteValidator minimality;
		TestSuite tsReduced;
		SpecificityChecker spcheck;
		int countSpec;
		int countNotSpec;
		BDDCITTestGenerator genBDD = new BDDCITTestGenerator(newFM, 2);
		ts = genBDD.generateTestSuite();
		ts.setStrength(2);
		minimality = new MinimalityTestSuiteValidator(ts);
		tsReduced = minimality.reduceSize();
		spcheck = new SpecificityChecker(oldFM, newFM, false);
		countSpec = 0;
		countNotSpec = 0;
		for (ctwedge.util.Test t : tsReduced.getTests()) {
			if (spcheck.isSpecific(t))
				countSpec++;
			else
				countNotSpec++;
		}

		// BDD output
		fw.write(new File(oldFm).getName() + ";" + new File(newFm).getName() + ";" + tsReduced.getGeneratorName() + ";"
				+ tsReduced.getTests().size() + ";" + tsReduced.getGeneratorTime() + ";" + countSpec + ";"
				+ countNotSpec + ";" + (float) countSpec / tsReduced.getTests().size() + ";"
				+ computeFaultDetectionCapability(newFMPath, tsReduced) + "\n");
	}

	private void generateWithACTS(String oldFm, String newFm, BufferedWriter fw, Path newFMPath, CitModel result,
			SpecificityChecker spcheck)
			throws IOException, FileNotFoundException, UnsupportedModelException, NoSuchExtensionException {
		int countSpec;
		int countNotSpec;
		ACTSTranslator acts = new ACTSTranslator();
		TestSuite tsACTS = acts.getTestSuite(result, 2, false);
		tsACTS.setGeneratorName("ACTS");
		countSpec = 0;
		countNotSpec = 0;
		for (ctwedge.util.Test t : tsACTS.getTests()) {
			if (spcheck.isSpecific(t))
				countSpec++;
			else
				countNotSpec++;
		}

		// ACTS output
		fw.write(new File(oldFm).getName() + ";" + new File(newFm).getName() + ";" + tsACTS.getGeneratorName() + ";"
				+ tsACTS.getTests().size() + ";" + tsACTS.getGeneratorTime() + ";" + countSpec + ";" + countNotSpec
				+ ";" + (float) countSpec / tsACTS.getTests().size() + ";"
				+ computeFaultDetectionCapability(newFMPath, tsACTS) + "\n");
	}

	/**
	 * Computes the fault detection capability of the evolved test suite
	 * 
	 * @param fm : the new feature model path
	 * @param ts : the new test suite
	 * @return : the fault detection capability of the evolved test suite
	 * @throws NoSuchExtensionException
	 * @throws UnsupportedModelException
	 * @throws FileNotFoundException
	 */
	private float computeFaultDetectionCapability(Path fm, TestSuite ts)
			throws FileNotFoundException, UnsupportedModelException, NoSuchExtensionException {
		float totMut = 0;
		float killedMut = 0;

		IFeatureModel fm2 = FeatureModelManager.load(fm);

		// Define the mutators
		@SuppressWarnings("deprecation")
		List<FMMutator> mutatorList = FMMutationProcess.allMutationOperatorsAsList();

		// Apply the mutations
		for (FMMutator mut : mutatorList) {
			// Fetch all the obtained mutants
			Iterator<FMMutation> mutations = mut.mutate(fm2);
			while (mutations.hasNext()) {
				totMut++;
				FMMutation fmM = mutations.next();

				// Transform the test to a configuration
				IFeatureModel featureModel = fmM.getFirst();
				for (ctwedge.util.Test test : ts.getTests()) {
					MutationScore.treat_missing_feature_as = MissingFeatureTreatment.SKIP;
					Boolean result = MutationScore.isTestValidBool(featureModel, test);
					if (!result) {
						killedMut++;
						break;
					}
				}
			}
		}

		return totMut != 0 ? killedMut / totMut : 0;
	}

}
