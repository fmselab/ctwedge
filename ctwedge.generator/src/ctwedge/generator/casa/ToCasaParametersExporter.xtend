package ctwedge.generator.casa;

import ctwedge.ctWedge.Bool
import ctwedge.ctWedge.CitModel
import ctwedge.ctWedge.Enumerative
import ctwedge.ctWedge.Parameter
import ctwedge.ctWedge.Range
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IFileSystemAccess2

class ToCasaParametersExporter {

// exports 	the part of the model containing the parameters (not the constraints)
	def void doGenerate(Resource resource, IFileSystemAccess2 fsa, int n) {
		fsa.generateFile(resource.className+".citmodel", getCasaModel(resource, n));
	}

	def CharSequence getCasaModel(Resource resource, int n) {
		return toCasaCode(resource.contents.get(0) as CitModel, n);
	}

	def className(Resource res) {
		var name = res.URI.lastSegment
		return name.substring(0, name.indexOf('.'))
	}

	def toCasaCode(CitModel sm, int n) {
		'''
			«n»
			«sm.getParameters.size»
			«FOR param : sm.getParameters»«param.getSize» «ENDFOR»
		'''

	}

	def getSize(Parameter param) {
		switch (param) {
			Enumerative: '''«(param as Enumerative).elements.size»'''
			Bool: '''2'''
			Range: {
				if ((param as Range).step != 0) 
				'''«(((param as Range).end-(param as Range).begin +1)/(param as Range).step as Integer).toString»''' 
				else 
				'''«(((param as Range).end-(param as Range).begin +1) as Integer).toString»'''
			}
		}
	}
}
