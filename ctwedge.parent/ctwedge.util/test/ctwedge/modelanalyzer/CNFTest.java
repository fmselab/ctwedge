package ctwedge.modelanalyzer;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ctwedge.ctWedge.CitModel;
import ctwedge.util.ext.Utility;

public class CNFTest {
	
	@Test
	public void testModel() {
		checkIsCNF("Model NUMC_4\n"
				+ "Parameters:\n"
				+ "Par0: {PAR0_0,PAR0_1,PAR0_2,PAR0_3,PAR0_4,PAR0_5,PAR0_6,PAR0_7,PAR0_8,PAR0_9,PAR0_10,PAR0_11,PAR0_12,PAR0_13,PAR0_14,PAR0_15}\n"
				+ "Par1: {PAR1_0,PAR1_1,PAR1_2,PAR1_3,PAR1_4,PAR1_5}\n"
				+ "Par2: [-20 .. -13]\n"
				+ "Par3: [-48 .. -43]\n"
				+ "Par4: [-49 .. -22]\n"
				+ "Par5 : Boolean\n"
				+ "Par6 : Boolean\n"
				+ "Par7: [-44 .. -29]\n"
				+ "Par8: {PAR8_0,PAR8_1,PAR8_2,PAR8_3,PAR8_4}\n"
				+ "\n"
				+ "Constraints:\n"
				+ "# (((not (Par8 = PAR8_4)) OR Par1 = PAR1_5) AND (Par4 > -43 OR Par1 != PAR1_4)) #");
	}
	
	
	private void checkIsCNF(String model2) {
		CitModel model = Utility.loadModel(model2);		
		assertTrue(AllInCNF.eInstance.process(model));
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
