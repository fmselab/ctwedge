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
package ctwedge.ui.outline;

import org.eclipse.xtext.ui.editor.outline.IOutlineNode;
import org.eclipse.xtext.ui.editor.outline.actions.SortOutlineContribution.DefaultComparator;
import org.eclipse.xtext.ui.editor.outline.impl.EObjectNode;

import ctwedge.ctWedge.CtWedgePackage;

public class CTWedgeOutlineNodeComparator extends DefaultComparator {
	@Override
	public int getCategory(IOutlineNode node) {
		if (node instanceof EObjectNode)
			switch (((EObjectNode) node).getEClass().getClassifierID()) {
			// MR commented out
//			case CtWedgePackage.CONSTANT:
//				return 1;
//			case CtWedgePackage.ENUMERATIVE_TYPE:
//				return 10;
//			case CtWedgePackage.BOOLEAN:
//				return 20;
//			case CtWedgePackage.NUMBERS:
//				return 30;
			case CtWedgePackage.RANGE:
				return 40;
			case CtWedgePackage.ENUMERATIVE:
				return 50;
//			case CtWedgePackage.SEED:
//				return 60;
//			case CtWedgePackage.EXPECTED_CONDITION:
//				return 70;
//				
			}
		return 90;
	}
}
