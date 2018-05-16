package ctwedge.generator.exporter

import ctwedge.util.TestSuite
import org.eclipse.xtext.generator.IFileSystemAccess2

class ToCSV {

	def void doGenerate(TestSuite input, String filename,IFileSystemAccess2 fsa){
		fsa.generateFile(filename,toCSVcode(input));
	}
	def toCSVcode(TestSuite input){
		'''
		«FOR assignment :input.tests.get(0).assignments»
		«IF(input.tests.get(0).assignments.indexOf(assignment)!=0)»
		, «assignment.parameter.name»«ELSE»«assignment.parameter.name»«ENDIF»«ENDFOR» 
		«FOR test :input.tests»«FOR assignment: test.assignments»
		«IF(test.assignments.indexOf(assignment)!=0)»
		, «assignment.value»«ELSE»«assignment.value»«ENDIF»«ENDFOR»
		«ENDFOR» 
		'''  
		  
	}  
	
	 
} 