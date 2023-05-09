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

import static ctwedge.importer.featureide.FeatureIdeModelImporterTest.FI_MODELS_DIR;
import static org.junit.Assert.assertNotNull;

import java.io.FileNotFoundException;

import org.junit.Assert;
import org.junit.Test;

import ctwedge.ctWedge.CitModel;
import ctwedge.util.simplifier.Simplificator;
import de.ovgu.featureide.fm.core.io.UnsupportedModelException;

/**
 * test the simplification over the citmodels
 *
 */
public class FeatureIdeModelSimplifierTest {

	@Test
	public void readmodelDB() throws FileNotFoundException,
			UnsupportedModelException {
		CitModel cellPhone = FeatureIdeModelImporterTest
				.readModel(FI_MODELS_DIR +"modelDB.xml");
		System.out.println("original size " + cellPhone.getParameters().size());
		CitModel c2 = new Simplificator(cellPhone).getSimplifiedVersion();
		System.out.println("new size " + c2.getParameters().size());
	}

	
	@Test
	public void readCellPhone() throws FileNotFoundException,
			UnsupportedModelException {
		CitModel cellPhone = FeatureIdeModelImporterTest
				.readModel(FI_MODELS_DIR +"cellphone_15.xml");
		System.out.println("original size " + cellPhone.getParameters().size());
		CitModel c2 = new Simplificator(cellPhone).getSimplifiedVersion();
		System.out.println("new size " + c2.getParameters().size());
		int count = CombinationCounter.count(c2);
		System.out.println(count);
		Assert.assertEquals(61, count);
	}
	@Test
	public void readConnector() throws FileNotFoundException,
			UnsupportedModelException {
		CitModel cellPhone = FeatureIdeModelImporterTest.readModel(FI_MODELS_DIR +"connector_fm.xml");
		CitModel c2 = new Simplificator(cellPhone).getSimplifiedVersion();
		int count = CombinationCounter.count(c2);
		System.out.println(count);
		System.out.println(FeatureIdeModelImporterTest.modelToStringSerializer(c2));
		Assert.assertEquals(18, count);
	}
	@Test
	public void readConnectorSimple() throws FileNotFoundException,
			UnsupportedModelException {
		CitModel cellPhone = FeatureIdeModelImporterTest.readModel(FI_MODELS_DIR +"connector_fm_sim.xml");
		CitModel c2 = new Simplificator(cellPhone).getSimplifiedVersion();
		int count = CombinationCounter.count(c2);
		System.out.println(count);
		System.out.println(FeatureIdeModelImporterTest.modelToStringSerializer(c2));
		Assert.assertEquals(4, count);
	}
	@Test
	public void readInsurancePolicy() throws FileNotFoundException,
			UnsupportedModelException {
		CitModel cellPhone = FeatureIdeModelImporterTest.readModel(FI_MODELS_DIR +"model_20091109_376438407.xml");
		CitModel c2 = new Simplificator(cellPhone).getSimplifiedVersion();
		int count = CombinationCounter.count(c2);
		System.out.println(count);
		System.out.println(FeatureIdeModelImporterTest.modelToStringSerializer(c2));
		Assert.assertEquals(1248, count);
	}


	@Test
	public void readMotorEngine() throws FileNotFoundException,
			UnsupportedModelException {
		CitModel cellPhone = FeatureIdeModelImporterTest
				.readModel(FI_MODELS_DIR +"model_20101120_2091447559_monitor_engine_new.xml");
		System.out.println("original size " + cellPhone.getParameters().size());
		CitModel c2 = new Simplificator(cellPhone).getSimplifiedVersion();
		System.out.println("new size " + c2.getParameters().size());
		int count = CombinationCounter.count(c2);
		System.out.println(count);
		//Assert.assertEquals(48, count);
		//
		System.out.println(FeatureIdeModelImporterTest.modelToStringSerializer(cellPhone));
		System.out.println(FeatureIdeModelImporterTest.modelToStringSerializer(c2));
		
	}
	@Test
	public void readCIMSProductLine() throws FileNotFoundException,
			UnsupportedModelException {
		SxfmFiImporter importer = new SxfmFiImporter();
		CitModel model = importer.importModel("splotmodels_new/model_20100412_337845737.xml");
		assertNotNull(model);
		CitModel c2 = new Simplificator(model).getSimplifiedVersion();
		System.out.println("new size " + c2.getParameters().size());
	}
	
}
