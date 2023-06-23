package ctwedge.util.smt;

/*******************************************************************************
 * Copyright (c) 2020 University of Bergamo - Italy
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *   Andrea Bombarda - initial API and implementation
 ******************************************************************************/

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.sosy_lab.common.ShutdownManager;
import org.sosy_lab.common.configuration.Configuration;
import org.sosy_lab.common.configuration.InvalidConfigurationException;
import org.sosy_lab.common.log.BasicLogManager;
import org.sosy_lab.common.log.LogManager;
import org.sosy_lab.java_smt.SolverContextFactory;
import org.sosy_lab.java_smt.SolverContextFactory.Solvers;
import org.sosy_lab.java_smt.api.BooleanFormula;
import org.sosy_lab.java_smt.api.Formula;
import org.sosy_lab.java_smt.api.ProverEnvironment;
import org.sosy_lab.java_smt.api.SolverContext;
import org.sosy_lab.java_smt.api.SolverContext.ProverOptions;
import org.sosy_lab.java_smt.api.SolverException;

import ctwedge.ctWedge.CitModel;
import ctwedge.ctWedge.Constraint;
import ctwedge.ctWedge.Parameter;
import ctwedge.util.validator.SubSetMaker;

public class SMTConstraintChecker {

	private static final Logger logger = Logger.getLogger(SMTConstraintChecker.class);

	public SMTConstraintChecker() {
	}


	/**
	 * return true if consistent
	 * 
	 * @throws InvalidConfigurationException
	 * @throws InterruptedException
	 * @throws SolverException
	 **/
	boolean checkConsistency(CitModel model, Boolean deleteCtx)
			throws InvalidConfigurationException, SolverException, InterruptedException {

		SMTModelTranslator trans = new SMTModelTranslator();
		// Create a new Context
		SolverContext ctx = trans.createCtx();
		ProverEnvironment prover = ctx.newProverEnvironment(ProverOptions.GENERATE_MODELS);

		// Add all the parameters, and their types
		prover = trans.createCtxFromModel(model, model.getConstraints(), ctx, prover);
		Boolean result = prover.isUnsat();

		// Delete the existing context
		if (deleteCtx) {
			ctx.close();
			ctx = trans.createCtx();
		}
		return !result;
	}

	ArrayList<Constraint> findMaxConstraintsSet(CitModel model, Boolean deleteCtx)
			throws InvalidConfigurationException, SolverException, InterruptedException {

		SMTModelTranslator trans = new SMTModelTranslator();
		// Create a new Context
		SolverContext ctx = trans.createCtx();

		// If all the constraints are satisfiable, then return the whole list of
		// constraints
		if (checkConsistency(model, deleteCtx))
			return new ArrayList<Constraint>(model.getConstraints());

		// Create the list of all the subsets of constraints, and sort it by the length
		ArrayList<ArrayList<Constraint>> subSets = SubSetMaker
				.getSubsets(new ArrayList<Constraint>(model.getConstraints()));
		Collections.sort(subSets, new Comparator<ArrayList<Constraint>>() {

			@Override
			public int compare(ArrayList<Constraint> o1, ArrayList<Constraint> o2) {
				Integer size1 = o1.size();
				Integer size2 = o2.size();
				return size2.compareTo(size1);
			}

		});

		// Iterate all the constraints
		for (ArrayList<Constraint> constraints : subSets) {
			if (constraints.size() == 0)
				return null;

			ctx = trans.createCtx();
			ProverEnvironment prover = ctx.newProverEnvironment(ProverOptions.GENERATE_MODELS);
			prover = trans.createCtxFromModel(model, constraints, ctx, prover);

			if (prover.isUnsat()) {
				System.out.println(" non soddisfa ");
			} else {
				logger.debug("soddisfa - constraints: " + constraints.size() + " = " + constraints);
				return constraints;
			}
		}
		return null;
	}

}
