
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.junit.Test;

import com.google.inject.Injector;

import ctwedge.CTWedgeStandaloneSetup;
import ctwedge.ctWedge.CitModel;

/**
 * Servlet implementation class for the benchmark table
 */
public class Benchmarks {

	@Test
	public void printCSV() {
		System.out.println(generateCSV());
	}

	public String generateCSV() {
		return "Model;Paper;# parameters;# constraints\n" + crawlDir(new File("models/"));
	}

	public String crawlDir(File folder) {
		StringBuilder st = new StringBuilder();
		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()
					&& (listOfFiles[i].getName().endsWith(".citw") || listOfFiles[i].getName().endsWith(".ctw"))) {
				// System.out.println("File " + listOfFiles[i].getName());
				try {
					String model = readFromFile(listOfFiles[i]);
					CitModel m = loadModel(model);
					st.append(listOfFiles[i].getName().replace(".citw", "").replace(".ctw", "") + ";" + folder.getName()
							+ ";" + m.getParameters().size() + ";" + m.getConstraints().size());
				} catch (Exception e) {
					e.printStackTrace();
				}
				st.append("\n");
			} else if (listOfFiles[i].isDirectory()) {
				st.append(crawlDir(listOfFiles[i]));
			}
		}
		return st.toString();
	}

	public static String addNumbers(String ts) {
		StringBuffer sb = new StringBuffer();
		String[] st = ts.split("\n");
		sb.append("#;" + st[0] + "\n");
		for (int i = 1; i < st.length; i++) {
			sb.append(i + ";" + st[i] + "\n");
		}
		return sb.toString();
	}

	// the string contains the model itself
	public static CitModel loadModel(String model) {
		Injector injector = new CTWedgeStandaloneSetup().createInjectorAndDoEMFRegistration();
		XtextResourceSet resourceSet = injector.getInstance(XtextResourceSet.class);
		resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);
		// https://wiki.eclipse.org/Xtext/FAQ#How_do_I_load_my_model_in_a_standalone_Java_application.C2.A0.3F
		Resource resource = resourceSet.createResource(URI.createURI("dummy:/example.citw"));
		InputStream in = new ByteArrayInputStream(model.getBytes());
		try {
			resource.load(in, resourceSet.getLoadOptions());
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		CitModel res = (CitModel) resource.getContents().get(0);
		// System.out.println("Original model: "+ACTSConstraintTranslator.dump(res,""));
		return res;
	}

	public static String readFromFile(File f) throws Exception {
		StringBuilder sb = new StringBuilder();
		BufferedReader fin = new BufferedReader(new FileReader(f));
		String s = "";
		while ((s = fin.readLine()) != null)
			sb.append(s + "\n");
		fin.close();
		return sb.toString();
	}

	public static void writeToFile(String filename, String content) throws Exception {
		PrintWriter fout = new PrintWriter(new FileWriter(filename));
		fout.print(content);
		fout.close();
	}
}
