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

import constraints.PropositionalFormula;
import fm.FeatureGroup;
import fm.FeatureModel;
import fm.FeatureModelStatistics;
import fm.FeatureTreeNode;
import fm.RootNode;
import fm.SolitaireFeature;
import fm.XMLFeatureModel;

public class SplotParserExample {

	public static void main(String args[]) {
		new SplotParserExample().parse();
	} 
	
	public void parse() {
		
		try {

			String featureModelFile = "splotmodels/model_20101120_2091447559_monitor_engine" +
					".xml";
			
			/* Creates the Feature Model Object
			 * ********************************
			 * - Constant USE_VARIABLE_NAME_AS_ID indicates that if an ID has not been defined for a feature node
			 *   in the XML file the feature name should be used as the ID. 
			 * - Constant SET_ID_AUTOMATICALLY can be used to let the system create an unique ID for feature nodes 
			 *   without an ID specification
			 *   Note: if an ID is specified for a feature node in the XML file it will always prevail
			 */			
			FeatureModel featureModel = new XMLFeatureModel(featureModelFile, XMLFeatureModel.USE_VARIABLE_NAME_AS_ID);
			
			// Load the XML file and creates the feature model
			featureModel.loadModel();
			
			// A feature model object contains a feature tree and a set of contraints			
			// Let's traverse the feature tree first. We start at the root feature in depth first search.
			System.out.println("FEATURE TREE --------------------------------");
			traverseDFS(featureModel.getRoot(), 0);
			
			// Now, let's traverse the extra constraints as a CNF formula
			System.out.println("EXTRA CONSTRAINTS ---------------------------");
			traverseConstraints(featureModel);

			// Now, let's print some statistics about the feature model
			FeatureModelStatistics stats = new FeatureModelStatistics(featureModel);
			stats.update();
			
			stats.dump();
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
		
	public void traverseDFS(FeatureTreeNode node, int tab) {
		for( int j = 0 ; j < tab ; j++ ) {
			System.out.print('\t');
		}
		// Root Feature
		if ( node instanceof RootNode ) {
			System.out.print("Root");
		}
		// Solitaire Feature
		else if ( node instanceof SolitaireFeature ) {
			// Optional Feature
			if ( ((SolitaireFeature)node).isOptional())
				System.out.print("Optional");
			// Mandatory Feature
			else
				System.out.print("Mandatory");
		}
		// Feature Group
		else if ( node instanceof FeatureGroup ) {
			int minCardinality = ((FeatureGroup)node).getMin();
			int maxCardinality = ((FeatureGroup)node).getMax();
			System.out.print("Feature Group[" + minCardinality + "," + maxCardinality + "]"); 
		}
		// Grouped feature
		else {
			System.out.print("Grouped");
		}
		System.out.print( "(ID=" + node.getID() + ", NAME=" + node.getName() + ")\r\n");
		for( int i = 0 ; i < node.getChildCount() ; i++ ) {
			traverseDFS((FeatureTreeNode )node.getChildAt(i), tab+1);
		}
	}
	
	public void traverseConstraints(FeatureModel featureModel) {
		for( PropositionalFormula formula : featureModel.getConstraints() ) {
			System.out.println(formula);			
		}
	}
	
}
