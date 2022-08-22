package ctwedge.fmtester.experiments;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import ctwedge.ctWedge.CitModel;
import ctwedge.fmtester.Converter;
import ctwedge.fmtester.DistancesCalculator;
import ctwedge.generator.acts.ACTSTranslator;
import ctwedge.importer.featureide.FeatureIdeImporter;
import ctwedge.importer.featureide.XmlFeatureModelImporter;
import ctwedge.util.TestSuite;
import pMedici.main.PMedici;
import pMedici.main.PMediciPlus;
import pMedici.safeelements.TestContext;

public class TestSimpleExampleForPaper {

	@Test
	public void test() throws IOException, InterruptedException {
		TestContext.IN_TEST = true;
		// genera con tecnica 1 pMedici e calcola distanza
		FeatureIdeImporter importer = new XmlFeatureModelImporter();
		// MODELLO 1
		String fmName = "fmexamples/ex_paper1";
		// convert to enum ctwedge (could be done only once)
		Converter.fromFMtoCTWedge_ENUM(fmName + ".xml", fmName + "_ctwedge_enum.ctw");
		PMedici pMedici = new PMedici();
		TestSuite mediciTS1 = pMedici.generateTests(fmName + "_ctwedge_enum.ctw", 2, 6);
		// MODELLO 2
		String fmName2 = "fmexamples/ex_paper2";
		// convert to enum ctwedge (could be done only once)
		Converter.fromFMtoCTWedge_ENUM(fmName2 + ".xml", fmName2 + "_ctwedge_enum.ctw");
		TestSuite mediciTS2 = pMedici.generateTests(fmName + "_ctwedge_enum.ctw", 2, 6);
		// now compute the distance
		DistancesCalculator.PRINT_DEBUG = true;
		float distance = DistancesCalculator.percTestSuitesDist(mediciTS1, mediciTS2);
		System.out.println(distance);
		// tecnica 2
		
		String newTs = PMediciPlus.genereteTests(pMedici.getModel(), null, null);
		
		
	}

}
