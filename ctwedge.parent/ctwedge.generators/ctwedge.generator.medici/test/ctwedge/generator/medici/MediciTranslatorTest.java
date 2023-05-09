package ctwedge.generator.medici;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ctwedge.ctWedge.CitModel;
import ctwedge.util.NotConvertableModel;
import ctwedge.util.ext.Utility;

public class MediciTranslatorTest {

	static MediciCITGenerator medici = new MediciCITGenerator(); 

	@Test(expected = NotConvertableModel.class)
	public void testEnumEQ_ENUMS() {
		// EQ or NEQ between two enumertaive paramters 
		CitModel loadModelFromPath = Utility.loadModelFromPath("models_test/model_112.ctw");
		assertNotNull(loadModelFromPath);
		medici.translateModel(loadModelFromPath, false);
	}
	
	@Test
	public void testSimpleModel() {
		CitModel loadModelFromPath = Utility.loadModelFromPath("models_test/model_112.ctw");
		assertNotNull(loadModelFromPath);
		medici.translateModel(loadModelFromPath, false);
	}	
	
	private static final String model1 = "Model Phone\r\n" + 
			" Parameters:\r\n" + 
			"   emailViewer : Boolean\r\n" + 
			"   textLines:  [ 25 .. 30 ]\r\n" + 
			"\r\n" + 
			" Constraints:\r\n" + 
			"   # emailViewer != TRUE #";
	
	@Test
	public void testSimpleModelWTRUE() {
		CitModel loadModelFromPath = Utility.loadModel(model1);
		assertNotNull(loadModelFromPath);
		String s = medici.translateModel(loadModelFromPath, false);
		System.out.println(s);
		assertFalse(s.contains("-1"));
	}
	@Test
	public void testFolderWithTests() {
		testFilesInFolder("models_test/", false);
	}

	@Test
	public void testFolderWithBenchmarks() {
		testFilesInFolder("../../ctwedge.benchmarks", false);
	}
	
	@Test
	public void testFolderForCompetition() {
		testFilesInFolder("/Users/andrea/Downloads/CTWedge", true);
	}
	


	private void testFilesInFolder(String string, boolean createFile) {
		int errors = 0;
		String error_files = "";
		List<File> fileList = new ArrayList<>();
		listFiles(new File(string), fileList);
		for (File file : fileList) {
			try {
				System.out.println("*** modello " + file.getName());
				String translateModel = medici.translateModel(Utility.loadModelFromPath(file.getPath()), false);
				assertNotNull(translateModel);
				if (createFile) {
					String path = file.getParent();
					File newFile = new File(path + "/" + file.getName() + ".medici");
					newFile.createNewFile();
					FileWriter writer = new FileWriter(newFile);
					writer.write(translateModel);
					writer.close();
				}
			} catch (NotConvertableModel e) {
				errors++;
				error_files += file.getName() + "\n";
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
				errors++;
				//fail("wrong exception ");				
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
}
