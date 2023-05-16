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
package ctwedge.generator.casa;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EObject;

import ctwedge.ctWedge.AtomicPredicate;
import ctwedge.ctWedge.EqualExpression;
import ctwedge.ctWedge.Expression;
import ctwedge.ctWedge.NotExpression;
import ctwedge.util.SimpleExpressionToString;

/**
 * From WIKIPEDIA: In Boolean logic, a formula is in conjunctive normal form
 * (CNF) if it is a conjunction of clauses, where a clause is a disjunction of
 * literals. As a normal form, it is useful in automated theorem proving. It is
 * similar to the product of sums form used in circuit theory.
 * 
 * All conjunctions of literals and all disjunctions of literals are in CNF, as
 * they can be seen as conjunctions of one-literal clauses and conjunctions of a
 * single clause, respectively. As in the disjunctive normal form (DNF), the
 * only propositional connectives a formula in CNF can contain are and, or, and
 * not. The not operator can only be used as part of a literal, which means that
 * it can only precede a propositional variable.
 * 
 * NOTATION: (a \/ b) /\ (c \/d) ... (a+b) * (c+d)
 */
public class CNF {

	List<Clause> clauses;

	CNF() {
		clauses = new ArrayList<>();
	}

	public void add(List<? extends Expression> clause) {
		clauses.add(new Clause(clause));
	}

	@Override
	public String toString() {
		return clauses.toString();
	}

	// it can be a literal?
	// atomic or not atomic
	public static boolean isLiteral(Expression e) {
		return e instanceof AtomicPredicate
				|| (e instanceof EqualExpression)
				|| (e instanceof NotExpression && ((NotExpression) e)
						.getPredicate() instanceof AtomicPredicate);
	}

	/** the number of clauses */
	public int getNumberClauses() {
		return clauses.size();
	}

	/** all the clauses in the CNF */
	public List<Clause> getClauses() {
		return Collections.unmodifiableList(clauses);
	}

	/**
	 * single clause in the CNF expression
	 */
	public class Clause {

		List<Expression> literals;

		Clause(List<? extends Expression> clause) {
			assert (onlyAtomicIn(clause));
			literals = new ArrayList<>();
			literals.addAll(clause);
		}

		private boolean onlyAtomicIn(List<? extends Expression> clause) {
			// for ()
			// TODO
			return true;
		}
		
		public List<Expression> getLiterals(){
			return Collections.unmodifiableList(literals);
		}

		@Override
		public String toString() {
			StringBuffer result = new StringBuffer();
			for (Expression e : literals) {
				String doSwitch = lit2String.doSwitch(e);
				assert doSwitch != null : e.getClass();
				result.append(doSwitch).append('+');
			}
			result.deleteCharAt(result.length() - 1);
			return result.toString();
		}
	}

	static final LiteralToString lit2String = new LiteralToString();

	static class LiteralToString extends SimpleExpressionToString {


		protected LiteralToString() {
			super();
		}

		@Override
		public String doSwitch(EObject eObject) {
			String res = super.doSwitch(eObject);
			assert res != null;
			return res;
		}

		@Override
		public String caseNotExpression(NotExpression object) {
			// if can be not x = false -> ! !x -> x
			Expression notE = object.getPredicate();
			String res = this.doSwitch(notE);
			if (res.length() > 0 && res.charAt(0) == '!')
				return res.substring(1);
			else
				return "!" + res;
		}
		
	}
}
