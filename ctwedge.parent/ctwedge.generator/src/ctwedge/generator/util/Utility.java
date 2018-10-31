package ctwedge.generator.util;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;

import com.google.inject.Injector;

import ctwedge.CTWedgeStandaloneSetup;
import ctwedge.ctWedge.CitModel;
import ctwedge.generator.acts.ACTSConstraintTranslator;
import ctwedge.generator.acts.ACTSTranslator;
import ctwedge.generator.casa.CASATranslator;
import ctwedge.util.TestSuite;

public class Utility {
	
	public static final Utility INSTANCE = new Utility();
	
	public static TestSuite getTestSuite(String model, String generator, int t, boolean ignoreC, String executableFolderPath) throws Exception {
		TestSuite ts = null;
		if (generator.equalsIgnoreCase("casa")) { // CASA
			ts = new CASATranslator(executableFolderPath).getTestSuite(loadModel(model), t, ignoreC);
		} else { // ACTS
			ts = new ACTSTranslator().getTestSuite(loadModel(model), t, ignoreC);
		}
		return ts;
	}
	
	public static String getTestSuiteWithNumbers(String model, String generator, int t, boolean ignoreC, String executableFolderPath) throws Exception {
		String ts = getTestSuite(model, generator, t, ignoreC, executableFolderPath).toString();
		ts = addNumbers(ts);
		return ts;
	}

	// the string contains the model itself
	public static CitModel loadModel(String model) {
		Injector injector = new CTWedgeStandaloneSetup().createInjectorAndDoEMFRegistration();
		XtextResourceSet resourceSet = injector.getInstance(XtextResourceSet.class);
		resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);
		// https://wiki.eclipse.org/Xtext/FAQ#How_do_I_load_my_model_in_a_standalone_Java_application.C2.A0.3F
		Resource resource = resourceSet.createResource(URI.createURI("dummy:/example.ctw"));
		InputStream in = new ByteArrayInputStream(model.getBytes());
		try {
			resource.load(in, resourceSet.getLoadOptions());
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		CitModel res = (CitModel) resource.getContents().get(0);
		System.out.println("Original model: " + ACTSConstraintTranslator.dump(res, ""));
		return res;
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

}