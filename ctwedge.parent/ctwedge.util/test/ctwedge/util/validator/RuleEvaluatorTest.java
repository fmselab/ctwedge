package ctwedge.util.validator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import ctwedge.ctWedge.CitModel;
import ctwedge.util.ModelUtils;
import ctwedge.util.Test;
import ctwedge.util.ext.Utility;

/**
 * @author marco radavelli
 */
public class RuleEvaluatorTest {
	

	@SuppressWarnings("serial")
	@org.junit.Test
	public void test1() {
		CitModel model = Utility.loadModel("Model example Parameters: a:Boolean; b:Boolean; c:Boolean Constraints: # !(a and b) #");
		Test t1 = new Test(new HashMap<String,String>() {{put("a","true"); put("b","true"); put("c","true");}});
		// false
		assertFalse(new RuleEvaluator(t1).evaluateModel(model));
		// true
		Test t2 = new Test(new HashMap<String,String>() {{put("a","false"); put("b","true"); put("c","true");}});
		assertTrue(new RuleEvaluator(t2).evaluateModel(model));
	}
	
	@SuppressWarnings("serial")
	@org.junit.Test
	public void test2() {
		CitModel model = Utility.loadModel("Model example Parameters: a:Boolean; b:Boolean; d:{d1 d2 d3}; Constraints: # !(a and b and d==d3) #");
		Test t1 = new Test(new HashMap<String,String>() {{put("a","true"); put("b","true"); put("d","d3");}});
		assertFalse(new RuleEvaluator(t1).evaluateModel(model));
		Test t2 = new Test(new HashMap<String,String>() {{put("a","false"); put("b","true"); put("d","d3");}});
		assertTrue(new RuleEvaluator(t2).evaluateModel(model));
		Test t3 = new Test(new HashMap<String,String>() {{put("a","true"); put("b","true"); put("d","d2");}});
		assertTrue(new RuleEvaluator(t3).evaluateModel(model));
	}
	@org.junit.Test
	public void test3() {
		CitModel model = Utility.loadModel("Model example Parameters: a:Boolean; b:Boolean; d:{d1 d2 d3}; Constraints: # d!=d3 #");
		Test t1 = new Test(new HashMap<String,String>() {{put("a","true"); put("b","true"); put("d","d3");}});
		assertFalse(new RuleEvaluator(t1).evaluateModel(model));
		Test t3 = new Test(new HashMap<String,String>() {{put("a","true"); put("b","true"); put("d","d2");}});
		assertTrue(new RuleEvaluator(t3).evaluateModel(model));
	}
	// with numbers
	@org.junit.Test
	public void test4() {
		CitModel model = Utility.loadModel("Model Phone\n"
				+ " Parameters:\n"
				+ "   emailViewer : Boolean\n"
				+ "   textLines:  [ 25 .. 30 ]\n"
				+ " Constraints:\n"
				+ "   # emailViewer => textLines > 28 #");
		Test t1 = new Test(new HashMap<String,String>() {{put("emailViewer","true"); put("textLines","15");}});
		assertFalse(new RuleEvaluator(t1).evaluateModel(model));
		Test t2 = new Test(new HashMap<String,String>() {{put("emailViewer","false"); put("textLines","15");}});
		assertTrue(new RuleEvaluator(t2).evaluateModel(model));
		Test t3 = new Test(new HashMap<String,String>() {{put("emailViewer","true"); put("textLines","30");}});
		assertTrue(new RuleEvaluator(t3).evaluateModel(model));
	}
	
	@org.junit.Test
	public void testbenchmarks() throws IOException {
		Path path = Paths.get("../ctwedge.benchmarks/models_test");
		testFile(path);
	}
	
	@org.junit.Test
	public void testComp() throws IOException {
		Path path = Paths.get("../../../CIT_Benchmark_Generator\\Benchmarks_CITCompetition_2023\\");
		testFile(path);
	}
	
	private static final int NUM_TEST = 10;

	private void testFile(Path path) throws IOException {
		List<File> fileList = Files.walk(path)
                .filter(Files::isRegularFile)
                .map(Path::toFile)
                .filter(x -> x.getName().endsWith(".ctw"))
                .collect(Collectors.toList());
		for (File file : fileList) {
			CitModel model = Utility.loadModelFromPath(file.getCanonicalPath());
			ModelUtils mu = new ModelUtils(model);
			for (int i = 0; i < NUM_TEST; i++) {
				System.out.print(file.getCanonicalPath());
				Test test = mu.getRandomTestFromModel();
				System.out.print(" " + test);
				Boolean value = new RuleEvaluator(test).evaluateModel(model);
				System.out.println(" " + value);
			}
		}
	}
	
	
	
	
	
	
	
}
