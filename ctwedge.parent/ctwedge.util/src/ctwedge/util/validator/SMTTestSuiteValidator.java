package ctwedge.util.validator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.sosy_lab.common.ShutdownManager;
import org.sosy_lab.common.configuration.Configuration;
import org.sosy_lab.common.configuration.InvalidConfigurationException;
import org.sosy_lab.common.log.BasicLogManager;
import org.sosy_lab.common.log.LogManager;
import org.sosy_lab.java_smt.SolverContextFactory;
import org.sosy_lab.java_smt.SolverContextFactory.Solvers;
import org.sosy_lab.java_smt.api.ArrayFormula;
import org.sosy_lab.java_smt.api.ArrayFormulaManager;
import org.sosy_lab.java_smt.api.BooleanFormula;
import org.sosy_lab.java_smt.api.Formula;
import org.sosy_lab.java_smt.api.FormulaType;
import org.sosy_lab.java_smt.api.NumeralFormula.IntegerFormula;
import org.sosy_lab.java_smt.api.ProverEnvironment;
import org.sosy_lab.java_smt.api.SolverContext;
import org.sosy_lab.java_smt.api.SolverContext.ProverOptions;
import org.sosy_lab.java_smt.api.SolverException;

import ctwedge.ctWedge.Bool;
import ctwedge.ctWedge.CitModel;
import ctwedge.ctWedge.Constraint;
import ctwedge.ctWedge.CtWedgeFactory;
import ctwedge.ctWedge.Enumerative;
import ctwedge.ctWedge.Parameter;
import ctwedge.ctWedge.Range;
import ctwedge.ctWedge.impl.EnumerativeImpl;
import ctwedge.util.Pair;
import ctwedge.util.Test;
import ctwedge.util.TestSuite;

public class SMTTestSuiteValidator extends TestSuiteAnalyzer {

	private static final Logger logger = Logger.getLogger(SMTTestSuiteValidator.class);

	public SMTTestSuiteValidator(TestSuite ts) {
		super(ts);
		List<Logger> loggers = Collections.<Logger>list(org.apache.log4j.LogManager.getCurrentLoggers());
		loggers.add(org.apache.log4j.LogManager.getRootLogger());
		for (Logger logger : loggers) {
			logger.setLevel(Level.OFF);
		}
	}

	@SuppressWarnings("rawtypes")
	private boolean covers(Map<Parameter, ?> test, Map<Parameter, ?> requirement) {
		int counter = 0;
		for (Parameter p : requirement.keySet()) {
			Object valueInTest = null;
			for (Entry e : test.entrySet()) {
				if (((Parameter) e.getKey()).getName().equals(p.getName()))
					valueInTest = e.getValue();
			}

			Object valueInReqs = null;
			for (Entry e : requirement.entrySet()) {
				if (((Parameter) e.getKey()).getName().equals(p.getName()))
					valueInReqs = e.getValue();
			}

			if (valueInReqs != null && valueInTest != null)
				if (valueInTest.equals(valueInReqs))
					counter++;
		}

		return counter == requirement.size();

	}

	/**
	 * return if the test suite satisfies all the constraints
	 */
	public Boolean isValid() {
		EList<Constraint> constraints = ts.getModel().getConstraints();
		for (Test t : ts.getTests()) {
			// Check the validity of a test in terms of assignments
			ParametersEvaluator evaluator = new ParametersEvaluator(ts.getModel(), t);
			if (!evaluator.isTestOk()) {
				return false;
			}
			for (Constraint rule : constraints) {
				if (rule.eContainer() instanceof CitModel) {

					RuleEvaluator rl = new RuleEvaluator(t);
					if (!(Boolean) rl.evaluateConstraint(rule))
						return false;
				}
			}
		}
		return true;
	}

	public Integer howManyTestAreValid() {
		int invalidTests = 0;
		EList<Constraint> constraints = ts.getModel().getConstraints();
		for (Test t : ts.getTests()) {
			// Check the validity of a test in terms of assignments
			ParametersEvaluator evaluator = new ParametersEvaluator(ts.getModel(), t);
			if (!evaluator.isTestOk()) {
				invalidTests++;
				continue;
			}
			for (Constraint rule : constraints) {
				if (rule.eContainer() instanceof CitModel) {

					RuleEvaluator rl = new RuleEvaluator(t);
					if (!(Boolean) rl.evaluateConstraint(rule)) {
						invalidTests++;
						break;
					}
				}
			}
		}
		return ts.getTests().size() - invalidTests;
	}

