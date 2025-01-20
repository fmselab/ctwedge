package ctwedge.fmtester.experiments;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import ctwedge.ctWedge.CitModel;
import ctwedge.generator.acts.ACTSTranslator;
import ctwedge.importer.featureide.FeatureIdeImporter;
import ctwedge.importer.featureide.FeatureIdeImporterBoolean;
import ctwedge.util.TestSuite;
import ctwedge.util.validator.ValidatorException;
import de.ovgu.featureide.fm.core.ExtensionManager.NoSuchExtensionException;
import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.init.FMCoreLibrary;
import de.ovgu.featureide.fm.core.init.LibraryManager;
import de.ovgu.featureide.fm.core.io.UnsupportedModelException;
import de.ovgu.featureide.fm.core.io.manager.FeatureModelManager;
import featuremodels.specificity.SpecificCITTestGenerator;
import fmautorepair.mutationoperators.FMMutation;
import fmautorepair.mutationoperators.FMMutator;
import fmautorepair.mutationprocess.FMMutationProcess;
import pMedici.main.PMedici;
import pMedici.util.TestContext;

public class Experiments_JSS_SI_SPLC {

	/**
	 * The available evolutions
	 */
	public static final String[] EV_MOBMEDIA = new String[] { "evolutionModels/MobileMedia/", "MobileMediaV3",
			"MobileMediaV4", "MobileMediaV5", "MobileMediaV6", "MobileMediaV7", "MobileMediaV8" };
	public static final String[] EV_SHOME = new String[] { "evolutionModels/SmartHome/", "SmartHomeV2",
			"SmartHomeV2.2" };
	public static final String[] EV_BCS = new String[] { "evolutionModels/BCS/", "BCS1", "BCS2", "BCS3" };
	public static final String[] EV_HSYS = new String[] { "evolutionModels/HelpSystem/", "HelpSystem1", "HelpSystem2" };
	public static final String[] EV_ERP = new String[] { "evolutionModels/ERP/", "ERP_SPL_s1", "ERP_SPL_s2" };
	public static final String[] EV_WSTAT = new String[] { "evolutionModels/WeatherStation/", "WeatherStationv1",
			"WeatherStationv2" };
	public static final String[] EV_SMARTW = new String[] { "evolutionModels/SmartWatch/", "SmartWatchv1",
			"SmartWatchv2" };
	public static final String[] EV_SMARTH = new String[] { "evolutionModels/SmartHotel/", "SmartHotelv1",
			"SmartHotelv2" };
	public static final String[] EV_PARKING = new String[] { "evolutionModels/ParkingAssistant/", "ParkingAssistantv1",
			"ParkingAssistantv2", "ParkingAssistantv3", "ParkingAssistantv4", "ParkingAssistantv5" };
	public static final String[] EV_LINUX = new String[] { "evolutionModels/LinuxKernel/", "LinuxKernelv1",
			"LinuxKernelv2", "LinuxKernelv3" };
	public static final String[] EV_CARBODY = new String[] { "evolutionModels/CarBody/", "CarBodyv1", "CarBodyv2",
			"CarBodyv3", "CarBodyv4" };
	public static final String[] EV_BOING = new String[] { "evolutionModels/Boeing/", "Boeingv1", "Boeingv2",
			"Boeingv3" };
	public static final String[] EV_AUTOM = new String[] { "evolutionModels/AutomotiveMultimedia/",
			"AutomotiveMultimediav1", "AutomotiveMultimediav2", "AutomotiveMultimediav3" };
	public static final String[] EV_ALIV = new String[] { "evolutionModels/AmbientAssistedLiving/",
			"AmbientAssistedLivingv1", "AmbientAssistedLivingv2" };
	public static final String[] EV_PPU = new String[] { "evolutionModels/PPU/", "PPUv1", "PPUv2", "PPUv3", "PPUv4",
			"PPUv5", "PPUv6", "PPUv7", "PPUv8", "PPUv9" };

	/**
	 * The path where the resulting test suites are stored
	 */
	public static final String PATH = "./JSSExperiments";
	public static final String FILE_NAME = "JSSExperiments.txt";

	/**
	 * Maximum number of FMMutation obtained from each mutator
	 */
	public static final int N_MAX_PER_MUTATION = 50;

	/**
	 * The number of mutations applied
	 */
	public static HashMap<Class<? extends FMMutator>, Integer> mutations;

