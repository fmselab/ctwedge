package ctwedge.generator.smttest.safeelements;

import java.util.Arrays;
import java.util.Vector;

import org.sosy_lab.java_smt.api.BooleanFormula;
import org.sosy_lab.java_smt.api.ProverEnvironment;
import org.sosy_lab.java_smt.api.SolverContext;
import org.sosy_lab.java_smt.api.SolverContext.ProverOptions;

import ctwedge.generator.smttest.util.Operations;
import ctwedge.generator.smttest.util.Pair;

public class TestContext {
	
	public static int UNDEF = -1;
	
	/**
	 * The (partial) test containing the values already set
	 */
	int[] test;
	
	/**
	 * Are the constraints present?
	 */
	boolean useConstraints;

	/**
	 *  Number of tuples covered by this test context 
	 */
	int nCovered;
	
	/**
	 * The context
	 */
	SolverContext context;
	
	/**
	 * The prover environment
	 */
	ProverEnvironment prover;
	
	/**
	 * The semaphore for managing a test context in a mutex manner
	 */
	public ExtendedSemaphore testMutex;
	
	/**
	 * Builds a new TestContext
	 * 
	 * @param nParams: the number of parameters of the combinatorial problem
	 * @param useConstraints: are constraints present?
	 * @param context: the context
	 */
	public TestContext(int nParams, boolean useConstraints, SolverContext context) {
		this.useConstraints = useConstraints;
		this.test = new int[nParams];
		this.nCovered = 0;
		this.context = context;
		this.testMutex = new ExtendedSemaphore();
		Arrays.fill(this.test, UNDEF);
		this.prover = context.newProverEnvironment(ProverOptions.GENERATE_UNSAT_CORE);
	}
	
	/**
	 * Checks whether the tuple given as parameter is coverable or not by the current context
	 * 
	 * @param tuple: the tuple to be checked
	 * @return true if the tuple is coverable, false otherwise
	 * @throws InterruptedException 
	 */
	public boolean isCoverable(Vector<Pair<Integer, Integer>> tuple) throws InterruptedException {
		// We must use a test context in a mutex mode
		assert (this.testMutex.lockedByCaller() || nCovered == 0);
		
		// Checks using the test vector
		for (Pair<Integer, Integer> t : tuple) {
			int valueInTest = test[t.getFirst()];
			if (valueInTest != UNDEF) {
				// If the value is not undef, and differs from that in the tuple
				if (valueInTest != t.getSecond()) {
					return false;
				}
			}
		}
		
		// If the tuple is not compatible, then obviously is not coverable by the current TestContext
		if (!isCompatible(tuple, true)) {
			return false;
		}

		return true;
	}

	/**
	 * Checks whether the tuple given as parameter is compatible or not by the current context
	 * 
	 * @param tuple: the tuple to be checked
	 * @param skipFirstStep: true if the check with the vector has already been done
	 * @return true if the tuple is compatible, false otherwise
	 * @throws InterruptedException 
	 */
	private boolean isCompatible(Vector<Pair<Integer, Integer>> tuple, boolean skipFirstStep) throws InterruptedException {
		// We must use a test context in a mutex mode
		assert (this.testMutex.lockedByCaller() || nCovered == 0);
		
		// First phase - Check without the MDD
		if (!skipFirstStep) {
			for (Pair<Integer, Integer> t : tuple) {
				int valueInTest = test[t.getFirst()];
				if (valueInTest != UNDEF) {
					// If the value is not undef, and differs from that in the tuple
					if (valueInTest != t.getSecond()) {
						return false;
					}
				}
			}
		}

		// Now, if constraints are available check with the MDD in order to verify if it is compatible even with the constraints
		// Otherwise, it is compatible by default
		if (!useConstraints)
			return true;

		return verifyWithSAT(tuple);
	}
	
