package ctwedge.generator.casa;
import org.junit.Assert;
import org.junit.Test;

import ctwedge.generator.util.Utility;

public class CASATest {
	
	static CASATranslator casa = new CASATranslator(); 
	
	@Test
	public void test1() throws Exception {
		String s = casa.getTestSuite(Utility.loadModel("Model prova\nParameters:\n a: Boolean; b: Boolean; c: Boolean;\nConstraints:\n # a #\n"), 2, false).toString();
		System.out.println("Risultato:\n"+s);
	}
	
	@Test
	public void test2() throws Exception {
		String s = Utility.getTestSuite("Model prova\nParameters:\n a: Boolean; b: Boolean; c: Boolean;\nConstraints:\n # a==false #\n", casa, 2, false, null).toString();
		
		System.out.println("Risultato:\n"+s);
	}
	
	@Test
	public void test3() throws Exception {
		String s = Utility.getTestSuite("Model prova\nParameters:\n a: Boolean; b: Boolean; c: Boolean;\nConstraints:\n # !a and c #\n", casa, 2, false, null).toString();
		
		System.out.println("Risultato:\n"+s);
	}
	
	@Test
	public void test4() throws Exception {
		String s = Utility.getTestSuite("Model prova\nParameters:\n a: Boolean; b: Boolean; c: Boolean; d: {x, y};\nConstraints:\n # !a and c or d==x #\n", casa, 2, false, null).toString();
		
		System.out.println("Risultato:\n"+s);
	}

	@Test
	public void test5() throws Exception {
		try {
			Utility.getTestSuite("Model prova\nParameters:\n a: Boolean; b: Boolean; c: Boolean; d: {x, y}; Range e: [1..10]; \nConstraints:\n # !a and c or d==x and e>5 #\n", casa, 2, false, null);
		} catch (CASAConstraintException e) {
			System.out.println("OK: eccezione lanciata");
			return;
		}
		Assert.fail();
	}
	
	/** test dell'uguale nel modello Phone di esempio */
	@Test
	public void test6() throws Exception {
		String s = Utility.getTestSuite("Model Phone\nParameters:\n emailViewer : Boolean\n textLines:  [ 25 .. 30 ]\n display : {16MC, 8MC, BW}\n\n Constraints:\n  # emailViewer => textLines=28 #\n", casa, 2, false, null).toString();		
		System.out.println("Risultato:\n"+s);
	}
	
	/** test dell'uguale nel modello Phone di esempio no constraint */
	@Test
	public void test7() throws Exception {
		String s = Utility.getTestSuite("Model Phone\nParameters:\n emailViewer : Boolean\n textLines:  [ 25 .. 30 ]\n display : {16MC, 8MC, BW}\n\n Constraints:\n  # emailViewer => textLines=28 #\n", casa, 2, true, null).toString();		
		System.out.println("Risultato:\n"+s);
	}

}
