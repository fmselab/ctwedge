package ctwedge.util;

import static org.junit.Assert.*;

import org.apache.poi.ss.formula.functions.T;
import org.junit.Test;

import ctwedge.ctWedge.CitModel;
import ctwedge.util.ext.Utility;

public class TestSuiteTest {

	@Test
	public void testTestSuiteStringCitModel() {
		CitModel ctmodel = Utility.loadModel("Model al\r\n"
				+ " Parameters:\r\n"
				+ "   AmbientAssistedLiving : Boolean\r\n"
				+ "   SmartphoneType : Boolean \r\n"
				+ "   Android : Boolean\r\n"
				+ "   iPhone : Boolean");
		String tsCSV = "AmbientAssistedLiving;SmartphoneType;Android;iPhone;\n"
				+ "true;false;false;false;\n"
				+ "true;true;true;false;\n"
				+ "true;true;false;true;";
		TestSuite ts = new TestSuite(tsCSV, ctmodel);
		assertEquals(tsCSV,ts.toString());
		assertEquals("{AmbientAssistedLiving=true, Android=false, SmartphoneType=false, iPhone=false}",ts.getTests().get(0).toString());
	}

}