	/**
	 * The number of repetitions
	 */
	public static final int N_REP = 10;

	/**
	 * Generators settings
	 */
	public static final Boolean USE_SPECGEN = false;
	public static final Boolean USE_GFE = false;
	public static final Boolean USE_GFS = false;
	public static final Boolean USE_Original = true;

	/**
	 * Tests the evolution with industrial models
	 * 
	 * @throws ValidatorException
	 * @throws InterruptedException
	 * @throws NoSuchExtensionException
	 * @throws UnsupportedModelException
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	@Test
	public void testEvolutionIndustrial() throws FileNotFoundException, IOException, UnsupportedModelException,
			NoSuchExtensionException, InterruptedException, ValidatorException {
		LibraryManager.registerLibrary(FMCoreLibrary.getInstance());
		for (int i = 0; i < N_REP; i++) {
			testEvo(EV_ALIV, i);
			testEvo(EV_PPU, i);
			testEvo(EV_AUTOM, i);
			testEvo(EV_BOING, i);
			testEvo(EV_CARBODY, i);
			testEvo(EV_LINUX, i);
			testEvo(EV_PARKING, i);
			testEvo(EV_BCS, i);
			testEvo(EV_ERP, i);
			testEvo(EV_HSYS, i);
			testEvo(EV_MOBMEDIA, i);
			testEvo(EV_SHOME, i);
			testEvo(EV_SMARTH, i);
			testEvo(EV_SMARTW, i);
			testEvo(EV_WSTAT, i);
		}
	}

	/**
	 * Tests the evolution with mutations
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws UnsupportedModelException
	 * @throws NoSuchExtensionException
	 * @throws InterruptedException
	 * @throws ValidatorException
	 */
	@Test
	public void testEvolutionMutations() throws FileNotFoundException, IOException, UnsupportedModelException,
			NoSuchExtensionException, InterruptedException, ValidatorException {
		LibraryManager.registerLibrary(FMCoreLibrary.getInstance());
		for (int i = 0; i < N_REP; i++) {
			testEvoMutation(EV_ALIV, i);
			testEvoMutation(EV_PPU, i);
			testEvoMutation(EV_AUTOM, i);
			testEvoMutation(EV_BOING, i);
			testEvoMutation(EV_CARBODY, i);
			testEvoMutation(EV_LINUX, i);
			testEvoMutation(EV_PARKING, i);
			testEvoMutation(EV_BCS, i);
			testEvoMutation(EV_ERP, i);
			testEvoMutation(EV_HSYS, i);
			testEvoMutation(EV_MOBMEDIA, i);
			testEvoMutation(EV_SHOME, i);
			testEvoMutation(EV_SMARTH, i);
			testEvoMutation(EV_SMARTW, i);
			testEvoMutation(EV_WSTAT, i);
		}
	}

