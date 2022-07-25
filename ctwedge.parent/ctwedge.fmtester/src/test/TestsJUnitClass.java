package test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import ctwedge.ctWedge.CitModel;
import ctwedge.fmtester.ACTSGenerator;
import ctwedge.fmtester.DistancesCalculator;
import ctwedge.generator.acts.ACTSTranslator;
import ctwedge.importer.featureide.FeatureIdeImporterBoolean;
import ctwedge.importer.featureide.XmlFeatureModelImporter;
import ctwedge.util.ModelUtils;
import ctwedge.util.TestSuite;

public class TestsJUnitClass {

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
	public void testGenerator() {
		ACTSGenerator.generateBooleanTestAndExportCSV_FromCTWedgeModel("../ctwedge.fmtester/evolutionModels/AmbientAssistedLiving/AmbientAssistedLivingv1_ctwedge.ctw", "../ctwedge.fmtester/cancellami/trial.csv");
	}

	// ------------------------------------------------------------------
	// BEGIN TESTS FOR DistancesCalculator class
	// ------------------------------------------------------------------

	@Test
	public void test_getTestSuite() {
		String fm = "evolutionModels\\SmartHotel\\SmartHotelv1.xml";
		TestSuite ts = getTestSuite(fm);
		System.out.println("Test suite manipulation da qui:");
		// ts.getTests() restituisce la Test Suite
		// ts.getTests().get(0) restituisce il primo Test Case della Test Suite
		// il test case è una Map<Key,Value> che contiene tutte le coppie
		// feature-value relative a quel caso di test
		// NB: Map<key,value> è stata implementata come TreeMap<>
		System.out.println(ts.getTests());
		System.out.println("Dim:" + ts.getTests().size());
		System.out.println(ts.getTests().get(0));

		// prendo la chiave e il valore della prima feature nel primo caso di test
		System.out.println(ts.getTests().get(0).firstKey());
		System.out.println(ts.getTests().get(0).get("Alarm"));
	}

