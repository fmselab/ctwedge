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
import java.util.Iterator;

import javax.naming.OperationNotSupportedException;

import org.junit.Test;

import splar.core.constraints.Assignment;
import splar.core.fm.FeatureModelException;
import splar.core.fm.XMLFeatureModel;
import splar.core.heuristics.FTPreOrderSortedECTraversalHeuristic;
import splar.core.heuristics.VariableOrderingHeuristic;
import splar.core.heuristics.VariableOrderingHeuristicsManager;
import splar.plugins.configuration.bdd.javabdd.catalog.ProductCatalog;
import splar.plugins.reasoners.bdd.javabdd.FMReasoningWithBDD;
import splar.plugins.reasoners.bdd.javabdd.ReasoningWithBDD;

/** get valid configurations using SPLOT/SPLAR
 * 
 *
 */
public class SplotProductsTest {


	@Test
	public void listProducts() throws Exception {
//		String featureModelPath = "splotmodels/connector_fm.xml";
		String featureModelPath = "small_splotexamples/connector_fm_sim.xml";
		listProducts(featureModelPath);

	}

	private static void listProducts(String featureModelPath)
			throws FeatureModelException, Exception,
			OperationNotSupportedException {
		// Create feature model object from an XML file (SXFM format - see
		// www.splot-research.org for details)
		// If an identifier is not provided for a feature use the feature name
		// as id
		splar.core.fm.FeatureModel featureModel = new XMLFeatureModel(
				featureModelPath, XMLFeatureModel.USE_VARIABLE_NAME_AS_ID);
		// load feature model from
		featureModel.loadModel();
		// get products
		ProductCatalog pc = new ProductCatalog(featureModel);
		System.out.println(pc.toString());
		// create BDD variable order heuristic
		new FTPreOrderSortedECTraversalHeuristic("Pre-CL-MinSpan",
				featureModel, FTPreOrderSortedECTraversalHeuristic.FORCE_SORT);
		VariableOrderingHeuristic heuristic = VariableOrderingHeuristicsManager
				.createHeuristicsManager().getHeuristic("Pre-CL-MinSpan");
		// Creates the BDD reasoner
		ReasoningWithBDD reasoner = new FMReasoningWithBDD(featureModel,
				heuristic, 50000, 50000, 60000, "pre-order");

		// Initialize the reasoner (BDD is created at this moment)
		reasoner.init();
		//
		Iterator<Assignment> It = reasoner.iterateOverValidConfigurations();
		while (It.hasNext()){
			System.out.println(It.next());			
		}
	}

}
