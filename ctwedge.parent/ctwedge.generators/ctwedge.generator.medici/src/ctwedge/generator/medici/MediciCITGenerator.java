package ctwedge.generator.medici;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import ctwedge.ctWedge.CitModel;
import ctwedge.ctWedge.Constraint;
import ctwedge.ctWedge.Parameter;
import ctwedge.generator.util.Benchmarkable;
import ctwedge.generator.util.ParameterSize;
import ctwedge.util.Pair;
import ctwedge.util.ParameterValuesToInt;
import ctwedge.util.Test;
import ctwedge.util.TestSuite;
import ctwedge.util.ext.ICTWedgeTestGenerator;

public class MediciCITGenerator extends ICTWedgeTestGenerator implements Benchmarkable {

	private static final boolean READ_STD_OUT = true;
	
	Process p;

	public MediciCITGenerator() {
		super("Medici");
	}

	@Override
	public TestSuite getTestSuite(CitModel loadModel, int t, boolean ignoreC) throws Exception {
		// TODO
		// convert to medici
		File model = new File("model.txt");
		FileWriter wf = new FileWriter(model);
		String translateModel = translateModel(loadModel);
		wf.write(translateModel);
		System.out.println(translateModel);
		wf.close();
		// run the tool
		List<String> command = new ArrayList<String>();
		command.add("./medici.exe");
		// output}
		File tempoutput = File.createTempFile("medici_output" + loadModel.getName(), ".txt");
		// model
		command.add("--m");
		command.add(model.getAbsolutePath());
		// output
		command.add("--o");
		command.add(tempoutput.getAbsolutePath());
		// run
		ProcessBuilder pc = new ProcessBuilder(command);
		pc.command(command);
		//	error redirect
		File tempError = File.createTempFile("medici_error", ".txt");
		tempError.deleteOnExit();
		pc.redirectError(tempError);
		System.out.println("running " + command);
		p = pc.start();
		// wait it finishes
		try {
			if (READ_STD_OUT) {
				BufferedReader bri = new BufferedReader(new InputStreamReader(p.getInputStream()));
				String line;
				while ((line = bri.readLine()) != null) {
					System.out.println(line);
				}
				bri.close();
			}
			p.waitFor();
			System.out.println("command finished ");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (checkError(tempError)) {
			System.out.println("******************** ERRORE RILEVATO *****************************");
			return null;
		}
		//
		return translateOutput(tempoutput, loadModel);
	}
	
	private boolean checkError(File errorFile) throws IOException {
		BufferedReader fin = new BufferedReader(new FileReader(errorFile));
		String s = "";
		boolean errorFound = false;
		while ((s = fin.readLine()) != null) {
			System.out.println(s);
			errorFound = true;
		}
		fin.close();
		return errorFound;
	}

	private String translateModel(CitModel sm) {
		StringBuffer sb = new StringBuffer();
		// parameters
		sb.append("2" + "\n" + sm.getParameters().size() + "\n");
		for (Parameter param : sm.getParameters())
			sb.append(ParameterSize.eInstance.doSwitch(param)).append(" ");
		sb.append("\n");
		// add all the constraints
		sb.append(sm.getConstraints().size() + "\n");
		// TODO if the constraints are to be ignored
		ConstraintToMediciIds translator = new ConstraintToMediciIds(sm);
		for (Constraint c : sm.getConstraints())
			sb.append(translator.doSwitch(c)).append("\n");
		return sb.toString();
	}
	// format for the output
	//2 --> number of tests
	//1 2 4 -> values for test1 
	//1 2 5 ...
	private TestSuite translateOutput(File file, CitModel model) throws IOException {
		String csv_out = "";
		//	first row -> param names
		for (Parameter param : model.getParameters())
			csv_out += param.getName() + ";";
		csv_out = csv_out.substring(0, csv_out.length() - 1);
		csv_out += "\n";
		//	other rows -> param values
		ParameterValuesToInt valToInt = new ParameterValuesToInt(model);
		BufferedReader fin = new BufferedReader(new FileReader(file));
		String test = "";
		boolean firstrow = true;
		while ((test = fin.readLine()) != null) {
			if (firstrow) {
				firstrow = false;
				continue;
			}
			String[] values = test.split(" ");
			for (int i = 0; i < values.length; i++)
				csv_out += valToInt.convertInt(Integer.parseInt(values[i])).getValue() + ";";
			csv_out = csv_out.substring(0, csv_out.length() - 1);
			csv_out += "\n";
		}
		fin.close();
		
		TestSuite result = new TestSuite(csv_out, model);
		return result;
	}

	@Override
	public TestSuite benchmark_run(CitModel loadModel) {
		try {
			File model = new File("model.txt");
			FileWriter wf = new FileWriter(model);
			String translateModel = translateModel(loadModel);
			wf.write(translateModel);
			System.out.println(translateModel);
			wf.close();
			// run the tool
			List<String> command = new ArrayList<String>();
			command.add("./medici.exe");
			// output}
			File tempoutput = File.createTempFile("medici_output" + loadModel.getName(), ".txt");
			// model
			command.add("--m");
			command.add(model.getAbsolutePath());
			// output
			command.add("--o");
			command.add(tempoutput.getAbsolutePath());
			// run
			ProcessBuilder pc = new ProcessBuilder(command);
			pc.command(command);
			//	error redirect
			File tempError = File.createTempFile("medici_error", ".txt");
			tempError.deleteOnExit();
			pc.redirectError(tempError);
			System.out.println("running " + command);
			p = pc.start();
			// wait it finishes
			try {
				if (READ_STD_OUT) {
					BufferedReader bri = new BufferedReader(new InputStreamReader(p.getInputStream()));
					String line;
					while ((line = bri.readLine()) != null) {
						System.out.println(line);
					}
					bri.close();
				}
				p.waitFor();
				System.out.println("command finished ");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (checkError(tempError)) {
				System.out.println("******************** ERRORE RILEVATO *****************************");
				return null;
			}
			TestSuite ts = translateOutput(tempoutput, loadModel);
			return ts;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public void destroyProcess() {
		if (p != null)
			p.destroy();
	}
}
