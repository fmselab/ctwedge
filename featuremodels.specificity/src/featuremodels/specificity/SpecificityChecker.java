package featuremodels.specificity;

import ctwedge.fmtester.experiments.MutationScore;
import ctwedge.util.Test;
import de.ovgu.featureide.fm.core.analysis.cnf.formula.FeatureModelFormula;
import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.base.impl.FeatureModel;
import de.ovgu.featureide.fm.core.configuration.Configuration;

// check the spcificty of a test
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
		// 
		Boolean validInOld = useEnum ? MutationScore.isTestValidEnum(oldFm, t) : MutationScore.isTestValidBool(oldFm, t);

		// let's assume that it is valid in the new one
		// this assumption could be retracted
		Boolean validInNew = MutationScore.isTestValidEnum(newFm, t);
		assert validInNew;
		
		return validInNew != validInOld;
	}
	
	
	
	
	

}
