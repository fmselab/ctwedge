package ctwedge.generator.smttest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.sosy_lab.common.configuration.InvalidConfigurationException;
import org.sosy_lab.java_smt.api.SolverContext;

import ctwedge.ctWedge.CitModel;
import ctwedge.generator.smttest.combinations.TupleGenerator;
import ctwedge.generator.smttest.safeelements.ExtendedSemaphore;
import ctwedge.generator.smttest.safeelements.SafeQueue;
import ctwedge.generator.smttest.safeelements.TestContext;
import ctwedge.generator.smttest.threads.TestBuilder;
import ctwedge.generator.smttest.threads.TupleFiller;
import ctwedge.generator.smttest.util.Operations;
import ctwedge.generator.smttest.util.Pair;
import ctwedge.generator.util.Utility;

public class SMTTest {
	
	public static void main(String[] args) throws IOException, InterruptedException, InvalidConfigurationException {
		String fileName = "";
		int strength = 2;
		SolverContext ctx;
		
		// Read the test model from arguments
		if (args.length > 0) {
			strength = Integer.parseInt(args[0]);
			fileName = args[1];
		}
		
		// Read the combinatorial model and get the MDD representing the model without constraints		
		CitModel m;
		if (fileName.equals(""))
			m = Utility.loadModelFromPath("./examples/testfile3.medici");
		else
			m = Utility.loadModelFromPath(fileName);
		
		int nCovered = 0;
		int totTuples = 0;
		
		// Get current time
		long start = System.currentTimeMillis();
		
		// Create the context and add constraints and parameters
		ctx = Operations.updateContext(m);
		
		// Shared object between producer and consumer
		SafeQueue tuples = new SafeQueue();

		// Combination generator
		Iterator<List<Pair<Integer, Integer>>> tg = TupleGenerator.getAllKWiseCombination(m, strength);

		// Start the filler thread
		TupleFiller tFiller = new TupleFiller(tg, tuples);
		Thread tFillerThread = new Thread(tFiller);
		tFillerThread.start();
		
		// Start all the TestBuilder threads
		int nThreads = Runtime.getRuntime().availableProcessors();
		ExtendedSemaphore testContextsMutex = new ExtendedSemaphore();
		Vector<TestContext> tcList = new Vector<TestContext>();
		boolean sort = false;
		int nParams = m.getParameters().size();
		ArrayList<Thread> testBuilderThreads = new ArrayList<Thread>();
		for (int i=0; i<nThreads; i++) {
			Thread tBuilder = new Thread(new TestBuilder(tuples, tcList, sort, nParams, nParams > 0, ctx, testContextsMutex));
			testBuilderThreads.add(tBuilder);
			tBuilder.start();
		}
		
		// Join all the test builder threads
		for (int i=0; i<nThreads; i++) {
			testBuilderThreads.get(i).join();
		}
		
		// Print the tests
		for (TestContext tc : tcList) {
			nCovered += tc.getNCovered();
			System.out.println(tc.getTest(false));
		}
		
		totTuples = tuples.getNTuples();
		System.out.println("Covered: " + nCovered + " tuples");
		System.out.println("Uncovered: " + (totTuples - nCovered) + " tuples");
		System.out.println("Total number of tuples: " + totTuples + " tuples");
		System.out.println("Time required for test suite generation: " + (System.currentTimeMillis() - start) + " ms");
		System.out.println("Generated " + tcList.size() + " tests");
		
		// Join the tuple filler thread
		tFillerThread.join();
	}
	
}
