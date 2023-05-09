package ctwedge.generator.casa;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Test;

import ctwedge.util.ext.Utility;

public class CASATranslationTest {

	private int i;

	@Test
	public void test() throws Exception {
		ctwedge.ctWedge.CitModel citModel = Utility.loadModel(
				"Model Phone\nParameters:\n emailViewer : Boolean\n textLines:  [ 25 .. 30 ]\n display : {16MC, 8MC, BW}\n\n Constraints:\n  # textLines=28 #\n");
		// System.out.println(new CASATranslator().getTestSuite(m, 2, false));
		ToCasaParametersExporter mygen = new ToCasaParametersExporter();
		// get the model in casa
		CharSequence modelS = mygen.toCasaCode(citModel, 2);
		System.out.println(modelS);

		ConvertToAbstractID exporter = new ConvertToAbstractID(citModel);
		CharSequence constraints = exporter.translateConstraints(); // can throw exception
		assert constraints != null;
		System.out.println(constraints);
	}

	@Test
	public void traslateAllFiles() throws IOException {

		Logger.getLogger(ConvertToAbstractID.class).setLevel(Level.OFF);
		Path path = Paths.get("../../ctwedge.benchmarks/models_test");
		Files.walk(path).filter(Files::isRegularFile).map(Path::toFile).filter(x -> x.getName().endsWith(".ctw"))
				.forEach(x -> {
					System.out.print(x.getName());
					ctwedge.ctWedge.CitModel citModel = Utility.loadModelFromPath(x.getAbsolutePath());
					// System.out.println(new CASATranslator().getTestSuite(m, 2, false));
					ToCasaParametersExporter mygen = new ToCasaParametersExporter();
					// get the model in casa
					CharSequence modelS = mygen.toCasaCode(citModel, 2);
					assert modelS.length() > 0;
					ConvertToAbstractID exporter = new ConvertToAbstractID(citModel);
					CharSequence constraints;
					try {
						constraints = exporter.translateConstraints();// can throw exception
						assert constraints != null;
						System.out.println(" ok");
					} catch (Throwable e) {
						// TODO Auto-generated catch block
						System.out.println(" error " + e.getClass());
						i++;
					}
				});
		System.out.println("erros:" + i);

	}
}
