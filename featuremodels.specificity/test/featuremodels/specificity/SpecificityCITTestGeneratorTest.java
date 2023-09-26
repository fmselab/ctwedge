package featuremodels.specificity;

import java.io.IOException;
import java.nio.file.Path;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Test;

import ctwedge.fmtester.experiments.MutationScore;
import ctwedge.importer.featureide.FeatureIdeImporter;
import ctwedge.util.TestSuite;
import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.init.FMCoreLibrary;
import de.ovgu.featureide.fm.core.io.manager.FeatureModelManager;

public class SpecificityCITTestGeneratorTest {
	
	private static final String PP_UV2_XML = "..\\featuremodels.evotester\\evolutionModels\\PPU\\PPUv2.xml";
	private static final String PP_UV1_XML = "..\\featuremodels.evotester\\evolutionModels\\PPU\\PPUv1.xml";

	static {
		FMCoreLibrary.getInstance().install();
		Logger.getLogger("fmautorepair.mutationoperators").setLevel(Level.OFF);
		Logger.getLogger(FeatureIdeImporter.class).setLevel(Level.OFF);
		Logger.getLogger(MutationScore.class).setLevel(Level.OFF);
	}
	
	@Test
	public void test1() throws IOException {
		Path oldFMPath = Path.of(PP_UV1_XML);
		IFeatureModel  oldFM = FeatureModelManager.load(oldFMPath);
		Path newFMPath = Path.of(PP_UV2_XML);
		IFeatureModel newFM = FeatureModelManager.load(newFMPath);
		
		SpecificCITTestGenerator gen = new SpecificCITTestGenerator(oldFM, newFM, false, 2);
		TestSuite ts = gen.generateSpecificTestSuite();
	}
	
}

