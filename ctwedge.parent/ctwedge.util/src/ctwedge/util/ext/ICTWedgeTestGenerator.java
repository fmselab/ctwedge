/******************************************************************************* 
 * Copyright (c) 2013 University of Bergamo - Italy 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 *  
 * Contributors: 
 *   Paolo Vavassori - initial API and implementation 
 *   Angelo Gargantini - utils and architecture 
 ******************************************************************************/
package ctwedge.util.ext;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import ctwedge.ctWedge.CitModel;
import ctwedge.util.TestSuite;
import ctwedge.util.genprefs.CitlabPreferncesSet;

/**
 * The Class ICitLabTestGenerator: abstract class for test generators.
 */

public abstract class ICTWedgeTestGenerator extends ICTWedgeModelProcessor implements Callable<TestSuite> {

	private boolean ignoreConstraints;
	private int nWise;
	private String generatorName;

	/** The citModel. */
	private CitModel citModel = null;
	
	/**
	 * The Process
	 */
	public Process executableProcess;

	
	/**
	 * @return the generatorName
	 */
	public String getGeneratorName() {
		return generatorName;
	}

	protected CitlabPreferncesSet genProperties;

	private Future<TestSuite> futureTS;

	private ExecutorService es;

	protected ICTWedgeTestGenerator(String generator) {
		this.generatorName = generator;
	}
	
	public ICTWedgeTestGenerator() {	}

	/**
	 * generates the citModel and save some meta info in the test suite.
	 * 
	 * @param citModel          the cit model
	 * @param ignoreConstraints the ignore constraints
	 * @param ignoreSeeds       the ignore seeds
	 * @param ignoreTestGoals   the ignore test goals
	 * @param nWise             the n wise
	 * @param generatorName     the generator name
	 * @return the test suite
	 */
	final public TestSuite generateTestsAndInfo(CitModel model, boolean ignoreConstraints, int nWise)
			throws CTWedgeException {
		System.out.println("I'm called....");
		this.ICitLabTestGeneratorSetter(model, ignoreConstraints, nWise);
		// save the time
		long time = System.currentTimeMillis();
		// es = Executors.newFixedThreadPool(4);
		// es = Executors.newCachedThreadPool();
		es = Executors.newSingleThreadScheduledExecutor();
		futureTS = es.submit(this);
		TestSuite ts = null;
		try {
			// take the test suite generate by the generator
			System.out.println("Before getting the test suite..:");
			ts = futureTS.get();// 10,TimeUnit.SECONDS);
			// register
			long delta = System.currentTimeMillis() - time;
			// System.out.println(String.valueOf(delta));
			// convert from missecs to secs
			ts.setGeneratorTime((float) (delta / 1000.0));
			// set the name
			ts.setGeneratorName(generatorName);
			ts.setStrength(nWise);
			System.out.println("Got the test suite " + generatorName + " " + nWise + " " + (float) (delta / 1000.0));
		} catch (ExecutionException e) {
			if (e.getCause() instanceof NotConvertableModel)
				throw (NotConvertableModel) e.getCause();
		} catch (Exception e2) {
			if (e2.getCause() instanceof java.lang.OutOfMemoryError)
				throw (java.lang.OutOfMemoryError) e2.getCause();
		} finally {
			es.shutdown();
		}
		System.out.println("Returning test suite " + ts);
		return ts;
	}
	
	@Override
	final public TestSuite call() throws Exception {
		if (citModel == null) throw new RuntimeException("use generateTestsAndInfo instead");
		// TODO use log
		// System.out.println("ACTS chiamato");
		// System.out.println("citModel: "+citModel+"  nWise: "+nWise+"  ignoreConstraints: "+ignoreConstraints);
		TestSuite ts = getTestSuite(citModel, nWise, ignoreConstraints);
		// System.out.println("ACTS test suite: "+ts);
		return ts;
	}

	
	/**
	 * real method implementing the algorithm for generating the test suite
	 * 
	 * 
	 */
	public abstract TestSuite getTestSuite(CitModel loadModel, int t, boolean ignoreC) throws Exception;

	
	public CitlabPreferncesSet getGenProperties() {
		return genProperties;
	}

	/**
	 * Icitab test generator setter.
	 * 
	 * @param model             the model
	 * @param ignoreConstraints the ignore constraints
	 * @param ignoreSeeds       the ignore seeds
	 * @param ignoreTestGoals   the ignore test goals
	 * @param nWise             the n wise
	 */
	private void ICitLabTestGeneratorSetter(CitModel model, boolean ignoreConstraints, int nWise) {
		this.citModel = model;
		this.ignoreConstraints = ignoreConstraints;
		this.nWise = nWise;
	}

	public void setGenProperties(CitlabPreferncesSet genProperties) {
		this.genProperties = genProperties;
	}

	public final void stopGeneration() {
		assert (futureTS != null && es != null);
		if (!futureTS.isDone()) {
			futureTS.cancel(true);
			es.shutdown();
			// System.out.println("shutting down");
			// es.shutdownNow();
			// System.out.println("es.isShutdown()" +es.isShutdown());
			// while(!es.isTerminated()) System.out.print('.');
			// System.out.println(futureTS.isCancelled() + " " + futureTS.isDone());
		}
	}


}