package ctwedge.generator.cagen;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ctwedge.generator.util.Utility;

public class CAgenTest {
	
	@Test
	public void test() {
		try {
			String model = "Model Phone\r\n" + 
					" Parameters:\r\n" + 
					"   emailViewer : Boolean\r\n" + 
					"   textLines:  [ 25 .. 30 ]\r\n" + 
					"   display : {16MC, 8MC, BW}\r\n" + 
					"\r\n" + 
					" Constraints:\r\n" + 
					"   # emailViewer => textLines > 28 #";
			CAgenGenerator generator = new CAgenGenerator();
			//	Test con constraint
			generator.getTestSuite(Utility.loadModel(model), 2, false);
			//	Test senza constraint
			generator.getTestSuite(Utility.loadModel(model), 2, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test_bench() {
		try {
			String model = "Model Phone\r\n" + 
					" Parameters:\r\n" + 
					"   emailViewer : Boolean\r\n" + 
					"   textLines:  [ 25 .. 30 ]\r\n" + 
					"   display : {16MC, 8MC, BW}\r\n" + 
					"\r\n" + 
					" Constraints:\r\n" + 
					"   # emailViewer => textLines > 28 #";
			CAgenGenerator generator = new CAgenGenerator();
			System.out.println(generator.benchmark_run(Utility.loadModel(model)).getTests().size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	@Test
	public void testFolder() {
		int errors = 0;
		String error_files = "";
		CAgenGenerator generator = new CAgenGenerator();
		List<File> fileList = new ArrayList<>();
		listFiles(new File("models/"), fileList);
		for (File file : fileList) {
			String model;
			try {
				model = readFromFile(file);
				if (generator.getTestSuite(Utility.loadModel(model), 2, false) == null) {
					errors++;
					error_files += file.getName() + "\n";
				}
			} catch (Exception e) {
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
