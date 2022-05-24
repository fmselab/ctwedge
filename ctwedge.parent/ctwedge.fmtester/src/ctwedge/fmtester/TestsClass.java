package ctwedge.fmtester;

import static org.junit.Assert.*;

import org.junit.Test;

import ctwedge.ctWedge.CitModel;
import ctwedge.generator.acts.ACTSTranslator;
import ctwedge.importer.featureide.FeatureIdeImporterBoolean;
import ctwedge.importer.featureide.XmlFeatureModelImporter;
import ctwedge.util.ModelUtils;
import ctwedge.util.TestSuite;

public class TestsClass {

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
	@Test
	public void test3() {
		String fm = "fmexamples\\CarBodyv1.xml";
		extracted(fm);			
	}
	@Test
	public void test4() {
		String fm = "fmexamples\\CarBodyv1_man.xml";
		extracted(fm);			
	}
	
	@Test
	public void testBool() {
		String fm = "evolutionModels\\PPU\\PPUv1.xml";
		extractBooleanTS(fm);			
	}
	
	@Test
	public void test_getTestSuite() {
		String fm = "evolutionModels\\SmartHotel\\SmartHotelv1.xml";
		TestSuite ts = DistancesCalculator.getTestSuite(fm);
		System.out.println("Test suite manipulation da qui:");
		// ts.getTests() restituisce la Test Suite
		// ts.getTests().get(0) restituisce il primo Test Case della Test Suite
		// il test case è una Map<Key,Value> che contiene tutte le coppie
		// feature-value relative a quel caso di test
		// NB: Map<key,value> è stata implementata come TreeMap<>
		System.out.println(ts.getTests());
		System.out.println(ts.getTests().get(0));

		// prendo la chiave e il valore della prima feature nel primo caso di test
		System.out.println(ts.getTests().get(0).firstKey());
		System.out.println(ts.getTests().get(0).get("Alarm"));
	}
	
	@Test
	public void test_featDist() {
		String fm = "evolutionModels\\SmartHotel\\SmartHotelv1.xml";
		TestSuite ts = DistancesCalculator.getTestSuite(fm);
		System.out.println(ts.getTests());
		ctwedge.util.Test t0 = ts.getTests().get(0); //Alarm=false
		System.out.println(t0);
		ctwedge.util.Test t1 = ts.getTests().get(1); //Alarm=true
		System.out.println(t1);
		ctwedge.util.Test t2 = ts.getTests().get(2); //Alarm=false
		System.out.println(t2);
		
		// Alarm: t0-t0
		assertEquals(0, DistancesCalculator.featDist(t0.get("Alarm"), t0.get("Alarm")));
		// Alarm: t0-t1
		assertEquals(1, DistancesCalculator.featDist(t0.get("Alarm"), t1.get("Alarm")));
		// Alarm: t0-t2
		assertEquals(0, DistancesCalculator.featDist(t0.get("Alarm"), t2.get("Alarm")));
		
		System.out.println("Key set:" + t0.keySet());
	}
	
	@Test
	public void test_testCasesDist() {
		
		System.out.println("******** SMARTHOTELV1 **********");
		String fm = "evolutionModels\\SmartHotel\\SmartHotelv1.xml";
		TestSuite ts = DistancesCalculator.getTestSuite(fm);
		System.out.println(ts.getTests());
		ctwedge.util.Test t1 = ts.getTests().get(0); //Alarm=false
		System.out.println(t1);
		ctwedge.util.Test t2 = ts.getTests().get(1); //Alarm=true
		System.out.println(t2);
		ctwedge.util.Test t3 = ts.getTests().get(2); //Alarm=false
		System.out.println(t3);
		
		// t1-t1 -> distanza 0
		assertEquals(0, DistancesCalculator.testCasesDist(t1, t1));
		// t1-t2 -> distanza 4
		assertEquals(4, DistancesCalculator.testCasesDist(t1, t2));
		// t1-t3 -> distanza 1
		assertEquals(1, DistancesCalculator.testCasesDist(t1, t3));
		
		System.out.println();
		System.out.println("******** SMARTHOTELV2 **********");
		String fmp = "evolutionModels\\SmartHotel\\SmartHotelv2.xml";
		TestSuite tsp = DistancesCalculator.getTestSuite(fmp);
		System.out.println(tsp.getTests());
		ctwedge.util.Test t1p = tsp.getTests().get(0); 
		System.out.println(t1p);
		ctwedge.util.Test t2p = tsp.getTests().get(1); 
		System.out.println(t2p);
		ctwedge.util.Test t3p = tsp.getTests().get(2); 
		System.out.println(t3p);
		
		// t1-t1p -> distanza 4
		assertEquals(4, DistancesCalculator.testCasesDist(t1, t1p));
		// t1-t2p -> distanza 8
		assertEquals(8, DistancesCalculator.testCasesDist(t1, t2p));
		// t1-t3p -> distanza 5
		assertEquals(5, DistancesCalculator.testCasesDist(t1, t3p));
		
	}
	
	private void extractBooleanTS(String fm) {
		// importo Feature Model .xml
		FeatureIdeImporterBoolean importer = new FeatureIdeImporterBoolean();
		CitModel model = importer.importModel(fm);
		ModelUtils mu = new ModelUtils(model);
		System.out.println(mu.serializeToString());
		// genero con ACTS
		ACTSTranslator acts = new ACTSTranslator();
		// TestSuite ts = acts.generateTestsAndInfo(result, false, 2);
		TestSuite ts = acts.getTestSuite(model, 2, false);
	}
	
	private void extracted(String fm) {
		// importo Feature Model .xml
		XmlFeatureModelImporter importer = new XmlFeatureModelImporter();
		CitModel model = importer.importModel(fm);
		ModelUtils mu = new ModelUtils(model);
		System.out.println(mu.serializeToString());
		// genero con ACTS
		ACTSTranslator acts = new ACTSTranslator();
		// TestSuite ts = acts.generateTestsAndInfo(result, false, 2);
		TestSuite ts = acts.getTestSuite(model, 2, false);
	}

}
