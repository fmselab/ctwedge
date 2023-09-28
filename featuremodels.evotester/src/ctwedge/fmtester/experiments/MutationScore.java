package ctwedge.fmtester.experiments;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.junit.Test;

import aima.core.search.csp.Assignment;
import ctwedge.fmtester.Converter;
import ctwedge.util.TestSuite;
import de.ovgu.featureide.fm.core.analysis.cnf.formula.FeatureModelFormula;
import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.base.IFeatureStructure;
import de.ovgu.featureide.fm.core.configuration.Configuration;
import de.ovgu.featureide.fm.core.configuration.ConfigurationPropagator;
import de.ovgu.featureide.fm.core.configuration.SelectableFeature;
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
	
	static Logger LOG = Logger.getLogger(MutationScore.class);
	
	static {
		LibraryManager.registerLibrary(FMCoreLibrary.getInstance());
		TestContext.IN_TEST = true;
	}

	static List<FMMutator> mutOperators = Arrays.asList(AlternativeToAnd.instance, OptionalToMandatory.instance);

	@Test
	public void test1() throws IOException, InterruptedException {
		String fmName = "fmexamples/ex_paper1_AG";
		Converter.fromFMtoCTWedge_ENUM(fmName + ".xml", fmName + "_ctwedge_enum.ctw");
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
		int mutation = 0, killed = 0;
		for (FMMutator fmmut : mutOperators) {
			System.out.println("operator " + fmmut.toString());
			Iterator<FMMutation> mutations = fmmut.mutate(fm);
			while (mutations.hasNext()) {
				mutation++;
				FMMutation fmM = mutations.next();
				System.out.println(fmM.getSecond());
				// transform the test to a configuration
				for (ctwedge.util.Test test : ts.getTests()) {
					Boolean result = isTestValidBool(fmM.getFirst(), test);
					System.out.println("killed?" + !result);
					if (!result) {
						killed++;
						break;
					}
				}
			}
		}
		System.out.println("mutation score " + killed + "/" + mutation);
	}

	//
	// for enum translation
	// it transforms the test in a configuration an then it checks that it valid in
	// the FM
	public static Boolean isTestValidEnum(IFeatureModel featureModel, ctwedge.util.Test test) {
		FeatureModelFormula featureModelFormula = new FeatureModelFormula(featureModel);
		Configuration conf = new Configuration(featureModelFormula);
		// Set every assignment in the test as feature selected or not
		LOG.debug("test (ctwedge) " + test.entrySet());
		for (Entry<String, String> assignemnt : test.entrySet()) {
			String value = assignemnt.getValue();
			// is feature present
			String featurename = assignemnt.getKey();
			// TODO see below how to treat missing feature
			assert Utils.getFeatureNames(featureModel).contains(featurename);
			Selection sel;
			if (value.equals("true") || value.equals("false"))
				// Boolean values
				sel = value.equals("true") ? Selection.SELECTED : Selection.UNSELECTED;
			else {
				// Enums
				if (value.equals("NONE")) {
					sel = Selection.UNSELECTED;
					// quelle sotto sono tutte unselected
					List<IFeatureStructure> children = featureModel.getFeature(featurename).getStructure().getChildren();
					for(IFeatureStructure child:children) {
						LOG.debug("setting " + child.getFeature().getName() + " to " + Selection.UNSELECTED);
						conf.setManual(child.getFeature().getName(), Selection.UNSELECTED);
					}
				} else {
					sel = Selection.SELECTED;
					// di quelle sotto dire quali sono quelle selected e euqlle no?????
					List<IFeatureStructure> children = featureModel.getFeature(featurename).getStructure().getChildren();
					for(IFeatureStructure child:children) {
						Selection childsel;
						if (child.getFeature().getName().equals(value))
							childsel = Selection.SELECTED;
						else
							childsel = Selection.UNSELECTED;
						LOG.debug("setting " + child.getFeature().getName() + " to " +childsel);
						conf.setManual(child.getFeature().getName(), childsel);
					}
				}
			}
			LOG.debug("setting " + featurename + " to " + sel);
			conf.setManual(featurename, sel);
		}
		// check completeness of the configuration (partial tests are not allowed??)
		if (true) {
			// features that are actually selected or unslected explicitly by this method 
			Set<String> settedFeatures = conf.getSelectedFeatures().stream().map(x->x.getName()).collect(Collectors.toSet());
			settedFeatures.addAll(conf.getUnSelectedFeatures().stream().map(x->x.getName()).collect(Collectors.toSet()));
			// features in the test
			Set<String> featuresInTest = test.keySet();
			// features not setted but that are in the test:
			//Set<String> feeaturesInTest
			//if (!settedFeatures.equals(featuresInTest)) {
				LOG.debug("features in configuration setted by the test: " + settedFeatures);
				LOG.debug("features in the test: " + featuresInTest);
			//	assert false;
			//}
			// features in the feature model however are:
			LOG.debug("features in the feature model: " + Utils.getFeatureNames(featureModel));	
		}
		ConfigurationPropagator cp = new ConfigurationPropagator(featureModelFormula, conf);
		Boolean result = LongRunningWrapper.runMethod(cp.isValid());
		return result;
	}

	public enum MissingFeatureTreatment {
		ERROR, SKIP
	};

	static public MissingFeatureTreatment treat_missing_feature_as = MissingFeatureTreatment.SKIP;

	// for boolean translation (only true or false for each feature, plus '*' to allow undefined)
	// forse si può fondere con il precedente, cioè chi lo chiama può ignorare
	// questo ha in piu' il trattamento di cose succede se non è presente
	static public Boolean isTestValidBool(IFeatureModel featureModel, ctwedge.util.Test test) {
		FeatureModelFormula featureModelFormula = new FeatureModelFormula(featureModel);
		Configuration conf = new Configuration(featureModelFormula);
		// set every assignment in the test as feature selected or not
		for (Entry<String, String> assignemnt : test.entrySet()) {
			String value = assignemnt.getValue();
			assert value.equals("true") || value.equals("false") || value.equals("*"): "only boolean: this is " + value;
			String featurename = assignemnt.getKey();
			Selection sel;
			if (!Utils.getFeatureNames(featureModel).contains(featurename)) {
				switch (treat_missing_feature_as) {
				case ERROR:
					throw new RuntimeException(featurename + " not found");
				case SKIP:
					continue;
				default:
					throw new RuntimeException();
				}
			} else {
				sel = switch (value) {
					case "true" -> Selection.SELECTED;
					case "false" -> Selection.UNSELECTED;
					// feature is undefined in the tests
					//case "*" 
					default -> Selection.UNDEFINED;
				};
			}
			conf.setManual(featurename, sel);
		}
		ConfigurationPropagator cp = new ConfigurationPropagator(featureModelFormula, conf);
		Boolean result = LongRunningWrapper.runMethod(cp.isValid());
		return result;
	}
}
