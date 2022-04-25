package ctwedge.fmtester;

import java.io.OutputStream;
import java.io.PrintStream;

import ctwedge.ctWedge.CitModel;
import ctwedge.generator.acts.ACTSTranslator;
import ctwedge.generator.exporter.CSVExporter;
import ctwedge.importer.featureide.FeatureIdeImporterMultipleLevels;
import ctwedge.importer.featureide.XmlFeatureModelImporter;
import ctwedge.util.TestSuite;

public class Test {

	private static final String EVOLUTION_MODELS_INPUT_FOLDER = "evolutionModels/";

	public static void main(String[] args) {

		// input folder
		String FMName;
		String FMSubFolder;
		String FMInputPath;
		long FMtime;

		/* EVOLUTION MODEL: AutomotiveMultimedia */
		FMSubFolder = "AutomotiveMultimedia";
		FMInputPath = EVOLUTION_MODELS_INPUT_FOLDER + FMSubFolder + "/";

		/* v1 */
		FMName = "AutomotiveMultimediav1.xml";
		FMtime = generateTestAndExportCSV(FMName, FMInputPath);
		System.out.println("\n ** " + FMName + "**\n" + "Elapsed time in milliseconds:" + FMtime);

	}

	/**
	 * Generate the test suite for the specified input Feature Model.
	 * 
	 * @param the name of the Feature Model xml file
	 * @param the path of the input folder where the xml file is located
	 * @return the time taken by the generator (in milliseconds) for generating the
	 *         test suite
	 */
	private static long generateTestAndExportCSV(final String FMName, final String FMinputPath) {

		// disabilito temporaneamente system.out a console per evitare di stampare
		// informazioni aggiuntive sulla test suite durante la sua generazione
		PrintStream originalStream = System.out;
		consolePrintingOff();

		// importa il modello
		FeatureIdeImporterMultipleLevels importer = new XmlFeatureModelImporter();
		CitModel result = importer.importModel(FMinputPath + FMName);

		// genera il caso di test e cattura il tempo impiegato
		ACTSTranslator acts = new ACTSTranslator();
		long start1 = System.currentTimeMillis();
		TestSuite ts = acts.generateTestsAndInfo(result, false, 2);
		long end1 = System.currentTimeMillis();

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

	private static void consolePrintingOff() {

		PrintStream emptyStream = new PrintStream(new OutputStream() {
			public void write(int b) {
				// NO-OP
			}
		});

		System.setOut(emptyStream);
	}
	
	private static void consolePrintingOn(PrintStream originalStream) {
		System.setOut(originalStream);
	}

}
