package test;

import static org.junit.Assert.*;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;

import de.ovgu.featureide.fm.core.FeatureModelAnalyzer;
import de.ovgu.featureide.fm.core.analysis.cnf.LiteralSet;
import de.ovgu.featureide.fm.core.base.IFeature;
import de.ovgu.featureide.fm.core.base.IFeatureModel;

import org.junit.Test;

import de.ovgu.featureide.fm.core.explanations.fm.DeadFeatureExplanation;
import de.ovgu.featureide.fm.core.init.FMCoreLibrary;
import de.ovgu.featureide.fm.core.io.manager.FeatureModelManager;
import de.ovgu.featureide.fm.core.job.monitor.IMonitor;
import de.ovgu.featureide.fm.core.job.monitor.IMonitor.MethodCancelException;
import de.ovgu.featureide.fm.core.job.monitor.*;

public class DeadFeaturesTester {

	@Test
	public void test() {
		FMCoreLibrary.getInstance().install();
		Path path = Path.of("evolutionModels\\PPU\\PPUv1.xml");
		assertTrue(path.toFile().exists());
		IFeatureModel root = FeatureModelManager.load(path);
		FeatureModelAnalyzer analyzer = new FeatureModelAnalyzer(root);
		
		List<IFeature> exp = analyzer.getDeadFeatures(new NullMonitor<>());
		System.out.println(exp);
		exp = analyzer.getCoreFeatures(new NullMonitor<>());
		System.out.println(exp);


	}

}
