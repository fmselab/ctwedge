/*******************************************************************************
 * Copyright (c) 2013 University of Bergamo - Italy
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *   Paolo Vavassori - initial API and implementation
 *   Angelo Gargantini - utils and architecture
 ******************************************************************************/
package splot;

import java.io.File;
import java.io.FilenameFilter;

import org.junit.Assert;
import org.junit.Test;

import citlab.core.ext.NotImportableException;
import ctwedge.ctWedge.CitModel;
import ctwedge.importer.featureide.SxfmFiImporter;
import citlab.model.simplifier.Simplificator;
import citlab.model.util.CombinationCounter;
import de.ovgu.featureide.fm.core.FeatureModel;
import de.ovgu.featureide.fm.core.io.UnsupportedModelException;
import de.ovgu.featureide.fm.core.io.sxfm.SXFMReader;

/**
 * check the product number in citlab and in splot should be the same
 * 
 * @author garganti
 * 
 */
public class Experiment_ProductsCounter extends Experiment_SPLOTmodels{

	private static final int COMBINATIONS_LIMIT = 100000;


	@Test
	public void testSplotModels() throws Exception {
		testModelsIn("splotmodels_new");
	}

/*@Test
	public void testSmallExamples() throws Exception {
		testModelsIn("small_splotexamples");
	}*/

	private void testModelsIn(String dirPath) throws Exception {
		File dir = new File(dirPath);
		Assert.assertTrue(dir.isDirectory());
		File[] children = dir.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".xml");
			}
		});
		for (int i = 0; i < children.length; i++) {
			// Get filename of file or directory
			File filename = children[i];
			checkSPLOTmodel(filename.getAbsolutePath());
		}
	}

	/** return true if successful, false if not done, exception if wrong
	 * 
	 * @param path
	 * @param destination
	 * @return
	 * @throws Exception
	 */
	public boolean checkSPLOTmodel(String path)
			throws Exception {
		// leggi il file SPLOT using the FeatureiDE reader
		FeatureModel fm = new FeatureModel();
		
		SXFMReader reader = new SXFMReader(fm);
		File modelFile = new File(path);
		assert modelFile.exists() && modelFile.isFile();
		try {
			reader.readFromFile(modelFile);
		} catch (UnsupportedModelException e) {
			System.err.println("SKIPPING " + path + " cause :" + e.getMessage());
			return false;
		}
		splar.core.fm.FeatureModel model = getSplotModel(path);
		// facciamo analisi di quello nuovo - numero prodotti
		long numberOfCombinationsSPLOT = getNumberOfValidProducts(model);
		System.out.println(numberOfCombinationsSPLOT);
		if (numberOfCombinationsSPLOT < COMBINATIONS_LIMIT) {
           System.out.println("FILE:      "+ path);
			// il numero di prodotti calcolato da noi
			int numberOfCombinationsCitLab = getNumberOfCombinationsCitLab(path);
			//
			if (numberOfCombinationsCitLab != numberOfCombinationsSPLOT) {
				throw new RuntimeException("error in " + path);
			}
			return true;
		} else {
			System.err.println("SKIPPING " + path + " too many products");
			return false;
		}
	}


	private int getNumberOfCombinationsCitLab(String newPath)
			throws NotImportableException {
		// leggi il file SXFM usando l'importer
		SxfmFiImporter importer = new SxfmFiImporter();
		CitModel model = importer.importModel(newPath);
		// get the simplified version
		CitModel sModel = new Simplificator(model).getSimplifiedVersion();
		return CombinationCounter.count(sModel);
	}
}
