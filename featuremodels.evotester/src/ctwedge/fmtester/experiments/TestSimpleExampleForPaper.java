package ctwedge.fmtester.experiments;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Test;

import ctwedge.fmtester.Converter;
import ctwedge.fmtester.DistancesCalculator;
import ctwedge.generator.exporter.ToCSV;
import ctwedge.util.TestSuite;
import ctwedge.util.validator.MinimalityTestSuiteValidator;
import de.ovgu.featureide.fm.core.ExtensionManager.NoSuchExtensionException;
import de.ovgu.featureide.fm.core.FeatureModelAnalyzer;
import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.io.UnsupportedModelException;
import de.ovgu.featureide.fm.core.io.manager.FeatureModelManager;
import de.ovgu.featureide.fm.core.io.manager.SimpleFileHandler;
import de.ovgu.featureide.fm.core.io.xml.XmlFeatureModelFormat;
import de.ovgu.featureide.fm.core.job.monitor.NullMonitor;
import fmautorepair.mutationoperators.FMMutation;
import fmautorepair.mutationoperators.FMMutator;
import fmautorepair.mutationprocess.FMMutationProcess;
import fmautorepair.utils.Utils;
import pMedici.main.PMedici;
import pMedici.threads.TestBuilder;
import pMedici.util.TestContext;

public class TestSimpleExampleForPaper {

	private static final int MAX_MUTATIONS = 10;
	static boolean REDUCE_TEST_SUITE = true;

	private static final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");

	Timestamp timestamp = new Timestamp(System.currentTimeMillis());

	String outputFile = "output" + sdf1.format(timestamp) + ".csv";

	@Test
	public void test1() throws IOException, InterruptedException {
		// distance 6 and 5
		extracted("ex_paper1_AG", "ex_paper2_AG");

	}

