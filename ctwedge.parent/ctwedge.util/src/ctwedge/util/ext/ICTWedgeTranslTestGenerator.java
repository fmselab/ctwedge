/******************************************************************************* 
 * Copyright (c) 2020 University of Bergamo - Italy 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 *  
 * Contributors: 
 *   Angelo Gargantini - utils and architecture 
 ******************************************************************************/
package ctwedge.util.ext;

import ctwedge.ctWedge.CitModel;

/**
 * The Class ICTWedgeTranslTestGenerator: abstract class for test generators based on the translation of the model to a single String (CASA not suitable for now).
 */

public abstract class ICTWedgeTranslTestGenerator extends ICTWedgeTestGenerator{
	
	public ICTWedgeTranslTestGenerator(String name) {
		super(name);
	}

	/**
	 * Translate model.
	 *
	 * @param citModel the cit model
	 * @param ignoreConstraints the ignore constraints
	 * @return the string representing the translation. Never null - exception if not possible (to check) TODO use Option?
	 */
	public abstract String translateModel(CitModel citModel, boolean ignoreConstraints) throws NotConvertableModel; 
	
}