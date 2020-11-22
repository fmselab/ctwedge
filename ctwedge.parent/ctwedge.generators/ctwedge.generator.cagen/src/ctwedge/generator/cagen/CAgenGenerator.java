package ctwedge.generator.cagen;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import ctwedge.ctWedge.CitModel;
import ctwedge.ctWedge.Parameter;
import ctwedge.util.TestSuite;
import ctwedge.util.ext.ICTWedgeTestGenerator;

public class CAgenGenerator extends ICTWedgeTestGenerator{
	
	private String path;
	
	public CAgenGenerator() {
		super("CAgen");
		path = CAgenGenerator.class.getProtectionDomain().getCodeSource().getLocation().getPath().split("/target")[0]; 
		if (path.contains(":") && path.startsWith("/"))
			path = path.substring(1);
	}
	
	public String convert(CitModel model, boolean ignoreConstraints){
		String header = "[System]\nName: " + model.getName() + "\n";
		CAgenTranslator translator = new CAgenTranslator();
		String param = "[Parameter]\n" + translator.paramToCAgenCode(model);
		if (ignoreConstraints)
			return header + "\n" + param;
		String constraints = "[Constraint]\n" + translator.constraintToCAgenCode(model);
		return header + "\n" + param + "\n" + constraints;
	}	

	@Override
	public TestSuite getTestSuite(CitModel citModel, int nWise, boolean ignoreConstraints) throws Exception {
		File tempModel = File.createTempFile("cagenmodel_" + citModel.getName(), ".txt");
		tempModel.deleteOnExit();
		//System.out.println(tempModel.getAbsolutePath());
		BufferedWriter out = new BufferedWriter(new FileWriter(tempModel));
		String cagenModel = convert(citModel, ignoreConstraints);
		out.append(cagenModel);
		out.close();
		System.out.println("\n------- MODELLO CAgen -------\n");
		System.out.println(cagenModel);
		long t_start = System.currentTimeMillis();
		String output = runTool(tempModel, nWise);
		long t_end = System.currentTimeMillis();
		//	Caso errore nell'esecuzione del tool
		if (output == null)
			return null;
		String pNamesRow = "";
		for (Parameter param : citModel.getParameters())
			pNamesRow += param.getName() + ";";
		pNamesRow = pNamesRow.substring(0, pNamesRow.length() - 1);
		pNamesRow += "\n";
		output = pNamesRow + output;
		System.out.println(output);
		TestSuite testSuite = new TestSuite(output, citModel);
		testSuite.setStrength(nWise);
		testSuite.setGeneratorTime(t_end - t_start);
		return testSuite;
	}
	
	/**
	 * run CAgen as external tool
	 * 
	 * @param model
	 *            file for the model
	 * @param nWise
	 * 			  the strength            
	 * @return the file containing the test suite
	 * @throws IOException
	 */
	private String runTool(File model, int nWise) throws IOException {
		StringBuilder sb = new StringBuilder();
		List<String> command = new ArrayList<String>();
		String cagenExecutable = path + "fipo-cli.exe";
		command.add(cagenExecutable);
		command.add("--instance");
		command.add(model.getAbsolutePath().replace("\\", "/"));
		command.add("--strength");
		command.add("" + nWise);
		command.add("--print");
		command.add("--randomize");
		ProcessBuilder pc = new ProcessBuilder(command);
		pc.command(command);
		File tempError = File.createTempFile("cagen_error", ".txt");
		tempError.deleteOnExit();
		pc.redirectError(tempError);
		System.out.println("running " + command);
		pc.directory(new File(path));
		System.out.println(pc.directory() + " " + pc.command());
		executableProcess = pc.start();
		try {
			BufferedReader bri = new BufferedReader(new InputStreamReader(executableProcess.getInputStream()));
			String line;
			while ((line = bri.readLine()) != null) {
				line = line.replace(",", ";");
				//System.out.println(line);
				sb.append(line + "\n");
			}
			bri.close();
			executableProcess.waitFor();
			System.out.println("command finished ");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (checkError(tempError)) {
			System.out.println("******************** ERRORE RILEVATO *****************************");
			return null;
		}
		return sb.toString();
	}
	
	private boolean checkError(File errorFile) throws IOException {
		BufferedReader fin = new BufferedReader(new FileReader(errorFile));
		String s = "";
		boolean errorFound = false;
		while ((s = fin.readLine()) != null) {
			if (s.contains("Error") || errorFound) {
				System.out.println(s);
				errorFound = true;
			}
		}
		fin.close();
		return errorFound;
	}
}
