package ctwedge.fmtester.experiments;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.junit.Test;

import ctwedge.ctWedge.CitModel;
import ctwedge.fmtester.Converter;
import ctwedge.fmtester.DistancesCalculator;
import ctwedge.generator.acts.ACTSTranslator;
import ctwedge.generator.exporter.ToCSV;
import ctwedge.importer.featureide.FeatureIdeImporter;
import ctwedge.importer.featureide.XmlFeatureModelImporter;
import ctwedge.util.TestSuite;
import pMedici.importer.CSVImporter;
import pMedici.main.PMedici;
import pMedici.main.PMediciPlus;
import pMedici.safeelements.TestContext;
import pMedici.util.Operations;
import pMedici.util.TestModel;

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
		TestSuite mediciTS1 = pMedici.generateTests(fmName + "_ctwedge_enum.ctw", 2, 2);
		// MODELLO 2
		String fmName2 = "fmexamples/ex_paper2";
		// convert to enum ctwedge (could be done only once)
		Converter.fromFMtoCTWedge_ENUM(fmName2 + ".xml", fmName2 + "_ctwedge_enum.ctw");
		TestSuite mediciTS2 = pMedici.generateTests(fmName2 + "_ctwedge_enum.ctw", 2, 2);
		// now compute the distance
		DistancesCalculator.PRINT_DEBUG = true;
		float distance = DistancesCalculator.percTestSuitesDist(mediciTS1, mediciTS2);
		System.out.println(distance);
		// tecnica 2
		String mediciModel = pMedici.buildMediciModel(fmName2 + "_ctwedge_enum.ctw");
		TestModel m = Operations.readModelFromReader(new BufferedReader(new StringReader(mediciModel)));
		ToCSV converter = new ToCSV();		
		String oldTsStr = converter.toCSVcode(mediciTS1);
		Vector<Map<String, String>> oldTs = CSVImporter.readFromReader(new StringReader(oldTsStr));
		
		String newTs = PMediciPlus.generateTests(pMedici.getModel(), m, oldTs);
		TestSuite technique2TS = new TestSuite(newTs, pMedici.getModel());
		float distance2 = DistancesCalculator.percTestSuitesDist(mediciTS1, technique2TS);
		System.out.println(distance2);
		
	}

}
