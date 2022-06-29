package ctwedge.fmtester;

import ctwedge.ctWedge.CitModel;
import ctwedge.generator.acts.ACTSTranslator;
import ctwedge.generator.exporter.CSVExporter;
import ctwedge.generator.util.Utility;
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
public class ACTSGenerator {

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
		// Disabling console printing
		ConsoleManager cm = new ConsoleManager(System.out);
		cm.consolePrintingOff();

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
		cm.consolePrintingOn();

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
		// Disabling console printing
		ConsoleManager cm = new ConsoleManager(System.out);
		cm.consolePrintingOff();

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
		cm.consolePrintingOn();

		// ritorno tempo impiegato per generare test suite
		return (end1 - start1);

	}

	
	/**
	 * Generate the boolean test suite for the specified input CIT Model (.ctw) using ACTS.
	 * 
	 * @param CTWedgeModelPath the name of the CIT Model file (.ctw)
	 * @param outPath the path to the output file where to print the test suite
	 * 
	 * @return the time taken by the generator (in milliseconds) for generating the
	 *         test suite
	 */
	public static long generateBooleanTestAndExportCSV_FromCTWedgeModel(final String CTWedgeModelPath, final String outPath) {

		// disabilito temporaneamente system.out a console per evitare di stampare
		// informazioni aggiuntive sulla test suite durante la sua generazione
		ConsoleManager cm = new ConsoleManager(System.out);
		cm.consolePrintingOff();

		/* inizio generazione caso di test */

		// inizio a catturare il tempo impiegato
		long start1 = System.currentTimeMillis();

		// importa il modello
		CitModel result = Utility.loadModelFromPath(CTWedgeModelPath);
		
		ACTSTranslator acts = new ACTSTranslator();
		// TestSuite ts = acts.generateTestsAndInfo(result, false, 2);
		TestSuite ts = acts.getTestSuite(result, 2, false); // 2 = n-wise = 2-wise

		// non includo il tempo per l'esportazione
		long end1 = System.currentTimeMillis();

		/* fine generazione caso di test */

		// esporta il file in CSV
		CSVExporter exporterCSV = new CSVExporter();
		final String exportPath = outPath;
		exporterCSV.generateOutput(ts, exportPath);

		// riabilito system out per poter stampare il tempo impiegato nel main
		cm.consolePrintingOn();

		// ritorno tempo impiegato per generare test suite
		return (end1 - start1);

	}
}
