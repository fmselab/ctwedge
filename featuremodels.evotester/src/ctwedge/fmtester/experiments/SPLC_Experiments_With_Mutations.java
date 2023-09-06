package ctwedge.fmtester.experiments;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import ctwedge.util.TestSuite;
import de.ovgu.featureide.fm.core.ExtensionManager.NoSuchExtensionException;
import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.io.UnsupportedModelException;
import de.ovgu.featureide.fm.core.io.manager.SimpleFileHandler;
import de.ovgu.featureide.fm.core.io.xml.XmlFeatureModelFormat;
import fmautorepair.mutationoperators.FMMutation;
import fmautorepair.mutationoperators.FMMutator;
import fmautorepair.mutationprocess.FMMutationProcess;
import fmautorepair.utils.Utils;

public class SPLC_Experiments_With_Mutations {
	
	private static final int MAX_MUTATIONS = 10;
	
	/**
	 * Class for performing experiments and applying mutations
	 * 
	 * We pass as argument:
	 *  - The number of threads to be used
	 *  - The name of the original model
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
		String outputFile = args[2];
		
		TestSimpleExampleForPaper tester = new TestSimpleExampleForPaper();
		
		// Read the feature model to be mutated
		IFeatureModel fm = Utils.readModel(originalModel + ".xml");
		String oldModel = tester.convertModelFromFMToCTW(originalModel);

		// Define the mutators
		List<FMMutator> mutatorList = FMMutationProcess.allMutationOperatorsAsList();

		// Repeat the experiments for higher number of mutations
		int nModel = 0;
		Random generator = new Random();
		for (int j = 1; j <= MAX_MUTATIONS; j++) {
			// Extract the index of mutations
			ArrayList<Integer> mutationIndex = new ArrayList<>();
			for (int i = 0; i < j; i++) {
				mutationIndex.add(generator.nextInt(mutatorList.size()));
			}
			// Now, apply the mutations to the model
			for (int index : mutationIndex) {
				ArrayList<FMMutation> mutationsList = new ArrayList<>();
				Iterator<FMMutation> mutations = mutatorList.get(index).mutate(fm);
				while (!mutations.hasNext()) {
					index = generator.nextInt(mutatorList.size());
					mutations = mutatorList.get(index).mutate(fm);
					System.out.println("New index");
				}
				mutations.forEachRemaining(mutationsList::add);
				int mutationListIndex = generator.nextInt(mutationsList.size());
				fm = mutationsList.get(mutationListIndex).getFirst();
			}

			// Then, execute the test using the two different techniques
			String newModel = "";
			try {
				newModel = tester.convertModelFromFMToCTW(fm, originalModel + "_" + nModel);
				SimpleFileHandler.save(new File(newModel + ".xml").toPath(), fm, new XmlFeatureModelFormat());
				// Technique 1
				TestSuite oldTs = tester.regenerationFromScratch(oldModel, newModel, 2, nThreads, outputFile, j);
				assert oldTs.getStrength() == 2;
				// Technique 2
				tester.generateWithPMediciPlus(oldModel, newModel, oldTs, 2, nThreads, outputFile, j);
			} catch (Exception e) {
				continue;
			}
			nModel++;
		}
	}
}
