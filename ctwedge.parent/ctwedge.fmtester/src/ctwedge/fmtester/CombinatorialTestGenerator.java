package ctwedge.fmtester;

import java.io.OutputStream;
import java.io.PrintStream;

import ctwedge.ctWedge.CitModel;
import ctwedge.generator.acts.ACTSTranslator;
import ctwedge.generator.exporter.CSVExporter;
import ctwedge.importer.featureide.FeatureIdeImporter;
import ctwedge.importer.featureide.FeatureIdeImporterBoolean;
import ctwedge.importer.featureide.XmlFeatureModelImporter;
import ctwedge.util.TestSuite;

/**
 * {@link CombinatorialTestGenerator} offers a static method to generate combinatorial tests in .csv format for the specified input Feature Model.
 * 
 * </br>
 * The main() method generates combinatorial tests in .csv files for all models in the <i>evolutionModels</i> folder. 
 * 
 */
public class CombinatorialTestGenerator {

	private static final String EVOLUTION_MODELS_INPUT_FOLDER = "evolutionModels/";

	public static void main(String[] args) {

		// * Nota *
		// Se genero le test suites tutte insieme, la prima richiede sempre
		// un tempo maggiore rispetto a tutte le altre (a causa probabilmente
		// di inizializzazioni varie).
		// Per calcolare il tempo di generazione in modo "equo" conviene quindi
		// creare sempre una sola test suite per 1 solo modello alla volta
		// commentando il codice degli altri FM.

		System.out.println("-- TEST GENERATION --");

		// input folder
		String FMName;
		String FMSubFolder;
		String FMInputPath;
		long FMtime;

		// ------------------------------------------------------------------
		/* EVOLUTION MODEL: AmbientAssistedLiving */
		FMSubFolder = "AmbientAssistedLiving";
		FMInputPath = EVOLUTION_MODELS_INPUT_FOLDER + FMSubFolder + "/";
		System.out.println("----------------------------------------------");
		/* v1 */
		FMName = "AmbientAssistedLivingv1.xml";
		FMtime = generateBooleanTestAndExportCSV(FMName, FMInputPath);
		System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");
		
		/* v1 */
		FMName = "AmbientAssistedLivingv1.xml";
		FMtime = generateBooleanTestAndExportCSV(FMName, FMInputPath);
		System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");
		
		/* v1 */
		FMName = "AmbientAssistedLivingv1.xml";
		FMtime = generateBooleanTestAndExportCSV(FMName, FMInputPath);
		System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");
		
		/* v2 */
		FMName = "AmbientAssistedLivingv2.xml";
		FMtime = generateBooleanTestAndExportCSV(FMName, FMInputPath);
		System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");
		// ------------------------------------------------------------------

		// ------------------------------------------------------------------
		/* EVOLUTION MODEL: AutomotiveMultimedia */
		FMSubFolder = "AutomotiveMultimedia";
		FMInputPath = EVOLUTION_MODELS_INPUT_FOLDER + FMSubFolder + "/";
		System.out.println("----------------------------------------------");
		/* v1 */
		FMName = "AutomotiveMultimediav1.xml";
		FMtime = generateBooleanTestAndExportCSV(FMName, FMInputPath);
		System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");
		/* v2 */
		FMName = "AutomotiveMultimediav2.xml";
		FMtime = generateBooleanTestAndExportCSV(FMName, FMInputPath);
		System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");
		/* v3 */
		FMName = "AutomotiveMultimediav3.xml";
		FMtime = generateBooleanTestAndExportCSV(FMName, FMInputPath);
		System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");
		// ------------------------------------------------------------------

		// ------------------------------------------------------------------
		/* EVOLUTION MODEL: Boeing */
		FMSubFolder = "Boeing";
		FMInputPath = EVOLUTION_MODELS_INPUT_FOLDER + FMSubFolder + "/";
		System.out.println("----------------------------------------------");
		/* v1 */
		FMName = "Boeingv1.xml";
		FMtime = generateBooleanTestAndExportCSV(FMName, FMInputPath);
		System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");
		/* v2 */
		FMName = "Boeingv2.xml";
		FMtime = generateBooleanTestAndExportCSV(FMName, FMInputPath);
		System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");
		/* v3 */
		FMName = "Boeingv3.xml";
		FMtime = generateBooleanTestAndExportCSV(FMName, FMInputPath);
		System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");
		// ------------------------------------------------------------------

		// ------------------------------------------------------------------
		/* EVOLUTION MODEL: CarBody */
		FMSubFolder = "CarBody";
		FMInputPath = EVOLUTION_MODELS_INPUT_FOLDER + FMSubFolder + "/";
		System.out.println("----------------------------------------------");
		/* v1 */
		FMName = "CarBodyv1.xml";
		FMtime = generateBooleanTestAndExportCSV(FMName, FMInputPath);
		System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");
		/* v2 */
		FMName = "CarBodyv2.xml";
		FMtime = generateBooleanTestAndExportCSV(FMName, FMInputPath);
		System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");
		/* v3 */
		FMName = "CarBodyv3.xml";
		FMtime = generateBooleanTestAndExportCSV(FMName, FMInputPath);
		System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");
		/* v4 */
		FMName = "CarBodyv4.xml";
		FMtime = generateBooleanTestAndExportCSV(FMName, FMInputPath);
		System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");
		// ------------------------------------------------------------------

		// ------------------------------------------------------------------
		/* EVOLUTION MODEL: LinuxKernel */
		FMSubFolder = "LinuxKernel";
		FMInputPath = EVOLUTION_MODELS_INPUT_FOLDER + FMSubFolder + "/";
		System.out.println("----------------------------------------------");
		/* v1 */
		FMName = "LinuxKernelv1.xml";
		FMtime = generateBooleanTestAndExportCSV(FMName, FMInputPath);
		System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");
		/* v2 */
		FMName = "LinuxKernelv2.xml";
		FMtime = generateBooleanTestAndExportCSV(FMName, FMInputPath);
		System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");
		/* v3 */
		FMName = "LinuxKernelv3.xml";
		FMtime = generateBooleanTestAndExportCSV(FMName, FMInputPath);
		System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");
		// ------------------------------------------------------------------

		// ------------------------------------------------------------------
		/* EVOLUTION MODEL: ParkingAssistant */
		FMSubFolder = "ParkingAssistant";
		FMInputPath = EVOLUTION_MODELS_INPUT_FOLDER + FMSubFolder + "/";
		System.out.println("----------------------------------------------");
		/* v1 */
		FMName = "ParkingAssistantv1.xml";
		FMtime = generateBooleanTestAndExportCSV(FMName, FMInputPath);
		System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");
		/* v2 */
		FMName = "ParkingAssistantv2.xml";
		FMtime = generateBooleanTestAndExportCSV(FMName, FMInputPath);
		System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");
		/* v3 */
		FMName = "ParkingAssistantv3.xml";
		FMtime = generateBooleanTestAndExportCSV(FMName, FMInputPath);
		System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");
		/* v4 */
		FMName = "ParkingAssistantv4.xml";
		FMtime = generateBooleanTestAndExportCSV(FMName, FMInputPath);
		System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");
		/* v5 */
		FMName = "ParkingAssistantv5.xml";
		FMtime = generateBooleanTestAndExportCSV(FMName, FMInputPath);
		System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");
		// ------------------------------------------------------------------

		// ------------------------------------------------------------------
		/* EVOLUTION MODEL: PPU */
		FMSubFolder = "PPU";
		FMInputPath = EVOLUTION_MODELS_INPUT_FOLDER + FMSubFolder + "/";
		System.out.println("----------------------------------------------");
		/* v1 */
		FMName = "PPUv1.xml";
		FMtime = generateBooleanTestAndExportCSV(FMName, FMInputPath);
		System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");
		/* v2 */
		FMName = "PPUv2.xml";
		FMtime = generateBooleanTestAndExportCSV(FMName, FMInputPath);
		System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");
		/* v3 */
		FMName = "PPUv3.xml";
		FMtime = generateBooleanTestAndExportCSV(FMName, FMInputPath);
		System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");
		/* v4 */
		FMName = "PPUv4.xml";
		FMtime = generateBooleanTestAndExportCSV(FMName, FMInputPath);
		System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");
		/* v5 */
		FMName = "PPUv5.xml";
		FMtime = generateBooleanTestAndExportCSV(FMName, FMInputPath);
		System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");
		/* v6 */
		FMName = "PPUv6.xml";
		FMtime = generateBooleanTestAndExportCSV(FMName, FMInputPath);
		System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");
		/* v7 */
		FMName = "PPUv7.xml";
		FMtime = generateBooleanTestAndExportCSV(FMName, FMInputPath);
		System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");
		/* v8 */
		FMName = "PPUv8.xml";
		FMtime = generateBooleanTestAndExportCSV(FMName, FMInputPath);
		System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");
		/* v9 */
		FMName = "PPUv9.xml";
		FMtime = generateBooleanTestAndExportCSV(FMName, FMInputPath);
		System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");
		// ------------------------------------------------------------------
		
		// ------------------------------------------------------------------
		/* EVOLUTION MODEL: SmartHotel */
		FMSubFolder = "SmartHotel";
		FMInputPath = EVOLUTION_MODELS_INPUT_FOLDER + FMSubFolder + "/";
		System.out.println("----------------------------------------------");
		/* v1 */
		FMName = "SmartHotelv1.xml";
		FMtime = generateBooleanTestAndExportCSV(FMName, FMInputPath);
		System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");
		/* v2 */
		FMName = "SmartHotelv2.xml";
		FMtime = generateBooleanTestAndExportCSV(FMName, FMInputPath);
		System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");
		// ------------------------------------------------------------------

		// ------------------------------------------------------------------
		/* EVOLUTION MODEL: SmartWatch */
		FMSubFolder = "SmartWatch";
		FMInputPath = EVOLUTION_MODELS_INPUT_FOLDER + FMSubFolder + "/";
		System.out.println("----------------------------------------------");
		/* v1 */
		FMName = "SmartWatchv1.xml";
		FMtime = generateBooleanTestAndExportCSV(FMName, FMInputPath);
		System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");
		/* v2 */
		FMName = "SmartWatchv2.xml";
		FMtime = generateBooleanTestAndExportCSV(FMName, FMInputPath);
		System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");
		// ------------------------------------------------------------------

		// ------------------------------------------------------------------
		/* EVOLUTION MODEL: WeatherStation */
		FMSubFolder = "WeatherStation";
		FMInputPath = EVOLUTION_MODELS_INPUT_FOLDER + FMSubFolder + "/";
		System.out.println("----------------------------------------------");
		/* v1 */
		FMName = "WeatherStationv1.xml";
		FMtime = generateBooleanTestAndExportCSV(FMName, FMInputPath);
		System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");
		/* v2 */
		FMName = "WeatherStationv2.xml";
		FMtime = generateBooleanTestAndExportCSV(FMName, FMInputPath);
		System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");
		// ------------------------------------------------------------------

	}

