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
import pMedici.threads.TestBuilder;
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
		String fmName = convertModelFromFMToCTW(oldFMname, "fmexamples/");
		PMedici pMedici = new PMedici();
		TestSuite mediciTS1 = pMedici.generateTests(fmName, 2, 1);
		// MODELLO 2
		String fmName2 = convertModelFromFMToCTW(newFMname, "fmexamples/");
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
	public void experimentsForPaper()
			throws IOException, InterruptedException, UnsupportedModelException, NoSuchExtensionException {		
		TestBuilder.KeepPartialOldTests = true;
		launchSingleExperiment("ex_paper1_AG", "ex_paper2_AG", "fmexamples/");
		launchSingleExperiment("PPUv1", "PPUv2", "evolutionModels/PPU/");
		launchSingleExperiment("PPUv2", "PPUv3", "evolutionModels/PPU/");
		launchSingleExperiment("PPUv3", "PPUv4", "evolutionModels/PPU/");
		launchSingleExperiment("PPUv4", "PPUv5", "evolutionModels/PPU/");
		launchSingleExperiment("PPUv5", "PPUv6", "evolutionModels/PPU/");
		launchSingleExperiment("PPUv6", "PPUv7", "evolutionModels/PPU/");
		launchSingleExperiment("PPUv7", "PPUv8", "evolutionModels/PPU/");
		launchSingleExperiment("PPUv8", "PPUv9", "evolutionModels/PPU/");
		launchSingleExperiment("AmbientAssistedLivingv1", "AmbientAssistedLivingv2", "evolutionModels/AmbientAssistedLiving/");
		launchSingleExperiment("AutomotiveMultimediav1", "AutomotiveMultimediav2", "evolutionModels/AutomotiveMultimedia/");
		launchSingleExperiment("AutomotiveMultimediav2", "AutomotiveMultimediav3", "evolutionModels/AutomotiveMultimedia/");
		launchSingleExperiment("Boeingv1", "Boeingv2", "evolutionModels/Boeing/");
		launchSingleExperiment("Boeingv2", "Boeingv3", "evolutionModels/Boeing/");
		launchSingleExperiment("CarBodyv1", "CarBodyv2", "evolutionModels/CarBody/");
		launchSingleExperiment("CarBodyv2", "CarBodyv3", "evolutionModels/CarBody/");
		launchSingleExperiment("CarBodyv3", "CarBodyv4", "evolutionModels/CarBody/");
		launchSingleExperiment("LinuxKernelv1", "LinuxKernelv2", "evolutionModels/LinuxKernel/");
		launchSingleExperiment("LinuxKernelv2", "LinuxKernelv3", "evolutionModels/LinuxKernel/");
		launchSingleExperiment("ParkingAssistantv1", "ParkingAssistantv2", "evolutionModels/ParkingAssistant/");
		launchSingleExperiment("ParkingAssistantv2", "ParkingAssistantv3", "evolutionModels/ParkingAssistant/");
		launchSingleExperiment("ParkingAssistantv3", "ParkingAssistantv4", "evolutionModels/ParkingAssistant/");
		launchSingleExperiment("ParkingAssistantv4", "ParkingAssistantv5", "evolutionModels/ParkingAssistant/");
		launchSingleExperiment("SmartHotelv1", "SmartHotelv2", "evolutionModels/SmartHotel/");
		launchSingleExperiment("SmartWatchv1", "SmartWatchv2", "evolutionModels/SmartWatch/");
		launchSingleExperiment("WeatherStationv1", "WeatherStationv2", "evolutionModels/WeatherStation/");
		
	}

	/**
	 * Executes the single experiment
	 * 
	 * @param model1 : the first model
	 * @param model2 : the evolved model
	 * @param path   : the path in which models are stored
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws NoSuchExtensionException
	 * @throws UnsupportedModelException
	 */
	public void launchSingleExperiment(String model1, String model2, String path)
			throws IOException, InterruptedException, UnsupportedModelException, NoSuchExtensionException {
		TestContext.IN_TEST = true;

		String oldModel = convertModelFromFMToCTW(model1, path);
		String newModel = convertModelFromFMToCTW(model2, path);

		// Technique 1
		TestSuite oldTs = regenerationFromScratch(oldModel, newModel, 2, 1, "output.csv");

		// Technique 2
		generateWithPMediciPlus(oldModel, newModel, oldTs, 2, 1, "output.csv");
	}

	/**
	 * Converts a FM (in xml format) into a CTW model
	 * 
	 * @param modelName : the name of the feature model
	 * @param path      : the path of the feature model
	 * @return : the path of the CTW model corresponding to the feature model given
	 *         as input
	 * 
	 * @throws IOException
	 */
	public String convertModelFromFMToCTW(String modelName, String path) throws IOException {
		String fmName = path + modelName;
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
	 * @return : the test suite produced by pMEDICI with the original model
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws NoSuchExtensionException
	 * @throws UnsupportedModelException
	 */
	public TestSuite regenerationFromScratch(String oldFMname, String newFMname, int strength, int nThreads,
			String outputPath)
			throws IOException, InterruptedException, UnsupportedModelException, NoSuchExtensionException {
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

		return mediciTS1;
	}

	/**
	 * Generates the test suite with pMEDICI+
	 * 
	 * @param oldFMname  : the path of the old FM
	 * @param newFMname  : the path of the evolved FM
	 * @param originaTS	 : the original Test suite
	 * @param strength   : the strength
	 * @param nThreads   : the number of threads to be used by pMEDICI
	 * @param outputPath : the path of the output file where statistics are stored
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws NoSuchExtensionException
	 * @throws UnsupportedModelException
	 */
	public void generateWithPMediciPlus(String oldFMname, String newFMname, TestSuite originalTS, int strength,
			int nThreads, String outputPath)
			throws IOException, InterruptedException, UnsupportedModelException, NoSuchExtensionException {
		PMedici pMedici = new PMedici();
		// First model
		TestSuite mediciTS1 = originalTS;
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
	 * @param ts        : the new test suite
	 * @return : the fault detection capability of the evolved test suite
	 * @throws NoSuchExtensionException
	 * @throws UnsupportedModelException
	 * @throws FileNotFoundException
	 */
	private float computeFaultDetectionCapability(String newFMname, TestSuite ts)
			throws FileNotFoundException, UnsupportedModelException, NoSuchExtensionException {
		String fmPath = newFMname.split("_ctwedge_enum.ctw")[0] + ".xml";
		float totMut = 0;
		float killedMut = 0;

		// Read the feature model
		IFeatureModel fm = Utils.readModel(fmPath);

		// Define the mutators
		FMMutator[] mutatorList = FMMutationProcess.allMutationOperators();

		// Apply the mutations
		for (FMMutator mut : mutatorList) {
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
						Selection sel;
						if (value.equals("true") || value.equals("false"))
							// Boolean values
							sel = value.equals("true") ? Selection.SELECTED : Selection.UNSELECTED;
						else {
							// Enums
							if (value.equals("NONE"))
								sel = Selection.UNDEFINED;
							else
								sel = Selection.SELECTED;
						}
						if (Utils.getFeatureNames(fmM.getFirst()).contains(assignemnt.getKey()))
							conf.setManual(assignemnt.getKey(), sel);
					}

					ConfigurationPropagator cp = new ConfigurationPropagator(featureModelFormula, conf);
					Boolean result = LongRunningWrapper.runMethod(cp.isValid());
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
