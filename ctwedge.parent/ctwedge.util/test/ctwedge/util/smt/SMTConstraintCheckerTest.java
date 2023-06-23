package ctwedge.util.smt;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.sosy_lab.common.configuration.InvalidConfigurationException;
import org.sosy_lab.java_smt.api.ProverEnvironment;
import org.sosy_lab.java_smt.api.SolverContext;
import org.sosy_lab.java_smt.api.SolverContext.ProverOptions;
import org.sosy_lab.java_smt.api.SolverException;

import com.google.common.collect.ImmutableMap;

import ctwedge.ctWedge.CitModel;
import ctwedge.util.ext.Utility;

public class SMTConstraintCheckerTest {

	static {
		Logger.getLogger(SMTParameterAdder.class).setLevel(Level.ALL);
		Logger.getLogger(SMTConstraintTranslator.class).setLevel(Level.ALL);
		Logger.getLogger("ctwedge.util.smt").setLevel(Level.ALL);
	}


	@Test
	public void testBoolean() throws InvalidConfigurationException, SolverException, InterruptedException {
		SMTConstraintChecker cc = new SMTConstraintChecker();
		// Create a new Context
		SolverContext ctx = cc.createCtx();
		ProverEnvironment prover = ctx.newProverEnvironment(ProverOptions.GENERATE_MODELS);

		CitModel model = Utility.loadModel(
				"Model prova\nParameters:\n a: Boolean; b: Boolean; c: Boolean;\nConstraints:\n # a -> b #\n");
		// read a model
		// Add all the parameters, and their types
		prover = SMTConstraintChecker.createCtxFromModel(model, model.getConstraints(), ctx, prover);
		System.out.println(prover.isUnsat());
	}

	@Test
	public void testEnum() throws InvalidConfigurationException, SolverException, InterruptedException {
		SMTConstraintChecker cc = new SMTConstraintChecker();
		// Create a new Context
		SolverContext ctx = cc.createCtx();
		ProverEnvironment prover = ctx.newProverEnvironment(ProverOptions.GENERATE_MODELS);

		SMTParameterAdder.enumTreatment = SMTParameterAdder.EnumTreatment.INTEGER;
		// read a model
		CitModel model = Utility.loadModel(
					"Model prova\nParameters:\na: {16MC, 8MC, BW}; b: {WIN , MAC, LINUX};\nConstraints:\n # a = 16MC -> b = WIN#\n");			
		// Add all the parameters, and their types
		prover = SMTConstraintChecker.createCtxFromModel(model, model.getConstraints(), ctx, prover);
		System.out.println(prover.isUnsat());
		System.out.println(prover.getModelAssignments());		
		// check
		
	}
	
	// se ho enum duplicati !!! TODO 
}
