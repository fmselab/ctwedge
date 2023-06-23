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

	SolverContext createCtx() throws InvalidConfigurationException {
		Configuration config = Configuration.defaultConfiguration();
		LogManager logger = BasicLogManager.create(config);
		ShutdownManager shutdown = ShutdownManager.create();

		return SolverContextFactory.createSolverContext(config, logger, shutdown.getNotifier(), Solvers.SMTINTERPOL);
	}

	public static ProverEnvironment createCtxFromModel(CitModel model, List<Constraint> list, SolverContext ctx, ProverEnvironment prover) {
		Map<String, List<String>> declaredElements = new HashMap<>();
		Map<Parameter, List<Formula>> variables = new HashMap<Parameter, List<Formula>>();
		return createCtxFromModel(model, model.getConstraints(), ctx, declaredElements, variables, prover);
	}
	
	
	public static ProverEnvironment createCtxFromModel(CitModel model, List<Constraint> list, SolverContext ctx,
			Map<String, List<String>> declaredElements, Map<Parameter, List<Formula>> variables, ProverEnvironment prover) {
		// Add all the parameters to the new CTX
		addParameters(model, ctx, declaredElements, variables);
		// Translate all the constraints and add them to the context
		for (Constraint r : list) {
			SMTConstraintTranslator translator = new SMTConstraintTranslator(ctx, variables, declaredElements);
			Formula constraint = translator.doSwitch(r);
			assert constraint instanceof BooleanFormula : "Constraints must be boolean";
			// Add this constraint
			try {
				prover.addConstraint((BooleanFormula) constraint);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		return prover;
	}

	static void addParameters(CitModel model, SolverContext ctx, Map<String, List<String>> declaredElements,
			Map<Parameter, List<Formula>> variables) {
		// Add all the parameters to the
		SMTParameterAdder pa = new SMTParameterAdder(ctx, declaredElements);
		for (Parameter nt : model.getParameters()) {
			List<Formula> variable = pa.doSwitch(nt);
			variables.put(nt, variable);
		}
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

		// Create a new Context
		SolverContext ctx = createCtx();
		ProverEnvironment prover = ctx.newProverEnvironment(ProverOptions.GENERATE_MODELS);

		// Add all the parameters, and their types
		prover = createCtxFromModel(model, model.getConstraints(), ctx, prover);
		Boolean result = prover.isUnsat();

		// Delete the existing context
		if (deleteCtx) {
			ctx.close();
			ctx = createCtx();
		}

		return !result;
	}

	ArrayList<Constraint> findMaxConstraintsSet(CitModel model, Boolean deleteCtx)
			throws InvalidConfigurationException, SolverException, InterruptedException {

		// Create a new Context
		SolverContext ctx = createCtx();

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

			ctx = createCtx();
			ProverEnvironment prover = ctx.newProverEnvironment(ProverOptions.GENERATE_MODELS);
			HashMap<String, List<String>> declaredElements = new HashMap<>();
			HashMap<Parameter, List<Formula>> variables = new HashMap<Parameter, List<Formula>>();
			prover = createCtxFromModel(model, constraints, ctx, declaredElements, variables, prover);

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