	/**
	 * Starting from data produced with the two previous test cases, it produces
	 * summary data
	 * 
	 * @throws IOException
	 */
	@SuppressWarnings("deprecation")
	@Test
	public void getSummaryData() throws IOException {
		// Write the header in the file
		File f = new File(PATH + "/" + FILE_NAME);
		BufferedWriter fw = new BufferedWriter(new FileWriter(f));
		List<String> mutationsList = FMMutationProcess.allMutationOperatorsAsList().stream()
				.map(x -> x.getClass().getSimpleName()).toList();
		fw.write("FM1,FM2,Mutation,Generator,RepCount,Time,Size,Dissimilarity,Specificity,MutationScore\n");

		Files.walk(Paths.get(PATH)).forEach(x -> {
			// Filter only test suites
			if (x.toString().endsWith(".txt") && !x.toString().contains("JSSExperiments.txt")) {
				try {
					String fmName = null, fm2Name = null, mutationName = null, generatorName = null;
					long repCount = 0, size;
					float dissimilarity = 0, specificity = 0, mutationScore = 0, time;
					Stream<String> lines = null;

					lines = Files.lines(x);
					size = lines.count() - 1;
					lines.close();

					// Get the file name, excluding the path
					String fileName = x.toString().substring(x.toString().lastIndexOf("/") + 1);
					int countUnderscore = StringUtils.countMatches(fileName, "_");
					String[] parts = fileName.split("_");
					time = Float.parseFloat(
							Files.readAllLines(Paths.get(x.toString().replace(".txt", ".time"))).getFirst());

					if (fileName.contains("ORIGINAL")) {
						// TS generated only for the original model
						if (countUnderscore == 2) {
							// Any model
							fmName = parts[0];
							fm2Name = "";
							mutationName = "";
							generatorName = parts[1];
							repCount = Long.parseLong(parts[2].replace(".txt", ""));
						} else {
							// ERP SPL Model
							// Any model
							fmName = parts[0] + "_" + parts[1] + "_" + parts[2];
							fm2Name = "";
							mutationName = "";
							generatorName = parts[3];
							repCount = Long.parseLong(parts[4].replace(".txt", ""));
						}
						dissimilarity = 0;
						specificity = 0;
						mutationScore = 0;
					} else if (fileName.contains("ERP_SPL_")) {
						// TS generated for ERP SPL, which has a different number of underscores in name
						fmName = parts[0] + "_" + parts[1] + "_" + parts[2];
						fm2Name = parts[3] + "_" + parts[4] + "_" + parts[5];
						if (countUnderscore == 7) {
							// Basic industrial version
							mutationName = "";
							generatorName = parts[6];
							repCount = Long.parseLong(parts[7].replace(".txt", ""));
						} else {
							// Version obtained by mutation
							mutationName = "";
							for (String mut : mutationsList) {
								if (mut.equals(fm2Name.replace(fmName, "")))
									mutationName = mut;
							}
							generatorName = parts[7];
							repCount = Long.parseLong(parts[8].replace(".txt", ""));
						}
						dissimilarity = 0;
						specificity = 0;
						mutationScore = 0;
					} else {
						// TS generated for the other models
						fmName = parts[0];
						fm2Name = parts[1];
						if (countUnderscore == 3) {
							// Basic industrial version
							mutationName = "";
							generatorName = parts[2];
							repCount = Long.parseLong(parts[3].replace(".txt", ""));
						} else {
							// Version obtained by mutation
							mutationName = "";
							for (String mut : mutationsList) {
								if (mut.equals(fm2Name.replace(fmName, "")))
									mutationName = mut;
							}
							generatorName = parts[3];
							repCount = Long.parseLong(parts[4].replace(".txt", ""));
						}
						dissimilarity = 0;
						specificity = 0;
						mutationScore = 0;
					}
					fw.write(fmName + "," + fm2Name + "," + mutationName + "," + generatorName + "," + repCount + ","
							+ time + "," + size + "," + dissimilarity + "," + specificity + "," + mutationScore + "\n");
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
			}
		});

		fw.close();
	}

	/**
	 * Tests the evolution for the given models and the specific repetition count
	 * 
	 * @param modelsList the models list
	 * @param repCount   the current repetition
	 * @throws ValidatorException
	 * @throws InterruptedException
	 * @throws NoSuchExtensionException
	 * @throws UnsupportedModelException
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public void testEvo(String[] modelsList, int repCount) throws FileNotFoundException, IOException,
			UnsupportedModelException, NoSuchExtensionException, InterruptedException, ValidatorException {
		for (int i = 1; i < modelsList.length - 1; i++) {
			for (int j = 2; j < modelsList.length; j++)
				if (i != j) {
					// Execute the experiments on the original model
					executeTest("../../featuremodels.evotester/" + modelsList[0] + "/" + modelsList[i] + ".xml",
							"../../featuremodels.evotester/" + modelsList[0] + "/" + modelsList[j] + ".xml", repCount);
				}
		}
	}

	/**
	 * Tests the evolution for the given models and the specific repetition count
	 * 
	 * @param modelsList the models list
	 * @param repCount   the current repetition
	 * @throws ValidatorException
	 * @throws InterruptedException
	 * @throws NoSuchExtensionException
	 * @throws UnsupportedModelException
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public void testEvoMutation(String[] modelsList, int repCount) throws FileNotFoundException, IOException,
			UnsupportedModelException, NoSuchExtensionException, InterruptedException, ValidatorException {
		for (int i = 1; i < modelsList.length; i++) {
			// Execute the experiments on the original model
			mutations = new HashMap<>();
			executeTest("../../featuremodels.evotester/" + modelsList[0] + "/" + modelsList[i] + ".xml", repCount);
		}
	}

	/**
	 * Executes test
	 * 
	 * @param oldFm    old feature model
	 * @param newFm    new feature model
	 * @param repCount the repetition count
	 * @throws ValidatorException
	 * @throws InterruptedException
	 * @throws NoSuchExtensionException
	 * @throws UnsupportedModelException
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private void executeTest(String oldFm, int repCount) throws FileNotFoundException, IOException,
			UnsupportedModelException, NoSuchExtensionException, InterruptedException, ValidatorException {
		// Load the two feature models. One is the original one, the other is the
		// mutated one
		Path oldFMPath = Path.of(oldFm);
		IFeatureModel oldFM = FeatureModelManager.load(oldFMPath);

		// Define the mutators
		Iterator<FMMutation> mutants = FMMutationProcess.getAllMutantsRndOrderFOM(oldFM);

		// Apply the mutations
		while (mutants.hasNext()) {
			FMMutation next = mutants.next();
			if (next == null)
				continue;

			if (mutations.get(next.getMutationClass()) != null) {
				if (mutations.get(next.getMutationClass()) > N_MAX_PER_MUTATION)
					continue;

				mutations.put(next.getMutationClass(), mutations.get(next.getMutationClass()) + 1);
			} else {
				mutations.put(next.getMutationClass(), 1);
			}

			// Transform the test to a configuration
			IFeatureModel mutatedFeatureModel = next.getFirst();
			// Generate test suites
			generateMultipleTestSuites(oldFm, oldFMPath, oldFM, mutatedFeatureModel, repCount,
					next.getMutationClass().toString());
		}
	}

	/**
	 * Executes test
	 * 
	 * @param oldFm    old feature model
	 * @param newFm    new feature model
	 * @param repCount the repetition count
	 * @throws ValidatorException
	 * @throws InterruptedException
	 * @throws NoSuchExtensionException
	 * @throws UnsupportedModelException
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private void executeTest(String oldFm, String newFm, int repCount) throws FileNotFoundException, IOException,
			UnsupportedModelException, NoSuchExtensionException, InterruptedException, ValidatorException {
		// Load the two feature models
		Path oldFMPath = Path.of(oldFm);
		IFeatureModel oldFM = FeatureModelManager.load(oldFMPath);
		Path newFMPath = Path.of(newFm);
		IFeatureModel newFM = FeatureModelManager.load(newFMPath);

		// Generate test suites
		generateMultipleTestSuites(oldFm, newFm, oldFMPath, oldFM, newFMPath, newFM, repCount);
	}

	/**
	 * Generates a test suite with SPECGEN
	 * 
	 * @param oldFm     the old feature model name
	 * @param newFmName the new feature model name
	 * @param oldFM     the old feature model object
	 * @param newFM     the new feature model object
	 * @param repCount  the counter for the current repetition
	 * 
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws UnsupportedModelException
	 * @throws NoSuchExtensionException
	 * @throws InterruptedException
	 * @throws ValidatorException
	 */
	private void generateWithSpecgen(String oldFm, String newFmName, IFeatureModel oldFM, IFeatureModel newFM,
			int repCount) throws IOException, FileNotFoundException, UnsupportedModelException,
			NoSuchExtensionException, InterruptedException, ValidatorException {
		System.out.println("Generating with SPECGEN");
		SpecificCITTestGenerator gen = new SpecificCITTestGenerator(oldFM, newFM, 2);
		TestSuite ts = gen.generateTestSuite();
		if (ts == null)
			return;
		ts.setStrength(2);

		ts.getGeneratorTime();

		File f = new File(PATH + "/" + oldFm.substring(oldFm.lastIndexOf("/") + 1).replace(".xml", "") + "_"
				+ newFmName.substring(newFmName.lastIndexOf("/") + 1).replace(".xml", "") + "_SPECGEN_" + repCount
				+ ".txt");
		BufferedWriter fw = new BufferedWriter(new FileWriter(f));
		fw.write(ts.toString());
		fw.close();

		f = new File(PATH + "/" + oldFm.substring(oldFm.lastIndexOf("/") + 1).replace(".xml", "") + "_"
				+ newFmName.substring(newFmName.lastIndexOf("/") + 1).replace(".xml", "") + "_SPECGEN_" + repCount
				+ ".time");
		fw = new BufferedWriter(new FileWriter(f));
		fw.write(String.valueOf(ts.getGeneratorTime()));
		fw.close();
	}

	/**
	 * Generates tests with GFE
	 * 
	 * @param oldFm
	 * @param newFmName
	 * @param evolvedModel
	 * @param originalModel
	 * @param repCount
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws UnsupportedModelException
	 * @throws NoSuchExtensionException
	 */
	private void generateWithGFE(String oldFm, String newFmName, CitModel evolvedModel, CitModel originalModel,
			int repCount)
			throws IOException, InterruptedException, UnsupportedModelException, NoSuchExtensionException {
		TestContext.IN_TEST = true;

		// Get the first test suite with ACTS
		ACTSTranslator acts = new ACTSTranslator();
		TestSuite tsACTS = acts.getTestSuite(originalModel, 2, false);
		tsACTS.setGeneratorName("ACTS");

		// Second model
		PMedici pMedici = new PMedici();
		pMedici.setSeeds(tsACTS.getTests());
		TestSuite gfeTS = null;
		try {
			gfeTS = pMedici.generateTests(evolvedModel, 2, 1);
		} catch (NullPointerException ex) {
		}
		if (gfeTS == null)
			return;

		assert gfeTS.getGeneratorTime() >= 0;
		assert gfeTS.getStrength() >= 0;

		File f = new File(PATH + "/" + oldFm.substring(oldFm.lastIndexOf("/") + 1).replace(".xml", "") + "_"
				+ newFmName.substring(newFmName.lastIndexOf("/") + 1).replace(".xml", "") + "_GFE_" + repCount
				+ ".txt");
		BufferedWriter fw = new BufferedWriter(new FileWriter(f));
		fw.write(gfeTS.toString());
		fw.close();

		f = new File(PATH + "/" + oldFm.substring(oldFm.lastIndexOf("/") + 1).replace(".xml", "") + "_"
				+ newFmName.substring(newFmName.lastIndexOf("/") + 1).replace(".xml", "") + "_GFE_" + repCount
				+ ".time");
		fw = new BufferedWriter(new FileWriter(f));
		fw.write(String.valueOf(gfeTS.getGeneratorTime()));
		fw.close();
	}

	/**
	 * Generates tests with GFS
	 * 
	 * @param oldFm
	 * @param newFm
	 * @param evolvedModel
	 * @param originalModel
	 * @param repCount
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws UnsupportedModelException
	 * @throws NoSuchExtensionException
	 */
	private void generateWithGFS(String oldFm, String newFmName, CitModel evolvedModel, CitModel originalModel,
			int repCount)
			throws IOException, InterruptedException, UnsupportedModelException, NoSuchExtensionException {
		TestContext.IN_TEST = true;

		// Second model
		PMedici pMedici = new PMedici();
		TestSuite gfeTS = null;
		try {
			gfeTS = pMedici.generateTests(evolvedModel, 2, 1);
		} catch (NullPointerException ex) {
		}
		if (gfeTS == null)
			return;

		assert gfeTS.getGeneratorTime() >= 0;
		assert gfeTS.getStrength() >= 0;

		File f = new File(PATH + "/" + oldFm.substring(oldFm.lastIndexOf("/") + 1).replace(".xml", "") + "_"
				+ newFmName.substring(newFmName.lastIndexOf("/") + 1).replace(".xml", "") + "_GFS_" + repCount
				+ ".txt");
		BufferedWriter fw = new BufferedWriter(new FileWriter(f));
		fw.write(gfeTS.toString());
		fw.close();

		f = new File(PATH + "/" + oldFm.substring(oldFm.lastIndexOf("/") + 1).replace(".xml", "") + "_"
				+ newFmName.substring(newFmName.lastIndexOf("/") + 1).replace(".xml", "") + "_GFS_" + repCount
				+ ".time");
		fw = new BufferedWriter(new FileWriter(f));
		fw.write(String.valueOf(gfeTS.getGeneratorTime()));
		fw.close();
	}

	/**
	 * @param oldFm
	 * @param newFm
	 * @param oldFMPath * @param oldFM2
	 * @param newFMPath
	 * @param newFM2
	 * @param repCount
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws UnsupportedModelException
	 * @throws NoSuchExtensionException
	 * @throws InterruptedException
	 * @throws ValidatorException
	 */
	private void generateMultipleTestSuites(String oldFm, String newFm, Path oldFMPath, IFeatureModel oldFM2,
			Path newFMPath, IFeatureModel newFM2, int repCount) throws FileNotFoundException, IOException,
			UnsupportedModelException, NoSuchExtensionException, InterruptedException, ValidatorException {

		FeatureIdeImporter importer;
		CitModel resultNew, resultOld;

		// SPECGEN
		if (USE_SPECGEN)
			generateWithSpecgen(oldFm, newFm, oldFM2, newFM2, repCount);

		// GFE
		if (USE_GFE) {
			importer = new FeatureIdeImporterBoolean();
			resultNew = importer.importModel(newFMPath.toString());
			importer = new FeatureIdeImporterBoolean();
			resultOld = importer.importModel(oldFMPath.toString());
			generateWithGFE(oldFm, newFm, resultNew, resultOld, repCount);
		}

		// TS for FM
		if (USE_Original) {
			importer = new FeatureIdeImporterBoolean();
			resultOld = importer.importModel(oldFMPath.toString());
			generateTSFM(oldFm, resultOld, repCount);
		}

		// GFS
		if (USE_GFS) {
			importer = new FeatureIdeImporterBoolean();
			resultNew = importer.importModel(newFMPath.toString());
			importer = new FeatureIdeImporterBoolean();
			resultOld = importer.importModel(oldFMPath.toString());
			generateWithGFS(oldFm, newFm, resultNew, resultOld, repCount);
		}
	}

	/**
	 * @param oldFm
	 * @param newFm
	 * @param oldFMPat
	 * @param newFM2
	 * @param repCount
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws UnsupportedModelException
	 * @throws NoSuchExtensionException
	 * @throws InterruptedException
	 * @throws ValidatorException
	 */
	private void generateMultipleTestSuites(String oldFm, Path oldFMPath, IFeatureModel oldFM2, IFeatureModel newFM2,
			int repCount, String mutationName) throws FileNotFoundException, IOException, UnsupportedModelException,
			NoSuchExtensionException, InterruptedException, ValidatorException {

		FeatureIdeImporter importer;
		CitModel resultNew, resultOld;
		String newFmName = oldFm.substring(oldFm.lastIndexOf("/") + 1).replace(".xml", "")
				+ mutationName.substring(mutationName.lastIndexOf(".") + 1) + "_" + System.identityHashCode(newFM2);

		// SPECGEN
		if (USE_SPECGEN)
			generateWithSpecgen(oldFm, newFmName, oldFM2, newFM2, repCount);

		// GFE
		if (USE_GFE) {
			importer = new FeatureIdeImporterBoolean();
			resultNew = importer.importModel(newFM2);
			importer = new FeatureIdeImporterBoolean();
			resultOld = importer.importModel(oldFMPath.toString());
			generateWithGFE(oldFm, newFmName, resultNew, resultOld, repCount);
		}

		// TS for FM
		if (USE_Original) {
			importer = new FeatureIdeImporterBoolean();
			resultOld = importer.importModel(oldFMPath.toString());
			generateTSFM(oldFm, resultOld, repCount);
		}

		// GFS
		if (USE_GFS) {
			importer = new FeatureIdeImporterBoolean();
			resultNew = importer.importModel(newFM2);
			importer = new FeatureIdeImporterBoolean();
			resultOld = importer.importModel(oldFMPath.toString());
			generateWithGFS(oldFm, newFmName, resultNew, resultOld, repCount);
		}
	}

	/**
	 * Generates the TS for FM
	 * 
	 * @param oldFm
	 * @param resultOld
	 * @param repCount
	 * @throws IOException
	 */
	private void generateTSFM(String oldFm, CitModel resultOld, int repCount) throws IOException {
		// Check whether the file for the original test suite already exists
		String fileName = PATH + "/" + oldFm.substring(oldFm.lastIndexOf("/") + 1).replace(".xml", "") + "_ORIGINAL_"
				+ repCount + ".txt";
		File file = new File(fileName);
		if (file.exists())
			return;

		// Get the first test suite with ACTS
		ACTSTranslator acts = new ACTSTranslator();
		TestSuite tsACTS = acts.getTestSuite(resultOld, 2, false);
		tsACTS.setGeneratorName("ACTS");

		File f = new File(fileName);
		BufferedWriter fw = new BufferedWriter(new FileWriter(f));
		fw.write(tsACTS.toString());
		fw.close();

		f = new File(PATH + "/" + oldFm.substring(oldFm.lastIndexOf("/") + 1).replace(".xml", "") + "_ORIGINAL_"
				+ repCount + ".time");
		fw = new BufferedWriter(new FileWriter(f));
		fw.write(String.valueOf(tsACTS.getGeneratorTime()));
		fw.close();
	}

}
