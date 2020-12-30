package ctwedge.generator.medici;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import ctwedge.ctWedge.CitModel;
import ctwedge.ctWedge.Constraint;
import ctwedge.ctWedge.Parameter;
import ctwedge.generator.util.ParameterSize;
import ctwedge.util.ParameterValuesToInt;
import ctwedge.util.TestSuite;
import ctwedge.util.ext.ICTWedgeTranslTestGenerator;

public class MediciCITGenerator extends ICTWedgeTranslTestGenerator{

	private static final boolean READ_STD_OUT = true;
	
	private String path;

	public MediciCITGenerator() {
		super("Medici");
		path = MediciCITGenerator.class.getProtectionDomain().getCodeSource().getLocation().getPath().split("/target")[0]; 
		if (path.contains(":") && path.startsWith("/"))
			path = path.substring(1);
	}

	@Override
	public TestSuite getTestSuite(CitModel loadModel, int t, boolean ignoreC) throws Exception {
		// TODO
		// convert to medici
		File model = new File("model.txt");
		FileWriter wf = new FileWriter(model);
		String translateModel = translateModel(loadModel, ignoreC);
		wf.write(translateModel);
		System.out.println(translateModel);
		wf.close();
		// run the tool
		List<String> command = new ArrayList<String>();
		String mediciExecutable = path + "medici.exe";
		command.add(mediciExecutable);
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
		long t_end = 0;
		long t_start = System.currentTimeMillis();
		executableProcess = pc.start();
		// wait it finishes
		try {
			if (READ_STD_OUT) {
				BufferedReader bri = new BufferedReader(new InputStreamReader(executableProcess.getInputStream()));
				String line;
				while ((line = bri.readLine()) != null) {
					System.out.println(line);
				}
				bri.close();
			}
			executableProcess.waitFor();
			t_end = System.currentTimeMillis();
			System.out.println("command finished ");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (checkError(tempError)) {
			System.out.println("******************** ERRORE RILEVATO *****************************");
			return null;
		}
		//
		TestSuite ts = translateOutput(tempoutput, loadModel);
		ts.setStrength(t);
		ts.setGeneratorTime(t_end - t_start);
		return ts;
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
	// translation of the model to String
	@Override
	public String translateModel(CitModel sm, boolean ignoreConstraints) {
		StringBuffer sb = new StringBuffer();
		// parameters
		sb.append("2" + "\n" + sm.getParameters().size() + "\n");
		for (Parameter param : sm.getParameters())
			sb.append(ParameterSize.eInstance.doSwitch(param)).append(" ");
		sb.append("\n");
		// add all the constraints
		if (!ignoreConstraints) {
			sb.append(sm.getConstraints().size() + "\n");			
			ConstraintToMediciIds translator = new ConstraintToMediciIds(sm);
			for (Constraint c : sm.getConstraints())
				sb.append(translator.doSwitch(c)).append("\n");
		} else {
			// no constraints
			sb.append("0\n");
		}
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
}
