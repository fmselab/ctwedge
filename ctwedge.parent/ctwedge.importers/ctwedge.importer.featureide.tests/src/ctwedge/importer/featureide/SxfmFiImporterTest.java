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
package ctwedge.importer.featureide;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FilenameFilter;

import org.junit.Assert;
import org.junit.Test;

import citlab.core.ext.NotImportableException;
import ctwedge.ctWedge.CitModel;
import ctwedge.importer.featureide.FeatureIdeImporterMultipleLevels;
import ctwedge.importer.featureide.SxfmFiImporter;

/**
 * convert all the splot models (corrected)
 * 
 * @author garganti
 * 
 */
public class SxfmFiImporterTest{

	@Test
	public void testSplotModels(){
		testModelsIn("splotmodels_new");
	}
	
	private void testModelsIn(String dirPath){
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
			String origin = filename.getAbsolutePath();
			readSXFMModel(origin);
		}
	}

	private CitModel readSXFMModel(String origin) {
		FeatureIdeImporterMultipleLevels importer = new SxfmFiImporter();
		try {
			CitModel result = importer.importModel(origin);
			// check result
			return result;
		} catch (NotImportableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	@Test
	public void testAccents(){
		CitModel result = readSXFMModel("splotmodels_new/model_20091205_755658379.xml");
		assertNotNull(result);
		// org.eclipse.xtext.conversion.ValueConverterException: ID 'Carroceríade4metros' contains invalid characters: 'í' (0xed)
		System.out.println(FeatureIdeModelImporterTest.modelToStringSerializer(result));
	}
}
