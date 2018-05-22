# ctwedge
 Combinatorial Testing Web Editor and GEnerator

It comes into two flavors:

- CTWedge for Eclipse: https://fmselab.github.io/ctwedge/ctwedge_update/ (from Eclipse, press Help -> Install New Software..., and add the link. Obs: note that Xtext is required: if not present, please install it first, e.g., from Eclipse Marketplace)
- [CTWedge Web](http://foselab.unibg.it/ctwedge)

ctwedge.benchmarks   => benchmarks
					-d-> ctwedge & generator (why ?)

ctwedge.eclipse  => eclipse plugins
	ctwedge.eclipse  => extension points and abstract classes (generator)
					-d-> ctwedge
	ctwedge.eclipse.feature => feature for update site
	ctwedge.eclipse.ui => UI extensions for buttons and so on, view, tables ....

ctwedge.generator => the generators (ACTS, CASA ...) 
				-d-> ctwedge, ctwedge.eclipse 
ctwedge.generator.test 
ctwedge.generator.webapp => servlets for generation
						-d-> ctwedge.generator
ctwedge.generator.webapp.test

ctwedge.parent  => language definitions
	ctwedge
	ctwedge.ide
	ctwedge.web  => web editor
	gradle
