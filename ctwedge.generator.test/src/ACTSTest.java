import org.junit.Test;

import ctwedge.generator.util.Utility;

public class ACTSTest {

	@Test
	public void test1() throws Exception {
		System.out.println(Utility.getTestSuite(
				"Model prova\nParameters:\n a: Boolean; b: Boolean; c: Boolean;\nConstraints:\n # a -> b #\n", "acts",
				2, false));
	}

	@Test
	public void test2() throws Exception {
		System.out.println(Utility.getTestSuite(
				"Model prova\nParameters:\n a: Boolean; b: Boolean; c: {x, y}; \nConstraints:\n # a -> b #\n", "acts",
				3, true));
	}

	@Test
	public void test3() throws Exception {
		System.out.println(Utility.getTestSuite(
				"Model prova\nParameters:\n a: Boolean; b: Boolean; c: {x, y}; \nConstraints:\n # a -> b #\n# c=x #\n",
				"acts", 2, false));
	}

	@Test
	public void test4() throws Exception {
		System.out.println(Utility.getTestSuite(
				"Model prova\nParameters:\n a: Boolean; b: Boolean; c: {x, y}; d: [1..10];\nConstraints:\n # a -> b #\n# c=x #\n# d+2=10 #\n",
				"acts", 2, false));
	}

	@Test
	public void test5() {
		// System.out.println(Generator.getTestSuite("Model prova\nParameters:\n a:
		// Boolean; b: Boolean; c: {x, y}; \nConstraints:\n # a -> b #\n# c=x #\n",
		// null));
	}

	@Test
	public void testPhone() throws Exception {
		System.out.println(Utility.getTestSuite("Model Phone\n" + " Parameters:\n" + "   emailViewer : Boolean\n"
				+ "   textLines:  [ 25 .. 30 ]\n" + "   display : {16MC, 8MC, BW}\n" + "\n" + " Constraints:\n"
				+ "   # emailViewer => textLines > 28 #\n", "acts", 3, true));
	}
	
	@Test
	public void test6() throws Exception {
		System.out.println(Utility.getTestSuite(
				"Model equation\r\n" + 
				"\r\n" + 
				"Parameters:\r\n" + 
				"// numero di soluzioni\r\n" + 
				"	soluzione: { zero una due };\r\n" + 
				"	a: { -3 0 5 };\r\n" + 
				"	b: { -3 0 5 };\r\n" + 
				"	c: { -3 0 5 };\r\n" + 
				"\r\n" + 
				"Constraints:\r\n" + 
				"	# a!=0 # // vincolo aggiuntivo\r\n" + 
				"	# (soluzione==zero) => b*b-4*a*c<0 #\r\n" + 
				"	# (soluzione==una) => b*b-4*a*c==0 #\r\n" + 
				"	# (soluzione==due) => b*b-4*a*c>0 #\r\n" + 
				"", "acts",
				2, false));
	}

}
