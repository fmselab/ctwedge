package ctwedge.generator.exporter;

import org.eclipse.xtext.generator.IFileSystemAccess2;

import ctwedge.util.Assignment;
import ctwedge.util.Test;
import ctwedge.util.TestSuite;

public class ToCSV {
	void doGenerate(TestSuite input, String filename,IFileSystemAccess2 fsa){
		fsa.generateFile(filename,toCSVcode(input));
	}
	String toCSVcode(TestSuite input) {
		String s = "";
		for (Assignment assignment : input.getTests().get(0).getAssignments()) {
			if (input.getTests().get(0).getAssignments().indexOf(assignment)!=0) {
				s+=", "+assignment.getParameter().getName();
			} else {
				s+=assignment.getParameter().getName();
			}
		}
		for (Test test : input.getTests()) for (Assignment assignment: test.getAssignments()) {
			if (test.getAssignments().indexOf(assignment)!=0) {
				s+=", "+assignment.getValue();
			} else {
				s+=assignment.getValue();
			}
		}
		return s;
	}
}