	// esempi fatti all'inizio
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
		ToCSV converter = new ToCSV();
		String oldTsStr = converter.toCSVcode(mediciTS1);
		Path tempFile = Files.createTempFile(null, null);
		Files.write(tempFile, oldTsStr.getBytes(StandardCharsets.UTF_8));
		pMedici = new PMedici();
		pMedici.setOldTs(tempFile.toString());
		TestSuite technique2TS = pMedici.generateTests(fmName2, 2, 0);
		float distance2 = DistancesCalculator.percTestSuitesDist(mediciTS1, technique2TS);
		System.out.println(distance2);
	}

	@Test
	public void experimentsForPaper()
			throws IOException, InterruptedException, UnsupportedModelException, NoSuchExtensionException {
		TestBuilder.KeepPartialOldTests = true;
		Logger.getLogger(MinimalityTestSuiteValidator.class).setLevel(Level.OFF);
		Logger.getLogger("fmautorepair.mutationoperators").setLevel(Level.OFF);
		int N_REPETITIONS = 10;
		// int[] nThreadsList = new int[] { 1, 2, 4, 6, 8 };
		int[] nThreadsList = new int[] { 1 };

		for (int i = 0; i < N_REPETITIONS; i++) {
			for (int nThreads : nThreadsList) {
				// Example in paper
				// launchSingleExperiment("ex_paper1_AG", "ex_paper2_AG", "fmexamples/");
				launchMultipleExperiment(new String[] { "PPUv1", "PPUv2", "PPUv3", "PPUv4", "PPUv5", "PPUv6", "PPUv7",
						"PPUv8", "PPUv9" }, "evolutionModels/PPU/", nThreads);
				launchMultipleExperiment(new String[] { "AmbientAssistedLivingv1", "AmbientAssistedLivingv2" },
						"evolutionModels/AmbientAssistedLiving/", nThreads);
				launchMultipleExperiment(
						new String[] { "AutomotiveMultimediav1", "AutomotiveMultimediav2", "AutomotiveMultimediav3" },
						"evolutionModels/AutomotiveMultimedia/", nThreads);
				launchMultipleExperiment(new String[] { "Boeingv1", "Boeingv2", "Boeingv3" }, "evolutionModels/Boeing/",
						nThreads);
				launchMultipleExperiment(new String[] { "CarBodyv1", "CarBodyv2", "CarBodyv3", "CarBodyv4" },
						"evolutionModels/CarBody/", nThreads);
				launchMultipleExperiment(new String[] { "LinuxKernelv1", "LinuxKernelv2", "LinuxKernelv3" },
						"evolutionModels/LinuxKernel/", nThreads);
				launchMultipleExperiment(
						new String[] { "ParkingAssistantv1", "ParkingAssistantv2", "ParkingAssistantv3",
								"ParkingAssistantv4", "ParkingAssistantv5" },
						"evolutionModels/ParkingAssistant/", nThreads);
				launchMultipleExperiment(new String[] { "SmartHotelv1", "SmartHotelv2" }, "evolutionModels/SmartHotel/",
						nThreads);
				launchMultipleExperiment(new String[] { "SmartWatchv1", "SmartWatchv2" }, "evolutionModels/SmartWatch/",
						nThreads);
				launchMultipleExperiment(new String[] { "WeatherStationv1", "WeatherStationv2" },
						"evolutionModels/WeatherStation/", nThreads);
				launchMultipleExperiment(new String[] { "ERP_SPL_s1", "ERP_SPL_s2" }, "evolutionModels/ERP/", nThreads);
				launchMultipleExperiment(new String[] { "HelpSystem1", "HelpSystem2" }, "evolutionModels/HelpSystem/",
						nThreads);
				launchMultipleExperiment(new String[] { "MobileMediaV3", "MobileMediaV4", "MobileMediaV5",
						"MobileMediaV6", "MobileMediaV7", "MobileMediaV8" }, "evolutionModels/MobileMedia/", nThreads);
				launchMultipleExperiment(new String[] { "SmartHomeV2", "SmartHomeV2.2" }, "evolutionModels/SmartHome/",
						nThreads);
				launchMultipleExperiment(new String[] { "BCS1", "BCS2", "BCS3" }, "evolutionModels/BCS/",
						nThreads);
			}
		}
	}

	@Test
	public void experimentsForPaperWithMutations()
			throws IOException, InterruptedException, UnsupportedModelException, NoSuchExtensionException {
		TestBuilder.KeepPartialOldTests = true;
		Logger.getLogger(MinimalityTestSuiteValidator.class).setLevel(Level.OFF);
		Logger.getLogger("fmautorepair.mutationoperators").setLevel(Level.OFF);
		int N_REPETITIONS = 20;
		int[] nThreadsList = new int[] { 1, 2, 4, 6, 8 };

		for (int i = 0; i < N_REPETITIONS; i++) {
			for (int nThreads : nThreadsList) {
				// Example in paper
				// launchSingleExperiment("ex_paper1_AG", "ex_paper2_AG", "fmexamples/");
				launchSingleExperimentMutation("PPUv1", "evolutionModels/PPU/", nThreads);
				launchSingleExperimentMutation("AmbientAssistedLivingv1", "evolutionModels/AmbientAssistedLiving/",
						nThreads);
				launchSingleExperimentMutation("AutomotiveMultimediav1", "evolutionModels/AutomotiveMultimedia/",
						nThreads);
				launchSingleExperimentMutation("Boeingv1", "evolutionModels/Boeing/", nThreads);
				launchSingleExperimentMutation("CarBodyv1", "evolutionModels/CarBody/", nThreads);
				launchSingleExperimentMutation("LinuxKernelv1", "evolutionModels/LinuxKernel/", nThreads);
				launchSingleExperimentMutation("ParkingAssistantv1", "evolutionModels/ParkingAssistant/", nThreads);
				launchSingleExperimentMutation("SmartHotelv1", "evolutionModels/SmartHotel/", nThreads);
				launchSingleExperimentMutation("SmartWatchv1", "evolutionModels/SmartWatch/", nThreads);
				launchSingleExperimentMutation("WeatherStationv1", "evolutionModels/WeatherStation/", nThreads);
				launchSingleExperimentMutation("ERP_SPL_s1", "evolutionModels/ERP/", nThreads);
				launchSingleExperimentMutation("HelpSystem1", "evolutionModels/HelpSystem/", nThreads);
				launchSingleExperimentMutation("MobileMediaV3", "evolutionModels/MobileMedia/", nThreads);
				launchSingleExperimentMutation("SmartHomeV2", "evolutionModels/SmartHome/", nThreads);
			}
		}
	}

	@Test
	public void experimentsForPaperWithHigherMutations()
			throws IOException, InterruptedException, UnsupportedModelException, NoSuchExtensionException {
		TestBuilder.KeepPartialOldTests = true;
		Logger.getLogger(MinimalityTestSuiteValidator.class).setLevel(Level.OFF);
		Logger.getLogger("fmautorepair.mutationoperators").setLevel(Level.OFF);
		int N_REPETITIONS = 20;
		int[] nThreadsList = new int[] { 1, 2, 4, 6, 8 };

		for (int i = 0; i < N_REPETITIONS; i++) {
			for (int nThreads : nThreadsList) {
				launchSingleExperimentHigherMutation("PPUv1", "evolutionModels/PPU/", nThreads);
				launchSingleExperimentHigherMutation("AmbientAssistedLivingv1",
						"evolutionModels/AmbientAssistedLiving/", nThreads);
				launchSingleExperimentHigherMutation("AutomotiveMultimediav1", "evolutionModels/AutomotiveMultimedia/",
						nThreads);
				launchSingleExperimentHigherMutation("Boeingv1", "evolutionModels/Boeing/", nThreads);
				launchSingleExperimentHigherMutation("CarBodyv1", "evolutionModels/CarBody/", nThreads);
				launchSingleExperimentHigherMutation("LinuxKernelv1", "evolutionModels/LinuxKernel/", nThreads);
				launchSingleExperimentHigherMutation("ParkingAssistantv1", "evolutionModels/ParkingAssistant/",
						nThreads);
				launchSingleExperimentHigherMutation("SmartHotelv1", "evolutionModels/SmartHotel/", nThreads);
				launchSingleExperimentHigherMutation("SmartWatchv1", "evolutionModels/SmartWatch/", nThreads);
				launchSingleExperimentHigherMutation("WeatherStationv1", "evolutionModels/WeatherStation/", nThreads);
				launchSingleExperimentHigherMutation("ERP_SPL_s1", "evolutionModels/ERP/", nThreads);
				launchSingleExperimentHigherMutation("HelpSystem1", "evolutionModels/HelpSystem/", nThreads);
				launchSingleExperimentHigherMutation("MobileMediaV3", "evolutionModels/MobileMedia/", nThreads);
				launchSingleExperimentHigherMutation("SmartHomeV2", "evolutionModels/SmartHome/", nThreads);
			}
		}
	}

	// all the files in directory
	@Test
	public void experimentsForPaperWithHigherMutationsDir()
			throws IOException, InterruptedException, UnsupportedModelException, NoSuchExtensionException {
		TestBuilder.KeepPartialOldTests = true;
		Logger.getLogger(MinimalityTestSuiteValidator.class).setLevel(Level.OFF);
		Logger.getLogger("fmautorepair.mutationoperators").setLevel(Level.OFF);
		int N_REPETITIONS = 20;
//		int[] nThreadsList = new int[] { 1, 2, 4, 6, 8 };
		int[] nThreadsList = new int[] { 1 };
		for (int i = 0; i < N_REPETITIONS; i++) {
			for (int nThreads : nThreadsList) {
				launchSingleExperimentHigherMutation("evolutionModels/PPU/", nThreads);
				launchSingleExperimentHigherMutation("evolutionModels/AmbientAssistedLiving/", nThreads);
				launchSingleExperimentHigherMutation("evolutionModels/AutomotiveMultimedia/", nThreads);
				launchSingleExperimentHigherMutation("evolutionModels/Boeing/", nThreads);
				launchSingleExperimentHigherMutation("evolutionModels/CarBody/", nThreads);
				launchSingleExperimentHigherMutation("evolutionModels/LinuxKernel/", nThreads);
				launchSingleExperimentHigherMutation("evolutionModels/ParkingAssistant/", nThreads);
				launchSingleExperimentHigherMutation("evolutionModels/SmartHotel/", nThreads);
				launchSingleExperimentHigherMutation("evolutionModels/SmartWatch/", nThreads);
				launchSingleExperimentHigherMutation("evolutionModels/WeatherStation/", nThreads);
				launchSingleExperimentHigherMutation("evolutionModels/ERP/", nThreads);
				launchSingleExperimentHigherMutation("evolutionModels/HelpSystem/", nThreads);
				launchSingleExperimentHigherMutation("evolutionModels/MobileMedia/", nThreads);
				launchSingleExperimentHigherMutation("evolutionModels/SmartHome/", nThreads);
			}
		}
	}

	// all models in path
	private void launchSingleExperimentHigherMutation(String path, int nThreads) throws IOException {
		Path Ppath = Path.of(path);
		Stream<Path> walk = Files.walk(Ppath).filter(Files::isRegularFile)
				.filter(x -> x.getFileName().toString().endsWith(".xml"));
		walk.forEach(x -> {
			String modelName = x.getFileName().toString().replace(".xml", "");
			try {
				launchSingleExperimentHigherMutation(modelName, path, nThreads);
			} catch (UnsupportedModelException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (NoSuchExtensionException e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * Executes the experiments by starting from the model given as parameter and
	 * compares the results with those obtained when mutations are applied to the
	 * model. In this case, more than a single mutation is applied
	 * 
	 * @param model    : the model
	 * @param path     : the path in which model is stored
	 * @param nThreads : the number of threads to be used
	 * @throws UnsupportedModelException
	 * @throws IOException
	 * @throws NoSuchExtensionException
	 * @throws InterruptedException
	 */
	private void launchSingleExperimentHigherMutation(String model, String path, int nThreads)
			throws UnsupportedModelException, IOException, InterruptedException, NoSuchExtensionException {
		TestContext.IN_TEST = true;

		String oldModel = convertModelFromFMToCTW(model, path);

		// Read the feature model to be mutated
		IFeatureModel fm = Utils.readModel(path + model + ".xml");

		// Define the mutators
		FMMutator[] mutatorList = FMMutationProcess.allMutationOperators();

		// Repeat the experiments for higher number of mutations
		int nModel = 0;
		Random generator = new Random();
		for (int j = 1; j <= MAX_MUTATIONS; j++) {
			// Extract the index of mutations
			ArrayList<Integer> mutationIndex = new ArrayList<>();
			for (int i = 0; i < j; i++) {
				mutationIndex.add(generator.nextInt(mutatorList.length));
			}
			// Now, apply the mutations to the model
			for (int index : mutationIndex) {
				ArrayList<FMMutation> mutationsList = new ArrayList<>();
				Iterator<FMMutation> mutations = mutatorList[index].mutate(fm);
				while (!mutations.hasNext()) {
					index = generator.nextInt(mutatorList.length);
					mutations = mutatorList[index].mutate(fm);
					System.out.println("New index");
				}
				mutations.forEachRemaining(mutationsList::add);
				int mutationListIndex = generator.nextInt(mutationsList.size());
				fm = mutationsList.get(mutationListIndex).getFirst();
			}

			// Then, execute the test using the two different techniques
			String newModel = "";
			try {
				System.out.println("Starting test generation");
				newModel = convertModelFromFMToCTW(fm, path, model + "_" + nModel);
				SimpleFileHandler.save(new File(newModel + ".xml").toPath(), fm, new XmlFeatureModelFormat());
				// Technique 1
				TestSuite oldTs = regenerationFromScratch(oldModel, newModel, 2, nThreads, outputFile, j);
				assert oldTs.getStrength() == 2;
				// Technique 2
				generateWithPMediciPlus(oldModel, newModel, oldTs, 2, nThreads, outputFile, j);
			} catch (Exception e) {
				continue;
			}
			nModel++;
		}
	}

	/**
	 * Executes the experiments by starting from the model given as parameter and
	 * compares the results with those obtained when mutations are applied to the
	 * model
	 * 
	 * @param model    : the model
	 * @param path     : the path in which model is stored
	 * @param nThreads : the number of threads to be used
	 * @throws UnsupportedModelException
	 * @throws IOException
	 * @throws NoSuchExtensionException
	 * @throws InterruptedException
	 */
	private void launchSingleExperimentMutation(String model, String path, int nThreads)
			throws UnsupportedModelException, IOException, InterruptedException, NoSuchExtensionException {
		TestContext.IN_TEST = true;

		String oldModel = convertModelFromFMToCTW(model, path);

		// Read the feature model to be mutated
		IFeatureModel fm = Utils.readModel(path + model + ".xml");

		// Define the mutators
		FMMutator[] mutatorList = FMMutationProcess.allMutationOperators();
		int i = 0;

		// Apply the mutations
		for (FMMutator mut : mutatorList) {
			// Fetch all the obtained mutants
			Iterator<FMMutation> mutations = mut.mutate(fm);
			while (mutations.hasNext()) {
				FMMutation fmM = mutations.next();
				IFeatureModel muted = fmM.getFirst();
				String newModel = "";
				try {
					newModel = convertModelFromFMToCTW(muted, path, model + "_" + i);
					SimpleFileHandler.save(new File(newModel + ".xml").toPath(), muted, new XmlFeatureModelFormat());
					// Technique 1
					TestSuite oldTs = regenerationFromScratch(oldModel, newModel, 2, nThreads, "outputSynthetic.csv");
					assert oldTs.getStrength() == 2;
					// Technique 2
					generateWithPMediciPlus(oldModel, newModel, oldTs, 2, nThreads, "outputSynthetic.csv");
				} catch (Exception e) {
					continue;
				}
				i++;
			}
		}
	}

	/**
	 * Executes the experiments on a set of models
	 * 
	 * @param models   : the list of models
	 * @param path     : the path in which models are stored
	 * @param nThreads : the number of threads to be used
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws NoSuchExtensionException
	 * @throws UnsupportedModelException
	 */
	public void launchMultipleExperiment(String[] models, String path, int nThreads)
			throws IOException, InterruptedException, UnsupportedModelException, NoSuchExtensionException {
		for (int i = 0; i < models.length - 1; i++) {
			int j = i + 1;
			launchSingleExperiment(models[i], models[j], path, nThreads);
		}
	}

	/**
	 * Executes the single experiment
	 * 
	 * @param model1   : the first model
	 * @param model2   : the evolved model
	 * @param path     : the path in which models are stored
	 * @param nThreads : the number of threads to be used
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws NoSuchExtensionException
	 * @throws UnsupportedModelException
	 */
	public void launchSingleExperiment(String model1, String model2, String path, int nThreads)
			throws IOException, InterruptedException, UnsupportedModelException, NoSuchExtensionException {
		TestContext.IN_TEST = true;

		String oldModel = convertModelFromFMToCTW(model1, path);
		String newModel = convertModelFromFMToCTW(model2, path);

		DistancesCalculator.changeableCardinality = getChangeableFeaturesCardinality(model1, model2, path);

		// Technique 1
		TestSuite oldTs = regenerationFromScratch(oldModel, newModel, 2, nThreads, outputFile);
		assert oldTs.getStrength() == 2;
		// Technique 2
		generateWithPMediciPlus(oldModel, newModel, oldTs, 2, nThreads, outputFile);
	}

	/**
	 * @param model1
	 * @param model2
	 * @param path
	 */
	private int getChangeableFeaturesCardinality(String model1, String model2, String path) {
		// Extract info from the first feature model
		IFeatureModel rootModel1 = FeatureModelManager.load(Path.of(path + model1 + ".xml"));
		FeatureModelAnalyzer analyzer = new FeatureModelAnalyzer(rootModel1);
		Set<String> dead1 = analyzer.getDeadFeatures(new NullMonitor<>()).stream().map(x -> x.getName())
				.collect(Collectors.toSet());
		Set<String> core1 = analyzer.getCoreFeatures(new NullMonitor<>()).stream().map(x -> x.getName())
				.collect(Collectors.toSet());
		Set<String> feature1 = rootModel1.getFeatures().stream().map(x -> x.getName()).collect(Collectors.toSet());

		// Extract info from the second feature model
		IFeatureModel rootModel2 = FeatureModelManager.load(Path.of(path + model2 + ".xml"));
		analyzer = new FeatureModelAnalyzer(rootModel2);
		Set<String> dead2 = analyzer.getDeadFeatures(new NullMonitor<>()).stream().map(x -> x.getName())
				.collect(Collectors.toSet());
		Set<String> core2 = analyzer.getCoreFeatures(new NullMonitor<>()).stream().map(x -> x.getName())
				.collect(Collectors.toSet());
		Set<String> feature2 = rootModel2.getFeatures().stream().map(x -> x.getName()).collect(Collectors.toSet());

		// Compute the cardinality of the changeable features
		feature1.addAll(feature2);

		// Compute the features dead in both models
		List<String> deadBoth = dead1.stream().filter(x -> dead2.contains(x)).collect(Collectors.toList());
		// Remove them from the list of features
		feature1.removeAll(deadBoth);

		// Compute the features core in both models
		List<String> coreBoth = core1.stream().filter(x -> core2.contains(x)).collect(Collectors.toList());
		// Remove them from the list of features
		feature1.removeAll(coreBoth);

		// Now, the number of features to be considered for the distance is the size of
		// the list feature1
		return feature1.size();
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
		return convertModelFromFMToCTW(fmName);
	}

	/**
	 * Converts a FM (in xml format) into a CTW model
	 * 
	 * @param modelName : the path of the feature model
	 * @return : the path of the CTW model corresponding to the feature model given
	 *         as input
	 * 
	 * @throws IOException
	 */
	public String convertModelFromFMToCTW(String modelNameAndPath) throws IOException {
		String fmName = modelNameAndPath;
		// convert to CTWedge (could be done only once)
		Converter.fromFMtoCTWedge_ENUM(fmName + ".xml", fmName + "_ctwedge_enum.ctw");
		return fmName + "_ctwedge_enum.ctw";
	}

	/**
	 * Converts a FM into a CTW model
	 * 
	 * @param model  : the feature model
	 * @param path   : the path of the feature model
	 * @param fmName : the name of the feature model
	 * @return : the path of the CTW model corresponding to the feature model given
	 *         as input
	 * 
	 * @throws IOException
	 */
	public String convertModelFromFMToCTW(IFeatureModel model, String path, String fmName) throws IOException {
		Converter.fromIFeatureModeltoCTWedge_ENUM(model, path + fmName + "_ctwedge_enum_mutated.ctw");
		return path + fmName + "_ctwedge_enum_mutated.ctw";
	}

	/**
	 * Converts a FM into a CTW model
	 * 
	 * @param model  : the feature model
	 * @param path   : the path of the feature model
	 * @param fmName : the name of the feature model
	 * @return : the path of the CTW model corresponding to the feature model given
	 *         as input
	 * 
	 * @throws IOException
	 */
	public String convertModelFromFMToCTW(IFeatureModel model, String path) throws IOException {
		Converter.fromIFeatureModeltoCTWedge_ENUM(model, path + "_ctwedge_enum_mutated.ctw");
		return path + "_ctwedge_enum_mutated.ctw";
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
		return regenerationFromScratch(oldFMname, newFMname, strength, nThreads, outputPath, 0);
	}

	/**
	 * Generates the test suite from scratch
	 * 
	 * @param oldFMname  : the path of the old FM
	 * @param newFMname  : the path of the evolved FM
	 * @param strength   : the strength
	 * @param nThreads   : the number of threads to be used by pMEDICI
	 * @param outputPath : the path of the output file where statistics are stored
	 * @param nMutations : the number of mutations
	 * 
	 * @return : the test suite produced by pMEDICI with the original model
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws NoSuchExtensionException
	 * @throws UnsupportedModelException
	 */
	public TestSuite regenerationFromScratch(String oldFMname, String newFMname, int strength, int nThreads,
			String outputPath, int nMutations)
			throws IOException, InterruptedException, UnsupportedModelException, NoSuchExtensionException {
		PMedici pMedici = new PMedici();
		FileWriter fw = new FileWriter(outputPath, true);
		BufferedWriter bw = new BufferedWriter(fw);

		// First model
		TestSuite mediciTS1 = pMedici.generateTests(oldFMname, strength, nThreads);
		assert mediciTS1.getStrength() == strength;
		// Second model
		pMedici = new PMedici();
		TestSuite mediciTS2 = pMedici.generateTests(newFMname, strength, nThreads);
		assert mediciTS2.getStrength() == strength;

		// Distance
		float distance = DistancesCalculator.testSuitesDist(mediciTS1, mediciTS2);
		float distancePerc = DistancesCalculator.percTestSuitesDist(mediciTS1, mediciTS2);
		// Mutation score
		float faultDetectionCapability = computeFaultDetectionCapability(newFMname, mediciTS2);

		// Write statistics to file
		bw.write("T1;" + oldFMname + ";" + mediciTS1.getTests().size() + ";" + mediciTS1.getGeneratorTime() + ";"
				+ newFMname + ";" + mediciTS2.getTests().size() + ";" + mediciTS2.getGeneratorTime() + ";" + distance
				+ ";" + faultDetectionCapability + ";" + nThreads + ";" + distancePerc + ";" + nMutations + ";");
		bw.newLine();

		// Minimize test suites
		if (REDUCE_TEST_SUITE) {
			mediciTS1 = reduceTestSuite(mediciTS1);
			assert mediciTS1.getStrength() == strength;
			mediciTS2 = reduceTestSuite(mediciTS2);
			assert mediciTS2.getStrength() == strength;
			System.gc();

			// Distance
			distance = DistancesCalculator.testSuitesDist(mediciTS1, mediciTS2);
			distancePerc = DistancesCalculator.percTestSuitesDist(mediciTS1, mediciTS2);
			// Mutation score
			faultDetectionCapability = computeFaultDetectionCapability(newFMname, mediciTS2);

			// Write statistics to file
			bw.write("T1Reduced;" + oldFMname + ";" + mediciTS1.getTests().size() + ";" + mediciTS1.getGeneratorTime()
					+ ";" + newFMname + ";" + mediciTS2.getTests().size() + ";" + mediciTS2.getGeneratorTime() + ";"
					+ distance + ";" + faultDetectionCapability + ";" + nThreads + ";" + distancePerc + ";" + nMutations
					+ ";");
			bw.newLine();
		}
		bw.flush();
		bw.close();
		fw.close();
		return mediciTS1;
	}

	/**
	 * Reduces the test suite given as parameter
	 * 
	 * @param ts : the test suite to be reduced
	 * @return the reduced test suite
	 */
	private TestSuite reduceTestSuite(TestSuite ts) {
		MinimalityTestSuiteValidator minimality = new MinimalityTestSuiteValidator(ts);
		TestSuite tsReduced = minimality.reduceSize();
		assert tsReduced.getStrength() == ts.getStrength();
		return tsReduced;
	}

	/**
	 * Generates the test suite with pMEDICI+
	 * 
	 * @param oldFMname  : the path of the old FM
	 * @param newFMname  : the path of the evolved FM
	 * @param originaTS  : the original Test suite
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
		generateWithPMediciPlus(oldFMname, newFMname, originalTS, strength, nThreads, outputPath, 0);
	}

	/**
	 * Generates the test suite with pMEDICI+
	 * 
	 * @param oldFMname  : the path of the old FM
	 * @param newFMname  : the path of the evolved FM
	 * @param originaTS  : the original Test suite
	 * @param strength   : the strength
	 * @param nThreads   : the number of threads to be used by pMEDICI
	 * @param outputPath : the path of the output file where statistics are stored
	 * @param nMutations : the number of mutations
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws NoSuchExtensionException
	 * @throws UnsupportedModelException
	 */
	public void generateWithPMediciPlus(String oldFMname, String newFMname, TestSuite originalTS, int strength,
			int nThreads, String outputPath, int nMutations)
			throws IOException, InterruptedException, UnsupportedModelException, NoSuchExtensionException {
		// First model
		TestSuite mediciTS1 = originalTS;
		Collections.shuffle(mediciTS1.getTests());

		// Second model
		PMedici pMedici = new PMedici();
		pMedici.setSeeds(mediciTS1.getTests());
		TestSuite mediciTS2 = pMedici.generateTests(newFMname, 2, nThreads);
		assert mediciTS2.getGeneratorTime()  >= 0;
		assert mediciTS2.getStrength()  >= 0;

		// Distance
		float distance = DistancesCalculator.testSuitesDist(mediciTS1, mediciTS2);
		float distancePerc = DistancesCalculator.percTestSuitesDist(mediciTS1, mediciTS2);
		// Mutation score
		float faultDetectionCapability = computeFaultDetectionCapability(newFMname, mediciTS2);

		// Write statistics to file before minimization
		FileWriter fw = new FileWriter(outputPath, true);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write("T2;" + oldFMname + ";" + mediciTS1.getTests().size() + ";" + mediciTS1.getGeneratorTime() + ";"
				+ newFMname + ";" + mediciTS2.getTests().size() + ";" + mediciTS2.getGeneratorTime() + ";" + distance
				+ ";" + faultDetectionCapability + ";" + nThreads + ";" + distancePerc + ";" + nMutations + ";");
		bw.newLine();
		bw.flush();

		// Minimize test suite
		if (REDUCE_TEST_SUITE) {
			mediciTS2 = reduceTestSuite(mediciTS2);
			System.gc();
			// Distance
			distance = DistancesCalculator.testSuitesDist(mediciTS1, mediciTS2);
			distancePerc = DistancesCalculator.percTestSuitesDist(mediciTS1, mediciTS2);
			// Mutation score
			faultDetectionCapability = computeFaultDetectionCapability(newFMname, mediciTS2);
			// Write statistics to file
			bw.write("T2Reduced;" + oldFMname + ";" + mediciTS1.getTests().size() + ";" + mediciTS1.getGeneratorTime() + ";"
				+ newFMname + ";" + mediciTS2.getTests().size() + ";" + mediciTS2.getGeneratorTime() + ";" + distance
				+ ";" + faultDetectionCapability + ";" + nThreads + ";" + distancePerc + ";" + nMutations + ";");
			bw.newLine();
		}
		bw.flush();
		bw.close();
		fw.close();
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
				IFeatureModel featureModel = fmM.getFirst();
				for (ctwedge.util.Test test : ts.getTests()) {
					Boolean result = MutationScore.isValid2(featureModel, test);
					if (!result) {
						killedMut++;
						break;
					}
				}
			}
		}

		return totMut != 0 ? killedMut / totMut : 0;
	}

	// caso di test per cercare di migliroare i tempi
	@Test
	public void testName() throws Exception {
		PMedici.verb = false;
		// for(int i = 0; i < 10; i++) {
		launchSingleExperiment("AutomotiveMultimediav1", "AutomotiveMultimediav2",
				"evolutionModels/AutomotiveMultimedia/", 1);
		// }
	}

}
