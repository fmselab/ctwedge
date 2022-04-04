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
package ctwedge.util.simplifier;

import ctwedge.ctWedge.CitModel;

/**
 * Represents several possible simplification of citmodels
 * 
 * @author garganti
 *
 */
public abstract class Simplifier {
		
	/**
	 * simplify the model model (it can change it)
	 * 
	 * @param model
	 */
	abstract void simplify(CitModel model);	
	
	// TODO: add a concretizer of test suites?
	
}
