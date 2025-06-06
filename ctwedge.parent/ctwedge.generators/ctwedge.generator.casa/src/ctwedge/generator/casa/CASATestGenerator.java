package ctwedge.generator.casa;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import ctwedge.ctWedge.CitModel;
import ctwedge.ctWedge.Parameter;
import ctwedge.util.Pair;
import ctwedge.util.TestSuite;
import ctwedge.util.ext.ICTWedgeTestGenerator;

public class CASATestGenerator extends ICTWedgeTestGenerator{

	private static final boolean READ_STD_OUT = true;
	private String path;
	
//	String path = Generator.context != null ? Generator.context.getRealPath("/static/" + execName) // if it is
			// on server
// : CASATranslator.class.getProtectionDomain().getCodeSource().getLocation().getPath()
//.split("/target")[0] + "/WebContent/static/" + execName; // get the path of the current
// class
	
	public CASATestGenerator() {
		super("CASA");
		path = CASATestGenerator.class.getProtectionDomain().getCodeSource().getLocation().getPath()
			.split("/target")[0] + "/casa/"; // get the path of the current class
		if (path.contains(":") && path.startsWith("/")) path = path.substring(1);
	}
	
	public CASATestGenerator(String path) {
		this();
		if (path!=null) this.path = path + "/";
	}

	@Override
	public TestSuite getTestSuite(CitModel citModel, int nWise, boolean ignoreConstraints) throws Exception {
		// 1. MODEL
		// convert to temporary file
		File tempModel = File.createTempFile("casamodel_" + citModel.getName(), ".citmodel");
		// Delete temp file when program exits.
		tempModel.deleteOnExit();
		ToCasaParametersExporter mygen = new ToCasaParametersExporter();
		BufferedWriter out = new BufferedWriter(new FileWriter(tempModel));
		// get the model in casa
		CharSequence modelS = mygen.toCasaCode(citModel, nWise);
		out.append(modelS);
		out.close();
		//
		// 2. the constraints
		File output;
		// write the constraints
		ConvertToAbstractID exporter = new ConvertToAbstractID(citModel);
		long t_end = 0;
		long t_start = 0;
		if (!ignoreConstraints && !citModel.getConstraints().isEmpty()) {
			CharSequence constraints = exporter.translateConstraints(); // can throw exception
			assert constraints != null;
			File tempConstr = File.createTempFile("casamodel" + citModel.getName(), ".constraints");
			// Write to temp file
			out = new BufferedWriter(new FileWriter(tempConstr));
			out.append(constraints);
			out.close();
			//
			t_start = System.currentTimeMillis();
			output = runTool(citModel, tempModel, tempConstr);
			t_end = System.currentTimeMillis();
		} else {
			t_start = System.currentTimeMillis();
			output = runTool(citModel, tempModel, null);
			t_end = System.currentTimeMillis();
		}
		// parse the results
		String ts = parseResults(output, exporter);
		TestSuite tst = new TestSuite(ts, citModel);
		tst.setGeneratorTime(t_end - t_start);
		tst.setStrength(nWise);
		
		return tst;
	}

