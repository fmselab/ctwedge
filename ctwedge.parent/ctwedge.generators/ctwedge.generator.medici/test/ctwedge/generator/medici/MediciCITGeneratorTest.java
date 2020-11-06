package ctwedge.generator.medici;

import static org.junit.Assert.*;

import org.junit.Test;

import ctwedge.generator.util.Utility;

public class MediciCITGeneratorTest {

	private static final String model1 = "Model prova\nParameters:\n a: Boolean; b: Boolean; c: Boolean;\nConstraints:\n # a #\n";
	private static final String model2 = "Model prova\nParameters:\n a: Boolean; b: Boolean; c: Boolean;\nConstraints:\n # not a #\n";
	private static final String model3 = "Model prova\nParameters:\n a: Boolean; b: Boolean; c: Boolean;\nConstraints:\n # a or c or c#\n";
	
	static MediciCITGenerator medici = new MediciCITGenerator(); 
	
	@Test
	public void test1() throws Exception {
		String s = medici.getTestSuite(Utility.loadModel(model1), 2, false).toString();
		System.out.println("Risultato:\n"+s);
	}
	@Test
	public void test2() throws Exception {
		String s = medici.getTestSuite(Utility.loadModel(model2), 2, false).toString();
		System.out.println("Risultato:\n"+s);
	}
	@Test
	public void test3() throws Exception {
		String s = medici.getTestSuite(Utility.loadModel(model3), 2, false).toString();
		System.out.println("Risultato:\n"+s);
	}

}
