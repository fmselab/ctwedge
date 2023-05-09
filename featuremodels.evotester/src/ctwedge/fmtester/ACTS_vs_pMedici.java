package ctwedge.fmtester;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;

import org.junit.BeforeClass;
import org.junit.Test;

import ctwedge.ctWedge.CitModel;
import ctwedge.generator.acts.ACTSTranslator;
import ctwedge.importer.featureide.FeatureIdeImporter;
import ctwedge.importer.featureide.XmlFeatureModelImporter;
import ctwedge.util.TestSuite;
import ctwedge.util.validator.MinimalityTestSuiteValidator;
import pMedici.main.PMedici;
import pMedici.util.TestContext;
import pMedici.threads.TestBuilder;

/**
 * confronta ACTS con pMedici con diverse opzioni
 * 
 * @author angelo gargantini
 */
public class ACTS_vs_pMedici {

	@BeforeClass
	public static void setupEn() {
		TestContext.IN_TEST = true;
		TestContext.LockTCOnlyOnWriting = true;
	}

	@Test
	public void testOneModel() throws IOException, InterruptedException {
		String fmName = "evolutionModels/AmbientAssistedLiving/AmbientAssistedLivingv1";
		System.out.println(compareGenerators(fmName));
	}

	@Test
	public void testAllModels() throws IOException, InterruptedException {
		GenerateForFM pf = new GenerateForFM();
		Files.walkFileTree(Path.of("evolutionModels"), pf);
		System.out.println(pf.collectedResult);
	}

	public static class GenerateForFM extends SimpleFileVisitor<Path> {

		String collectedResult = "";

		public java.nio.file.FileVisitResult visitFile(Path file, java.nio.file.attribute.BasicFileAttributes attrs)
				throws IOException {
			String string = file.toString();
			if (string.endsWith(".xml")) {
				try {
					collectedResult += (compareGenerators(string.substring(0, string.length() - 4)));
				} catch (IOException | InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return FileVisitResult.CONTINUE;
		};
	}

	private static String compareGenerators(String fmName) throws IOException, InterruptedException {
		FeatureIdeImporter importer = new XmlFeatureModelImporter();
		CitModel citModel = importer.importModel(fmName + ".xml");
		String result = "";
		for (int i = 1; i <= 10; i++) {
			// Force the garbage collector
			System.gc();
			// ACTS
			ACTSTranslator acts = new ACTSTranslator();
			long start = System.currentTimeMillis();
			TestSuite actsTS = acts.getTestSuite(citModel, 2, false); // 2 = n-wise = 2-wise
			long end = System.currentTimeMillis();
			result += fmName + "\t" + "ACTS\t" + actsTS.getTests().size() + "\t" + (end - start) + "\n";
			// pMedici
			// convert to enum ctwedge (could be done only once)
			Converter.fromFMtoCTWedge_ENUM(fmName + ".xml", fmName + "_ctwedge_enum.ctw");
			//
			start = System.currentTimeMillis();
			PMedici pMedici = new PMedici();
			TestSuite mediciTS = pMedici.generateTests(fmName + "_ctwedge_enum.ctw", 2, 6);
			end = System.currentTimeMillis();
			result += fmName + "\t" + ("pmedici\t" + mediciTS.getTests().size() + "\t" + (end - start)) + "\n";
			// pMedici con riduzione
			MinimalityTestSuiteValidator minimality = new MinimalityTestSuiteValidator(mediciTS);
			TestSuite reducedTS = minimality.reduceSize();
			end = System.currentTimeMillis();
			result += fmName + "\t" + ("pmedici\t" + reducedTS.getTests().size() + "\t" + (end - start)) + "\n";
		}
		return result;
	}

}