	// check the completness of a test suite
	// validator execption if the solver has some errors (to avoid depenency with sosym solver)
	//
	public Boolean isComplete() throws InterruptedException, ValidatorException {
		try {
			Configuration config = Configuration.defaultConfiguration();
			LogManager logger = BasicLogManager.create(config);
			ShutdownManager shutdown = ShutdownManager.create();

			SolverContext ctx = SolverContextFactory.createSolverContext(config, logger, shutdown.getNotifier(),
					Solvers.SMTINTERPOL);
			ProverEnvironment prover = ctx.newProverEnvironment(ProverOptions.GENERATE_MODELS);

			Set<Map<Parameter, String>> testSuiteSet = getTestMap();
			List<Map<Parameter, String>> listMapReq = getRequirements();
			Iterator<Map<Parameter, String>> iSeed = testSuiteSet.iterator();

			while (iSeed.hasNext()) {
				Map<Parameter, ?> seed = iSeed.next();
				if (!listMapReq.isEmpty()) {
					Iterator<Map<Parameter, String>> iReq = listMapReq.iterator();
					while (iReq.hasNext()) {
						if (covers(seed, iReq.next()))
							iReq.remove();
					}
				}
			}
			Map<String, String> declaredElements = new HashMap<>();
			Map<Parameter, Formula> variables = new HashMap<Parameter, Formula>();
			prover = SMTConstraintChecker.createCtxFromModel(ts.getModel(), ts.getModel().getConstraints(), ctx,
					declaredElements, variables, prover);

			// Prove
			if (prover.isUnsat())
				throw new RuntimeException("The list of constraints is unsatisfiable");

			// Add the n-wise tuple to the context
			Iterator<Map<Parameter, String>> i = listMapReq.iterator();
			return checkRequirementsConsistency(ctx, listMapReq, declaredElements, variables, i, prover);
		} catch (SolverException|InvalidConfigurationException e) {
			throw new ValidatorException(e.getMessage());
		}
	}

