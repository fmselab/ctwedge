package ctwedge.fmtester.experiments;

import java.io.IOException;

import ctwedge.util.TestSuite;
import de.ovgu.featureide.fm.core.ExtensionManager.NoSuchExtensionException;
import de.ovgu.featureide.fm.core.io.UnsupportedModelException;

public class SPLC_Experiments_No_Mutations {
	
	/**
	 * Class for performing experiments without applying mutations
	 * 
	 * We pass as argument:
	 *  - The number of threads to be used
	 *  - The name of the original model
	 *  - The name of the evolved model
	 *  - The name of the file to be populated with output
	 * @param args
	 * @throws IOException 
	 * @throws NoSuchExtensionException 
	 * @throws UnsupportedModelException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws IOException, InterruptedException, UnsupportedModelException, NoSuchExtensionException {
		int nThreads = Integer.parseInt(args[0]);
		String originalModel = args[1];
		String evolvedModel = args[2];
		String outputFile = args[3];
		
		TestSimpleExampleForPaper tester = new TestSimpleExampleForPaper();
		
		String oldModel = tester.convertModelFromFMToCTW(originalModel);
		String newModel = tester.convertModelFromFMToCTW(evolvedModel);

		// Technique 1
		TestSuite oldTs = tester.regenerationFromScratch(oldModel, newModel, 2, nThreads, outputFile);
		assert oldTs.getStrength() == 2;
		// Technique 2
		tester.generateWithPMediciPlus(oldModel, newModel, oldTs, 2, nThreads, outputFile);
	}
}
