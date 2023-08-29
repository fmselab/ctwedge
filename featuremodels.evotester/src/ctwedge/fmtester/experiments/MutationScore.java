package ctwedge.fmtester.experiments;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.junit.Test;

import ctwedge.fmtester.Converter;
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
import fmautorepair.utils.Utils;
import pMedici.main.PMedici;
import pMedici.util.TestContext;

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
					Boolean result = isTestValid(featureModelFormula, test);
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

	// for enum transaation	
	public static Boolean isValid2(IFeatureModel featureModel, ctwedge.util.Test test) {
		FeatureModelFormula featureModelFormula = new FeatureModelFormula(featureModel);
		Configuration conf = new Configuration(featureModelFormula);
	
		// Set every assignment in the test as feature selected or not
		for (Entry<String, String> assignemnt : test.entrySet()) {
			String value = assignemnt.getValue();
			Selection sel;
			if (value.equals("true") || value.equals("false"))
				// Boolean values
				sel = value.equals("true") ? Selection.SELECTED : Selection.UNSELECTED;
			else {
				// Enums
				if (value.equals("NONE"))
					sel = Selection.UNDEFINED;
				else
					sel = Selection.SELECTED;
			}
			// is feature present
			assert Utils.getFeatureNames(featureModel).contains(assignemnt.getKey());
			conf.setManual(assignemnt.getKey(), sel);
		}
	
		ConfigurationPropagator cp = new ConfigurationPropagator(featureModelFormula, conf);
		Boolean result = LongRunningWrapper.runMethod(cp.isValid());
		return result;
	}

	// for boolean tranlsation (true or false)
	static public Boolean isTestValid(FeatureModelFormula featureModelFormula, ctwedge.util.Test test) {
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
		return result;
	}
}
