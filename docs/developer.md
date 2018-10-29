# CTWedge

### **Combinatorial Testing Web EDitor and GEnerator**

# Instructions to Compile the CTWedge Eclipse Plugin from Source, and to use its APIs
The web version (no need for installation) and the Eclipse plugin links are availabe in the [home page of the project](https://github.com/fmselab/ctwedge).

## Dependencies
To compile the source code, you need:
- Eclipse JEE (for the web part) with PDT (Plugin Development Toolkit)
- JavaCC, Xtext, and Xtend (can be installed from the Eclipse Marketplace)
- `acts-3.0.jar` to be copied into `ctwedge.generator/libs` folder. This repository now does not contain this jar: you can find it online from NIST. Other versions of ACTS should work as well: rename the chosen version as `acts-3.0.jar` to be recognized by the current project configurations, or change some parts of the source code to refence that exact library version.

## Project structure
- `ctwedge.parent`  language definitions
	- `ctwedge` contains the language definition, and the parser
	- `ctwedge.ide`
	- `ctwedge.tests`
	- `ctwedge.util` utlity functions (depends on `ctwedge`)
	- `ctwedge.web`  web editor and servlets for generation (depends on `ctwedge`, `ctwedge.generator`, `ctwedge.util`)	
- `ctwedge.generator` the generators (ACTS, CASA ...) common both to the eclipse plugin and the web version.
				It depends on `ctwedge`, `ctwedge.util`
- `ctwedge.generator.test` tests for the generator methods
- `ctwedge.web.test`  tests for the web interface
- `ctwedge.benchmarks` benchmarks (depends on `ctwedge`)
- `ctwedge.eclipse`  eclipse plugins (depends on `ctwedge`, `ctwedge.generator`, `ctwedge.util`)
	- `ctwedge.eclipse`  extension points and abstract classes (generator)
	- `ctwedge.eclipse.feature` feature for update site
	- `ctwedge.eclipse.ui` UI extensions for buttons and so on, view, tables ....

To create a model and obtain a test suite programmatically, look at examples in the `ctwedge.generator.test` folder.

## Compile from source
CTWedge is accessible via any browser at http://foselab.unibg.it/ctwedge. However, this is the instruction to build the .war file from source, and install it to your own server:
- make sure to have copied the `acts-3.0.jar` (not included in this repository) into `ctwedge.generator/libs` folder. 
- create a folder called local-maven-repo under the root of the repository, and from the project root, run the command `mvn deploy:deploy-file -DgroupId=ctwedge -DartifactId=acts -Dversion=3.0 -Durl=file:./local-maven-repo/ -DrepositoryId=local-maven-repo -DupdateReleaseInfo=true -Dfile=ctwedge.generator/libs/acts-3.0.jar`
- run `mvn install` on the `ctwedge.parent` project, and in any other projects if necessary.
- Finally, the `target` folder in the `ctwedge.web` project will contain the war file to deploy on the server. The `ctwedge.eclipse` folder contains the eclipse plugin and can be run as Eclipse Application to debug.

In case of any problems regarding the setup, contact [Marco Radavelli](mailto://marco.radavelli@unibg.it).


## Licence
This software is released under the Eclipse Public License - v 2.0
