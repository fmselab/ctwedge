package ctwedge.util.smt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.sosy_lab.common.ShutdownManager;
import org.sosy_lab.common.configuration.Configuration;
import org.sosy_lab.common.configuration.InvalidConfigurationException;
import org.sosy_lab.common.log.BasicLogManager;
import org.sosy_lab.common.log.LogManager;
import org.sosy_lab.java_smt.SolverContextFactory;
import org.sosy_lab.java_smt.SolverContextFactory.Solvers;
import org.sosy_lab.java_smt.api.BooleanFormula;
import org.sosy_lab.java_smt.api.Formula;
import org.sosy_lab.java_smt.api.NumeralFormula.IntegerFormula;
import org.sosy_lab.java_smt.api.ProverEnvironment;
import org.sosy_lab.java_smt.api.SolverContext;

import ctwedge.ctWedge.CitModel;
import ctwedge.ctWedge.Constraint;
import ctwedge.ctWedge.Enumerative;
import ctwedge.ctWedge.Parameter;

/**
 * tarslates a CT model to a SMT problem
 * 
 */
public class SMTModelTranslator {

	public SMTModelTranslator() {
	}

	SolverContext createCtx() throws InvalidConfigurationException {
		Configuration config = Configuration.defaultConfiguration();
		LogManager logger = BasicLogManager.create(config);
		ShutdownManager shutdown = ShutdownManager.create();

		return SolverContextFactory.createSolverContext(config, logger, shutdown.getNotifier(), Solvers.SMTINTERPOL);
	}

	Map<String, List<String>> declaredElements;
	Map<Parameter, List<Formula>> variables;

	public ProverEnvironment createCtxFromModel(CitModel model, List<Constraint> list, SolverContext ctx,
			ProverEnvironment prover) {
		declaredElements = new HashMap<>();
		variables = new HashMap<Parameter, List<Formula>>();
		// Add all the parameters to the new CTX
		SMTModelTranslator.addParameters(model, ctx, declaredElements, variables);
		// add the constrainst due to the parameters
		addConstraintsForSMTParam(ctx, prover);
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

	//
	private void addConstraintsForSMTParam(SolverContext ctx, ProverEnvironment prover) {
		switch (SMTParameterAdder.enumTreatment) {
		// Create the new EnumType
		case INTEGER:
			// Add all the constraints related to the bounds of the enums
			for (Entry<Parameter, List<Formula>> type : variables.entrySet()) {
				Parameter key = type.getKey();
				if (key instanceof Enumerative) {
					List<String> elements = declaredElements.get(key.getName());
					// only one formula for it
					assert type.getValue().size() == 1;
					IntegerFormula smtvar = (IntegerFormula) type.getValue().get(0);
					BooleanFormula tBound = null;
					SMTTestSuiteValidator.logger
							.debug("adding contraints for enum " + key.getName() + " --> " + elements);
					for (int i = 0; i < elements.size(); i++) {
						// enum = i
						// for "i"
						IntegerFormula ith = ctx.getFormulaManager().getIntegerFormulaManager().makeNumber(i);
						BooleanFormula eq = ctx.getFormulaManager().getIntegerFormulaManager().equal(smtvar, ith);
						if (tBound == null)
							tBound = eq;
						else
							tBound = ctx.getFormulaManager().getBooleanFormulaManager().or(tBound, eq);

					}
					// is that possible to have an enum without constants ? is quela to false?
					assert tBound != null;
					// Add the bound constraint
					try {
						prover.addConstraint(tBound);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			return;
		default:
			throw new RuntimeException("enums as " + SMTParameterAdder.enumTreatment);
		}
	}

	private static void addParameters(CitModel model, SolverContext ctx, Map<String, List<String>> declaredElements,
			Map<Parameter, List<Formula>> variables) {
		// Add all the parameters to the
		SMTParameterAdder pa = new SMTParameterAdder(ctx, declaredElements);
		for (Parameter nt : model.getParameters()) {
			List<Formula> variable = pa.doSwitch(nt);
			variables.put(nt, variable);
		}
	}

}
