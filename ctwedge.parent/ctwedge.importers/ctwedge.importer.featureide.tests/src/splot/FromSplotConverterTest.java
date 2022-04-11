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

import static org.junit.Assert.fail;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import citlab.core.ext.NotImportableException;
import ctwedge.ctWedge.CitModel;
import ctwedge.importer.featureide.FeatureIdeModelImporterTest;
import ctwedge.importer.featureide.SxfmFiImporter;
import citlab.model.simplifier.Simplificator;


/**
 * test the conversion from SPLOT via featureide to check if it works
 * 
 * @author garganti
 * 
 */
public class FromSplotConverterTest {

	@Test
	public void CIMSProductLine() throws NotImportableException {
		CitModel m = readSPLTOmodel("splotmodels/model_20100412_337845737.xml");
		System.out.println(FeatureIdeModelImporterTest
				.modelToStringSerializer(m));
		// simplify
		CitModel ms = new Simplificator(m).getSimplifiedVersion();
		System.out.println(FeatureIdeModelImporterTest
				.modelToStringSerializer(ms));
	}

//	@Test
//	public void testAlloriginalModels(){
//		List<String> testModelsIn = testModelsIn("splotmodels");
//		if (testModelsIn.isEmpty()) return;
//		System.err.println(testModelsIn);
//		fail("some models are not imported");
//	}

	@Test
	public void testAllmodifiedModels(){
		List<String> testModelsIn = testModelsIn("ValidationSet");
		if (testModelsIn.isEmpty()) return;
		System.err.println(testModelsIn);
		fail("some models are not imported");
	}
	
	/** returns the mdoel which could not be imported
	 * 
	 * @param dirPath
	 * @return
	 */
	private List<String> testModelsIn(String dirPath){
		List<String> notImported = new ArrayList<>();
		File dir = new File(dirPath);
		Assert.assertTrue(dir.isDirectory());
		File[] children = dir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".m");
			}
		});
		for (int i = 0; i < children.length; i++) {
			// Get filename of file or directory
			File filename = children[i];
			try {
				CitModel model = readSPLTOmodel(filename.getAbsolutePath());
			    model = new Simplificator(model).getSimplifiedVersion();
				if(model.getParameters().size()==0)
					continue;
				System.out.println(model.getName());
				
				File resultFile = new File("modelCITLV"+File.separator+model.getName()+".citl");
				if(resultFile.exists())
					 resultFile = new File("modelCITLV"+File.separator+model.getName()+i+".citl");
				FileOutputStream fileOutputStream = new FileOutputStream(resultFile);
				PrintStream fileStdOutput = new PrintStream(new BufferedOutputStream(
						fileOutputStream));
				fileStdOutput.print(FeatureIdeModelImporterTest
				.modelToStringSerializer(model,model.getName()));
				fileStdOutput.close();
			} catch (NotImportableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				notImported.add(filename.getAbsolutePath());
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
		return notImported;
	}

	CitModel readSPLTOmodel(String path) throws NotImportableException {
		// leggi il file SXFM usando l'importer di citlab
		SxfmFiImporter importer = new SxfmFiImporter();
		CitModel model = importer.importModel(path);
		return model;
	}
}
