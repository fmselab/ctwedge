package ctwedge.importer.featureide;

import static org.junit.Assert.*;

import java.nio.file.Path;

import org.junit.Test;

import de.ovgu.featureide.fm.core.FeatureModel;
import de.ovgu.featureide.fm.core.analysis.cnf.formula.FeatureModelFormula;
import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.base.impl.DefaultFeatureModelFactory;
import de.ovgu.featureide.fm.core.base.impl.FMFactoryManager;
import de.ovgu.featureide.fm.core.base.impl.FMFormatManager;
import de.ovgu.featureide.fm.core.configuration.Configuration;
import de.ovgu.featureide.fm.core.configuration.Selection;
import de.ovgu.featureide.fm.core.init.FMCoreLibrary;
import de.ovgu.featureide.fm.core.io.manager.FeatureModelManager;
import de.ovgu.featureide.fm.core.io.xml.XmlFeatureModelFormat;

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
