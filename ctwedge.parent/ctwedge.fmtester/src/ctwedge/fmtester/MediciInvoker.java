package ctwedge.fmtester;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
* {@link MediciInvoker} allows to call medici.exe and offers a method to get data from the program call
*/
public class MediciInvoker {
	
	/**
	 * Call medici.exe and print data about the medici input model
	 * 
	 * @param inPath the path to the input medici .txt file
	 */
	public static void printMediciData(String inPath) throws IOException {		
		// call medici
		List<String> command = new ArrayList<String>();
		command.add("..//ctwedge.generators/ctwedge.generator.medici./medici.exe");
		// model
		command.add("--m");
		command.add(inPath);
		// do not generate
		command.add("--donotgenerate");
		System.out.println(command);
		// run
		ProcessBuilder pc = new ProcessBuilder(command);
		pc.command(command);
		pc.redirectError();
		Process p = pc.start();
		try {
			p.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("command finished ");
		try {
			BufferedReader bri = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			while ((line = bri.readLine()) != null) {
				System.out.println(line);
			}
			bri.close();
			p.waitFor();
			System.out.println("command finished ");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
