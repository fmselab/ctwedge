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

import java.util.Set;

import org.eclipse.emf.ecore.util.EcoreUtil;

import ctwedge.ctWedge.AtomicPredicate;
import ctwedge.ctWedge.CitModel;

/**
 * performs the semplification over a CitModel
 * 
 * @author garganti
 *
 */
public class Simplificator {

	private ConstraintSimplifier constSimpl = ConstraintSimplifier.eInstance;

	// MR private ParameterSimplifier paramSimpl = new ParameterSimplifier();

	private CitModel model;

	public Simplificator(CitModel model) {
		// MR paramSimpl = new ParameterSimplifier();
		this.model = model;
	}

	public Set<AtomicPredicate> getRemovedPA() {
		return null;// MR return paramSimpl.removed;

	}

	/**
	 * 
	 * @return build a new simplified model
	 */
	public CitModel getSimplifiedVersion() {
		// build a copy of the model
		CitModel m2 = EcoreUtil.copy(model);
		// simplify the constraints
		constSimpl.simplify(m2);
		// simplify the parameters
		// MR paramSimpl.simplify(m2);
		//
		return m2;
	}
}
