package featuremodels.specificity;

import ctwedge.fmtester.experiments.MutationScore;
import ctwedge.fmtester.experiments.MutationScore.MissingFeatureTreatment;
import ctwedge.util.Test;
import de.ovgu.featureide.fm.core.base.IFeatureModel;

// check the specificity of a test
public class SpecificityChecker {
	
	private IFeatureModel oldFm;
	private IFeatureModel newFm;
	private boolean useEnum;

	SpecificityChecker(IFeatureModel oldFm,IFeatureModel newFm, boolean useEnum){
		this.oldFm = oldFm;
		this.newFm = newFm;		
		this.useEnum  = useEnum;	
	}

	public boolean isSpecific(Test t) {
		// in old missing features are just skipped since some features may be added
		MutationScore.treat_missing_feature_as = MissingFeatureTreatment.SKIP;
		Boolean validInOld = useEnum ? MutationScore.isTestValidEnum(oldFm, t) : MutationScore.isTestValidBool(oldFm, t);

		// let's assume that it is valid in the new one and no missing features
		// this assumption could be retracted
		MutationScore.treat_missing_feature_as = MissingFeatureTreatment.ERROR;
		Boolean validInNew =  useEnum ? MutationScore.isTestValidEnum(newFm, t) : MutationScore.isTestValidBool(newFm, t);
		assert validInNew : fmautorepair.utils.Utils.getFeatureNames(newFm) + "\n" + t;
		
		return validInNew != validInOld;
	}
	
	
	
	
	

}
