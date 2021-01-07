package ctwedge.modelanalyzer;

import static org.junit.Assert.*;

import org.junit.Test;

import ctwedge.ctWedge.CitModel;
import ctwedge.generator.util.Utility;

public class FobiddenTuplesTest {

	@Test
	public void testCaseNOTAND() {
		// enums
		checkHasForbiddenTuples("Model example Parameters: a: {A1,A2}; b:{B1,B2};"
				+ "Constraints: # !(a=A1 and b=B2) #");
		checkHasForbiddenTuples("Model example Parameters: a: {A1,A2}; b:{B1,B2};  c: {C1,C2};"
				+ "Constraints: # !(a=A1 and b=B2) # # !(a=A2 and c=C2) #");
		// a boolean
		checkHasForbiddenTuples("Model example Parameters: a: Boolean; b:{B1,B2};  c: {C1,C2};"
				+ "Constraints: # !(a=true and b=B2) # # !(a=A2 and c=C2) #");
	}
	@Test
	public void testCaseORNOT() {
		// enums
		checkHasForbiddenTuples("Model example Parameters: a: {A1,A2}; b:{B1,B2};"
				+ "Constraints: # a!=A1 or b!=B2) #");
		checkHasForbiddenTuples("Model example Parameters: a: {A1,A2}; b:{B1,B2};  c: {C1,C2};"
				+ "Constraints: # a!=A1 or b!=B2) # # a!=A2 or c!=C2 #");
	}

	private void checkHasForbiddenTuples(String model2) {
		CitModel model = Utility.loadModel(model2);		
		assertTrue(FobiddenTuples.eInstance.process(model));
	}

}
