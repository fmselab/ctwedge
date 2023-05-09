package ctwedge.importer.featureide;

import java.nio.file.Path;

import org.junit.Test;

import de.ovgu.featureide.fm.core.analysis.cnf.formula.FeatureModelFormula;
import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.configuration.Configuration;
import de.ovgu.featureide.fm.core.configuration.Selection;
import de.ovgu.featureide.fm.core.init.FMCoreLibrary;
import de.ovgu.featureide.fm.core.io.manager.FeatureModelManager;

// come gestire le configurazioni

public class ConfigurationBuilder {

	@Test
	public void test() {
		FMCoreLibrary.getInstance().install();
//		FMFactoryManager.getInstance().addExtension(DefaultFeatureModelFactory.getInstance());
//		FMFormatManager.getInstance().addExtension(new XmlFeatureModelFormat());
		Path path = Path.of("models/featureide/aircraft_fm.xml");
		IFeatureModel featureModel = FeatureModelManager.load(path );
		Configuration configuration = new Configuration(new FeatureModelFormula(featureModel));
		configuration.setManual("Engine", Selection.SELECTED);
		System.out.println(configuration.getUnselectedFeatureNames());
		System.out.println(configuration.getUndefinedFeatureNames());
		
	}

}
