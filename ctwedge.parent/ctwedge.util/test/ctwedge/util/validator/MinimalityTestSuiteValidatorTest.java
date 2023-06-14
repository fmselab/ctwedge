package ctwedge.util.validator;

import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import ctwedge.ctWedge.Bool;
import ctwedge.ctWedge.CitModel;
import ctwedge.ctWedge.CtWedgeFactory;
import ctwedge.ctWedge.Element;
import ctwedge.ctWedge.Enumerative;
import ctwedge.ctWedge.Parameter;

public class MinimalityTestSuiteValidatorTest {

	@Test
	public void testGetRequirementsBoolBool() {
		// build amodel
		CitModel model = CtWedgeFactory.eINSTANCE.createCitModel();
		// add two parameters bool
		Bool A = CtWedgeFactory.eINSTANCE.createBool();
		A.setName("A");
		Bool B = CtWedgeFactory.eINSTANCE.createBool();
		B.setName("B");
		model.getParameters().add(A);
		model.getParameters().add(B);		
		List<Map<Parameter, String>> list = MinimalityTestSuiteValidator.getRequirements(model, 2);
		System.out.println(list);
		String f1 = list.get(0).get(A);
		String f2 = list.get(1).get(A);
		System.out.println(f1);
		System.out.println(f2);
		assertSame(f1, f2);
	}
	@Test
	public void testGetRequirementsBoolEnum() {
		// build amodel
		CitModel model = CtWedgeFactory.eINSTANCE.createCitModel();
		// add two parameters bool
		Bool A = CtWedgeFactory.eINSTANCE.createBool();
		A.setName("A");
		Enumerative B = CtWedgeFactory.eINSTANCE.createEnumerative();
		B.setName("B");
		Element el1 = CtWedgeFactory.eINSTANCE.createElement(); el1.setName("el1"); 
		Element el2 = CtWedgeFactory.eINSTANCE.createElement(); el2.setName("el2"); 
		B.getElements().addAll(Arrays.asList(el1,el2));
		model.getParameters().add(A);
		model.getParameters().add(B);		
		List<Map<Parameter, String>> list = MinimalityTestSuiteValidator.getRequirements(model, 2);
		System.out.println(list);
		String f1 = list.get(0).get(B);
		String f2 = list.get(2).get(B);
		System.out.println(f1);
		System.out.println(f2);
		assertSame(f1, f2);
	}

}
