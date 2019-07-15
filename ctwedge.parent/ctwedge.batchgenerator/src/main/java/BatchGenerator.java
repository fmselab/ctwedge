
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.io.PrintWriter;

import ctwedge.generator.casa.CASAConstraintException;
import ctwedge.generator.util.Utility;
public class BatchGenerator {
	final static String DIR_TODO = "C:\\Users\\ivanc\\Desktop\\Università\\Tesi\\BatchGenerator\\Todo";
	final static String DIR_DONE = "C:\\Users\\ivanc\\Desktop\\Università\\Tesi\\BatchGenerator\\Done";
	final static String DIR_RESULTS = "C:\\Users\\ivanc\\Desktop\\Università\\Tesi\\BatchGenerator\\Results";

	public static void main(String[] args) {
		long lastSec = 0;
		File todo = new File(DIR_TODO);

		while (true) {
			long sec = System.currentTimeMillis() / 1000;
			if (sec != lastSec) {
				File[] files = todo.listFiles();
				if (files.length > 0) {
					for (int i = 0; i < files.length; i++) {
						File file = files[i];
						String model = "";
						/*
						 * Formato nome file
						 * 
						 * yyyyMMddhhmmsslll_generator_strenght_ignore_name.ctw
						 * 
						 * dove lll = millisecondi
						 * 
						 * es: 20190712170930275_acts_2_false_Banking1.ctw
						 * 
						 */
						String[] name = file.getName().split("_");

						String timestamp = name[0];
						String generator = name[1];
						int strength = Integer.parseInt(name[2]);
						boolean ignoreCostraints = Boolean.parseBoolean(name[3]);
						String nome = name[4].substring(0, name[4].length() - 4);

						FileInputStream fis = null;
						try {
							fis = new FileInputStream(file);
							byte[] data = new byte[(int) file.length()];
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
							
							PrintWriter done = new PrintWriter(DIR_DONE + "/" + file.getName());
							PrintWriter results = new PrintWriter(DIR_RESULTS + "/" + timestamp + "_" + nome + ".csv");

							done.write(model);
							done.close();
							results.write(ts);
							results.close();

							file.delete();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} else {
					System.out.println("La cartella è vuota");
				}
				lastSec = sec;
			} // If():

		} // while()
	}
}