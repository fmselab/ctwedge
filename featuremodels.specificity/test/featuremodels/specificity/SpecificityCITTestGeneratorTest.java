package featuremodels.specificity;

import java.io.IOException;
import java.nio.file.Path;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Test;

import ctwedge.fmtester.experiments.MutationScore;
import ctwedge.fmtester.experiments.TestSimpleExampleForPaper;
import ctwedge.importer.featureide.FeatureIdeImporter;
import ctwedge.util.TestSuite;
import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.init.FMCoreLibrary;
import de.ovgu.featureide.fm.core.io.manager.FeatureModelManager;

public class SpecificityCITTestGeneratorTest {

	private static final String PP_UV2_XML = "..\\featuremodels.evotester\\evolutionModels\\PPU\\PPUv2.xml";
	private static final String PP_UV1_XML = "..\\featuremodels.evotester\\evolutionModels\\PPU\\PPUv1.xml";
	private static final String ERP_V2_XML = "..\\featuremodels.evotester\\evolutionModels\\ERP\\ERP_SPL_s2.xml";
	private static final String ERP_V1_XML = "..\\featuremodels.evotester\\evolutionModels\\ERP\\ERP_SPL_s1.xml";
	private static final String BOEING_V2_XML = "..\\featuremodels.evotester\\evolutionModels\\Boeing\\Boeingv2.xml";
	private static final String BOEING_V1_XML = "..\\featuremodels.evotester\\evolutionModels\\Boeing\\Boeingv1.xml";

	static {
		FMCoreLibrary.getInstance().install();
		Logger.getLogger("fmautorepair.mutationoperators").setLevel(Level.OFF);
		Logger.getLogger(FeatureIdeImporter.class).setLevel(Level.OFF);
		Logger.getLogger(MutationScore.class).setLevel(Level.OFF);
		Logger.getLogger(BDDCITTestGenerator.class).setLevel(Level.DEBUG);
	}

	@Test
	public void test1() throws IOException {
		Logger.getLogger(BDDCITTestGenerator.class).setLevel(Level.OFF);
		executeTest(PP_UV2_XML, PP_UV1_XML);
	}

	@Test
	public void test2() throws IOException {
		Logger.getLogger(BDDCITTestGenerator.class).setLevel(Level.INFO);
		executeTest(PP_UV1_XML, PP_UV2_XML);
	}

	@Test
	public void test3() throws IOException {
		executeTest(ERP_V2_XML, ERP_V1_XML);
	}

	@Test
	public void test4() throws IOException {
		executeTest(ERP_V1_XML, ERP_V2_XML);
	}

	@Test
	public void test5() throws IOException {
		executeTest(BOEING_V2_XML, BOEING_V1_XML);
	}

	@Test
	public void test6() throws IOException {
		executeTest(BOEING_V1_XML, BOEING_V2_XML);
	}

	@Test
	public void testAlternative() throws IOException {
		executeTest("fmodels/Alternative.xml", "fmodels/Alternative.xml");
	}

	@Test
	public void testSpecificity() throws IOException {
		Logger.getLogger(BDDCITTestGenerator.class).setLevel(Level.DEBUG);
		executeTest("fmodels/fm1.xml", "fmodels/fm2.xml");
	}

	@Test
	public void testExperiments() throws IOException {
		Logger.getLogger(BDDCITTestGenerator.class).setLevel(Level.OFF);
		testEvo(TestSimpleExampleForPaper.EV_ALIV);
		testEvo(TestSimpleExampleForPaper.EV_PPU);
		testEvo(TestSimpleExampleForPaper.EV_AUTOM);
		testEvo(TestSimpleExampleForPaper.EV_BOING);
		testEvo(TestSimpleExampleForPaper.EV_CARBODY);
		testEvo(TestSimpleExampleForPaper.EV_LINUX);
		testEvo(TestSimpleExampleForPaper.EV_PARKING);
		testEvo(TestSimpleExampleForPaper.EV_BCS);
		testEvo(TestSimpleExampleForPaper.EV_ERP);
		testEvo(TestSimpleExampleForPaper.EV_HSYS);
		testEvo(TestSimpleExampleForPaper.EV_MOBMEDIA);
		testEvo(TestSimpleExampleForPaper.EV_SHOME);
		testEvo(TestSimpleExampleForPaper.EV_SMARTH);
		testEvo(TestSimpleExampleForPaper.EV_SMARTW);
		testEvo(TestSimpleExampleForPaper.EV_WSTAT);
	}

	private void testEvo(String[] evo) throws IOException {
		for (int i = 1; i < evo.length - 1; i++) {
			executeTest("../featuremodels.evotester/" + evo[0] + "/" + evo[i] + ".xml",
					"../featuremodels.evotester/" + evo[0] + "/" + evo[i + 1] + ".xml");
		}

	}

	private TestSuite executeTest(String oldFm, String newFm) throws IOException {
		Path oldFMPath = Path.of(oldFm);
		IFeatureModel oldFM = FeatureModelManager.load(oldFMPath);
		Path newFMPath = Path.of(newFm);
		IFeatureModel newFM = FeatureModelManager.load(newFMPath);

		SpecificCITTestGenerator gen = new SpecificCITTestGenerator(oldFM, newFM, 2);
		TestSuite ts = gen.generateTestSuite();

		SpecificityChecker spcheck = new SpecificityChecker(oldFM, newFM, false);
		int countSpec = 0;
		int countNotSpec = 0;
		for (ctwedge.util.Test t : ts.getTests()) {
			if (spcheck.isSpecific(t))
				countSpec++;
			else
				countNotSpec++;
		}
		System.out.println("spec " + countSpec + " vs not spec " + countNotSpec);

		return ts;
	}

}
