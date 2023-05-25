package ctwedge.benchmarks;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Files;

import org.junit.Test;

import ctwedge.benchmarks.importer.CASAImporter;

/** Imports CitLab models */
public class ImportModels {

	public static final String ISSTA11 = "models/ISSTA11/";
	public static final String EMSE10 = "models/EMSE10/benchmarks/";
	public static final String SPLC12 = "models/SPLC12/";
	public static final String Petke = "models/Petke/artifacts_updated/";
	public static String OUTPUT_FOLDER = "models/EMSE10/";
	@Test
	public void importModels() {
		String folder = EMSE10;
		File folderFile = new File(folder);
		File[] listOfFiles = folderFile.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			String model = null, name=listOfFiles[i].getName();
			if (listOfFiles[i].isFile() && listOfFiles[i].getName().endsWith("_2way.model")) {
				//System.out.println("File " + listOfFiles[i].getName());
				try {
					name = name.replace("_2way.model", "");
					model = Files.readString(listOfFiles[i].toPath()) + Files.readString(new File(folder+name+".constraints").toPath());
					//model = CitLabImporter.importFromCitlab(model);
					model = CASAImporter.instance.importFromCASA(name, model);
					OUTPUT_FOLDER = "models/EMSE10/";
				} catch (Exception e) {e.printStackTrace();}
			} else if (listOfFiles[i].isFile() && listOfFiles[i].getName().endsWith(".model")) {
				try {
					name = name.replace(".model", "");
					model = Files.readString(listOfFiles[i].toPath()) + Files.readString(new File(folder+name+".constraints").toPath());
					//model = CitLabImporter.importFromCitlab(model);
					model = CASAImporter.instance.importFromCASA(name, model);
					OUTPUT_FOLDER = "models/Petke/";
				} catch (Exception e) {e.printStackTrace();}
			}
			if (model!=null) try {
				writeToFile(OUTPUT_FOLDER+name+".ctw", model);
			} catch (Exception e) {e.printStackTrace();}
			
		}
	}
	
	
	public static void writeToFile(String filename, String content) throws Exception {
		PrintWriter fout = new PrintWriter(new FileWriter(filename));
		fout.print(content);
		fout.close();
	}
}
