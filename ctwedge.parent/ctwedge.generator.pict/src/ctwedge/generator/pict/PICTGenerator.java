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
import ctwedge.generator.util.Benchmarkable;
import ctwedge.util.Pair;
import ctwedge.util.TestSuite;
import ctwedge.util.ext.ICTWedgeTestGenerator;

public class PICTGenerator extends ICTWedgeTestGenerator implements Benchmarkable{

	public PICTGenerator() {
		super("PICT");
	}
	
	public String convert(CitModel model){
		PICTTranslator paramTranslator = new PICTTranslator();
		String param = paramTranslator.paramToPictCode(model);
		String constraints = paramTranslator.constraintToPictCode(model);
		
		return param + "\n" + constraints;
	}
	
	public TestSuite getTestSuite(CitModel citModel, int nWise, boolean ignoreConstraints) throws Exception {
		File tempModel = File.createTempFile("pictmodel_" + citModel.getName(), ".txt");
		tempModel.deleteOnExit();
		System.out.println(tempModel.getAbsolutePath());
		BufferedWriter out = new BufferedWriter(new FileWriter(tempModel));
		String pictModel = convert(citModel);
		out.append(pictModel);
		out.close();
		System.out.println("\n------- MODELLO PICT -------\n");
		System.out.println(pictModel);
		String output = runTool(tempModel);
		TestSuite testSuite = new TestSuite(output, citModel);
		testSuite.populateTestSuite();
		testSuite.setStrength(nWise);
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
		String casaExecutable = "pict.exe";
		command.add(casaExecutable);
		command.add(model.getAbsolutePath());
		ProcessBuilder pc = new ProcessBuilder(command);
		pc.command(command);
		File tempError = File.createTempFile("pict_error", ".txt");
		tempError.deleteOnExit();
		pc.redirectError(tempError);
		System.out.println("running " + command);
		Process p = pc.start();
		try {
			if (true) {
				BufferedReader bri = new BufferedReader(new InputStreamReader(p.getInputStream()));
				String line;
				while ((line = bri.readLine()) != null) {
					line = toCSV(line);
					System.out.println(line);
					sb.append(line + "\n");
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
	
	@Override
	public TestSuite call() throws Exception {
		System.out.println("PICT generator called");
		return getTestSuite(citModel, nWise, true);
	}

	@Override
	public TestSuite benchmark_run(CitModel citModel) {
		try {
			File tempModel = File.createTempFile("pictmodel_" + citModel.getName(), ".txt");
			tempModel.deleteOnExit();
			BufferedWriter out = new BufferedWriter(new FileWriter(tempModel));
			String pictModel = convert(citModel);
			out.append(pictModel);
			out.close();
			StringBuilder sb = new StringBuilder();
			List<String> command = new ArrayList<String>();
			String casaExecutable = "pict.exe";
			command.add(casaExecutable);
			command.add(tempModel.getAbsolutePath());
			ProcessBuilder pc = new ProcessBuilder(command);
			pc.command(command);
			File tempError = File.createTempFile("pict_error", ".txt");
			tempError.deleteOnExit();
			pc.redirectError(tempError);
			long t_end = 0;
			long t_start = System.currentTimeMillis();
				Process p = pc.start();
				BufferedReader bri = new BufferedReader(new InputStreamReader(p.getInputStream()));
				String line;
				while ((line = bri.readLine()) != null) {
					line = toCSV(line);
					sb.append(line + "\n");
				}
				bri.close();
				p.waitFor();
			t_end = System.currentTimeMillis();
			if (checkError(tempError)) {
				System.out.println("******************** ERRORE RILEVATO *****************************");
			}
			String output = sb.toString();
			TestSuite testSuite = new TestSuite(output, citModel);
			testSuite.populateTestSuite();
			testSuite.setGeneratorTime(t_end - t_start);
			return testSuite;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	

	
}