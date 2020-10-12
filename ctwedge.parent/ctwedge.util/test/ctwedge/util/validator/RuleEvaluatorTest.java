package ctwedge.util.validator;

import java.util.HashMap;

import ctwedge.ctWedge.CitModel;
import ctwedge.generator.util.Utility;
import ctwedge.util.Test;

/**
 * @author marco radavelli
 */
public class RuleEvaluatorTest {
	
	@org.junit.Test
	public void test1() {
		CitModel model = Utility.loadModel("Model example Parameters: a:Boolean; b:Boolean; c:Boolean Constraints: # !(a and b) #");
		Test t1 = new Test(new HashMap<String,String>() {{put("a","true"); put("b","true"); put("c","true");}});
		Test t2 = new Test(new HashMap<String,String>() {{put("a","false"); put("b","true"); put("c","true");}});
		System.out.println(new RuleEvaluator(t1).evaluateConstraint(model.getConstraints().get(0)));
		System.out.println(new RuleEvaluator(t2).evaluateConstraint(model.getConstraints().get(0)));
	}
	
	@org.junit.Test
	public void test2() {
		CitModel model = Utility.loadModel("Model example Parameters: a:Boolean; b:Boolean; c:Boolean; d:{d1 d2 d3}; Constraints: # !(a and b and d==d3) #");
		Test t1 = new Test(new HashMap<String,String>() {{put("a","true"); put("b","true"); put("c","true"); put("d","d3");}});
		Test t2 = new Test(new HashMap<String,String>() {{put("a","false"); put("b","true"); put("c","true"); put("d","d3");}});
		System.out.println(new RuleEvaluator(t1).evaluateConstraint(model.getConstraints().get(0)));
		System.out.println(new RuleEvaluator(t2).evaluateConstraint(model.getConstraints().get(0)));
	}
}
