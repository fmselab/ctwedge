package featuremodels.specificity;

import static org.junit.Assert.*;

import java.nio.file.Path;

import org.junit.Test;

import ctwedge.ctWedge.CitModel;
import ctwedge.generator.acts.ACTSTranslator;
import ctwedge.importer.featureide.FeatureIdeImporter;
import ctwedge.importer.featureide.FeatureIdeImporterBoolean;
import ctwedge.importer.featureide.XmlFeatureModelImporter;
import ctwedge.util.TestSuite;
import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.init.FMCoreLibrary;
import de.ovgu.featureide.fm.core.io.manager.FeatureModelManager;

public class SpecificityCheckerTest {

	

	@Test
	public void testSpecificityCheckerEnum() {
		testSpec(true);
	}
	@Test
	public void testSpecificityCheckerBool() {
		testSpec(false);
	}

	private void testSpec(boolean useEnum) {
		FMCoreLibrary.getInstance().install();
		Path oldFMPath = Path.of("..\\featuremodels.evotester\\evolutionModels\\PPU\\PPUv1.xml");
		IFeatureModel oldFM = FeatureModelManager.load(oldFMPath);
		Path newFMPath = Path.of("..\\featuremodels.evotester\\evolutionModels\\PPU\\PPUv2.xml");
		IFeatureModel newFM = FeatureModelManager.load(newFMPath);
		
		SpecificityChecker spcheck = new SpecificityChecker(oldFM, newFM, useEnum);
		
		// generate from the new one to see how may are specific
		FeatureIdeImporter importer = useEnum? new XmlFeatureModelImporter() : new FeatureIdeImporterBoolean();	
		
		CitModel result = importer.importModel(newFMPath.toString());
		ACTSTranslator acts = new ACTSTranslator();
		TestSuite ts = acts.getTestSuite(result, 2, false); // 2 = n-wise = 2-wise
		// how many are specific for 
		int countSpec = 0, countNotSpec = 0;
		for(ctwedge.util.Test t:  ts.getTests()) {
			System.out.println("checking test " + t);
			if (spcheck.isSpecific(t)) 
				countSpec++;
			else 
				countNotSpec++;
		}
		System.out.println("spec " + countSpec + " vs not spec " + countNotSpec);
	}

}