	/**
	 * run CASA as external tool
	 * 
	 * @param model
	 *            file for the model
	 * @param constraints
	 *            file containing the constraints (null if none)
	 * @return the file containing the test suite
	 * @throws IOException
	 */
	private File runTool(CitModel citModel, File model, File constraints) throws IOException {
		//
		List<String> command = new ArrayList<String>();
		String casaExecutable = getCasaExecutable();
		command.add(casaExecutable);
		// output}
		File tempoutput = File.createTempFile("casaoutput" + citModel.getName(), ".txt");
		command.add("--output");
		command.add(tempoutput.getAbsolutePath());
		// constraints
		if (constraints != null) {
			command.add("--constrain");
			command.add(constraints.getAbsolutePath());
		}

		// set preferences (as default)
		command.add("-i");
		command.add("" + 256);
		command.add("-r");
		command.add("" + 2);
		command.add("-p");
		command.add("" + (2.0 / 3.0));

		// model
		command.add(model.getAbsolutePath());
		// run
		ProcessBuilder pc = new ProcessBuilder(command);
		pc.command(command);
		System.out.println("running " + command);
		executableProcess = pc.start();
		// wait it finishes
		try {
			if (READ_STD_OUT) {
				// redirect stderr to stout
				pc.redirectErrorStream();
				BufferedReader bri = new BufferedReader(new InputStreamReader(executableProcess.getInputStream()));
				String line;
				while ((line = bri.readLine()) != null) {
					System.out.println(line);
				}
				bri.close();
			}
			executableProcess.waitFor();
			System.out.println("command finished ");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return tempoutput;
	}

	// try to get the executable name
	private String getCasaExecutable() {
		String execName;
		if (System.getProperty("os.name").startsWith("Windows")) {
			execName = "cover.exe";
		} else if (System.getProperty("os.name").toLowerCase().contains("mac")) {
			execName = "casa-1.1b";
		} else {
			execName = "cover";
		}
		try {
			if (!System.getProperty("os.name").startsWith("Windows")) {
				// Bundle bundle = Platform.getBundle("ctwedge.generator");
				// URL url = Paths.get(execName).toUri().toURL();
				// FileLocator.resolve(FileLocator.find(bundle, new Path(execName),
				// Collections.emptyMap()));
				System.out.println(path+execName);
				Set<PosixFilePermission> perms = new HashSet<PosixFilePermission>();
				// add owners permission
				perms.add(PosixFilePermission.OWNER_READ);
				perms.add(PosixFilePermission.OWNER_WRITE);
				perms.add(PosixFilePermission.OWNER_EXECUTE);
				// add group permissions
				perms.add(PosixFilePermission.GROUP_READ);
				perms.add(PosixFilePermission.GROUP_WRITE);
				perms.add(PosixFilePermission.GROUP_EXECUTE);
				// add others permissions
				perms.add(PosixFilePermission.OTHERS_READ);
				perms.add(PosixFilePermission.OTHERS_WRITE);
				perms.add(PosixFilePermission.OTHERS_EXECUTE);
	
				Files.setPosixFilePermissions(Paths.get(path+execName), perms);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Returning path: "+path+execName);
		return path+execName;
	}

	private String parseResults(File output, ConvertToAbstractID exporter) throws FileNotFoundException {
		StringBuffer sb = new StringBuffer();

		Scanner sc = new Scanner(output);
		assert output != null;
		assert output.canRead();
		int numberOfTests = sc.nextInt();
		sc.nextLine(); // skip line this very line
		System.out.println("reading " + numberOfTests + " tests from " + output.getAbsolutePath());
		for (int i = 0; i < numberOfTests; i++) {
			String testS = sc.nextLine().trim();
			System.out.println("reading line " + i + " " + testS);
			// convert test to a single test
			String[] assignments = testS.split(" ");
			if (i == 0) { // PRINT CSV HEADER
				for (int j = 0; j < assignments.length; j++) {
					assert assignments[j].length() > 0 : "line " + testS;
					Pair<Parameter, String> singleA = exporter.convertInt(Integer.parseInt(assignments[j]));
					sb.append((j > 0 ? ";" : "") + singleA.getFirst().getName());
				}
				sb.append("\n");
			}
			for (int j = 0; j < assignments.length; j++) { // then PRINT CSV BODY (tests)
				String s = assignments[j];
				assert s.length() > 0 : "line " + testS;
				// covert s to int
				int aNum = Integer.parseInt(s);
				// convert from int to paramter and value
				Pair<Parameter, String> singleA = exporter.convertInt(aNum);
				sb.append((j > 0 ? ";" : "") + singleA.getSecond());
			}
			sb.append("\n");
		}
		sc.close();
		return sb.toString();
	}

	@Override
	public TestSuite getTestSuite(CitModel model, int strength, boolean ignoreConstraints, TestSuite ts)
			throws Exception {
		return getTestSuite(model, strength, ignoreConstraints);
	}
}
