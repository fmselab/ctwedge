package ctwedge.batchgenerator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import ctwedge.generator.DiscoverGenerators;
import ctwedge.util.ext.Utility;

public class ExecThread extends Thread {
	private final static String DIR_DONE = "/var/www/foselab_html/ctwedge/done";
	private final static String DIR_RESULTS = "/var/www/foselab_html/ctwedge/results";

	File file;
	int name;

	public ExecThread(File model, int i) {
		this.file = model;
		this.name = i + 1;
	}

	@Override
	public void run() {
		System.out.println("Sono il thread " + this.name);
		//System.out.println(BatchGenerator.doingList.toString());
		String model = "";
		String[] name = this.file.getName().split("_");

		String timestamp = name[0];
		String generator = name[1];
		int strength = Integer.parseInt(name[2]);
		boolean ignoreCostraints = Boolean.parseBoolean(name[3]);
		// String nome = name[4].substring(0, name[4].length() - 4);

		FileInputStream fis = null;
		try {
			fis = new FileInputStream(this.file);
			byte[] data = new byte[(int) this.file.length()];
			fis.read(data);
			fis.close();
			model = new String(data, "UTF-8");
		} catch (FileNotFoundException e) {
			System.out.println("File non trovato!");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Errore");
			e.printStackTrace();
		}

		String ts = "";
		try {
			ts = Utility
					.getTestSuite(model, DiscoverGenerators.getGenerator(generator), strength, ignoreCostraints, "./")
					.toString().replace(";", ",");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {

			PrintWriter done = new PrintWriter(DIR_DONE + "/" + this.file.getName());
			PrintWriter results = new PrintWriter(DIR_RESULTS + "/" + timestamp + ".csv");

			done.write(model);
			done.close();
			results.write(ts);
			results.close();

			this.file.delete();
			synchronized (BatchGenerator.activeThreads) {
				BatchGenerator.doingList.remove(this.file);
				BatchGenerator.activeThreads--;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
