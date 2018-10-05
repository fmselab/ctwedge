# CTWedge
 Combinatorial Testing Web EDitor and GEnerator

It comes into two flavors:

- [CTWedge Web](http://foselab.unibg.it/ctwedge): http://<i></i>foselab.unibg.it<i></i>/ctwedge
- [CTWedge for Eclipse](https://fmselab.github.io/ctwedge/): https://<i></i>fmselab.github.io<i></i>/ctwedge/ctwedge_update/ . From Eclipse, press `Help` -> `Install New Software...`, add the link, and follow the instructions to install. Obs.: note that Xtext is required: if not present, please install it first (it can be found also in the Eclipse Marketplace).

## Dependences
It requires:
- JavaCC
- Xtext
- Xtend
- `acts-3.0.jar` to be copied into `ctwedge.generator/libs` folder

To compile source code, open all the projects in Eclipse. Run Maven install on the ctwedge.parent project, and in any other projects if necessary.
The `target` folder in the `ctwedge.web` project will contain the war file to deploy on the server.
The `ctwedge.eclipse` folder contains the eclipse plugin and can be run as Eclipse Application to debug.

## Project structure
- `ctwedge.parent`  language definitions
	- `ctwedge`
	- `ctwedge.ide`
	- `ctwedge.web`  web editor and servlets for generation (depends on `ctwedge`, `ctwedge.generator`, `ctwedge.util`)
	- `ctwedge.util` utlity functions (depends on `ctwedge`)
- `ctwedge.generator` the generators (ACTS, CASA ...) 
				depends on `ctwedge`, `ctwedge.util`
- `ctwedge.benchmarks` benchmarks (depends on `ctwedge`)
- `ctwedge.eclipse`  eclipse plugins (depends on `ctwedge`, `ctwedge.generator`, `ctwedge.util`)
	- `ctwedge.eclipse`  extension points and abstract classes (generator)
	- `ctwedge.eclipse.feature` feature for update site
	- `ctwedge.eclipse.ui` UI extensions for buttons and so on, view, tables ....
	
- `ctwedge.generator.test`

## People
Most of the implementation work has been done by [Marco Radavelli](https://cs.unibg.it/radavelli/) and [Angelo Gargantini](http://cs.unibg.it/gargantini/). CTWedge extends the [CitLab](https://sourceforge.net/projects/citlab/) framework by [Paolo Vavassori](http://cs.unibg.it/vavassori/). For any problems, contact [Angelo Gargantini](mailto://angelo.gargantini@unibg.it)  

## Paper
If you use this tool for academic research, please cite:
[*Migrating Combinatorial Interaction Test Modeling and Generation to the Web*](https://cs.unibg.it/gargantini/research/abstracts/iwct2018.html)
(Gargantini, Angelo, and Radavelli, Paolo) in 7th International Workshop on Combinatorial Testing (IWCT 2018)
```
@inproceedings{Gargantini2018, 
  author={A. Gargantini and M. Radavelli}, 
  booktitle={2018 IEEE International Conference on Software Testing, Verification and Validation Workshops (ICSTW)}, 
  title={Migrating Combinatorial Interaction Test Modeling and Generation to the Web}, 
  year={2018}, 
  pages={308-317}, 
  keywords={formal specification;program testing;Web services;test generation;CIT specifications;Web service;combinatorial interaction test modeling;software as a service;software as a service paradigm;SaaS;combinatorial testing Web editing and generation;CTWEDGE;Xtext;Tools;Software as a service;Test pattern generators;Servers;Grammar;combinatorial interaction testing;Software as a Service;domain specific language;web editor}, 
  doi={10.1109/ICSTW.2018.00066}, 
  month={April}
}
```