	/**
	 * Checks whether the tuple given as parameter is compatible or not by the current test context without using the SAT Solver
	 * 
	 * @param tuple: the tuple to be checked
	 * @return true if the tuple is compatible, false otherwise
	 */
	public boolean isCompatiblePartialCheck(Vector<Pair<Integer, Integer>> tuple) {
		// We must use a test context in a mutex mode
		assert (this.testMutex.lockedByCaller() || nCovered == 0);
		
		for (Pair<Integer, Integer> t : tuple) {
			int valueInTest = test[t.getFirst()];
			if (valueInTest != UNDEF) {
				// If the value is not undef, and differs from that in the tuple
				if (valueInTest != t.getSecond()) {
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * Verify with the use of the SAT Solver if the tuple is compatible with this test context
	 * 
	 * @param tuple: the tuple to be checked
	 * @return true if the tuple is compatible, false otherwise
	 * @throws InterruptedException 
	 */
	private boolean verifyWithSAT(Vector<Pair<Integer, Integer>> tuple) throws InterruptedException {
		// We must use a test context in a mutex mode
		assert (this.testMutex.lockedByCaller() || nCovered == 0);
				
		// Create an MDD representing the tuple
		TupleConverter tc = new TupleConverter(context);
		BooleanFormula tupleFormula = tc.getMDDFromTuple(tuple);
		
		// Try computing the intersection (AND) 
		ExtendedSemaphore.OPERATION_SEMAPHORE.acquire();
		int intersection = MDDBaseOperators.AND.combine(context, this.mdd, tupleMdd);
		ExtendedSemaphore.OPERATION_SEMAPHORE.release();
		
		// If the intersection is empty, then the tuple is not compatible, otherwise it is
		PathSearcher searcher = new PathSearcher(context, 1);
		searcher.setNode(intersection);
		int nPaths = searcher.countPaths();
		
		return nPaths > 0;		
	}
	
	/**
	 * Returns the number of tuples covered by this test context
	 * 
	 * @return the number of tuples covered by this test context
	 */
	public int getNCovered() {
		return nCovered;
	}
	
	/**
	 * Checks if a tuple is implied (already contained in the partial test of the TestContext).
	 * It does not require the use of a SAT Solver
	 * 
	 * @param tuple: the tuple to be checked
	 * @return true if the tuple is implied, false otherwise
	 */
	public boolean isImplied(Vector<Pair<Integer, Integer>> tuple) {
		// We must use a test context in a mutex mode
		assert (this.testMutex.lockedByCaller() || nCovered == 0);
		
		// Check if it is implied
		for (Pair<Integer, Integer> t : tuple) {
			// If the values are not equal (undef or valid and different), it is not implied
			if (test[t.getFirst()] != t.getSecond()) {
				return false;
			}
		}
		
		nCovered++;
		return true;
	}
	
	/**
	 * This method adds a tuple to the partial test associated to the test context.
	 * The tuple to be added must be compatible with the partial test
	 * 
	 * @param tuple: the tuple to be added
	 * @throws InterruptedException 
	 */
	public void addTuple(Vector<Pair<Integer, Integer>> tuple) throws InterruptedException {
		// We must use a test context in a mutex mode
		assert (this.testMutex.lockedByCaller() || nCovered == 0);
		
		// Add the tuple to the partial test
		for (Pair<Integer, Integer> t : tuple) {
			test[t.getFirst()] = t.getSecond();
		}

		// Update the context, if constraints are available
		if (useConstraints) {
			updateContext(tuple);
		}
		else {
			nCovered++;
		}
	}

	/**
	 * Updates the internal context structure by adding the new tuple
	 * 
	 * @param tuple: the tuple to be added
	 * @throws InterruptedException 
	 */
	private void updateContext(Vector<Pair<Integer, Integer>> tuple) throws InterruptedException {
		// We must use a test context in a mutex mode
		assert (this.testMutex.lockedByCaller() || nCovered == 0);
				
		// Create an MDD representing the tuple
		TupleConverter tc = new TupleConverter(context);
		int tupleMdd = tc.getMDDFromTuple(tuple);
		
		// Compute the intersection (AND) 
		ExtendedSemaphore.OPERATION_SEMAPHORE.acquire();
		this.mdd = MDDBaseOperators.AND.combine(context, this.mdd, tupleMdd);
		ExtendedSemaphore.OPERATION_SEMAPHORE.release();
		
		// Now verify that the cardinality is still greater than 0
		PathSearcher searcher = new PathSearcher(context, 1);
		searcher.setNode(this.mdd);
		assert (searcher.countPaths() > 0);
		
		nCovered++;
	}
	
	/**
	 * Returns the test derived from this test context
	 * 
	 * @param printVector: if true the test in the vector is printed (to be used when no constraints are available), otherwise the test is extracted from the MDD
	 * @return the string representing the test derived from this test context
	 */
	public String getTest(boolean printVector) {
		String res = "";
		res = "[ ";
		
		if (printVector || !useConstraints || isComplete()) {
			for (int i : test)
				res += i + " ";
		} else {
			for (int i : Operations.getPathInMDD(this.mdd, context, this.test)) {
				res += i + " ";
			}
		}
		
		res += " --> T]";
		
		return res;
	}
	
	/**
	 * Checks whether the test is complete or not
	 * 
	 * @return true if the test is complete, false otherwise
	 */
	private boolean isComplete() {
		for (int i : test)
			if (i == UNDEF)
				return false;
		return true;
	}
}
