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

import org.junit.Test;

import splar.core.constraints.BooleanVariableInterface;
import splar.core.constraints.CNFClause;
import splar.core.constraints.CNFFormula;
import splar.core.constraints.CNFLiteral;
import splar.core.fm.FeatureGroup;
import splar.core.fm.FeatureModel;
import splar.core.fm.FeatureTreeNode;

/** convert a feature model as boolean problem using the SPLOT framework, which computes also the constraints
 *
 */
public class ToBoolConverter extends Experiment_SPLOTmodels{

	@Test
	public void testSplotModels() throws Exception {
		String featureModelPath = "splotmodels/aircraft_fm.xml";

		FeatureModel splotModel = getSplotModel(featureModelPath);

		System.out.println("model ");
		// print feature
		for( FeatureTreeNode node : splotModel.getNodes() ) {
			if ( !(node instanceof FeatureGroup) ) {
				System.out.println(node.getID());
			}
		}
		// constraints
		CNFFormula formula = splotModel.FT2CNF();		
		printFormula(formula);
		// extra constraints
		printFormula(splotModel.FM2CNF());
				
	}


	private void printFormula(CNFFormula formula) {
		for (CNFClause clause : formula.getClauses()){
			System.out.print("# ");
			boolean first = true;
			for(CNFLiteral lit: clause.getLiterals()){
				BooleanVariableInterface var = lit.getVariable();
				if (first) {
					first = false;
				} else{
					System.out.print(var.getID() + " or " );
				}
				if (lit.isPositive()) {
					System.out.print(var.getID() + " == true" );
				} else{
					System.out.print(var.getID() + " == false" );					
				}
			}
			System.out.println(" #");
		}
	}
}
