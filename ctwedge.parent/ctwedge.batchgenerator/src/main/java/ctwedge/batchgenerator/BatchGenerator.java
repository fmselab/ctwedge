package ctwedge.batchgenerator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.plaf.SliderUI;

import ctwedge.generator.casa.CASAConstraintException;
import ctwedge.generator.util.Utility;

public class BatchGenerator {
	private final static String DIR_TODO = "C:\\Users\\ivanc\\Desktop\\Universita\\Tesi\\BatchGenerator\\Todo";
	//private final static String DIR_DOING = "C:\\Users\\ivanc\\Desktop\\Universita\\Tesi\\BatchGenerator\\Doing";

	private final static String DIR_DONE = "C:\\Users\\ivanc\\Desktop\\Universita\\Tesi\\BatchGenerator\\Done";
	private final static String DIR_RESULTS = "C:\\Users\\ivanc\\Desktop\\Universita\\Tesi\\BatchGenerator\\Results";

	public static ArrayList<File> doingList = new ArrayList<File>();

	public static void main(String[] args) {
		long lastSec = 0;
		File todo = new File(DIR_TODO);

		while (true) {
			/*
			 * long sec = System.currentTimeMillis() / 1000; if (sec != lastSec) { File[]
			 * files = todo.listFiles(); if (files.length > 0) { for (int i = 0; i <
			 * files.length; i++) { File file = files[i]; String model = "";
			 * 
			 * String[] name = file.getName().split("_");
			 * 
			 * String timestamp = name[0]; String generator = name[1]; int strength =
			 * Integer.parseInt(name[2]); boolean ignoreCostraints =
			 * Boolean.parseBoolean(name[3]); //String nome = name[4].substring(0,
			 * name[4].length() - 4);
			 * 
			 * FileInputStream fis = null; try { fis = new FileInputStream(file); byte[]
			 * data = new byte[(int) file.length()]; fis.read(data); fis.close(); model =
			 * new String(data, "UTF-8"); } catch (FileNotFoundException e) {
			 * System.out.println("File non trovato!"); e.printStackTrace(); } catch
			 * (IOException e) { System.out.println("Errore"); e.printStackTrace(); }
			 * 
			 * String ts = ""; try { ts = Utility.getTestSuite(model, generator, strength,
			 * ignoreCostraints, "").toString(); } catch (Exception e) { // TODO
			 * Auto-generated catch block e.printStackTrace(); }
			 * 
			 * try {
			 * 
			 * PrintWriter done = new PrintWriter(DIR_DONE + "/" + file.getName());
			 * PrintWriter results = new PrintWriter(DIR_RESULTS + "/" + timestamp +
			 * ".csv");
			 * 
			 * done.write(model); done.close(); results.write(ts); results.close();
			 * 
			 * file.delete(); } catch (Exception e) { e.printStackTrace(); } } } else {
			 * System.out.println("La catella è vuota!"); } lastSec = sec; } // If():
			 */
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			File[] files = todo.listFiles();
			if (files.length > 0) {
				for (int i = 0; i < files.length; i++) {
					if (!doingList.contains(files[i])) {
						doingList.add(files[i]);
						new ExecThread(files[i], i).start();
					}					
				}
			} else {
				System.out.println("DoingList: " + doingList.toString());
			} 
		} // while()
	}
}