	/**
	 * Generate the test suite (not boolean) for the specified input Feature Model.
	 * 
	 * @param FMName the name of the Feature Model xml file
	 * @param FMinputPath the path to the input folder where the xml file is located
	 * @return the time taken by the generator (in milliseconds) for generating the
	 *         test suite
	 */
	private static long generateEnumTestAndExportCSV(final String FMName, final String FMinputPath) {

		// disabilito temporaneamente system.out a console per evitare di stampare
		// informazioni aggiuntive sulla test suite durante la sua generazione
		PrintStream originalStream = System.out;
		consolePrintingOff();
		
		/* inizio generazione caso di test */
		
		// cattura il tempo impiegato
		long start1 = System.currentTimeMillis();
		
		// importa il modello
		FeatureIdeImporter importer = new XmlFeatureModelImporter();
		CitModel result = importer.importModel(FMinputPath + FMName);

		ACTSTranslator acts = new ACTSTranslator();
			// TestSuite ts = acts.generateTestsAndInfo(result, false, 2);
		TestSuite ts = acts.getTestSuite(result, 2, false); // 2 = n-wise = 2-wise
		
		long end1 = System.currentTimeMillis();
		
		/* fine generazione caso di test */

		// esporta il file in CSV
		CSVExporter exporterCSV = new CSVExporter();
		final String exportPath = "evolutionModels_TestsCSV/" + "CSVTest_" + FMName.substring(0, FMName.length() - 4)
				+ ".csv";
		exporterCSV.generateOutput(ts, exportPath);

		// riabilito system out per poter stampare il tempo impiegato nel main
		consolePrintingOn(originalStream);

		// ritorno tempo impiegato per generare test suite
		return (end1 - start1);

	}
		
