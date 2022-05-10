package ctwedge.modelanalyzer;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.parser.IParseResult;
import org.eclipse.xtext.parser.IParser;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.util.CancelIndicator;
import org.eclipse.xtext.validation.CheckMode;
import org.eclipse.xtext.validation.IResourceValidator;
import org.eclipse.xtext.validation.Issue;

import com.google.inject.Injector;

import ctwedge.CTWedgeStandaloneSetup;

// check the validity of all the benchmarks
// it uses the grammar paser and the validator of xtext
public class BenchmarksSyntaxChecker {

	public static void main(String[] args) throws IOException {
		Stream<Path> walk = Files.walk(Paths.get("models_test"));
		Stream<Path> models = walk.filter(x -> x.getFileName().toString().endsWith(".ctw"));
		AtomicInteger right = new AtomicInteger(0);
		AtomicInteger wrong = new AtomicInteger(0);
		models.forEach(x -> {
			try {
				if (parseAndValidate(x)) right.incrementAndGet();
				else wrong.incrementAndGet();
			} catch (IOException e) {
				 wrong.incrementAndGet();
				e.printStackTrace();
			}
		});
		System.out.println("right " + right.get());
		System.out.println("wrong " + wrong.get());
		walk.close();
	}

	static boolean parseAndValidate(Path x) throws IOException {
		assert x.toFile().exists();
		System.out.print("checking " + x.toFile().toString() + " ");
		CTWedgeStandaloneSetup citLStandaloneSetup = new CTWedgeStandaloneSetup();
		Injector injector = citLStandaloneSetup.createInjectorAndDoEMFRegistration();
		XtextResourceSet resourceSet = injector.getInstance(XtextResourceSet.class);
		// create temp resource
		URI result = URI.createFileURI(x.toString());
		Resource resource = resourceSet.createResource(result);
		resource.load(resourceSet.getLoadOptions());
		// parse
		IParser parser = injector.getInstance(IParser.class);
		IParseResult pResult = parser.parse(new FileReader(x.toFile()));
		// check that there is a model resource.getAllContents()				
		// TODO elimina questi messaggi
		if (!pResult.hasSyntaxErrors())
			System.out.println("parser ok");
		else {
			System.out.println("parser errors ");
			for (INode issue : pResult.getSyntaxErrors()) {
				System.out.println(issue.getText() + " (Line " + issue.getStartLine() + "): "
						+ issue.getSyntaxErrorMessage().getMessage());
			}
			return false;
		}
		// validate
		IResourceValidator validator = injector.getInstance(IResourceValidator.class);
		List<Issue> list = validator.validate(resource, CheckMode.ALL, CancelIndicator.NullImpl);
		if (!list.isEmpty()) {
			for (Issue issue : list) {
				System.err.println(issue);
			}
			return false;
		}
		return true;
	}

}
