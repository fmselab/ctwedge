package ctwedge.fmtester;

import static org.junit.Assert.*;

import org.junit.Test;

import ctwedge.ctWedge.CitModel;
import ctwedge.generator.acts.ACTSTranslator;
import ctwedge.importer.featureide.XmlFeatureModelImporter;
import ctwedge.util.TestSuite;

public class TestGeneratorTest {

	@Test
	public void test() {
		String fm = "evolutionModels\\PPU\\PPUv1.xml";
		extracted(fm);			
	}

	@Test
	public void test2() {
		String fm = "fmexamples\\model_Man_Man.xml";
		extracted(fm);			
	}

	
	private void extracted(String fm) {
		// importo Feature Model .xml
		XmlFeatureModelImporter importer = new XmlFeatureModelImporter();
		CitModel model = importer.importModel(fm);
		// genero con ACTS
		ACTSTranslator acts = new ACTSTranslator();
		// TestSuite ts = acts.generateTestsAndInfo(result, false, 2);
		TestSuite ts = acts.getTestSuite(model, 2, false);
	}

}
