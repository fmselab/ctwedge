# Replication package for the paper "My Feature Model has Changed... What Should I Do With My Tests?"

This folder contains replication material for the experiments presented in the paper "My Feature Model has Changed... What Should I Do With My Tests?" submitted for evaluation to the Journal of Systems and Software.

* `JSSExperiments`: it contains the results of our experiments.
  * Every sub-folder contains all test suites generated for specific a specific FM family. Each file is named as \[Original_Model_Name\]\_\[Evolved_Model_Name\]\_\[RandomNumber\]\_\[Policy\]\_\[RepetitionNumber\]. For each test execution, two files are generated: one (`.txt`) contains the test suite, and one (`.time`) contains the time required for its generation.
  * `*.pdf` files are the figures used in the paper
  * `JSSExperiments.txt` contains the raw data for the experiments (models, time, size, dissimilarity, specificity, mutation score).
  * `JSSExperimentsDataAnalyzer.ipynb` is the jupyter notebook used to analyze the raw data, generate all figures, and perform the statistical test.

* `evolutionModels`: it contains all industrial models, along with their evolutions, we used in our evaluation.

* `src\ctwedge\fmtester\experiments\Experiments_JSS_SI_SPLC.java`: it contains the code executing the experiments.
