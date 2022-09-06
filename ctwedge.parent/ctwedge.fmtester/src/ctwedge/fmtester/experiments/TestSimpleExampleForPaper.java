package ctwedge.fmtester.experiments;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;

import org.apache.commons.collections4.iterators.ArrayListIterator;
import org.junit.Test;

import ctwedge.ctWedge.CitModel;
import ctwedge.fmtester.Converter;
import ctwedge.fmtester.DistancesCalculator;
import ctwedge.generator.acts.ACTSTranslator;
import ctwedge.generator.exporter.ToCSV;
import ctwedge.importer.featureide.FeatureIdeImporter;
import ctwedge.importer.featureide.XmlFeatureModelImporter;
import ctwedge.util.TestSuite;
import de.ovgu.featureide.fm.core.ExtensionManager.NoSuchExtensionException;
import de.ovgu.featureide.fm.core.analysis.cnf.formula.FeatureModelFormula;
import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.configuration.ConfigurationPropagator;
import de.ovgu.featureide.fm.core.configuration.Selection;
import de.ovgu.featureide.fm.core.io.UnsupportedModelException;
import de.ovgu.featureide.fm.core.job.LongRunningWrapper;
import fmautorepair.mutationoperators.FMMutation;
import fmautorepair.mutationoperators.FMMutator;
import fmautorepair.mutationoperators.features.*;
import fmautorepair.mutationprocess.FMMutationProcess;
import fmautorepair.utils.CollectionsUtil;
import fmautorepair.utils.Utils;
import pMedici.importer.CSVImporter;
import pMedici.main.PMedici;
import pMedici.main.PMediciPlus;
import pMedici.safeelements.TestContext;
import pMedici.util.Operations;
import pMedici.util.TestModel;
import de.ovgu.featureide.fm.core.configuration.Configuration;
import de.ovgu.featureide.fm.core.configuration.ConfigurationPropagator;

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
	public void experimentsForPaper() throws IOException, InterruptedException, UnsupportedModelException, NoSuchExtensionException {
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
	 * @throws NoSuchExtensionException 
	 * @throws UnsupportedModelException 
	 */
	public void launchSingleExperiment(String model1, String model2) throws IOException, InterruptedException, UnsupportedModelException, NoSuchExtensionException {
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
	 * @throws NoSuchExtensionException 
	 * @throws UnsupportedModelException 
	 */
	public void regenerationFromScratch(String oldFMname, String newFMname, int strength, int nThreads,
			String outputPath) throws IOException, InterruptedException, UnsupportedModelException, NoSuchExtensionException {
		PMedici pMedici = new PMedici();
		// First model
		TestSuite mediciTS1 = pMedici.generateTests(oldFMname, strength, nThreads);
		// Second model
		TestSuite mediciTS2 = pMedici.generateTests(newFMname, strength, nThreads);

		// Distance
		float distance = DistancesCalculator.testSuitesDist(mediciTS1, mediciTS2);
		// Mutation score
		float faultDetectionCapability = computeFaultDetectionCapability(newFMname, mediciTS2);

		// Write statistics to file
		FileWriter fw = new FileWriter(outputPath, true);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write("T1;" + oldFMname + ";" + mediciTS1.getTests().size() + ";" + mediciTS1.getGeneratorTime() + ";"
				+ newFMname + ";" + mediciTS2.getTests().size() + ";" + mediciTS2.getGeneratorTime() + ";" + distance
				+ ";" + faultDetectionCapability + ";");
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
	 * @throws NoSuchExtensionException 
	 * @throws UnsupportedModelException 
	 */
	public void generateWithPMediciPlus(String oldFMname, String newFMname, int strength, int nThreads,
			String outputPath) throws IOException, InterruptedException, UnsupportedModelException, NoSuchExtensionException {
		PMedici pMedici = new PMedici();
		// First model
		TestSuite mediciTS1 = pMedici.generateTests(oldFMname, strength, nThreads);
		// Second model
		String mediciModel = pMedici.buildMediciModel(newFMname);
		TestModel m = Operations.readModelFromReader(new BufferedReader(new StringReader(mediciModel)));
		ToCSV converter = new ToCSV();
		String oldTsStr = converter.toCSVcode(mediciTS1);
		Vector<Map<String, String>> oldTs = CSVImporter.readFromReader(new StringReader(oldTsStr));
		long start = System.currentTimeMillis();
		String newTs = PMediciPlus.generateTests(pMedici.getModel(), m, oldTs);
		TestSuite mediciTS2 = new TestSuite(newTs, pMedici.getModel());
		mediciTS2.setGeneratorTime(System.currentTimeMillis() - start);

		// Distance
		float distance = DistancesCalculator.testSuitesDist(mediciTS1, mediciTS2);
		// Mutation score
		float faultDetectionCapability = computeFaultDetectionCapability(newFMname, mediciTS2);

		// Write statistics to file
		FileWriter fw = new FileWriter(outputPath, true);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write("T2;" + oldFMname + ";" + mediciTS1.getTests().size() + ";" + mediciTS1.getGeneratorTime() + ";"
				+ newFMname + ";" + mediciTS2.getTests().size() + ";" + mediciTS2.getGeneratorTime() + ";" + distance
				+ ";" + faultDetectionCapability + ";");
		bw.newLine();
		bw.close();
	}

	/**
	 * Computes the fault detection capability of the evolved test suite
	 * 
	 * @param newFMname : the new feature model
	 * @param ts : the new test suite
	 * @return : the fault detection capability of the evolved test suite
	 * @throws NoSuchExtensionException 
	 * @throws UnsupportedModelException 
	 * @throws FileNotFoundException 
	 */
	private float computeFaultDetectionCapability(String newFMname, TestSuite ts) throws FileNotFoundException, UnsupportedModelException, NoSuchExtensionException {
		String fmPath = newFMname.split("_ctwedge_enum.ctw")[0] + ".xml";
		float totMut = 0;
		float killedMut = 0;
		
		// Read the feature model
		IFeatureModel fm = Utils.readModel(fmPath);
		
		// Define the mutators	
		FMMutator[] mutatorList = FMMutationProcess.allMutationOperators();
		
		// Apply the mutations
		for (FMMutator mut : mutatorList){
			// Fetch all the obtained mutants
			Iterator<FMMutation> mutations = mut.mutate(fm);
			while (mutations.hasNext()) {
				totMut++;
				FMMutation fmM = mutations.next();
				
				// Transform the test to a configuration
				FeatureModelFormula featureModelFormula = new FeatureModelFormula(fmM.getFirst());
				for (ctwedge.util.Test test : ts.getTests()) {
					Configuration conf = new Configuration(featureModelFormula);
					
					// Set every assignment in the test as feature selected or not
					for (Entry<String, String> assignemnt : test.entrySet()) {
						String value = assignemnt.getValue();
						assert value.equals("true") || value.equals("false");
						Selection sel = value.equals("true")? Selection.SELECTED :Selection.UNSELECTED;
						conf.setManual(assignemnt.getKey(), sel);
					}
					
					ConfigurationPropagator cp = new ConfigurationPropagator(featureModelFormula, conf);
					Boolean result = LongRunningWrapper.runMethod(cp.isValid());
					if (!result) {
						killedMut ++;
						break;
					}
				}
			}
		}			
		
		return totMut!=0 ? killedMut/totMut : 0;
	}
}
