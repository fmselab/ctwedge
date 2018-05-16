// this feature model is made available from the University of
// Texas at Austin by the Product Line Research Architecture
// research group at http://www.cs.utexas.edu/users/schwartz/


/* Grammar APL domain model March 1st, 2006 */

APL : Java [Xml] [cpp] [Gui] [drc] [bc] [aj] :: mainAPL ;

Java: [reform] [jedi] jak2java bali :: mainJava ;

Xml : xc | xak ;

Gui : [me] [mmatrix] [guidsl] [web] :: mainGUI ;

bc  : [bcjak2java] :: mainBc ;

aj  : [jak2aj] :: mainAj ;

%% // constraints

bc implies mmatrix ;
me implies mmatrix ;

