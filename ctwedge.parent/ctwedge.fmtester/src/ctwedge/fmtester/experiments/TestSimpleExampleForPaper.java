package ctwedge.fmtester.experiments;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.junit.Test;

import ctwedge.ctWedge.CitModel;
import ctwedge.fmtester.Converter;
import ctwedge.fmtester.DistancesCalculator;
import ctwedge.generator.acts.ACTSTranslator;
import ctwedge.generator.exporter.ToCSV;
import ctwedge.importer.featureide.FeatureIdeImporter;
import ctwedge.importer.featureide.XmlFeatureModelImporter;
import ctwedge.util.TestSuite;
import pMedici.importer.CSVImporter;
import pMedici.main.PMedici;
import pMedici.main.PMediciPlus;
import pMedici.safeelements.TestContext;
import pMedici.util.Operations;
import pMedici.util.TestModel;

public class TestSimpleExampleForPaper {

	@Test
	public void test1() throws IOException, InterruptedException {
		// distance 6 and 5
		extracted("ex_paper1_AG", "ex_paper2_AG");

	}

	@Test
	public void test2() throws IOException, InterruptedException {
		// distance 12 and 5
		extracted("ex_paper1_AB", "ex_paper2_AB");

	}

	public void extracted(String oldFMname, String newFMname) throws IOException, InterruptedException {
		TestContext.IN_TEST = true;
		// genera con tecnica 1 pMedici e calcola distanza
		// MODELLO 1
		String fmName = convertModelFromFMToCTW(oldFMname);
		PMedici pMedici = new PMedici();
		TestSuite mediciTS1 = pMedici.generateTests(fmName, 2, 1);
		// MODELLO 2
		String fmName2 = convertModelFromFMToCTW(newFMname);
		TestSuite mediciTS2 = pMedici.generateTests(fmName2, 2, 1);
		// now compute the distance
		DistancesCalculator.PRINT_DEBUG = true;
		float distance = DistancesCalculator.percTestSuitesDist(mediciTS1, mediciTS2);
		System.out.println(distance);
		// tecnica 2
		String mediciModel = pMedici.buildMediciModel(fmName2);
		TestModel m = Operations.readModelFromReader(new BufferedReader(new StringReader(mediciModel)));
		ToCSV converter = new ToCSV();
		String oldTsStr = converter.toCSVcode(mediciTS1);
		Vector<Map<String, String>> oldTs = CSVImporter.readFromReader(new StringReader(oldTsStr));

		String newTs = PMediciPlus.generateTests(pMedici.getModel(), m, oldTs);
		TestSuite technique2TS = new TestSuite(newTs, pMedici.getModel());
		float distance2 = DistancesCalculator.percTestSuitesDist(mediciTS1, technique2TS);
		System.out.println(distance2);
	}

	@Test
	public void experimentsForPaper() throws IOException, InterruptedException {
		launchSingleExperiment("ex_paper1_AG", "ex_paper2_AG");
	}

	/**
	 * Executes the single experiment
	 * 
	 * @param model1 : the first model
	 * @param model2 : the evolved model
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void launchSingleExperiment(String model1, String model2) throws IOException, InterruptedException {
		TestContext.IN_TEST = true;
		
		String oldModel = convertModelFromFMToCTW(model1);
		String newModel = convertModelFromFMToCTW(model2);

		// Technique 1
		regenerationFromScratch(oldModel, newModel, 2, 1, "output.csv");
		
		// Technique 2
		generateWithPMediciPlus(oldModel, newModel, 2, 1, "output.csv");
	}

	/**
	 * Converts a FM (in xml format) into a CTW model
	 * 
	 * @param modelName : the name of the feature model
	 * @return : the path of the CTW model corresponding to the feature model given
	 *         as input
	 * 
	 * @throws IOException
	 */
	public String convertModelFromFMToCTW(String modelName) throws IOException {
		String fmName = "fmexamples/" + modelName;
		// convert to CTWedge (could be done only once)
		Converter.fromFMtoCTWedge_ENUM(fmName + ".xml", fmName + "_ctwedge_enum.ctw");
		return fmName + "_ctwedge_enum.ctw";
	}

	/**
	 * Generates the test suite from scratch
	 * 
	 * @param oldFMname  : the path of the old FM
	 * @param newFMname  : the path of the evolved FM
	 * @param strength   : the strength
	 * @param nThreads   : the number of threads to be used by pMEDICI
	 * @param outputPath : the path of the output file where statistics are stored
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void regenerationFromScratch(String oldFMname, String newFMname, int strength, int nThreads,
			String outputPath) throws IOException, InterruptedException {
		PMedici pMedici = new PMedici();
		// First model
		TestSuite mediciTS1 = pMedici.generateTests(oldFMname, strength, nThreads);
		// Second model
		TestSuite mediciTS2 = pMedici.generateTests(newFMname, strength, nThreads);

		// Distance
		float distance = DistancesCalculator.testSuitesDist(mediciTS1, mediciTS2);

		// Write statistics to file
		FileWriter fw = new FileWriter(outputPath, true);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write("T1;" + oldFMname + ";" + mediciTS1.getTests().size() + ";" + mediciTS1.getGeneratorTime() + ";"
				+ newFMname + ";" + mediciTS2.getTests().size() + ";" + mediciTS2.getGeneratorTime() + ";" + distance
				+ ";");
		bw.newLine();
		bw.close();
	}
	
	/**
	 * Generates the test suite with pMEDICI+
	 * 
	 * @param oldFMname  : the path of the old FM
	 * @param newFMname  : the path of the evolved FM
	 * @param strength   : the strength
	 * @param nThreads   : the number of threads to be used by pMEDICI
	 * @param outputPath : the path of the output file where statistics are stored
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void generateWithPMediciPlus(String oldFMname, String newFMname, int strength, int nThreads,
			String outputPath) throws IOException, InterruptedException {
		PMedici pMedici = new PMedici();
		// First model
		TestSuite mediciTS1 = pMedici.generateTests(oldFMname, strength, nThreads);
		// Second model
		String mediciModel = pMedici.buildMediciModel(newFMname);
		TestModel m = Operations.readModelFromReader(new BufferedReader(new StringReader(mediciModel)));
		ToCSV converter = new ToCSV();
		String oldTsStr = converter.toCSVcode(mediciTS1);
		Vector<Map<String, String>> oldTs = CSVImporter.readFromReader(new StringReader(oldTsStr));
		String newTs = PMediciPlus.generateTests(pMedici.getModel(), m, oldTs);
		TestSuite mediciTS2 = new TestSuite(newTs, pMedici.getModel());

		// Distance
		float distance = DistancesCalculator.testSuitesDist(mediciTS1, mediciTS2);

		// Write statistics to file
		FileWriter fw = new FileWriter(outputPath, true);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write("T2;" + oldFMname + ";" + mediciTS1.getTests().size() + ";" + mediciTS1.getGeneratorTime() + ";"
				+ newFMname + ";" + mediciTS2.getTests().size() + ";" + mediciTS2.getGeneratorTime() + ";" + distance
				+ ";");
		bw.newLine();
		bw.close();
	}
}
