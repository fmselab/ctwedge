package featuremodels.specificity;

import java.nio.file.Path;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Test;

import ctwedge.ctWedge.CitModel;
import ctwedge.fmtester.experiments.MutationScore;
import ctwedge.generator.acts.ACTSTranslator;
import ctwedge.importer.featureide.FeatureIdeImporter;
import ctwedge.importer.featureide.FeatureIdeImporterBoolean;
import ctwedge.importer.featureide.XmlFeatureModelImporter;
import ctwedge.util.TestSuite;
import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.init.FMCoreLibrary;
import de.ovgu.featureide.fm.core.io.manager.FeatureModelManager;

public class SpecificityCheckerTest {

	private static final String PP_UV2_XML = "..\\featuremodels.evotester\\evolutionModels\\PPU\\PPUv2.xml";
	private static final String PP_UV1_XML = "..\\featuremodels.evotester\\evolutionModels\\PPU\\PPUv1.xml";

	static {
		FMCoreLibrary.getInstance().install();
		Logger.getLogger("fmautorepair.mutationoperators").setLevel(Level.OFF);
		Logger.getLogger(FeatureIdeImporter.class).setLevel(Level.OFF);
		Logger.getLogger(MutationScore.class).setLevel(Level.OFF);
	}

	@Test
	public void testSpecificityCheckerEnum() {
		testSpec(true, PP_UV1_XML, PP_UV2_XML);
	}

	@Test
	public void testSpecificityCheckerBool() {
		testSpec(false, PP_UV1_XML, PP_UV2_XML);
	}
	@Test
	public void testSpecificityCheckerInvBool() {
		testSpec(false, PP_UV2_XML, PP_UV1_XML);
	}

	@Test
	public void testALIV() {
		testSpec(false, PP_UV2_XML, PP_UV1_XML);
	}

	
	
	// generate using ACTS
	// generate some tests and check how many are specific
	private void testSpec(boolean useEnum, String oldFMPathStr, String newFMPathStr) {
		Path oldFMPath = Path.of(oldFMPathStr);
		IFeatureModel oldFM = FeatureModelManager.load(oldFMPath);
		Path newFMPath = Path.of(newFMPathStr);
		IFeatureModel newFM = FeatureModelManager.load(newFMPath);

		SpecificityChecker spcheck = new SpecificityChecker(oldFM, newFM, useEnum);

		// generate from the new one to see how may are specific
		FeatureIdeImporter importer = useEnum ? new XmlFeatureModelImporter() : new FeatureIdeImporterBoolean();

		CitModel result = importer.importModel(newFMPath.toString());
		ACTSTranslator acts = new ACTSTranslator();
		TestSuite ts = acts.getTestSuite(result, 2, false); // 2 = n-wise = 2-wise
		// how many are specific for
		int countSpec = 0, countNotSpec = 0;
		for (ctwedge.util.Test t : ts.getTests()) {
			System.out.println("checking test " + t);
			if (spcheck.isSpecific(t))
				countSpec++;
			else
				countNotSpec++;
		}
		System.out.println("spec " + countSpec + " vs not spec " + countNotSpec);
	}

}
