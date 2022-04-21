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

import de.ovgu.featureide.fm.core.FeatureModel;
import de.ovgu.featureide.fm.core.io.UnsupportedModelException;
import de.ovgu.featureide.fm.core.io.sxfm.SXFMReader;
import de.ovgu.featureide.fm.core.io.sxfm.SXFMWriter;

/**
 * convert all the splot models using the double conversion of feature ide (read an write).
 * All the resulting models will be read by featureide
 * 
 * @author garganti
 * 
 */
public class Experiment_ConvertSPLOTmodels extends Experiment_SPLOTmodels{

	@Test
	public void testSplotModels() throws Exception {
		testModelsIn("splotmodels");
	}

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
			boolean success = convertSPLOTmodel(filename.getAbsolutePath(), "splotmodels_new");
			if (! success){
				System.err.println("CONVERSION "+ filename + " has problems");
			}
			
		}
	}

	/** return true if successful, false if not done, exception if wrong
	 * 
	 * @param origin
	 * @param destination directory where to save the converted file
	 * @return
	 * @throws Exception
	 */
	public boolean convertSPLOTmodel(String origin, String destination)
			throws Exception {
		// preconditions
		File modelFileOrigin = new File(origin);
		assert modelFileOrigin.exists() && modelFileOrigin.isFile();
		assert new File(destination).isDirectory();
		//
		// read the same SPLOT file using the FeatureiDE reader
		FeatureModel fm_original = new FeatureModel();		
		SXFMReader reader = new SXFMReader(fm_original);
		try {
			reader.readFromFile(modelFileOrigin);
		} catch (UnsupportedModelException e) {
			System.err.println("SKIPPING " + origin + " cause :" + e.getMessage());
			return false;
		}
		// save with the same name in the other directory (same format sxfm)
		// using the featureidewriter
		SXFMWriter writer = new SXFMWriter(fm_original);
		String newPath = destination + File.separator + modelFileOrigin.getName();
		writer.writeToFile(new File(newPath));
		//
		// perform the analysis using the SPLAR reader and analyzer
		// take the two models 
		splar.core.fm.FeatureModel originalSplotModel = getSplotModel(origin);
		splar.core.fm.FeatureModel newSplotModel = getSplotModel(newPath);
		// number of nodes (should be the same)
		int nNodes = originalSplotModel.getNodes().size();
		int nNodesP = newSplotModel.getNodes().size();
		System.out.print(nNodes + " : " +  nNodesP + " ");	
		if (nNodes !=  nNodesP) return false;
		// number of valid products
		long splotModelNproducts = getNumberOfValidProducts(originalSplotModel);
		long splotModelNproductsP = getNumberOfValidProducts(newSplotModel);
		System.out.println(splotModelNproducts + " : " +  splotModelNproductsP);
		if (splotModelNproducts !=  splotModelNproductsP) return false;
		return true;
	}

}
