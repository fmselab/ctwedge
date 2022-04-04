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

import org.prop4j.And;
import org.prop4j.AtLeast;
import org.prop4j.AtMost;
import org.prop4j.Choose;
import org.prop4j.Equals;
import org.prop4j.Implies;
import org.prop4j.Literal;
import org.prop4j.Node;
import org.prop4j.Not;
import org.prop4j.Or;

 
/** switch node for prop4j */
public abstract class SwitchNode<E> {
	
	protected abstract E visit(And n);

	protected abstract E visit(AtLeast n);

	protected abstract E visit(AtMost n);

	protected abstract E visit(Choose n);

	protected abstract E visit(Equals n);

	protected abstract E visit(Implies n);

	protected abstract E visit(Literal n);
	

	
	E visit(Node n){
		if (n instanceof And)
			return visit((And) n);
		if (n instanceof Or)
			return visit((Or) n);
		if (n instanceof Not)
			return visit((Not) n);
		if (n instanceof Implies)
			return visit((Implies) n);
		if (n instanceof Literal)
			return visit((Literal) n);
		if (n instanceof Equals)
			return visit((Equals) n);
		throw new RuntimeException("not implemented for class " + n.getClass());
		// toDO complete
	}

	protected abstract E visit(Not n);

	protected abstract E visit(Or n);

}
