package ctwedge.generator.exporter;

import java.util.Map.Entry;

import org.eclipse.xtext.generator.IFileSystemAccess2;

import ctwedge.util.Test;
import ctwedge.util.TestSuite;

public class ToCSV {
	
	void doGenerate(TestSuite input, String filename,IFileSystemAccess2 fsa){
		fsa.generateFile(filename,toCSVcode(input));
	}
	
	String toCSVcode(TestSuite input) {
		String s = "";
		int i=0;
		for (Entry<String,String> assignment : input.getTests().get(0).entrySet()) {
			if (i>0) {
				s+=","+assignment.getKey();
			} else {
				s+=assignment.getKey();
			}
			i++;
		}
		s+="\n";
		i=0;
		for (Test test : input.getTests()) {
			i=0;
			for (Entry<String,String> assignment: test.entrySet()) {
				if (i>0) {
					s+=","+assignment.getValue();
				} else {
					s+=assignment.getValue();
				}
				i++;
			}
			s+="\n";
		}
		return s;
	}
}
