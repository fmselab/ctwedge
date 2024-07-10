package ctwedge.importer.featureide;

import org.junit.Test;

import de.ovgu.featureide.fm.core.init.FMCoreLibrary;

// come gestire le configurazioni

public class ConfigurationBuilder {

	static {
		FMCoreLibrary.getInstance().install();
	}
	
	@Test
	public void test() {
//		FMFactoryManager.getInstance().addExtension(DefaultFeatureModelFactory.getInstance());
//		FMFormatManager.getInstance().addExtension(new XmlFeatureModelFormat());
//		Path path = Path.of("models/featureide/aircraft_fm.xml");
//		IFeatureModel featureModel = FeatureModelManager.load(path );
//		Configuration configuration = new Configuration(new FeatureModelFormula(featureModel));
//		configuration.setManual("Engine", Selection.SELECTED);
//		System.out.println(configuration.getUnselectedFeatureNames());
//		System.out.println(configuration.getUndefinedFeatureNames());
		
	}

}
