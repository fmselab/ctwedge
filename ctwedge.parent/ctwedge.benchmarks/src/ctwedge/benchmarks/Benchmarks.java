package ctwedge.benchmarks;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import org.junit.Test;

import ctwedge.ctWedge.CitModel;
import ctwedge.generator.util.Utility;

/**
 * Servlet implementation class for the benchmark table
 */
public class Benchmarks {

	@Test
	public void printCSV() {
		System.out.println(generateCSV());
	}

	public String generateCSV() {
		return "Model;Paper;# parameters;# constraints\n" + crawlDir(new File("models/"));
	}

	public String crawlDir(File folder) {
		StringBuilder st = new StringBuilder();
		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()
					&& (listOfFiles[i].getName().endsWith(".citw") || listOfFiles[i].getName().endsWith(".ctw"))) {
				// System.out.println("File " + listOfFiles[i].getName());
				try {
					CitModel m = Utility.loadModelFromPath(listOfFiles[i].getAbsolutePath());
					st.append(listOfFiles[i].getName().replace(".citw", "").replace(".ctw", "") + ";" + folder.getName()
							+ ";" + m.getParameters().size() + ";" + m.getConstraints().size());
				} catch (Exception e) {
					e.printStackTrace();
				}
				st.append("\n");
			} else if (listOfFiles[i].isDirectory()) {
				st.append(crawlDir(listOfFiles[i]));
			}
		}
		return st.toString();
	}

	public static String addNumbers(String ts) {
		StringBuffer sb = new StringBuffer();
		String[] st = ts.split("\n");
		sb.append("#;" + st[0] + "\n");
		for (int i = 1; i < st.length; i++) {
			sb.append(i + ";" + st[i] + "\n");
		}
		return sb.toString();
	}

	public static void writeToFile(String filename, String content) throws Exception {
		PrintWriter fout = new PrintWriter(new FileWriter(filename));
		fout.print(content);
		fout.close();
	}
}
