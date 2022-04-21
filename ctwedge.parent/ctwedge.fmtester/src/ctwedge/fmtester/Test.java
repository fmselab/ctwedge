package ctwedge.fmtester;

import ctwedge.ctWedge.CitModel;
import ctwedge.generator.acts.ACTSTranslator;
import ctwedge.importer.featureide.FeatureIdeImporterMultipleLevels;
import ctwedge.importer.featureide.XmlFeatureModelImporter;
import ctwedge.util.TestSuite;
import ctwedge.util.ext.NotImportableException;

public class Test {

	private static final String FI_MODELS_DIR = "esempi/";

	public static void main(String[] args) {
		// importa il modello
		FeatureIdeImporterMultipleLevels importer = new XmlFeatureModelImporter();
		CitModel result = importer.importModel(FI_MODELS_DIR +"cellphone_15.xml");
		// genera il caso di test
		ACTSTranslator acts = new ACTSTranslator();
		TestSuite ts = acts.generateTestsAndInfo(result, false, 2);
		System.out.println("numeri di test");
		System.out.println(ts.getTests());
	}
	
}
