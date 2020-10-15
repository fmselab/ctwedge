
import ctwedge.generator.pict.PICTGenerator;
import ctwedge.generator.util.Utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;


public class PictTest {
	
	@Test
	public void test() {
		try {
			String model = "Model Concurrency\r\n" + 
					"\r\n" + 
					"Parameters:\r\n" + 
					"p1: { v1 v2 };\r\n" + 
					"p2: { v1 v2 };\r\n" + 
					"p3: { v1 v2 };\r\n" + 
					"p4: Boolean;\r\n" + 
					"p5: Boolean;\r\n" + 
					"\r\n" + 
					"Constraints:\r\n" + 
					"	# ( p3!=v1 OR p2!=v1 OR p5 OR p4 OR p1!=v1) #\r\n" + 
					"	# ( p1!=v2 OR p5!=true) #\r\n" + 
					"	# ( p2!=v1 OR p5 OR p4!=true OR p3!=v2 OR p1!=v1) #\r\n" + 
					"	# ( p5!=true OR p2!=v2) #\r\n" + 
					"	# ( p4 OR p3!=v2 OR p1!=v1) #\r\n" + 
					"	# ( p4!=true OR p1!=v2) #\r\n" + 
					"	# ( p3!=v1 OR p4!=true) #";
			PICTGenerator generator = new PICTGenerator();
			//	Test con constraint
			generator.getTestSuite(Utility.loadModel(model), 2, false);
			//	Test senza constraint
			generator.getTestSuite(Utility.loadModel(model), 2, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testFolder() {
		PICTGenerator generator = new PICTGenerator();
		List<File> fileList = new ArrayList<>();
		listFiles(new File("models/"), fileList);
		for (File file : fileList) {
			String model;
			try {
				model = readFromFile(file);
				generator.getTestSuite(Utility.loadModel(model), 2, false);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
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