	public BooleanFormula extractFormulaFromTuple(SolverContext ctx, Map<String, String> declaredElements,
			Map<Parameter, Formula> variables, Map<Parameter, String> requirement) {
		BooleanFormula t = ctx.getFormulaManager().getBooleanFormulaManager().makeTrue();

		for (Parameter p : requirement.keySet()) {
			BooleanFormula tNew = null;
			Formula varPointer = variables.get(p);
			assert varPointer != null;

			// Check the type of the parameter
			if (p instanceof Enumerative) {
				// Get the left side of the comparison
				Formula leftSide = null;
				for (Entry<Parameter, Formula> e : variables.entrySet()) {
					if (e.getKey().getName().equals(p.getName())) {
						leftSide = e.getValue();
						break;
					}
				}
				// Get the right side of the comparison
				String valueName = requirement.get(p);
				Formula rightSide = null;
				int counter = 0;
				for (Entry<String, String> e : declaredElements.entrySet()) {
					if (e.getKey().equals(valueName)) {
						rightSide = ctx.getFormulaManager().getIntegerFormulaManager().makeNumber(counter);
						break;
					}
					counter++;
				}

				tNew = ctx.getFormulaManager().getIntegerFormulaManager().equal((IntegerFormula) leftSide,
						(IntegerFormula) rightSide);

			} else if (p instanceof Bool) {
				if (requirement.get(p).toLowerCase().equals("true"))
					tNew = (BooleanFormula) varPointer;
				else
					tNew = ctx.getFormulaManager().getBooleanFormulaManager().not((BooleanFormula) varPointer);
			} else if (p instanceof Range) {
				// Get the left side of the comparison
				Formula leftSide = null;
				for (Entry<Parameter, Formula> e : variables.entrySet()) {
					if (e.getKey().getName().equals(p.getName()))
						leftSide = e.getValue();
				}
				// Get the right side of the comparison
				Formula rightSide = ctx.getFormulaManager().getIntegerFormulaManager()
						.makeNumber(Integer.parseInt(requirement.get(p)));

				tNew = ctx.getFormulaManager().getIntegerFormulaManager().equal((IntegerFormula) leftSide,
						(IntegerFormula) rightSide);

				// Add the constraint referring the limits of the range
				int lBound = ((Range) p).getBegin();
				int uBound = ((Range) p).getEnd();
				Formula lBoundFormula = ctx.getFormulaManager().getIntegerFormulaManager().makeNumber(lBound);
				Formula uBoundFormula = ctx.getFormulaManager().getIntegerFormulaManager().makeNumber(uBound);
				tNew = ctx.getFormulaManager().getBooleanFormulaManager().and(tNew,
						ctx.getFormulaManager().getIntegerFormulaManager().greaterOrEquals((IntegerFormula) leftSide,
								(IntegerFormula) lBoundFormula));
				tNew = ctx.getFormulaManager().getBooleanFormulaManager().and(tNew,
						ctx.getFormulaManager().getIntegerFormulaManager().lessOrEquals((IntegerFormula) leftSide,
								(IntegerFormula) uBoundFormula));
			}

			t = ctx.getFormulaManager().getBooleanFormulaManager().and(t, tNew);
		}
		return t;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Formula rangeComposer(Range range, SolverContext ctx) {
		// The Range object can be seen as an array of values
		ArrayFormulaManager afmgr = ctx.getFormulaManager().getArrayFormulaManager();
		ArrayFormula rangeFormula = null;
		int counter = 0;

		String typeName = range.getName();
		rangeFormula = afmgr.makeArray(typeName,
				new FormulaType.ArrayFormulaType<>(FormulaType.IntegerType, FormulaType.IntegerType));

		// Get the list of all possible values
		ArrayList<String> values = new ArrayList<String>(ParameterElementsGetterAsStrings.eInstance.caseRange(range));
		if (values.size() > 1) {
			for (String v : values) {
				afmgr.store(rangeFormula, ctx.getFormulaManager().getIntegerFormulaManager().makeNumber(counter),
						ctx.getFormulaManager().getIntegerFormulaManager().makeNumber(Integer.parseInt(v)));
				counter++;
			}
		} else
			throw new RuntimeException("Not valid");

		return rangeFormula;
	}

	@SuppressWarnings("unused")
	private Boolean checkRequirementsConsistency(SolverContext ctx, List<Map<Parameter, String>> listMapReq,
			Map<String, String> declaredElements, Map<Parameter, Formula> variables, Iterator<Map<Parameter, String>> i,
			ProverEnvironment prover) throws InterruptedException, SolverException {

		ArrayList<Formula> notComplete = new ArrayList<Formula>();

		prover.push();

		// Add all the constraints related to the bounds of the enums
		for (Entry<Parameter, Formula> type : variables.entrySet()) {
			if (type.getKey() instanceof EnumerativeImpl) {
				BooleanFormula tBound = ctx.getFormulaManager().getBooleanFormulaManager().makeFalse();
				int counter = 0;

				for (Entry<String, String> e : declaredElements.entrySet()) {
					if (Arrays.stream(e.getValue().split("")).filter(x -> x.equals(type.getKey().getName()))
							.count() > 0) {
						tBound = ctx.getFormulaManager().getBooleanFormulaManager().or(tBound,
								ctx.getFormulaManager().getIntegerFormulaManager().equal(
										(IntegerFormula) type.getValue(),
										ctx.getFormulaManager().getIntegerFormulaManager().makeNumber(counter)));
					}

					counter++;
				}

				// Add the bound constraint
				prover.addConstraint(tBound);
			}
		}

		while (i.hasNext()) {
			Map<Parameter, String> requirement = i.next();
			logger.debug("checking requirment " + requirement);

			BooleanFormula t = ctx.getFormulaManager().getBooleanFormulaManager().makeTrue();

			prover.push();
			for (Parameter p : requirement.keySet()) {

				BooleanFormula tNew = null;
				Formula varPointer = variables.get(p);
				assert varPointer != null;

				// Check the type of the parameter
				if (p instanceof Enumerative) {
					// Get the left side of the comparison
					Formula leftSide = null;
					for (Entry<Parameter, Formula> e : variables.entrySet()) {
						if (e.getKey().getName().equals(p.getName())) {
							leftSide = e.getValue();
							break;
						}
					}
					// Get the right side of the comparison
					String valueName = requirement.get(p);
					Formula rightSide = null;
					int counter = 0;
					for (Entry<String, String> e : declaredElements.entrySet()) {
						if (e.getKey().equals(valueName)) {
							rightSide = ctx.getFormulaManager().getIntegerFormulaManager().makeNumber(counter);
							break;
						}
						counter++;
					}

					tNew = ctx.getFormulaManager().getIntegerFormulaManager().equal((IntegerFormula) leftSide,
							(IntegerFormula) rightSide);

				} else if (p instanceof Bool) {
					if (requirement.get(p).toLowerCase().equals("true"))
						tNew = (BooleanFormula) varPointer;
					else
						tNew = ctx.getFormulaManager().getBooleanFormulaManager().not((BooleanFormula) varPointer);
				} else if (p instanceof Range) {
					// Get the left side of the comparison
					Formula leftSide = null;
					for (Entry<Parameter, Formula> e : variables.entrySet()) {
						if (e.getKey().getName().equals(p.getName()))
							leftSide = e.getValue();
					}
					// Get the right side of the comparison
					Formula rightSide = ctx.getFormulaManager().getIntegerFormulaManager()
							.makeNumber(Integer.parseInt(requirement.get(p)));

					tNew = ctx.getFormulaManager().getIntegerFormulaManager().equal((IntegerFormula) leftSide,
							(IntegerFormula) rightSide);

					// Add the constraint referring the limits of the range
					int lBound = ((Range) p).getBegin();
					int uBound = ((Range) p).getEnd();
					Formula lBoundFormula = ctx.getFormulaManager().getIntegerFormulaManager().makeNumber(lBound);
					Formula uBoundFormula = ctx.getFormulaManager().getIntegerFormulaManager().makeNumber(uBound);
					tNew = ctx.getFormulaManager().getBooleanFormulaManager().and(tNew,
							ctx.getFormulaManager().getIntegerFormulaManager()
									.greaterOrEquals((IntegerFormula) leftSide, (IntegerFormula) lBoundFormula));
					tNew = ctx.getFormulaManager().getBooleanFormulaManager().and(tNew,
							ctx.getFormulaManager().getIntegerFormulaManager().lessOrEquals((IntegerFormula) leftSide,
									(IntegerFormula) uBoundFormula));
				}

				t = ctx.getFormulaManager().getBooleanFormulaManager().and(t, tNew);
			}

			// Add the new constraint
			if (t != null)
				prover.addConstraint(t);

			// If the tuple is feasible, it returns false since the test suite is missing a
			// requirement
			if (!prover.isUnsat()) {
				notComplete.add(t);
				System.out.println(prover.getModelAssignments());
			}

			prover.pop();
		}

		// Terminate the context
		ctx.close();

		// Print all the formulas that have not been satisfied
		notComplete.forEach(x -> System.out.println(x));

		// If no return has been executed before, the requirements are consistent
		return notComplete.size() == 0;
	}

	private List<Map<Parameter, String>> getRequirements() {
		CitModel model = ts.getModel();
		Iterator<List<Pair<Parameter, String>>> reqs = ParameterSwitchToPairStrings.getTuples(model, ts.getStrength());

		List<Map<Parameter, String>> ListMapReq = new ArrayList<Map<Parameter, String>>();
		// Trasformo la lista di liste in una map scindendo i pairelement in key e value
		while (reqs.hasNext()) {
			List<Pair<Parameter, String>> req = reqs.next();
			Map<Parameter, String> map = new HashMap<Parameter, String>();
			for (Pair<Parameter, String> e : req) {
				map.put(e.getFirst(), e.getSecond());

			}
			ListMapReq.add(map);

		}

		return ListMapReq;
	}

	@SuppressWarnings("rawtypes")
	private Set<Map<Parameter, String>> getTestMap() {
		Set<Map<Parameter, String>> testSuiteSet = new HashSet<Map<Parameter, String>>();

		for (Test t : this.ts.getTests()) {
			Map<Parameter, String> singleMap = new HashMap<>();
			for (Entry e : t.entrySet()) {
				Parameter p = CtWedgeFactory.eINSTANCE.createParameter();
				p.setName((String) e.getKey());
				singleMap.put(p, (String) e.getValue());
			}
			testSuiteSet.add(singleMap);
		}

		return testSuiteSet;
	}

	public int howManyTuplesCovers() {
		Set<Map<Parameter, String>> testSuiteSet = getTestMap();
		List<Map<Parameter, String>> listMapReq = getRequirements();
		Iterator<Map<Parameter, String>> iSeed = testSuiteSet.iterator();
		int covered = 0;
		if (!listMapReq.isEmpty()) {
			while (iSeed.hasNext()) {
				Iterator<Map<Parameter, String>> iReq = listMapReq.iterator();
				Map<Parameter, ?> seed = iSeed.next();
				while (iReq.hasNext()) {
					if (covers(seed, iReq.next())) {
						covered = covered + 1;
						iReq.remove();
					}
				}
			}
		}
		return covered;
	}
}