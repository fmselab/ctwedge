package test;

import org.junit.Test;

import ctwedge.ctWedge.CitModel;
import ctwedge.generator.acts.ACTSTranslator;
import ctwedge.importer.featureide.FeatureIdeImporter;
import ctwedge.importer.featureide.FeatureIdeImporterBoolean;
import ctwedge.importer.featureide.XmlFeatureModelImporter;
import ctwedge.util.ModelUtils;
import ctwedge.util.TestSuite;

public class TestJSS {
	
	static FeatureIdeImporterBoolean importerAsBool = new FeatureIdeImporterBoolean();
	static XmlFeatureModelImporter importerWithEnum = new XmlFeatureModelImporter();
	
	@Test
	public void tsFig1JSS() {
		String fm = "fmexamples/Figure1JSS.xml";
		generateTestSuite(fm, importerWithEnum);
	}

	
	// ------------------------------------------------------------------
	// END TESTS FOR DistancesCalculator class
	// ------------------------------------------------------------------

	private void generateTestSuite(String fm, FeatureIdeImporter importer) {
		CitModel model = importer.importModel(fm);
		ModelUtils mu = new ModelUtils(model);
		System.out.println(mu.serializeToString());
		// genero con ACTS
		ACTSTranslator acts = new ACTSTranslator();
		// TestSuite ts = acts.generateTestsAndInfo(result, false, 2);
		TestSuite ts = acts.getTestSuite(model, 2, false);
		System.out.println(ts);
	}

}
