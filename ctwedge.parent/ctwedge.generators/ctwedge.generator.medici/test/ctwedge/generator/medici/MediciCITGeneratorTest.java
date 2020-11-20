package ctwedge.generator.medici;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ctwedge.ctWedge.CitModel;
import ctwedge.generator.util.Utility;
import ctwedge.util.TestSuite;

public class MediciCITGeneratorTest {

	private static final String model1 = "Model prova\nParameters:\n a: Boolean; b: Boolean; c: Boolean;\nConstraints:\n # a #\n";
	private static final String model2 = "Model prova\nParameters:\n a: Boolean; b: Boolean; c: Boolean;\nConstraints:\n # not a #\n";
	private static final String model3 = "Model prova\nParameters:\n a: Boolean; b: Boolean; c: Boolean;\nConstraints:\n # a or c or c#\n";
	
	private static final String model4 = "Model: model_0\n" + 
			"Parameters:\n" + 
			"A0: { V0,V4,V1,V2,V3 }\n" + 
			"A2: { V0,V1,V2,V3 }\n" + 
			"A3: { V0,V1,V2,V3,V4,V5,V6,V7,V8 }\n" + 
			"A4: { V0,V1,V2 }\n" + 
			"\n" + 
			"Constraints:\n" + 
			"# ((A0!=V4) || A2=V3) #\n";
	
	private static final String modelPhone = "Model Phone\r\n" + 
			" Parameters:\r\n" + 
			"   emailViewer : Boolean\r\n" + 
			"   textLines:  [ 25 .. 30 ]\r\n" + 
			"   display : {16MC, 8MC, BW}\r\n" + 
			"\r\n" + 
			" Constraints:\r\n" + 
			"   # emailViewer => textLines > 28 #";

	private static final String modelPhone2 = "Model Phone\r\n" + 
			" Parameters:\r\n" + 
			"   emailViewer : Boolean\r\n" + 
			"   textLines:  [ 25 .. 30 ]\r\n" + 
			"   display : {16MC, 8MC, BW}\r\n" + 
			"\r\n" + 
			" Constraints:\r\n" + 
			"   # emailViewer => textLines = 28 #";

	
	static MediciCITGenerator medici = new MediciCITGenerator(); 
	
	@Test
	public void test1() throws Exception {
		String s = medici.getTestSuite(Utility.loadModel(model1), 2, false).toString();
		System.out.println("Risultato:\n"+s);
	}
	@Test
	public void test2() throws Exception {
		String s = medici.getTestSuite(Utility.loadModel(model2), 2, false).toString();
		System.out.println("Risultato:\n"+s);
	}
	@Test
	public void test3() throws Exception {
		String s = medici.getTestSuite(Utility.loadModel(model3), 2, false).toString();
		System.out.println("Risultato:\n"+s);
	}

	@Test
	public void test4() throws Exception {
		String s = medici.getTestSuite(Utility.loadModel(model4), 2, false).toString();
		System.out.println("Risultato:\n"+s);
	}
	
	@Test(expected = RuntimeException.class)
	public void test_phone() throws Exception {
		medici.getTestSuite(Utility.loadModel(modelPhone), 2, false).toString();
	}
	
	@Test
	public void test_phoneCorrect() throws Exception {
		String s = medici.getTestSuite(Utility.loadModel(modelPhone2), 2, false).toString();
		System.out.println("Risultato:\n"+s);
	}
	
	@Test
	public void test_Storage5() throws Exception {
		CitModel loadModel = Utility.loadModel("../../models_test/ctwedge.benchmarks/Storage5.ctw");
		String s = medici.getTestSuite(loadModel, 2, false).toString();
		System.out.println("Risultato:\n"+s);
	}
		
	//	Usare Junit Plug-In test
	@Test
	public void test_bench() {
		try {
			MediciCITGenerator generator = new MediciCITGenerator();
			TestSuite ts = generator.benchmark_run(Utility.loadModel(model4));
			System.out.println(ts.getTests().size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	@Test
	public void testFolder() {
		int errors = 0;
		String error_files = "";
		MediciCITGenerator generator = new MediciCITGenerator();
		List<File> fileList = new ArrayList<>();
		listFiles(new File("models_test/"), fileList);
		for (File file : fileList) {
			String model;
			try {
				model = readFromFile(file);
				if (generator.getTestSuite(Utility.loadModel(model), 2, false) == null) {
					errors++;
					error_files += file.getName() + "\n";
				}
			} catch (Exception e) {
				errors++;
				error_files += file.getName() + "\n";
				e.printStackTrace();
			}
		}
		System.out.println("******\n"
				+ "Test eseguiti: " + fileList.size() + "\n"
				+ "Errori: " + errors);
		System.out.println("******\n"
				+ "File con errore: \n" + error_files);
	}
	
	public void listFiles(File folder, List<File> fileList) {
		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()	&& (listOfFiles[i].getName().endsWith(".citw") || listOfFiles[i].getName().endsWith(".ctw"))) {
				fileList.add(listOfFiles[i]);
			} else if (listOfFiles[i].isDirectory()) {
				listFiles(listOfFiles[i], fileList);
			}
		}
	}
	
	public static String readFromFile(File f) throws Exception {
		StringBuilder sb = new StringBuilder();
		BufferedReader fin = new BufferedReader(new FileReader(f));
		String s = "";
		while ((s = fin.readLine()) != null)
			sb.append(s + "\n");
		fin.close();
		return sb.toString();
	}

}
