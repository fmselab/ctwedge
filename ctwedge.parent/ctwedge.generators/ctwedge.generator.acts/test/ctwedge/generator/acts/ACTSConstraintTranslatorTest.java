package ctwedge.generator.acts;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ctwedge.ctWedge.CitModel;
import ctwedge.ctWedge.Constraint;
import ctwedge.ctWedge.NotExpression;
import ctwedge.generator.util.Utility;

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

}
