package ctwedge.generator.acts;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import ctwedge.ctWedge.CitModel;
import ctwedge.ctWedge.Constraint;
import ctwedge.ctWedge.NotExpression;
import ctwedge.util.ext.Utility;

public class ACTSConstraintTranslatorTest {

	@Test
	public void testACTSConstraintTranslator() {
		String filePath = "examples/bool1.ctw";
		CitModel ctmodel = Utility.loadModelFromPath(filePath);
		assertEquals(1, ctmodel.getConstraints().size());
		Constraint constraint = ctmodel.getConstraints().get(0);
		assertTrue(constraint instanceof NotExpression);
		NotExpression nc = (NotExpression) constraint;
		System.out.println(nc.getPredicate());
		ACTSConstraintTranslator trans = new ACTSConstraintTranslator(ctmodel);
		String res = trans.doSwitch(constraint);
		assertEquals("! (Par13)", res);
	}
	

	@Test(expected = RuntimeException.class)
	public void testACTSConstraintTranslatorError() {
		String filePath = "examples/error.ctw";
		CitModel ctmodel = Utility.loadModelFromPath(filePath);
	}

	@Test
	public void testACTSConstraintTranslator2() {
		
		String filePath = "examples/bool2.ctw";
		CitModel ctmodel = Utility.loadModelFromPath(filePath);
		assertEquals(1, ctmodel.getConstraints().size());
		Constraint constraint = ctmodel.getConstraints().get(0);
		ACTSTranslator.PRINT = false;
		ACTSConstraintTranslator trans = new ACTSConstraintTranslator(ctmodel);
		ACTSConstraintTranslator.PRINT = false;
		String res = trans.doSwitch(constraint);
		System.out.println("RES" + res.length());
		System.out.println("RES" + res.substring(0, 100));
		Map<Character, Integer> characterCounts = new HashMap<Character, Integer>();
		for (char character : res.toCharArray()) {
		    Integer characterCount = characterCounts.get(character);
		    if (characterCount == null) {
		        characterCount = 0;
		    }
		    characterCounts.put(character, characterCount + 1);
		}
		System.out.println(characterCounts);
		Map<String, Integer> characterStrings = new HashMap<String, Integer>();
		for (String s : res.split("\\(| |\\)")) {
		    Integer characterCount = characterStrings.get(s);
		    if (characterCount == null) {
		        characterCount = 0;
		    }
		    characterStrings.put(s, characterCount + 1);
		}
		System.out.println(characterStrings);
		System.out.println(characterStrings.keySet().size());
		characterStrings.keySet().forEach(x -> System.out.println("'" + x + "'"));
		// come intero modello
		
//		ACTSTranslator trans2 = new ACTSTranslator();
//		SUT sut = trans2.buildSUT(ctmodel,false,2);
//		System.out.println("CONSTR" + sut.getConstraints().get(0));
	}

}
