# CTWedge Eclipse Plugin usage
### Combinatorial Testing Web EDitor and GEnerator
<img src="https://raw.githubusercontent.com/fmselab/ctwedge/master/ctwedge.parent/ctwedge.web/WebRoot/logo.png" width="200" alt="CTWedge logo">

This page provides a user guide to the usage of the Eclipse Plugin of [CTWedge](https://github.com/fmselab/ctwedge).

1. First, install it, following the instructions in the [home page](https://github.com/fmselab/ctwedge).
2. Create any empty project (or use an existing one), then press `New` -> `Other...`, and choose `New CTWedge File Wizard`. Complete the wizard entering the filename and the project into which create the CTWedge model file (with the extension `.ctw`).

<img src="https://fmselab.github.io/ctwedge/images/newFile0.png" width="350" alt="Create New File 0"> <img src="https://fmselab.github.io/ctwedge/images/newFile1.png" width="350" alt="Create New File 1"> <img src="https://fmselab.github.io/ctwedge/images/newFile2.png" width="350" alt="Create New File 2">

3. Once written the model, to run CTWedge, right click on the file and press `Run As` -> `Run Configurations...`
<img src="https://fmselab.github.io/ctwedge/images/runConfigurations.png" width="400" alt="Run Configurations">

4. Create a new CTWedge configuration (if not already present), set the configurations wished, and press `Apply` and `Run`
<img src="https://fmselab.github.io/ctwedge/images/settings.png" width="350" alt="Settings"> <img src="https://fmselab.github.io/ctwedge/images/generatedTS.png" width="350" alt="Run As">

5. The generated Test Suite can be exported, as Excel file or CSV, specifying the location.
<img src="https://fmselab.github.io/ctwedge/images/export.png" width="350" alt="Export" />

### Possible Issues
- If a view with the generated test suite does not appear, or an error is shown, perform the previous three steps again, in the order above. Note: if you right click and press `Run As` -> `CTWedge`, it works *only* if a run configuration already exists (step 1 above has been performed previously), and it re-executes that configuration.
<img src="https://fmselab.github.io/ctwedge/images/runas.png" width="350" alt="Run As" />
- If a popup window with a `NULL` error shows up, please make sure to have Xtext installed in Eclipse.

For other problems, please contact [Marco Radavelli](mailto:marco.radavelli@unibg.it)

Last Update 21.05.2019
