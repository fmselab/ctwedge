package ctwedge.util.validator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.prefs.Preferences;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.emf.common.util.EList;
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
import org.sosy_lab.java_smt.api.SolverException;
import org.sosy_lab.java_smt.api.SolverContext.ProverOptions;

import com.sun.jna.Native;
import com.sun.jna.Pointer;

import ctwedge.ctWedge.*;
import ctwedge.ctWedge.impl.CtWedgeFactoryImpl;
import ctwedge.ctWedge.impl.ParameterImpl;
import ctwedge.util.Pair;
import ctwedge.util.Test;
import ctwedge.util.TestSuite;;

public class SMTTestSuiteValidator {

	private static final Logger logger = Logger.getLogger(SMTTestSuiteValidator.class);
	private TestSuite ts;

	@SuppressWarnings("unchecked")
	public void setTestSuite(TestSuite ts) {
		List<Logger> loggers = Collections.<Logger>list(org.apache.log4j.LogManager.getCurrentLoggers());
		loggers.add(org.apache.log4j.LogManager.getRootLogger());
		for (Logger logger : loggers) {
			logger.setLevel(Level.OFF);
		}
		this.ts = ts;
	};

	class Mycomp implements Comparator<Map<Parameter, ?>> {
		private List<Map<Parameter, String>> listMapReq;

		public void setReq(List<Map<Parameter, String>> listMapReq) {
			this.listMapReq = listMapReq;
		}

		public Mycomp(List<Map<Parameter, String>> listMapReq) {
			this.listMapReq = listMapReq;
		}

		@Override
		public int compare(Map<Parameter, ?> test1, Map<Parameter, ?> test2) {
			// System.out.println(test1+" \n"+test2);
			Integer size1 = 0, size2 = 0;
			if (!listMapReq.isEmpty()) {
				Iterator<Map<Parameter, String>> iReq = listMapReq.iterator();
				while (iReq.hasNext()) {
					Map<Parameter, ?> actual = iReq.next();

					if (covers(test1, actual)) {
						size1++;
					}
					if (covers(test2, actual)) {
						size2++;
					}

				}

			}
			return size2.compareTo(size1);
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
		for (Constraint rule : constraints) {
			if (rule.eContainer() instanceof CitModel) {
				for (Test t : ts.getTests()) {
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
		for (Constraint rule : constraints) {
			if (rule.eContainer() instanceof CitModel) {
				for (Test t : ts.getTests()) {
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

	public Boolean isComplete() throws SolverException, InterruptedException, InvalidConfigurationException {
		Configuration config = Configuration.defaultConfiguration();
		LogManager logger = BasicLogManager.create(config);
		ShutdownManager shutdown = ShutdownManager.create();

		SolverContext ctx = SolverContextFactory.createSolverContext(config, logger, shutdown.getNotifier(),
				Solvers.SMTINTERPOL);
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
		ProverEnvironment pe = SMTConstraintChecker.createCtxFromModel(ts.getModel(), ts.getModel().getConstraints(),
				ctx, declaredElements, variables);

		// Prove
		if (pe.isUnsat())
			throw new RuntimeException("The list of constraints is unsatisfiable");

		// Add the n-wise tuple to the context
		Iterator<Map<Parameter, String>> i = listMapReq.iterator();
		return checkRequirementsConsistency(ctx, listMapReq, declaredElements, variables, i);
	}

	private Boolean checkRequirementsConsistency(SolverContext ctx, List<Map<Parameter, String>> listMapReq,
			Map<String, String> declaredElements, Map<Parameter, Formula> variables,
			Iterator<Map<Parameter, String>> i) {
		ProverEnvironment prover = ctx.newProverEnvironment(ProverOptions.GENERATE_MODELS);
		
		while (i.hasNext()) {
			Map<Parameter, String> requirement = i.next();
			logger.debug("checking requirment " + requirement);

			prover.push();
			for (Parameter p : requirement.keySet()) {

				BooleanFormula t = null;
				Formula varPointer = variables.get(p);
				assert varPointer != null;
				
				// Check the type of the parameter
				if (p instanceof Enumerative) {
					String elementName = declaredElements.get(requirement.get(p).concat(p.getName()));
					assert elementName != null;
					
					Pointer a1 = yices.yices_parse_expression(ctx, elementName);
					t = yices.yices_mk_eq(ctx, varPointer, a1);
				} else if (p instanceof ctwedge.ctWedge.Bool) {
					if (requirement.get(p).toLowerCase().equals("true"))
						t = (BooleanFormula) varPointer;
					else
						t = ctx.getFormulaManager().getBooleanFormulaManager().not((BooleanFormula) varPointer);
				}
				// Add the new constraint
				prover.addConstraint(t);

				// The context is inconsistent, the tuple is unfeasible -> go to the next one
				if (prover.isUnsat()) {
					break;
				}
			}
			
			// If the tuple is feasible, it returns false since the test suite is missing a requirement
			if (!prover.isUnsat()) {
				ctx.close();
				return false;
			}
			
			prover.pop();
		}

		// Terminate the context
		ctx.close();

		// If no return has been executed before, the requirements are consistent
		return true;
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
				Parameter p = CtWedgeFactoryImpl.eINSTANCE.createParameter();
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