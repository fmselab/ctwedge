package ctwedge.fmtester.experiments;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import org.junit.Test;

import ctwedge.ctWedge.CitModel;
import ctwedge.fmtester.Converter;
import ctwedge.fmtester.DistancesCalculator;
import ctwedge.generator.acts.ACTSTranslator;
import ctwedge.generator.exporter.ToCSV;
import ctwedge.importer.featureide.FeatureIdeImporter;
import ctwedge.importer.featureide.XmlFeatureModelImporter;
import ctwedge.util.TestSuite;
import de.ovgu.featureide.fm.core.analysis.cnf.formula.FeatureModelFormula;
import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.configuration.Configuration;
import de.ovgu.featureide.fm.core.configuration.ConfigurationPropagator;
import de.ovgu.featureide.fm.core.configuration.Selection;
import de.ovgu.featureide.fm.core.init.FMCoreLibrary;
import de.ovgu.featureide.fm.core.init.LibraryManager;
import de.ovgu.featureide.fm.core.io.manager.FeatureModelManager;
import de.ovgu.featureide.fm.core.job.LongRunningWrapper;
import fmautorepair.mutationoperators.FMMutation;
import fmautorepair.mutationoperators.FMMutator;
import fmautorepair.mutationoperators.features.AlternativeToAnd;
import fmautorepair.mutationoperators.features.OptionalToMandatory;
import pMedici.main.PMedici;
import pMedici.main.PMediciPlus;
import pMedici.util.TestContext;
import pMedici.util.Operations;
import pMedici.util.TestModel;

public class MutationScore {
	static {
		LibraryManager.registerLibrary(FMCoreLibrary.getInstance());
		TestContext.IN_TEST = true;
	}

	static List<FMMutator> mutOperators = Arrays.asList(AlternativeToAnd.instance, OptionalToMandatory.instance);

	@Test
	public void test1() throws IOException, InterruptedException {
		String fmName = "fmexamples/ex_paper1_AG";
		Converter.fromFMtoCTWedge_ENUM(fmName  + ".xml", fmName + "_ctwedge_enum.ctw");
		PMedici pMedici = new PMedici();
		TestSuite mediciTS1 = pMedici.generateTests(fmName + "_ctwedge_enum.ctw", 2, 1);
		extracted(fmName + ".xml", mediciTS1);
	}

	public void extracted(String fmPath, TestSuite ts) {
		// open the feature model
		Path path = new File(fmPath).toPath();
		IFeatureModel fm = FeatureModelManager.load(path);
		assertNotNull(fm);
		// facciamo le mutazioni
		int mutation = 0 ,killed = 0;
		for (FMMutator fmmut : mutOperators) {
			System.out.println("operator " + fmmut.toString());
			Iterator<FMMutation> mutations = fmmut.mutate(fm);
			while (mutations.hasNext()) {
				mutation ++;
				FMMutation fmM = mutations.next();
				System.out.println(fmM.getSecond());
				// transform the test to a configuration
				FeatureModelFormula featureModelFormula = new FeatureModelFormula(fmM.getFirst());
				for (ctwedge.util.Test test : ts.getTests()) {
					Configuration conf = new Configuration(featureModelFormula);
					// set every assignement in the test as feature selected or not
					for (Entry<String, String> assignemnt : test.entrySet()) {
						String value = assignemnt.getValue();
						assert value.equals("true") || value.equals("false");
						Selection sel = value.equals("true")? Selection.SELECTED :Selection.UNSELECTED;
						conf.setManual(assignemnt.getKey(), sel);
					}
					ConfigurationPropagator cp = new ConfigurationPropagator(featureModelFormula, conf);
					Boolean result = LongRunningWrapper.runMethod(cp.isValid());
					System.out.println("killed?" + !result);
					if (! result) {
						killed ++;
						break;
					}
				}
			}
		}
		System.out.println("mutation score " + killed + "/" + mutation);
	}
}
