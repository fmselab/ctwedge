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
 * 
 * Offers static methods to generate combinatorial tests in .csv format for the
 * specified input Feature Model using ACTS.
 * 
 */
public class ACTSGeneratorFromFM {

	/**
	 * Generate the test suite (not boolean) for the specified input Feature Model using ACTS.
	 * 
	 * @param FMName      the name of the Feature Model xml file
	 * @param FMinputPath the path to the input folder where the xml file is located
	 * @return the time taken by the generator (in milliseconds) for generating the
	 *         test suite
	 */
	public static long generateEnumTestAndExportCSV(final String FMName, final String FMinputPath) {

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
	 * Generate the boolean test suite for the specified input Feature Model using ACTS.
	 * 
	 * @param FMName      the name of the Feature Model xml file
	 * @param FMinputPath the path to the input folder where the xml file is located
	 * @return the time taken by the generator (in milliseconds) for generating the
	 *         test suite
	 */
	public static long generateBooleanTestAndExportCSV(final String FMName, final String FMinputPath) {

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

		long end1 = System.currentTimeMillis(); // non includiamo il tempo per l'esportazione

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
