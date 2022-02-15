package ctwedge.generator.smttest.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.Vector;

import org.sosy_lab.common.configuration.InvalidConfigurationException;
import org.sosy_lab.java_smt.SolverContextFactory;
import org.sosy_lab.java_smt.SolverContextFactory.Solvers;
import org.sosy_lab.java_smt.api.Formula;
import org.sosy_lab.java_smt.api.FormulaManager;
import org.sosy_lab.java_smt.api.IntegerFormulaManager;
import org.sosy_lab.java_smt.api.SolverContext;

import ctwedge.ctWedge.CitModel;
import ctwedge.ctWedge.Constraint;
import ctwedge.ctWedge.Parameter;
import ctwedge.generator.smttest.safeelements.ExtendedSemaphore;
import ctwedge.generator.util.ParameterElementsGetterAsStrings;
import ctwedge.util.validator.SMTConstraintChecker;
import ctwedge.util.validator.SMTConstraintTranslator;

public class Operations {

	/**
	 * Return the tuple given as parameter as a string
	 * 
	 * @param tuple: the tuple to be printed
	 * @return the string representing the tuple
	 */
	public static String printTuple(Vector<Pair<Integer, Integer>> tuple) {
		String res = "";
		for (Pair<Integer, Integer> t : tuple) {
			res += ("<" + t.getFirst() + "," + t.getSecond() + ">");
		}
		return res;
	}

	/**
	 * Updates the MDD by inserting all the constraints previously loaded in the
	 * test mdoel
	 * 
	 * @param manager:  the MDD manager
	 * @param m:        the test model
	 * @param baseNode: the base MDD
	 * @return the identifier of the MDD
	 * @throws InterruptedException
	 */
	public static int updateMDDWithConstraints(MDDManager manager, TestModel m, int baseNode)
			throws InterruptedException {
		// Fetch all the constraints
		for (Constraint c : m.constraintList) {
			// Fetch all the elements inside the constraint
			Stack<Integer> tPList = new Stack<Integer>();
			while (!c.constraint.isEmpty()) {
				ConstraintElement ce = c.getElement();
				if (ce.isOperator()) {
					int newNode;
					int n1 = -1;
					int n2 = -1;
					switch (ce.operator) {
					case "+":
						// OR Operation
						assert (tPList.size() >= 2);
						n1 = tPList.pop();
						n2 = tPList.pop();
						ExtendedSemaphore.OPERATION_SEMAPHORE.acquire();
						newNode = MDDBaseOperators.OR.combine(manager, n1, n2);
						ExtendedSemaphore.OPERATION_SEMAPHORE.release();
						tPList.push(newNode);
						break;
					case "*":
						// AND Operation
						assert (tPList.size() >= 2);
						n1 = tPList.pop();
						n2 = tPList.pop();
						ExtendedSemaphore.OPERATION_SEMAPHORE.acquire();
						newNode = MDDBaseOperators.AND.combine(manager, n1, n2);
						ExtendedSemaphore.OPERATION_SEMAPHORE.release();
						tPList.push(newNode);
						break;
					case "-":
						// NOT Operation
						assert (tPList.size() >= 1);
						n1 = tPList.pop();
						ExtendedSemaphore.OPERATION_SEMAPHORE.acquire();
						newNode = manager.not(n1);
						ExtendedSemaphore.OPERATION_SEMAPHORE.release();
						tPList.push(newNode);
						break;
					}
				} else {
					// Convert the value in a MDD and store it into a list
					int newNode = getTupleFromParameter(ce.value, m.bounds, m.nParams, manager);
					tPList.push(newNode);
				}
			}

			// At the end of the single constraint management, each constraint must
			// correspond to a single node
			if (tPList.size() != 1) {
				System.out.println(tPList.size() + " - ERROR IN CONSTRAINTS DEFINITION \n");
				return -1;
			}

			// Now the top of the stack must contain the complete constraint representation
			// and we can update the base node
			ExtendedSemaphore.OPERATION_SEMAPHORE.acquire();
			baseNode = MDDBaseOperators.AND.combine(manager, baseNode, tPList.pop());
			ExtendedSemaphore.OPERATION_SEMAPHORE.release();
		}
		return baseNode;
	}

	/**
	 * Returns the corresponding index of a value read from file
	 * 
	 * @param value:   the value read in constraints from file
	 * @param bounds:  the bounds of all the parameters
	 * @param nParams: the number of parameters
	 * @param manager: the MDD Manager
	 * @return the corresponding index of a value read from file
	 * @throws InterruptedException
	 */
	private static int getTupleFromParameter(int value, int[] bounds, int nParams, MDDManager manager)
			throws InterruptedException {
		int index = 0;
		TupleConverter tc = new TupleConverter(manager);

		for (int i = 0; i < nParams; i++) {
			if (value < (index + bounds[i])) {
				Vector<Pair<Integer, Integer>> val = new Vector<Pair<Integer, Integer>>();
				val.add(new Pair<Integer, Integer>(i, value - index));
				return tc.getMDDFromTuple(val);
			}
			index += bounds[i];
		}

		return -1;
	}


	public static SolverContext updateContext(CitModel m) throws InvalidConfigurationException {
		SolverContext ctx = SolverContextFactory.createSolverContext(Solvers.Z3); 
		IntegerFormulaManager variableManager = ctx.getFormulaManager().getIntegerFormulaManager();
		SMTConstraintTranslator cTranslator = new SMTConstraintTranslator(ctx, null, null);
		SMTConstraintChecker cChecker = new SMTConstraintChecker();
		Map<Parameter, Formula> variables = new HashMap<Parameter, Formula>();
		
		cChecker.addParameters(m, ctx, null, variables);
		
		// Load all the parameters from the model into the context
		for (Parameter p : m.getParameters()) {
			String paramName = p.getName();
			ArrayList<String> paramValues = new ArrayList<String>(ParameterElementsGetterAsStrings.instance.doSwitch(p));

			
		}
		
		// Fetch all the constraints and load them into the context
		for (Constraint c : m.getConstraints()) {
			Formula constraint = cTranslator.doSwitch(c);
			
			
		}		
		
		return ctx;
	}
}