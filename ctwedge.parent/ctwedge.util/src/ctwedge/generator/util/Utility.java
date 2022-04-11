package ctwedge.generator.util;

import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.parser.IParseResult;
import org.eclipse.xtext.parser.IParser;
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
//		execute the parser
//		AG: 11/04/2022 questo dà un sacco di problemi se faccio anche il parsing
//		IParser parser = injector.getInstance(IParser.class);
//		assert parser != null;
//		IParseResult pResult = parser.parse(new InputStreamReader(in));
//		// check that there is a model resource.getAllContents()				
//		// TODO elimina questi messaggi
//		if (!pResult.hasSyntaxErrors())
//			System.out.println("parser ok");
//		else {
//			System.out.println("parser errors ");
//			for (INode issue : pResult.getSyntaxErrors()) {
//				System.out.println(issue.getText() + " (Line " + issue.getStartLine() + "): "
//						+ issue.getSyntaxErrorMessage().getMessage());
//			}
//			throw new RuntimeException("(praser) ERROR");
//		}		
		// validate the resource
		IResourceValidator validator = injector.getInstance(IResourceValidator.class);
		try {
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
		} catch(Exception e) {
			
		}
		// return 
		// get the model
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