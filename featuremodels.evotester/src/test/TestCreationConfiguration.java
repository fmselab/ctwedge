package test;

import static org.junit.Assert.*;

import java.nio.file.Path;

import org.junit.Test;

import de.ovgu.featureide.fm.core.analysis.cnf.formula.FeatureModelFormula;
import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.base.impl.FMFormatManager;
import de.ovgu.featureide.fm.core.configuration.Configuration;
import de.ovgu.featureide.fm.core.configuration.Selection;
import de.ovgu.featureide.fm.core.init.FMCoreLibrary;
import de.ovgu.featureide.fm.core.io.manager.FeatureModelManager;
import de.ovgu.featureide.fm.core.io.xml.XmlFeatureModelFormat;

public class TestCreationConfiguration {

	
	@Test
	public void test() {
		FMCoreLibrary.getInstance().install();
		Path path = Path.of("evolutionModels\\PPU\\PPUv1.xml");
		assertTrue(path.toFile().exists());
		IFeatureModel root = FeatureModelManager.load(path);
		
		Configuration c = new Configuration(new FeatureModelFormula(root));	
		c.reset();		
		System.out.println(c.getSelectedFeatureNames());
		System.out.println(c.getUnSelectedFeatures());
		System.out.println(c.getAutomaticFeatures());
		System.out.println(c.getRoot());
		c.setManual("PPU", Selection.UNSELECTED);
		System.out.println(c.getSelectedFeatureNames());
		System.out.println(c.getUnSelectedFeatures());
		System.out.println(c.getAutomaticFeatures());
		System.out.println(c.getManualFeatures());
	}

}
