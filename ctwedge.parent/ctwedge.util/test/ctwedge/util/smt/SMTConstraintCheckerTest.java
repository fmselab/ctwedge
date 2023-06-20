package ctwedge.util.smt;

import org.junit.Test;
import org.sosy_lab.common.configuration.InvalidConfigurationException;
import org.sosy_lab.java_smt.api.ProverEnvironment;
import org.sosy_lab.java_smt.api.SolverContext;
import org.sosy_lab.java_smt.api.SolverContext.ProverOptions;

import com.google.common.collect.ImmutableMap;

import ctwedge.ctWedge.CitModel;
import ctwedge.util.ext.Utility;

public class SMTConstraintCheckerTest {
	
	
	static {
		ImmutableMap.Builder<Class<? extends Iterable<?>>, Class<? extends Iterable<?>>> builder =
		        ImmutableMap.builder();
	}

	@Test
	public void testCreateCtx() throws InvalidConfigurationException {
		SMTConstraintChecker cc = new SMTConstraintChecker();
		// Create a new Context
		SolverContext ctx = cc.createCtx();
		ProverEnvironment prover = ctx.newProverEnvironment(ProverOptions.GENERATE_MODELS);

		CitModel model = Utility.loadModel(
				"Model prova\nParameters:\n a: Boolean; b: Boolean; c: Boolean;\nConstraints:\n # a -> b #\n");
		// read a model
		// Add all the parameters, and their types
		prover = SMTConstraintChecker.createCtxFromModel(model, model.getConstraints(), ctx, prover);
		
		
	}

}
