package ctwedge.generator.medici;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ctwedge.ctWedge.CitModel;
import ctwedge.generator.util.Utility;
import ctwedge.util.ext.NotConvertableModel;

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
	public void testFolderWithTests() {
		testFilesInFolder("models_test/");
	}

	@Test
	public void testFolderWithBenchmarks() {
		testFilesInFolder("../../ctwedge.benchmarks");
	}


	private void testFilesInFolder(String string) {
		int errors = 0;
		String error_files = "";
		List<File> fileList = new ArrayList<>();
		listFiles(new File(string), fileList);
		for (File file : fileList) {
			try {
				System.out.println("*** modello " + file.getName());
				String translateModel = medici.translateModel(Utility.loadModelFromPath(file.getPath()), false);
				assertNotNull(translateModel);
			} catch (NotConvertableModel e) {
				errors++;
				error_files += file.getName() + "\n";
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
				fail("wrong exception ");				
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