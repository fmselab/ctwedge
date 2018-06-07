package ctwedge.benchmarks;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

import org.junit.Test;

import ctwedge.generator.util.CASAImporter;

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
					model = readFromFile(listOfFiles[i]) + readFromFile(new File(folder+name+".constraints"));
					//model = CitLabImporter.importFromCitlab(model);
					model = CASAImporter.instance.importFromCASA(name, model);
					OUTPUT_FOLDER = "models/EMSE10/";
				} catch (Exception e) {e.printStackTrace();}
			} else if (listOfFiles[i].isFile() && listOfFiles[i].getName().endsWith(".model")) {
				try {
					name = name.replace(".model", "");
					model = readFromFile(listOfFiles[i]) + readFromFile(new File(folder+name+".constraints"));
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
	
	
	public static String readFromFile(File f) throws Exception {
		StringBuilder sb = new StringBuilder();
		BufferedReader fin = new BufferedReader(new FileReader(f));
		String s="";
		while ((s=fin.readLine())!=null) sb.append(s+"\n");
		fin.close();
		return sb.toString();
	}
	
	public static void writeToFile(String filename, String content) throws Exception {
		PrintWriter fout = new PrintWriter(new FileWriter(filename));
		fout.print(content);
		fout.close();
	}
}