	@Test
	public void test_featDist() {
		String fm = "evolutionModels\\SmartHotel\\SmartHotelv1.xml";
		TestSuite ts = getTestSuite(fm);
		System.out.println(ts.getTests());
		ctwedge.util.Test t0 = ts.getTests().get(0); // Alarm=false
		System.out.println(t0);
		ctwedge.util.Test t1 = ts.getTests().get(1); // Alarm=true
		System.out.println(t1);
		ctwedge.util.Test t2 = ts.getTests().get(2); // Alarm=false
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
		TestSuite ts = getTestSuite(fm);
		System.out.println(ts.getTests());
		ctwedge.util.Test t1 = ts.getTests().get(0); // Alarm=false
		System.out.println(t1);
		ctwedge.util.Test t2 = ts.getTests().get(1); // Alarm=true
		System.out.println(t2);
		ctwedge.util.Test t3 = ts.getTests().get(2); // Alarm=false
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
		TestSuite tsp = getTestSuite(fmp);
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

	@Test
	public void test_get_tcDist() {
		String fm = "evolutionModels\\SmartHotel\\SmartHotelv1.xml";
		TestSuite ts = getTestSuite(fm);
		String fmp = "evolutionModels\\SmartHotel\\SmartHotelv2.xml";
		TestSuite tsp = getTestSuite(fmp);

		// mi aspetto che il risultato sia uguale alla tabella
		// dell'elaborato (Table 1)
		int[][] table1 = new int[][] {
			new int[] { 4, 8, 5, 5, 7, 6, 4, 4 },
			new int[] { 7, 5, 6, 8, 4, 5, 7, 7 },
			new int[] { 5, 7, 4, 6, 6, 5, 5, 5 },
			new int[] { 6, 6, 5, 7, 5, 4, 6, 6 },
			new int[] { 7, 5, 6, 8, 4, 5, 7, 7 },
			};
			
		int[][] tcDist = DistancesCalculator.get_tcDist(ts, tsp);
		
		System.out.println("tcDist:\n"+Arrays.deepToString(DistancesCalculator.get_tcDist(ts, tsp)).replace("], ", "]\n"));
		System.out.println("table1:\n"+Arrays.deepToString(table1).replace("], ", "]\n"));
		
		assertTrue(Arrays.deepEquals(table1, tcDist));
	}
	
	@Test
	public void test_testSuitesDist() {
		String fm = "evolutionModels\\SmartHotel\\SmartHotelv1.xml";
		TestSuite ts = getTestSuite(fm);
		String fmp = "evolutionModels\\SmartHotel\\SmartHotelv2.xml";
		TestSuite tsp = getTestSuite(fmp);
		
		System.out.println("Distance:"+DistancesCalculator.testSuitesDist(ts, tsp));
		assertTrue(45==DistancesCalculator.testSuitesDist(ts, tsp));
	}

	@Test
	public void test_percTestSuitesDist() {

		String fm = "evolutionModels\\SmartHotel\\SmartHotelv1.xml";
		TestSuite ts = getTestSuite(fm);
		String fmp = "evolutionModels\\SmartHotel\\SmartHotelv2.xml";
		TestSuite tsp = getTestSuite(fmp);
		
		String fmOther = "evolutionModels\\PPU\\PPUv1.xml";
		TestSuite tsOther = getTestSuite(fmOther);
		
		System.out.println("Distance%: "+DistancesCalculator.percTestSuitesDist(ts, tsp)+"%");
		// Confrontando due modelli identici mi aspetto una distanza del 0%
		System.out.println("Distance%: "+DistancesCalculator.percTestSuitesDist(ts, ts)+"%");
		assertTrue(DistancesCalculator.percTestSuitesDist(ts, ts)==0.0);
		// Confrontando due modelli diversi mi aspetto una distanza del 100%
		System.out.println("Distance%: "+DistancesCalculator.percTestSuitesDist(ts, tsOther)+"%");
		assertTrue(DistancesCalculator.percTestSuitesDist(ts, tsOther)==100.0);
	
	}
	
	// test method used to demnostrate that the algorithm of the distance
	// is greedy and so it does not always return the lowest value of distance
	@Test
	public void test_demonstrationGreedyAlgorithm() {
		// PPU: V4-V5 = LO TROVA
		// PPU: V6-V7 = LO TROVA PIU' SIGNIFICATIVO (5.5 vs 8.58)
//		String fmPath = "evolutionModels\\PPU\\PPUv6.xml";
//		String fmpPath = "evolutionModels\\PPU\\PPUv7.xml";		
		String fmPath = "evolutionModels\\LinuxKernel\\LinuxKernelv2.xml";
		String fmpPath = "evolutionModels\\LinuxKernel\\LinuxKernelv3.xml";		
		
		// Getting the test suite ts of the first feature model
		FeatureIdeImporterBoolean importer = new FeatureIdeImporterBoolean();
		ACTSTranslator acts = new ACTSTranslator();
		CitModel model = importer.importModel(fmPath);
		TestSuite ts = acts.getTestSuite(model, 2, false);
		
		// Getting the test suite tsp of the second feature model
		FeatureIdeImporterBoolean importerp = new FeatureIdeImporterBoolean();
		ACTSTranslator actsp = new ACTSTranslator();
		model = importerp.importModel(fmpPath);
		TestSuite tsp = actsp.getTestSuite(model, 2, false);
		
		// |TS| < |TS'| so TS = row in tcDist
		float originalDistanceValue = DistancesCalculator.percTestSuitesDist(ts, tsp);
		boolean found = false;
		
		List<ctwedge.util.Test> TStestCases = ts.getTests();
		System.out.println("Tests before shuffle: "+TStestCases.toString());
		// Let's check if doing a shuffle on the order of test cases
		// in the List "tests" of TS the value of distance is changing
		for(int i=0; i<20; i++) {
			// shuffling the orderd of test cases in TS
			Collections.shuffle(TStestCases);
			System.out.println("Tests after shuffle["+i+"]: "+TStestCases.toString());
			ts.setTests(TStestCases);	
			float shuffledDistanceValue = DistancesCalculator.percTestSuitesDist(ts, tsp);
			if(originalDistanceValue!=shuffledDistanceValue) {
				System.out.println("**************************************************************");
				System.out.println("I FOUND A VALUE OF DISTANCE WHICH IS DIFFERENT!"+"\noriginal: "+originalDistanceValue+"\nshuffled:"+shuffledDistanceValue);
				System.out.println("**************************************************************");
				found = true;
			}
			//System.out.println("**"+DistancesCalculator.percTestSuitesDist(ts, tsp)+"% **");
		}
		
		System.out.println("TROVATO? "+ (found? "SI" : "NO"));
		System.out.println("original: "+originalDistanceValue+"%");
	}
	
	private static TestSuite getTestSuite(String pathToFM) {
		// import Feature Model .xml
		FeatureIdeImporterBoolean importer = new FeatureIdeImporterBoolean();
		CitModel model = importer.importModel(pathToFM);
		ModelUtils mu = new ModelUtils(model);
		System.out.println(mu.serializeToString());
		// generate boolean TS with ACTS (pair-wise)
		ACTSTranslator acts = new ACTSTranslator();
		return acts.getTestSuite(model, 2, false);
	}
	
	@Test
	public void percTestSuitesDist_FromTestSuites() throws IOException {
		String fmPath = "../ctwedge.fmtester/evolutionModels/Boeing/Boeingv1.xml";
		String tsPath = "../ctwedge.fmtester/evolutionModels_TestsCSV/CSVTest_Boeingv1.csv";
		String fmpPath = "../ctwedge.fmtester/evolutionModels/Boeing/Boeingv2.xml";
		String tspPath = "../ctwedge.fmtester/evolutionModels_TestsCSV/CSVTest_Boeingv2.csv";
		
		float res = DistancesCalculator.percTestSuitesDist_FromTestSuites(fmPath, tsPath, ",", fmpPath, tspPath, ",");
		System.out.println("Distance Boeing ts v1-v2: "+res+"%");
		
		fmPath = "../ctwedge.fmtester/evolutionModels/ParkingAssistant/ParkingAssistantv3.xml";
		tsPath = "../ctwedge.fmtester/evolutionModels_TestsCSV/CSVTest_ParkingAssistantv3.csv";
		fmpPath =  "../ctwedge.fmtester/evolutionModels/ParkingAssistant/ParkingAssistantv4.xml";
		tspPath = "../ctwedge.fmtester/evolutionModels_TestsCSV/CSVTest_ParkingAssistantv4.csv";
		
		res = DistancesCalculator.percTestSuitesDist_FromTestSuites(fmPath, tsPath, ",", fmpPath, tspPath, ",");
		System.out.println("Distance ParkingAssistant ts v3-v4: "+res+"%");
	}
	
	// ------------------------------------------------------------------
	// END TESTS FOR DistancesCalculator class
	// ------------------------------------------------------------------

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
