# Replication material for the paper entitled "Testing the Evolution of Feature Models with Specific Combinatorial Tests"

This folder contains the replication material for the paper entitled "Testing the Evolution of Feature Models with Specific Combinatorial Tests" and submitted to A-MOST 2024.
This is the Eclipse project containing the SPECGEN and the BDDGEN generators, the interface to the other state-of-the-art generators and the code executing the experiments

## Repository structure

* [`experiments`](https://github.com/fmselab/ctwedge/tree/master/featuremodels.specificity/experiments): it contains
	* [`specificity_analysis.ipynb`](https://github.com/fmselab/ctwedge/blob/master/featuremodels.specificity/experiments/specificity_analysis.ipynb), the jupyter notebook we have used for executing the Wilcoxon-Signed Rank tests and for producing the box-plots we report into the paper
	* [`resultsSPECIFICITY.csv`](https://github.com/fmselab/ctwedge/blob/master/featuremodels.specificity/experiments/resultsSPECIFICITY.csv), the output of the experiments
	* [`imgs`](https://github.com/fmselab/ctwedge/tree/master/featuremodels.specificity/experiments/imgs), it contains the box-plot pictures
* [`fmodels`](https://github.com/fmselab/ctwedge/tree/master/featuremodels.specificity/fmodels): it contains the feature models we used for testing purposes and for our experiments
	* [`evolutionModels`](https://github.com/fmselab/ctwedge/tree/master/featuremodels.specificity/fmodels/evolutionModels): it contains the model evolutions we considered in our experiments. Every model evolution is categorized into a directory with the name of the FM.  
* [`src/featuremodels/specificity`](https://github.com/fmselab/ctwedge/tree/master/featuremodels.specificity/src/featuremodels/specificity): it contains the source code of the generators
	* [`BDDCITTestGenerator.java`](https://github.com/fmselab/ctwedge/blob/master/featuremodels.specificity/src/featuremodels/specificity/BDDCITTestGenerator.java), the BDDGEN generator
	* [`FeatureIdeTestGenerator.java`](https://github.com/fmselab/ctwedge/blob/master/featuremodels.specificity/src/featuremodels/specificity/BDDCITTestGenerator.java), the interface for the INCLING generator
	* [`SpecificCITTestGenerator.java`](https://github.com/fmselab/ctwedge/blob/master/featuremodels.specificity/src/featuremodels/specificity/BDDCITTestGenerator.java), the SPECGEN generator
	* [`SpecificityChecker.java`](https://github.com/fmselab/ctwedge/blob/master/featuremodels.specificity/src/featuremodels/specificity/SpecificityChecker.java), given a feature model evolution and a test, it checks whether the test is specific for the model evolution
* [`test/featuremodels/specificity`](https://github.com/fmselab/ctwedge/tree/master/featuremodels.specificity/test/featuremodels/specificity): it contains the code executing the experiments we report in the paper and other unit tests
	* [`AMOSTExperiments.java`](https://github.com/fmselab/ctwedge/blob/master/featuremodels.specificity/test/featuremodels/specificity/AMOSTExperiments.java), the code executing the experiments we report in the paper 

## Troubleshooting

It may happen that the tests in the [`AMOSTExperiments.java`](https://github.com/fmselab/ctwedge/blob/master/featuremodels.specificity/test/featuremodels/specificity/AMOSTExperiments.java) class end with an `ArrayStoreException`. If this is your case, please check the order of the dependencies in the Eclipse Run Configuration and move the `Maven Dependencies` as the first item in the Classpath Entries list (as shown in the following picture).

![The screenshot showing the order of the Maven Dependencies in the Classpath Entries list](https://github.com/fmselab/ctwedge/blob/master/featuremodels.specificity/Order.png?raw=true)
