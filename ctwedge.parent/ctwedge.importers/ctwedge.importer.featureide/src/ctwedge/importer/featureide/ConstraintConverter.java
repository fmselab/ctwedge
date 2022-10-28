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

import java.text.Normalizer;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.xtext.EcoreUtil2;
import org.prop4j.And;
import org.prop4j.AtLeast;
import org.prop4j.AtMost;
import org.prop4j.Choose;
import org.prop4j.Equals;
import org.prop4j.Implies;
import org.prop4j.Literal;
import org.prop4j.Not;
import org.prop4j.Or;

import ctwedge.ctWedge.AndExpression;
import ctwedge.ctWedge.CtWedgeFactory;
import ctwedge.ctWedge.Expression;
import ctwedge.ctWedge.ImpliesExpression;
import ctwedge.ctWedge.ImpliesOperator;
import ctwedge.ctWedge.NotExpression;
import ctwedge.ctWedge.OrExpression;
import ctwedge.util.Pair;
import de.ovgu.featureide.fm.core.base.IFeature;

/**
 * converts the constraints from FeatureIde (prop4j) to Citlab
 *
 * @author garganti
 *
 */
public class ConstraintConverter extends SwitchNode<Expression> {

	Map<String, Expression> litParams;

	ConstraintConverter(Map<IFeature, Pair<Expression, String>> choosenExpr) {
		litParams = new HashMap<>();
		for (Entry<IFeature, Pair<Expression, String>> c : choosenExpr.entrySet()) {
			String name = FeatureIdeImporter.normalize(c.getKey().getName());
			assert litParams.get(name) == null;
			litParams.put(name, c.getValue().getFirst());
		}
	}

	@Override
	protected Expression visit(And n) {
		assert n.getChildren().length == 2 || n.getChildren().length == 1 : n.getChildren()[0];
		Expression n1 = visit(n.getChildren()[0]);
		Expression n2;
		if (n.getChildren().length == 2)
			n2 = visit(n.getChildren()[1]);
		else
			return n1;
		AndExpression and = CtWedgeFactory.eINSTANCE.createAndExpression();
		and.setLeft(n1);
		and.setRight(n2);
		// and.setOp(AndOperators.AND);
		return and;
	}

	@Override
	protected Expression visit(AtLeast n) {
		throw new ConstraintNotSupportedException();
	}

	@Override
	protected Expression visit(AtMost n) {
		throw new ConstraintNotSupportedException();
	}

	@Override
	protected Expression visit(Choose n) {
		throw new ConstraintNotSupportedException();
	}

	@Override
	protected Expression visit(Equals n) {
		assert n.getChildren().length == 2;
		Expression n1 = visit(n.getChildren()[0]);
		Expression n2 = visit(n.getChildren()[1]);
		ImpliesExpression and = CtWedgeFactory.eINSTANCE.createImpliesExpression();
		and.setLeft(n1);
		and.setRight(n2);
		and.setOp(ImpliesOperator.IFF);
		return and;
	}

	@Override
	protected Expression visit(Implies n) {
		assert n.getChildren().length == 2;
		Expression n1 = visit(n.getChildren()[0]);
		Expression n2 = visit(n.getChildren()[1]);
		ImpliesExpression and = CtWedgeFactory.eINSTANCE.createImpliesExpression();
		and.setLeft(n1);
		and.setRight(n2);
		and.setOp(ImpliesOperator.IMPL);
		return and;
	}

	@Override
	protected Expression visit(Literal n) {
		// if n is translated as boolean return n = true
		// if its root is an enumerative, and n is an element of the root return
		// root = n
		Expression eObject = litParams.get(FeatureIdeImporter.normalize((String)n.var));
		assert eObject != null : n.var;
		Expression p = EcoreUtil2.cloneIfContained(eObject);
		return p;
	}

	@Override
	protected Expression visit(Not n) {
		assert n.getChildren().length == 1;
		Expression n1 = visit(n.getChildren()[0]);
		NotExpression not = CtWedgeFactory.eINSTANCE.createNotExpression();
		not.setPredicate(n1);
		return not;
	}

	@Override
	protected Expression visit(Or or) {
		Expression n1 = visit(or.getChildren()[0]);
		Expression n2;
		if (or.getChildren().length == 2) {
			assert or.getChildren().length == 2 : or.getChildren()[0];
			n2 = visit(or.getChildren()[1]);
		} else {
			return n1;
		}

		OrExpression and = CtWedgeFactory.eINSTANCE.createOrExpression();
		and.setLeft(n1);
		and.setRight(n2);
		// and.setOp(OrOperators.OR);
		return and;
	}

}
