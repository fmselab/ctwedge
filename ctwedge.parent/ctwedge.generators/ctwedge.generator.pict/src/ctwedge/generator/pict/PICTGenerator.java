package ctwedge.generator.pict;
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
import ctwedge.util.TestSuite;
import ctwedge.util.ext.ICTWedgeTestGenerator;
import ctwedge.util.ext.ICTWedgeTranslTestGenerator;

public class PICTGenerator extends ICTWedgeTranslTestGenerator{

	private String path;
	
	public PICTGenerator() {
		super("PICT");
		path = PICTGenerator.class.getProtectionDomain().getCodeSource().getLocation().getPath().split("/target")[0]; 
		if (path.contains(":") && path.startsWith("/"))
			path = path.substring(1);
	}
	
	public String translateModel(CitModel model, boolean ignoreConstraints){
		PICTTranslator paramTranslator = new PICTTranslator();
		String param = paramTranslator.paramToPictCode(model);
		if (ignoreConstraints)
			return param;
		String constraints = paramTranslator.constraintToPictCode(model);
		return param + "\n" + constraints;
	}
	
	public TestSuite getTestSuite(CitModel citModel, int nWise, boolean ignoreConstraints) throws Exception {
		File tempModel = File.createTempFile("pictmodel_" + citModel.getName(), ".txt");
		System.out.println(tempModel.getAbsolutePath());
		//tempModel.deleteOnExit();
		System.out.println(tempModel.getAbsolutePath());
		BufferedWriter out = new BufferedWriter(new FileWriter(tempModel));
		String pictModel = translateModel(citModel, ignoreConstraints);
		out.append(pictModel);
		out.close();
		System.out.println("\n------- MODELLO PICT -------\n");
		System.out.println(pictModel);
		long t_start = System.currentTimeMillis();
		String output = runTool(tempModel);
		long t_end = System.currentTimeMillis();
		if (output == null)
			return null;
		TestSuite testSuite = new TestSuite(output, citModel);
		testSuite.setStrength(nWise);
		testSuite.setGeneratorTime(t_end - t_start);
		return testSuite;
	}
	
	/**
	 * run PICT as external tool
	 * 
	 * @param model
	 *            file for the model
	 * @return the file containing the test suite
	 * @throws IOException
	 */
	private String runTool(File model) throws IOException {
		StringBuilder sb = new StringBuilder();
		List<String> command = new ArrayList<String>();
		String pictExecutable = path + "/pict.exe";
		command.add(pictExecutable);
		command.add(model.getAbsolutePath());
		ProcessBuilder pc = new ProcessBuilder(command);
		pc.command(command);
		File tempError = File.createTempFile("pict_error", ".txt");
		tempError.deleteOnExit();
		pc.redirectError(tempError);
		System.out.println("running " + command);
		executableProcess = pc.start();
		try {
			if (true) {
				BufferedReader bri = new BufferedReader(new InputStreamReader(executableProcess.getInputStream()));
				String line;
				while ((line = bri.readLine()) != null) {
					line = toCSV(line);
					System.out.println(line);
					sb.append(line + "\n");
				}
				bri.close();
			}
			executableProcess.waitFor();
			System.out.println("command finished ");
		} catch (InterruptedException e) {
			e.printStackTrace();
			return null;
		}
		if (checkError(tempError)) {
			System.out.println("******************** ERRORE RILEVATO *****************************");
			return null;
		}
		return sb.toString();
	}

	
	private String toCSV(String input) {
		return input.replaceAll("\t", ";");
	}
	
	private boolean checkError(File errorFile) throws IOException {
		BufferedReader fin = new BufferedReader(new FileReader(errorFile));
		String s = "";
		boolean errorFound = false;
		while ((s = fin.readLine()) != null) {
			if (s.startsWith("Input Error")) {
				System.out.println(s);
				errorFound = true;
			}
		}
		fin.close();
		return errorFound;
	}
}