	/**
	 * Generate the boolean test suite for the specified input Feature Model.
	 * 
	 * @param FMName the name of the Feature Model xml file
	 * @param FMinputPath the path to the input folder where the xml file is located
	 * @return the time taken by the generator (in milliseconds) for generating the
	 *         test suite
	 */
	private static long generateBooleanTestAndExportCSV(final String FMName, final String FMinputPath) {

		// disabilito temporaneamente system.out a console per evitare di stampare
		// informazioni aggiuntive sulla test suite durante la sua generazione
		PrintStream originalStream = System.out;
		consolePrintingOff();
		
		/* inizio generazione caso di test */
		
		// cattura il tempo impiegato
		long start1 = System.currentTimeMillis();
		
		// importa il modello
		FeatureIdeImporterBoolean importer = new FeatureIdeImporterBoolean();
		CitModel result = importer.importModel(FMinputPath + FMName);

		ACTSTranslator acts = new ACTSTranslator();
			// TestSuite ts = acts.generateTestsAndInfo(result, false, 2);
		TestSuite ts = acts.getTestSuite(result, 2, false); // 2 = n-wise = 2-wise
		
		long end1 = System.currentTimeMillis();
		
		/* fine generazione caso di test */

		// esporta il file in CSV
		CSVExporter exporterCSV = new CSVExporter();
		final String exportPath = "evolutionModels_TestsCSV/" + "CSVTest_" + FMName.substring(0, FMName.length() - 4)
				+ ".csv";
		exporterCSV.generateOutput(ts, exportPath);

		// riabilito system out per poter stampare il tempo impiegato nel main
		consolePrintingOn(originalStream);

		// ritorno tempo impiegato per generare test suite
		return (end1 - start1);

	}

	/**
	 * Deactivate console printing
	 */
	static void consolePrintingOff() {

		PrintStream emptyStream = new PrintStream(new OutputStream() {
			public void write(int b) {
				// NO-OP
			}
		});

		System.setOut(emptyStream);
	}

	/**
	 * Activate console printing
	 */
	static void consolePrintingOn(PrintStream originalStream) {
		System.setOut(originalStream);
	}

}
