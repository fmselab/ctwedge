package ctwedge.batchgenerator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import ctwedge.generator.util.Utility;

public class ExecThread extends Thread{
	private final static String DIR_DONE = "C:\\Users\\ivanc\\Desktop\\Universita\\Tesi\\BatchGenerator\\Done";
	private final static String DIR_RESULTS = "C:\\Users\\ivanc\\Desktop\\Universita\\Tesi\\BatchGenerator\\Results";
	
	File file;
	int name;
	
	public ExecThread(File model, int i) {
		this.file = model;
		this.name = i;
	}
	
	@Override
	public void run() {
		System.out.println("Sono il thread " + this.name);
		System.out.println(BatchGenerator.doingList.toString());
		String model = "";
		String[] name = this.file.getName().split("_");

		String timestamp = name[0];
		String generator = name[1];
		int strength = Integer.parseInt(name[2]);
		boolean ignoreCostraints = Boolean.parseBoolean(name[3]);
		//String nome = name[4].substring(0, name[4].length() - 4);

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
			ts = Utility.getTestSuite(model, generator, strength, ignoreCostraints, "").toString();
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
			BatchGenerator.doingList.remove(this.file);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
