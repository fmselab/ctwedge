package ctwedge.generator.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.util.CancelIndicator;
import org.eclipse.xtext.validation.CheckMode;
import org.eclipse.xtext.validation.IResourceValidator;
import org.eclipse.xtext.validation.Issue;

import com.google.inject.Injector;

import ctwedge.CTWedgeStandaloneSetup;
import ctwedge.ctWedge.CitModel;
import ctwedge.util.TestSuite;
import ctwedge.util.ext.ICTWedgeTestGenerator;

public class Utility {

	public static final Utility INSTANCE = new Utility();

	public static TestSuite getTestSuite(String model, ICTWedgeTestGenerator generator, int t, boolean ignoreC,
			String executableFolderPath) throws Exception {
		TestSuite ts = null;
		ts = generator.getTestSuite(loadModel(model), t, ignoreC);
		return ts;
	}

	public static String getTestSuiteWithNumbers(String model, ICTWedgeTestGenerator generator, int t, boolean ignoreC,
			String executableFolderPath) throws Exception {
		String ts = getTestSuite(model, generator, t, ignoreC, executableFolderPath).toString();
		ts = addNumbers(ts);
		return ts;
	}

	// the string contains the model itself
	public static CitModel loadModel(String model) {
		// https://wiki.eclipse.org/Xtext/FAQ#How_do_I_load_my_model_in_a_standalone_Java_application.C2.A0.3F
		return loadModelFromInAndURI(new ByteArrayInputStream(model.getBytes()), URI.createURI("dummy:/example.ctw"));
	}

	// the string contains the model itself
	public static CitModel loadModelFromPath(String modelpath) {
		return loadModelFromInAndURI(null, URI.createFileURI(modelpath));
	}

	/**
	 * Load model from in and URI. In case of error, throw exception
	 *
	 * @param in  the input stream - can be null
	 * @param uri the uri
	 * @return the cit model
	 */
	private static CitModel loadModelFromInAndURI(InputStream in, URI uri) {
		Injector injector = new CTWedgeStandaloneSetup().createInjectorAndDoEMFRegistration();
		XtextResourceSet resourceSet = injector.getInstance(XtextResourceSet.class);
		resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);
		Resource resource = resourceSet.createResource(uri);
		try {
			if (in != null)
				resource.load(in, resourceSet.getLoadOptions());
			else
				resource.load(resourceSet.getLoadOptions());
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		// validate the resource
		IResourceValidator validator = injector.getInstance(IResourceValidator.class);
		List<Issue> issues = validator.validate(resource, CheckMode.ALL, CancelIndicator.NullImpl);
		for (Issue issue : issues) {
			switch (issue.getSeverity()) {
			case ERROR:
				throw new RuntimeException("(first) ERROR: " + issue.getMessage());
			case WARNING:
			case IGNORE: 
			case INFO :
				// TODO System.out.println("WARNING: " + issue.getMessage());
			}
		}
		// return 
		CitModel res = (CitModel) resource.getContents().get(0);
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