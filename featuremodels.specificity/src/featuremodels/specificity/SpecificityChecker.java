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

	SpecificityChecker(IFeatureModel oldFm,IFeatureModel newFm){
		this.oldFm = oldFm;
		this.newFm = newFm;		
		
	}

	public boolean isSpecific(Test t) {
		// 
		Boolean validInOld = MutationScore.isValid2(oldFm, t);

		// let's assume that it is valid in the new one
		// this assumption could be retracted
		Boolean validInNew = MutationScore.isValid2(newFm, t);
		assert validInNew;
		
		return validInNew != validInOld;
	}
	
	
	
	
	

}
