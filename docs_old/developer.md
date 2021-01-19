# CTWedge - Instructions to Compile the CTWedge Eclipse Plugin from Source, and to use its APIs

### **Combinatorial Testing Web EDitor and GEnerator**

The web version (no need for installation) and the Eclipse plugin links are availabe in the [home page of the project](https://github.com/fmselab/ctwedge). The info below are only intended for developers who want to integrate CTWedge in a toolchain programmatically, or who want to change it by accessing the source code.

## Dependencies
To compile the source code, you need:
- Eclipse JEE (for the web part) with PDT (Plugin Development Toolkit)
- JavaCC, Xtext, and Xtend (can be installed from the Eclipse Marketplace)
- `acts-3.0.jar` to be copied into `ctwedge.generator/libs` folder. This repository now does not contain this jar: you can find it online from NIST. Other versions of ACTS should work as well: rename the chosen version as `acts-3.0.jar` to be recognized by the current project configurations, or change some parts of the source code to refence that exact library version.

## Project structure
- `ctwedge.parent`  language definitions
	- `ctwedge` contains the language definition, and the parser
	- `ctwedge.ide`  created automatically by the Xtext web project wizard, and unsure about its purpose
	- `ctwedge.tests`  contains a sample model to parse to test for correct compilation
	- `ctwedge.util` utlity functions (depends on `ctwedge`)
	- `ctwedge.web`  web editor and servlets for generation (depends on `ctwedge`, `ctwedge.generator`, `ctwedge.util`)	
	- `ctwedge.generator` the generators (ACTS, CASA ...) common both to the eclipse plugin and the web version.
				It depends on `ctwedge`, `ctwedge.util`
	- `ctwedge.generator.test` tests for the generator methods
	- `ctwedge.web.test`  tests for the web interface
	- `ctwedge.benchmarks` benchmarks (depends on `ctwedge`)
	- `ctwedge.ui`  eclipse plugin (depends on `ctwedge`, `ctwedge.generator`, `ctwedge.util`)
	- `ctwedge.feature` feature for update site
	- `ctwedge.repository` the actual site for the update site

To create a model and obtain a test suite programmatically, look at examples in the `ctwedge.generator.test` folder.

## Compile from source
CTWedge is accessible via any browser at http://foselab.unibg.it/ctwedge. However, this is the instruction to build the .war file from source, and install it to your own server:
- from Eclipse, open all the projects (check the flag `Search for nested projects`)
- right click on the the file 'ctwedge.GenerateCTWEdge.mwe2' in the 'ctwedge' project and 'Run as MWE2 Workflow' (if the menu entry is not present, please check that Xtext is installed - you can install it from the Marketplace)
- run `Maven Install` on the `ctwedge.parent` project, and in any other projects if necessary.
- Finally, the `target` folder in the `ctwedge.web` project will contain the war file to deploy on the server. The `ctwedge.ui` folder contains the eclipse plugin and can be run as Eclipse Application to debug.

In case of any problems regarding the setup, contact [Marco Radavelli](mailto://marco.radavelli@unibg.it).

## FAQ
- Doing Maven Install on ctwedge.web, I get this error: "No compiler is provided in this environment. Perhaps you are running on a JRE rather than a JDK?" Solution: under Java -> Installed JREs in Eclipse preferences, set a JDK path (instead of the JRE), and set this jdk also as execution environment of the ctwedge.web project (Project Properties -> Java Build Path -> Libraries -> Double click on "JRE System Library").

## Licence
This software is released under the Eclipse Public License - v 2.0
