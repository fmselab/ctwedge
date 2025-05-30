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

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;

import org.junit.Assert;
import org.junit.Test;

import ctwedge.ctWedge.CitModel;
import ctwedge.util.ModelUtils;
import ctwedge.util.ext.NotImportableException;
import de.ovgu.featureide.fm.core.io.UnsupportedModelException;
/**
 * 
 * test of the importer from feature IDE (not simplifier) 
 *
 */
public class FeatureIdeModelImporterTest {

	public static final String FI_MODELS_DIR = "models/featureide/";

	
	@Test
	public void readAllModels() throws FileNotFoundException,	
	UnsupportedModelException {
		File dir = new File(FI_MODELS_DIR);
		for (String f: dir.list(new FilenameFilter() {			
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".xml");
			}
		})){
			try{
				readModel(FI_MODELS_DIR + File.separator + f);			
			} catch(Exception e) {
				System.err.println("error reading " + f);
				e.printStackTrace();
			}
		}
	}
	
	@Test
	public void readCellPhone() throws FileNotFoundException,
			UnsupportedModelException {
		CitModel cellPhone = readModel(FI_MODELS_DIR +"cellphone_15.xml");
		//int count = CombinationCounter.count(cellPhone);
		//System.out.println("count XXXX" + count);
		ModelUtils mu = new ModelUtils(cellPhone);
		String s = mu.serializeToString();
		System.out.println(s);
		//
		assertTrue(s.contains("# Cellphone == TRUE #"));
		assertTrue(s.contains("# Camera != NONE => Extras == TRUE #"));
	}

	@Test
	public void readConnector() throws FileNotFoundException,
			UnsupportedModelException {
		CitModel cellPhone = readModel(FI_MODELS_DIR +"connector_fm.xml");
		int count = CombinationCounter.count(cellPhone);
		System.out.println(count);
		Assert.assertEquals(18, count);

	}

	@Test
	public void readConnectorSimple() throws FileNotFoundException,
			UnsupportedModelException {
		CitModel connector = FeatureIdeModelImporterTest.readModel(FI_MODELS_DIR +"connector_fm_sim.xml");
		int count = CombinationCounter.count(connector);
		Assert.assertEquals(4, count);
	}

	@Test
	public void readModel_Alt_Alt() throws FileNotFoundException,
			UnsupportedModelException {
		// [model != NONE, a1!= NONE <=>model = a1]
		CitModel ct = readModel(FI_MODELS_DIR +"model_Alt_Alt.xml");
		ModelUtils mu = new ModelUtils(ct);
		String s = mu.serializeToString();
		System.out.println(s);
		//
		assertTrue(s.contains("# model != NONE #"));
		assertTrue(s.contains("# a1 != NONE <=> model == a1 #"));
	}

	@Test
	public void readModel_Alt_Or() throws FileNotFoundException,
			UnsupportedModelException {
		// [model != NONE, model = a1 => OR a11 OR a12, a11 = true => model =
		// a1, a12 = true => model = a1]
		readModel(FI_MODELS_DIR +"model_Alt_Or.xml");		
	}

	@Test
	public void readModel_Alt_Root() throws FileNotFoundException,
			UnsupportedModelException {
		// root is an alternative
		// [model != NONE]
		readModel(FI_MODELS_DIR +"model_Alt.xml");
	}

	@Test
	public void readModel_Alt_SubRoot() throws FileNotFoundException,
			UnsupportedModelException {
		// [model = true, A != NONE IFF model = true, B != NONE IFF model =
		// true]
		readModel(FI_MODELS_DIR +"model2.xml");
	}

	@Test
	public void readModel_Alt_SubRoot2() throws FileNotFoundException,
			UnsupportedModelException {
		// [model != NONE, B = true IFF model = a1, C = true => model = a2]
		readModel(FI_MODELS_DIR +"model_Alt_sub1.xml");
	}

	@Test
	public void readModel_Man2() throws FileNotFoundException,
			UnsupportedModelException {	
		// mandatory - mandatory
		CitModel ct = readModel(FI_MODELS_DIR +"model_Man_Man.xml");
		ModelUtils mu = new ModelUtils(ct);
		String s = mu.serializeToString();
		//
		assertTrue(s.contains("# model == TRUE #"));
		assertTrue(s.contains("# a1 == TRUE <=> model == TRUE #"));
		assertTrue(s.contains("# a11 == TRUE <=> a1 == TRUE #"));
	}
	@Test
	public void readModel_Or_Alt() throws FileNotFoundException,
			UnsupportedModelException {
		// [model != NONE, model = a1 => OR a11 OR a12, a11 = true => model =
		// a1, a12 = true => model = a1]
		readModel(FI_MODELS_DIR +"model_Or_Alt.xml");
	}
	@Test
	public void readModel_Or_Or() throws FileNotFoundException,
			UnsupportedModelException {
		// [model != NONE, model = a1 => OR a11 OR a12, a11 = true => model =
		// a1, a12 = true => model = a1]
		CitModel ct = readModel(FI_MODELS_DIR +"model_Or_Or.xml");
		ModelUtils mu = new ModelUtils(ct);
		String s = mu.serializeToString();
		//
		assertTrue(s.contains("# model == TRUE #"));
	}

	@Test
	public void readModelGraphLibrary() throws FileNotFoundException,
			UnsupportedModelException {
		CitModel ct = readModel(FI_MODELS_DIR +"model_GPLTiny.xml");
		ModelUtils mu = new ModelUtils(ct);
		String s = mu.serializeToString();
	}
	

	@Test
	public void readModel_OR_Root() throws FileNotFoundException,
			UnsupportedModelException {
		// [model = true, model = true => OR a1 OR a2 OR a3 OR a4,
		// a1 = true => model = true, a2 = true => model = true, a3 = true =>
		// model = true, a4 = true => model = true]
		CitModel ct = readModel(FI_MODELS_DIR +"model3_OR.xml");
		ModelUtils mu = new ModelUtils(ct);
		String s = mu.serializeToString();
		//
		assertTrue(s.contains("# model == TRUE #"));
		assertTrue(s.contains("# model == TRUE => a1 == TRUE || a2 == TRUE || a3 == TRUE || a4 == TRUE #"));
		assertTrue(s.contains("# a1 == TRUE => model == TRUE #"));
		//and the others
	}

	@Test
	public void readSmartHome() throws FileNotFoundException,
			UnsupportedModelException {
		CitModel cellPhone = readModel(FI_MODELS_DIR +"smart_phone.xml");
		// TOOMANY
		//int count = CombinationCounter.count(cellPhone);
		//System.out.println(count);

	}

	
	/** returns the citmodel as String
	 * 
	 * @param result
	 * @return
	 */
	public static String modelToStringSerializer(CitModel result) {
		ModelUtils m = new ModelUtils(result);
		return m.serializeToString();		
	}
	public static String modelToStringSerializer(CitModel result,String name) {
		// TODO pass also the name
		ModelUtils m = new ModelUtils(result);
		return m.serializeToString();		
	}   
	/**
	 * read the feature ide model
	 * 
	 * @param modelPath
	 *            path
	 * @return
	 * @throws FileNotFoundException
	 * @throws UnsupportedModelException
	 */
	static public CitModel readModel(String modelPath)
			throws FileNotFoundException, UnsupportedModelException {

		FeatureIdeImporter importer = new XmlFeatureModelImporter();
		CitModel result;
		try {
			result = importer.importModel(modelPath);
			// check the validity of the model - for now serialize it
			System.out.println(modelToStringSerializer(result));
			return result;
		} catch (NotImportableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}		
	}

}
