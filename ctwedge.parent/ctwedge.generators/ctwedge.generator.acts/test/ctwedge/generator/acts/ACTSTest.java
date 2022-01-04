package ctwedge.generator.acts;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.junit.Test;

import ctwedge.generator.util.Utility;

public class ACTSTest {
	
	static ACTSTranslator acts = new ACTSTranslator();

	@Test
	public void test1() throws Exception {
		acts.getTestSuite(Utility.loadModel("Model prova\nParameters:\n a: Boolean; b: Boolean; c: Boolean;\nConstraints:\n # a -> b #\n"), 2, false);
	}

	@Test
	public void test2() throws Exception {
		System.out.println(Utility.getTestSuite(
				"Model prova\nParameters:\n a: Boolean; b: Boolean; c: {x, y}; \nConstraints:\n # a -> b #\n", acts,
				3, true, null));
	}

	@Test
	public void test3() throws Exception {
		System.out.println(Utility.getTestSuite("Model example1\nParameters:\nP1 : {V1, V2}\nP2 : {V1, V2}\nP3 : {V1, V2, V3}\n	Constraints:\n# P1 != P2 #\n# P3=V1 => P2=V2 #\n",
				acts, 2, false, null));
	}
	
	@Test
	public void test9() throws Exception {
		System.out.println(Utility.getTestSuite(
				"Model prova\nParameters:\n a: Boolean; b: Boolean; c: {x, y}; \nConstraints:\n # a -> b #\n# c=x #\n",
				acts, 2, false, null));
	}

	@Test
	public void test4() throws Exception {
		System.out.println(Utility.getTestSuite(
				"Model prova\nParameters:\n a: Boolean; b: Boolean; c: {x, y}; d: [1..10];\nConstraints:\n # a -> b #\n# c=x #\n# d+2=10 #\n",
				acts, 2, false, null));
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
				+ "   # emailViewer => textLines > 28 #\n", acts, 3, true, null));
	}

	@Test
	public void test6() throws Exception {
		System.out.println(
				Utility.getTestSuite("Model equation\r\n" + "\r\n" + "Parameters:\r\n" + "// numero di soluzioni\r\n"
						+ "	soluzione: { zero una due };\r\n" + "	a: { -3 0 5 };\r\n" + "	b: { -3 0 5 };\r\n"
						+ "	c: { -3 0 5 };\r\n" + "\r\n" + "Constraints:\r\n" + "	# a!=0 # // vincolo aggiuntivo\r\n"
						+ "	# (soluzione==zero) => b*b-4*a*c<0 #\r\n" + "	# (soluzione==una) => b*b-4*a*c==0 #\r\n"
						+ "	# (soluzione==due) => b*b-4*a*c>0 #\r\n" + "", acts, 2, false, null));
	}

	@Test
	public void test7() throws Exception {
		System.out.println(Utility.getTestSuite("Model Magazzino\r\n" + "\r\n" + "Parameters:\r\n"
				+ "productIndex: {0, 3, 6}\r\n" + "addQuantity: {0, 1, 5, 10, 11}\r\n" + "returnedValue: Boolean\r\n"
				+ "nproductsOld: {0, 50, 90, 95, 100}\r\n" + "\r\n" + "Constraints:\r\n"
				+ "# returnedValue <=> (productIndex>0 and productIndex<6 and addQuantity>0 and addQuantity<11 and (nproductsOld + addQuantity <= 100)) #\r\n"
				+ "", acts, 2, false, null));
	}

	@Test
	public void test8() throws Exception {
		System.out.println(Utility.getTestSuite("Model Magazzino\r\n" + "\r\n" + "Parameters:\r\n" + "a: Boolean\r\n"
				+ "b: Boolean\r\n" + "Constraints:\r\n" + "# a == b #\r\n", acts, 2, false, null));
	}

	@Test
	public void testExample1() throws Exception {
		StringBuilder contentBuilder = new StringBuilder();
		String filePath = "examples/scommesse.ctw";
		Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8);
		stream.forEach(s -> contentBuilder.append(s).append("\n"));
		stream.close();
		System.out.println(Utility.getTestSuite(contentBuilder.toString(), acts,2,false, null));
	}
	
	@Test
	public void testExample2() throws Exception {
		StringBuilder contentBuilder = new StringBuilder();
		String filePath = "../../ctwedge.benchmarks/models_test/fse18/model_112.ctw";
		Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8);
		stream.forEach(s -> contentBuilder.append(s).append("\n"));
		stream.close();
		System.out.println(Utility.getTestSuite(contentBuilder.toString(), acts,2,false, null));
	}
	

}
