# CTWedge

### **Combinatorial Testing Web EDitor and GEnerator**

# Instructions to Compile the CTWedhe Eclipse Plugin from Source 
## Dependencies
It requires:
- Eclipse JEE with PDT (Plugin Development Toolkit)
- JavaCC, Xtext, and Xtend (can be installed from the Eclipse Marketplace)
- `acts-3.0.jar` to be copied into `ctwedge.generator/libs` folder

## Compile
To compile source code:
- create a folder called local-maven-repo in the root of the repository.
- make sure to have copied the `acts-3.0.jar` into `ctwedge.generator/libs` folder, and in the project root run the command `mvn deploy:deploy-file -DgroupId=ctwedge -DartifactId=acts -Dversion=3.0 -Durl=file:./local-maven-repo/ -DrepositoryId=local-maven-repo -DupdateReleaseInfo=true -Dfile=ctwedge.generator/libs/acts-3.0.jar`
- open all the projects in Eclipse 
- run `mvn install` on the `ctwedge.parent` project, and in any other projects if necessary.
- Finally, the `target` folder in the `ctwedge.web` project will contain the war file to deploy on the server. The `ctwedge.eclipse` folder contains the eclipse plugin and can be run as Eclipse Application to debug.

In case of any problems regarding the setup, contact [Marco Radavelli](mailto://marco.radavelli@unibg.it).

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

## Licence
This software is released undert the Eclipse Public License - v 2.0
