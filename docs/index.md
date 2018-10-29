# CTWEdge Eclipse Plugin usage

### Combinatorial Testing Web EDitor and GEnerator
This page provides a user guide to the usage of the Eclipse Plugin of [CTWedge](https://github.com/fmselab/ctwedge).

1. First, install it, following the instructions in the [home page](https://github.com/fmselab/ctwedge).
2. In any project, create a file with the extension `.ctw`. The CTWedge editor will open.
3. Once written the model, to run CTWedge, right click on the file and press `Run As` -> `Run Configurations...`
<img src="https://fmselab.github.io/ctwedge/images/runConfigurations.png" width="400" alt="Run Configurations">
4. Create a new CTWedge configuration (if not already present), set the configurations wished, and press `Apply` and `Run`
<img src="https://fmselab.github.io/ctwedge/images/settings.png" width="400" alt="Settings">
<img src="https://fmselab.github.io/ctwedge/images/generatedTS.png" width="400" alt="Run As">
5. If a view with the generated test suite does not appear, or an error is shown, right click again and press `Run As` -> `CTWedge`
<img src="https://fmselab.github.io/ctwedge/images/runas.png" width="400" alt="Run As">
6. If a popup window with a `NULL` error shows up, please make sure to have Xtext installed in Eclipse.

For other problems, please contact [Marco Radavelli](mailto:marco.radavelli@unibg.it)

Last Update 29.10.2018
