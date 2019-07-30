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

public class BatchGenerator extends Thread {
	private final static String DIR_TODO = "C:\\Users\\ivanc\\Desktop\\Universita\\Tesi\\BatchGenerator\\Todo";
	static Integer ActiveThread = 0;
	// private final static String DIR_DOING =
	// "C:\\Users\\ivanc\\Desktop\\Universita\\Tesi\\BatchGenerator\\Doing";

	// private final static String DIR_DONE =
	// "C:\\Users\\ivanc\\Desktop\\Universita\\Tesi\\BatchGenerator\\Done";
	// private final static String DIR_RESULTS =
	// "C:\\Users\\ivanc\\Desktop\\Universita\\Tesi\\BatchGenerator\\Results";

	public static ArrayList<File> doingList = new ArrayList<File>();

	public void run() {
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
				createThread(files);
			} else {
				System.out.println("DoingList: " + doingList.toString());
			}
		} // while()
	}

	public static void main(String[] args) {
		(new BatchGenerator()).start();
	}

	private void createThread(File[] files) {
		for (int i = 0; i < files.length; i++) {
			if (!doingList.contains(files[i])) {
				if (ActiveThread < 5) {
					doingList.add(files[i]);
					ActiveThread++;
					new ExecThread(files[i], i).start();
				} else {
					try {
						System.out.println("Sono il main, vado in attesa. Thread attivi: " + ActiveThread);
						Thread.sleep(1500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
}