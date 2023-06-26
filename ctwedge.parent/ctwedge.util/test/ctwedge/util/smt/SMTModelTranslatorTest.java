package ctwedge.util.smt;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.sosy_lab.common.configuration.InvalidConfigurationException;
import org.sosy_lab.java_smt.api.BooleanFormula;
import org.sosy_lab.java_smt.api.Formula;
import org.sosy_lab.java_smt.api.IntegerFormulaManager;
import org.sosy_lab.java_smt.api.NumeralFormula.IntegerFormula;
import org.sosy_lab.java_smt.api.ProverEnvironment;
import org.sosy_lab.java_smt.api.SolverContext;
import org.sosy_lab.java_smt.api.SolverContext.ProverOptions;
import org.sosy_lab.java_smt.api.SolverException;

import ctwedge.ctWedge.CitModel;
import ctwedge.ctWedge.Parameter;
import ctwedge.util.ext.Utility;
import ctwedge.util.smt.SMTParameterAdder.EnumTreatment;

public class SMTModelTranslatorTest {

	static {
		Logger.getLogger(SMTParameterAdder.class).setLevel(Level.ALL);
		Logger.getLogger(SMTConstraintTranslator.class).setLevel(Level.ALL);
		Logger.getLogger("ctwedge.util.smt").setLevel(Level.ALL);
	}


	@Test
	public void testBoolean() throws InvalidConfigurationException, SolverException, InterruptedException {
		SMTModelTranslator smtrans = new SMTModelTranslator(EnumTreatment.INTEGER);
		// Create a new Context
		SolverContext ctx = smtrans.createCtx();
		ProverEnvironment prover = ctx.newProverEnvironment(ProverOptions.GENERATE_MODELS);		
		CitModel model = Utility.loadModel(
				"Model prova\nParameters:\n a: Boolean; b: Boolean; c: Boolean;\nConstraints:\n # a -> b #\n \n # a = true #\n");
		// read a model
		// Add all the parameters, and their types
		prover = smtrans.createCtxFromModel(model, model.getConstraints(), ctx, prover);
		System.out.println(prover.isUnsat());
	}

	@Test
	public void testEnumInt() throws InvalidConfigurationException, SolverException, InterruptedException {
		testEnum(EnumTreatment.INTEGER);
	}

	@Test
	public void testEnumEnum() throws InvalidConfigurationException, SolverException, InterruptedException {
		testEnum(EnumTreatment.ENUM);
	}

	
	private void testEnum(EnumTreatment integer)
			throws InvalidConfigurationException, SolverException, InterruptedException {
		SMTModelTranslator smtrans = new SMTModelTranslator(integer);
		// Create a new Context
		SolverContext ctx = smtrans.createCtx();
		ProverEnvironment prover = ctx.newProverEnvironment(ProverOptions.GENERATE_MODELS);

		// read a model
		CitModel model = Utility.loadModel(
					"Model prova\nParameters:\na: {16MC, 8MC, BW}; b: {WIN , MAC, LINUX};\nConstraints:\n # a = 16MC -> b = WIN#\n");			
		// Add all the parameters, and their types
		prover = smtrans.createCtxFromModel(model, model.getConstraints(), ctx, prover);
		System.out.println(prover.isUnsat());
		System.out.println(prover.getModelAssignments());		
		// check 
		System.out.println(smtrans.declaredElements);
		System.out.println(smtrans.variables);
		IntegerFormulaManager integerFormulaManager = ctx.getFormulaManager().getIntegerFormulaManager();
		IntegerFormula MACF = integerFormulaManager.makeNumber(1);
		IntegerFormula WINF = integerFormulaManager.makeNumber(0);
		IntegerFormula e16MC = integerFormulaManager.makeNumber(0);
		Optional<Parameter> a = smtrans.variables.keySet().stream().filter(x -> x.getName().equals("a")).findFirst();
		Optional<Parameter> b = smtrans.variables.keySet().stream().filter(x -> x.getName().equals("b")).findFirst();
		Formula af = smtrans.variables.get(a.get()).get(0);
		Formula bf = smtrans.variables.get(b.get()).get(0);
		
		// a = 16MC and b = WIN is feasible
		BooleanFormula a1 = integerFormulaManager.equal((IntegerFormula) af, e16MC);
		BooleanFormula a2 = integerFormulaManager.equal((IntegerFormula) bf, WINF);
		BooleanFormula e = ctx.getFormulaManager().getBooleanFormulaManager().and(a1, a2);
		prover.push();
		prover.addConstraint(e);
		// --> false
		assertFalse(prover.isUnsat());
		System.out.println(prover.isUnsat());
		prover.pop();
		// a = 16MC and b = MAC is unfeasible
		a1 = integerFormulaManager.equal((IntegerFormula) af, e16MC);
		a2 = integerFormulaManager.equal((IntegerFormula) bf, MACF);
		e = ctx.getFormulaManager().getBooleanFormulaManager().and(a1, a2);
		// add also this but 
		prover.push();
		prover.addConstraint(e);
		// --> true
		assertTrue(prover.isUnsat());
		System.out.println(prover.isUnsat());
		prover.pop();
		// a can be 1
		IntegerFormula number = integerFormulaManager.makeNumber(1);
		a1 = integerFormulaManager.equal((IntegerFormula) af, number);
		prover.push();
		prover.addConstraint(a1);
		assertFalse(prover.isUnsat());
		System.out.println(prover.isUnsat());		
		prover.pop();
		// a cannot be 10
		IntegerFormula wrong = integerFormulaManager.makeNumber(10);
		a1 = integerFormulaManager.equal((IntegerFormula) af, wrong);
		prover.push();
		prover.addConstraint(a1);
		assertTrue(prover.isUnsat());
		System.out.println(prover.isUnsat());		
		prover.pop();
	}
	
	
	
	// se ho enum duplicati !!! TODO 
}
