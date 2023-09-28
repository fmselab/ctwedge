package featuremodels.specificity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Test;

import ctwedge.ctWedge.CitModel;
import ctwedge.fmtester.experiments.TestSimpleExampleForPaper;
import ctwedge.generator.acts.ACTSTranslator;
import ctwedge.importer.featureide.FeatureIdeImporter;
import ctwedge.importer.featureide.FeatureIdeImporterBoolean;
import ctwedge.util.TestSuite;
import ctwedge.util.validator.MinimalityTestSuiteValidator;
import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.init.FMCoreLibrary;
import de.ovgu.featureide.fm.core.io.manager.FeatureModelManager;

public class ICSTExperiments {

	static {
		FMCoreLibrary.getInstance().install();
	}

	@Test
	public void testExperiments() throws IOException {
		Logger.getLogger(SpecificCITTestGenerator.class).setLevel(Level.OFF);
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

	private void testEvo(String[] evo) throws IOException {
		for (int i = 1; i < evo.length - 1; i++) {
			for (int j = 1; j < evo.length - 1; j++)
				if (i != j)
					executeTest("../featuremodels.evotester/" + evo[0] + "/" + evo[i] + ".xml",
							"../featuremodels.evotester/" + evo[0] + "/" + evo[j] + ".xml");
		}
	}

	private void executeTest(String oldFm, String newFm) throws IOException {
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

		// Generate using SPECIFICITY
		SpecificCITTestGenerator gen = new SpecificCITTestGenerator(oldFM, newFM, 2);
		TestSuite ts = gen.generateSpecificTestSuite();
		ts.setStrength(2);
		MinimalityTestSuiteValidator minimality = new MinimalityTestSuiteValidator(ts);
		TestSuite tsReduced = minimality.reduceSize();
		SpecificityChecker spcheck = new SpecificityChecker(oldFM, newFM, false);
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
				+ countNotSpec + ";" + (float) countSpec / tsReduced.getTests().size() + "\n");

		// Generate with ACTS
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
				+ ";" + (float) countSpec / tsACTS.getTests().size() + "\n");

		fw.close();
	}

}
