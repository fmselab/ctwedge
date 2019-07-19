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

	//private final static String DIR_DONE = "C:\\Users\\ivanc\\Desktop\\Universita\\Tesi\\BatchGenerator\\Done";
	//private final static String DIR_RESULTS = "C:\\Users\\ivanc\\Desktop\\Universita\\Tesi\\BatchGenerator\\Results";

	public static ArrayList<File> doingList = new ArrayList<File>();

	public static void main(String[] args) {
		//long lastSec = 0;
		File todo = new File(DIR_TODO);

		while (true) {
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