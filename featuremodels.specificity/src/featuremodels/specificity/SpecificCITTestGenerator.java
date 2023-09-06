package featuremodels.specificity;

import de.ovgu.featureide.fm.core.base.IFeatureModel;

// it generates specific tests if possible
public class SpecificCITTestGenerator {

	private IFeatureModel oldFm;
	private IFeatureModel newFm;
	private boolean useEnum;

	SpecificCITTestGenerator(IFeatureModel oldFm,IFeatureModel newFm, boolean useEnum){
		this.oldFm = oldFm;
		this.newFm = newFm;		
		this.useEnum  = useEnum;	
	}

	
	void generateSpecificTestSuite(){
		// get all the tuples for this feature model
	}
	
	
	
}
