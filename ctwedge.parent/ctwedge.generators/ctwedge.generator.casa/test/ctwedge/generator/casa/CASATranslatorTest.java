package ctwedge.generator.casa;
import org.junit.Test;

import ctwedge.generator.casa.ConvertToAbstractID;
import ctwedge.generator.casa.ToCasaParametersExporter;
import ctwedge.generator.util.Utility;

public class CASATranslatorTest {
	
	@Test
	public void test() throws Exception {
		ctwedge.ctWedge.CitModel citModel = Utility.loadModel("Model Phone\nParameters:\n emailViewer : Boolean\n textLines:  [ 25 .. 30 ]\n display : {16MC, 8MC, BW}\n\n Constraints:\n  # textLines=28 #\n");	
		//System.out.println(new CASATranslator().getTestSuite(m, 2, false));
		ToCasaParametersExporter mygen = new ToCasaParametersExporter();
		// get the model in casa
		CharSequence modelS = mygen.toCasaCode(citModel, 2);
		System.out.println(modelS);

		ConvertToAbstractID exporter = new ConvertToAbstractID(citModel);
		CharSequence constraints = exporter.translateConstraints(); // can throw exception
		assert constraints != null;
		System.out.println(constraints);
	}
}
