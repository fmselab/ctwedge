package ctwedge.generator.casa;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import ctwedge.generator.util.Utility;

public class CASATest {
	
	static CASATestGenerator casa = new CASATestGenerator(); 
	
	@Test
	public void test1() throws Exception {
		String s = casa.getTestSuite(Utility.loadModel("Model prova\nParameters:\n a: Boolean; b: Boolean; c: Boolean;\nConstraints:\n # a #\n"), 2, false).toString();
		System.out.println("Risultato:\n"+s);
	}
	
	@Test
	public void test2() throws Exception {
		String s = Utility.getTestSuite("Model prova\nParameters:\n a: Boolean; b: Boolean; c: Boolean;\nConstraints:\n # a==false #\n", casa, 2, false, null).toString();
		
		System.out.println("Risultato:\n"+s);
	}
	
	@Test
	public void test3() throws Exception {
		String s = Utility.getTestSuite("Model prova\nParameters:\n a: Boolean; b: Boolean; c: Boolean;\nConstraints:\n # !a and c #\n", casa, 2, false, null).toString();
		
		System.out.println("Risultato:\n"+s);
	}
	
	@Test
	public void test4() throws Exception {
		String s = Utility.getTestSuite("Model prova\nParameters:\n a: Boolean; b: Boolean; c: Boolean; d: {x, y};\nConstraints:\n # !a and c or d==x #\n", casa, 2, false, null).toString();
		
		System.out.println("Risultato:\n"+s);
	}

	@Test
	public void test5() throws Exception {
		try {
			Utility.getTestSuite("Model prova\nParameters:\n a: Boolean; b: Boolean; c: Boolean; d: {x, y}; Range e: [1..10]; \nConstraints:\n # !a and c or d==x and e>5 #\n", casa, 2, false, null);
		} catch (CASAConstraintException e) {
			System.out.println("OK: eccezione lanciata");
			return;
		}
		Assert.fail();
	}
	
	/** test dell'uguale nel modello Phone di esempio */
	@Test
	public void test6() throws Exception {
		String s = Utility.getTestSuite("Model Phone\nParameters:\n emailViewer : Boolean\n textLines:  [ 25 .. 30 ]\n display : {16MC, 8MC, BW}\n\n Constraints:\n  # emailViewer => textLines=28 #\n", casa, 2, false, null).toString();		
		System.out.println("Risultato:\n"+s);
	}
	
	/** test dell'uguale nel modello Phone di esempio no constraint */
	@Test
	public void test7() throws Exception {
		String s = Utility.getTestSuite("Model Phone\nParameters:\n emailViewer : Boolean\n textLines:  [ 25 .. 30 ]\n display : {16MC, 8MC, BW}\n\n Constraints:\n  # emailViewer => textLines=28 #\n", casa, 2, true, null).toString();		
		System.out.println("Risultato:\n"+s);
	}
	
	@Test
	public void convertCTComp() throws IOException {
		Logger.getLogger(ConvertToAbstractID.class).setLevel(Level.OFF);
		Path path = Paths.get("C:\\Users\\Andrea_PC\\Desktop\\CTComp\\CTComp\\");
		Files.walk(path).filter(Files::isRegularFile).map(Path::toFile).filter(x -> x.getName().endsWith(".ctw"))
				.forEach(x -> {
					System.out.print(x.getName());
					ctwedge.ctWedge.CitModel citModel = Utility.loadModelFromPath(x.getAbsolutePath());
					// System.out.println(new CASATranslator().getTestSuite(m, 2, false));
					ToCasaParametersExporter mygen = new ToCasaParametersExporter();
					BufferedWriter out;
					try {
						out = new BufferedWriter(new FileWriter("C:\\Users\\Andrea_PC\\Desktop\\CTComp\\CTComp\\" + citModel.getName() + ".citmodel"));
						// get the model in casa
						CharSequence modelS = mygen.toCasaCode(citModel, 2);
						out.append(modelS);
						out.close();
						assert modelS.length() > 0;
						ConvertToAbstractID exporter = new ConvertToAbstractID(citModel);
						CharSequence constraints;
						try {
							constraints = exporter.translateConstraints();// can throw exception
							assert constraints != null;
							out = new BufferedWriter(new FileWriter("C:\\Users\\Andrea_PC\\Desktop\\CTComp\\CTComp\\" + citModel.getName() + ".constraints"));
							out.append(constraints);
							out.close();
							System.out.println(" ok");
						} catch (Throwable e) {
							// TODO Auto-generated catch block
							System.out.println(" error " + e.getClass() + " - " + e.getStackTrace());
						}
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				});
	}

}
