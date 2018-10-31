package ctwedge.generator.casa;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.generator.IFileSystemAccess2;

import ctwedge.ctWedge.Bool;
import ctwedge.ctWedge.CitModel;
import ctwedge.ctWedge.Enumerative;
import ctwedge.ctWedge.Parameter;
import ctwedge.ctWedge.Range;

public class ToCasaParametersExporter {
	// exports 	the part of the model containing the parameters (not the constraints)
		void doGenerate(Resource resource, IFileSystemAccess2 fsa, int n) {
			fsa.generateFile(resource.getClass().getName()+".ctw", getCasaModel(resource, n));
		}

		String getCasaModel(Resource resource, int n) {
			return toCasaCode((CitModel)resource.getContents().get(0), n);
		}

		String className(Resource res) {
			String name = res.getURI().lastSegment();
			return name.substring(0, name.indexOf('.'));
		}

		public String toCasaCode(CitModel sm, int n) {
			String s = n+"\n"+sm.getParameters().size()+"\n";
			for (Parameter param : sm.getParameters()) s+=getSize(param)+" ";
			return s;
		}

		int getSize(Parameter param) {
			if (param instanceof Enumerative) return ((Enumerative)param).getElements().size();
			if (param instanceof Bool) return 2;
			if (param instanceof Range) {
				if (((Range)param).getStep() != 0) {
					return ((((Range)param).getEnd()-((Range)param).getBegin() +1)/((Range)param).getStep());
				} else {
					return ((Range)param).getEnd()-((Range)param).getBegin() +1;
				}
			}
			return 0;
		}
}
