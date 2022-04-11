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
package splot;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import splar.core.fm.FeatureModelStatistics;
import splar.core.heuristics.FTPreOrderSortedECTraversalHeuristic;
import splar.core.heuristics.VariableOrderingHeuristic;
import splar.core.heuristics.VariableOrderingHeuristicsManager;
import splar.plugins.reasoners.bdd.javabdd.FMReasoningWithBDD;
import splar.plugins.reasoners.bdd.javabdd.ReasoningWithBDD;
import splar.plugins.reasoners.sat.sat4j.FMReasoningWithSAT;
import ctwedge.ctWedge.CitModel;
import ctwedge.ctWedge.Expression;
import ctwedge.ctWedge.Rule;
import ctwedge.importer.featureide.FeatureIdeImporterMultipleLevels;
import ctwedge.importer.featureide.SxfmFiImporter;
import citlab.model.logic.cnf.CNF;
import citlab.model.logic.cnf.CNFConverter;
import citlab.model.simplifier.Simplificator;
import citlab.model.simplifier.Simplifier;
import citlab.model.util.CombinationCounter;

import com.csvreader.CsvWriter;

import de.ovgu.featureide.fm.core.FeatureModel;
import de.ovgu.featureide.fm.core.FeatureModelAnalyzer;
import de.ovgu.featureide.fm.core.io.UnsupportedModelException;
import de.ovgu.featureide.fm.core.io.sxfm.SXFMReader;

/**
 * write the data about the models from SXFM file
 * 
 * @author garganti
 * 
 */
public class Experiment_DataWriter extends Experiment_SPLOTmodels{
	
	@BeforeClass
	static public void setUp(){
		Logger.getLogger(FeatureIdeImporterMultipleLevels.class).setLevel(Level.ERROR);
		//Logger.getLogger(CNFConverter.class).setLevel(Level.OFF);
	} 

	@Test
	public void testSplotModels() throws Exception {
		testModelsIn("splotmodels_new");
	}

/*@Test
	public void testSmallExamples() throws Exception {
		testModelsIn("small_splotexamples");
	}*/

	private void testModelsIn(String dirPath) throws Exception {
		File dir = new File(dirPath);
		Assert.assertTrue(dir.isDirectory());
		File[] children = dir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".xml");
			}
		});
		ArrayList<ArrayList<?>> metriche= new ArrayList<ArrayList<?>> ();
		for (int i = 0; i < children.length; i++) {
			// Get filename of file or directory
			File filename = children[i];
			readSPLOTmodel(filename.getAbsolutePath(), metriche);
		}
		//XlsExporter	xlsReport = new XlsExporter();
		//xlsReport.generateOutput(metriche, "Report.xls");
		try {
			// use FileWriter constructor that specifies open for appending
			CsvWriter csvOutput = new CsvWriter(new FileWriter("REPORT.csv", false), ';');
			csvOutput.write("NAMES");
			csvOutput.write("FEATURE");
			csvOutput.write("PRODUCTS");
			csvOutput.write("CTL PARAMETERS");
			csvOutput.write("CTL S PARAMETERS");
			csvOutput.write("BDD NODES");
			csvOutput.write("CTL RATE");
			csvOutput.write("CTL S RATE");
			csvOutput.write("SPLOT RATE");
			csvOutput.write("FI VARIABILITY");
			csvOutput.write("CTL CONSTRAINTS");
			csvOutput.write("CTL S CONSTRAINTS");
			csvOutput.write("CTL CNF CONSTRAINTS");
			csvOutput.write("CTL S CNF CONSTRAINTS");
			csvOutput.write("TOT CNF FI CONSTRAINTS");
			csvOutput.endRecord();

			// else assume that the file already has the correct header line

			NumberFormat formatter = new DecimalFormat();
			formatter = new DecimalFormat("0.######E0");
			for (ArrayList<?> metrica : metriche){
				for (Object q : metrica){
					if ((q instanceof Double)){
						csvOutput.write(formatter.format(q));}
					else csvOutput.write(q.toString());
				}
				csvOutput.endRecord();
			}

			csvOutput.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/** return true if successful, false if not done, exception if wrong
	 * 
	 * @param path
	 * @param destination
	 * @return
	 * @throws Exception
	 */
	public boolean readSPLOTmodel(String path, ArrayList<ArrayList<?>> metriche)
			throws Exception {
		// leggi il file SPLOT using the FeatureiDE reader
		FeatureModel fiModel = new FeatureModel();		
		SXFMReader reader = new SXFMReader(fiModel);
		File modelFile = new File(path);
		assert modelFile.exists() && modelFile.isFile();
		try {
			reader.readFromFile(modelFile);
		} catch (UnsupportedModelException e) {
			System.err.println("SKIPPING " + path + " cause :" + e.getMessage());
			return false;
		}
		// leggi lo stesso modello come SPLOT
		// facciamo analisi di quello vecchio - numero prodotti
		splar.core.fm.FeatureModel splotModel = getSplotModel(path);
		// leggi come citlab
		SxfmFiImporter importer = new SxfmFiImporter();
		CitModel citlabModel = importer.importModel(path);
		// simplified version
		CitModel citsModel = new Simplificator(citlabModel).getSimplifiedVersion();
		//
		// get the data
		// number of products (as in citlab)
		long nValCombinationsSPLOT = getNumberOfValidProducts(splotModel);
		System.out.println(nValCombinationsSPLOT);
        System.out.println("FILE:      "+path);
        ArrayList<Object> metrica = new ArrayList<>();
		// numero di prodotti calcolcato con featureIde
		FeatureModelAnalyzer analyser = fiModel.getAnalyser();
		analyser.analyzeFeatureModel(new NullProgressMonitor());
		// dead feature and common by FeatureIde
		int deadFeatures= analyser.getDeadFeatures().size();	
		int commonfeatures = analyser.commonFeatures(111000, fiModel.getRoot()).size();
		// calcolato con SAT di SPLOT
		List<String> commonFeatureSSAT = getListOfCommonFeaturesSPLOTUsingSat(splotModel);
		int commonferatureSPLOT = commonFeatureSSAT.size();
		//
		// name
		metrica.add(fiModel.getRoot().getName());
		// number of features
	    metrica.add(fiModel.getFeatures().size());
	    // number of products
		metrica.add(nValCombinationsSPLOT);
		// number of paramters
		metrica.add(citlabModel.getParameters().size());
		// number of paramters in trhe simplified version
		metrica.add(citsModel.getParameters().size());
		// number of BDD nodes
		metrica.add(getNumberOfNodeSPLOT(splotModel));
		// CITLRATE		
		double citlRate = ((double)nValCombinationsSPLOT)/CombinationCounter.countCartesianProduct(citlabModel).doubleValue();
		assert citlRate <= 1 && citlRate >0 : BigInteger.valueOf(nValCombinationsSPLOT) + " vs " + CombinationCounter.countCartesianProduct(citlabModel);
		metrica.add(citlRate);
		// CITL S RATE		
		metrica.add(((double) nValCombinationsSPLOT)/ CombinationCounter.countCartesianProduct(citsModel).doubleValue());
		// SPLOT RATE
		metrica.add( nValCombinationsSPLOT
					/ Math.pow(2, getNumberOfNodeSPLOT(splotModel)));
		// variability
		metrica.add(( nValCombinationsSPLOT
					/  Math.pow(2, fiModel.getFeatures().size())));
		// citlab constraints
		metrica.add((long)(citlabModel.getConstraints().size()));			
		// citlab s constraints
		metrica.add((long)(citsModel.getConstraints().size()));
		// citlab s constraints as CBF caluses
		metrica.add(countCNFConstraintsClauses(citlabModel.getConstraints()));
		// citlab s constraints as CBF caluses
		metrica.add(countCNFConstraintsClauses(citsModel.getConstraints()));
		// the constraints in the original models 
		FeatureModelStatistics stats = new FeatureModelStatistics(splotModel);
		// total of constraints
        int ftCNF = stats.countFeatureTreeCNFClauses();
        int ecCNF = ((splotModel.getConstraints().size() > 0) ? stats.countExtraConstraintCNFClauses(): 0 );
        metrica.add(ecCNF+ftCNF);
        //
		return metriche.add(metrica);
	}

	private int countCNFConstraintsClauses(EList<Rule> eList){
		int totalNumberofClauses = 0;
		CNFConverter converter = new CNFConverter();
		for (Rule r: eList){
			CNF cnf = converter.convertToCNF((Expression) r);
			totalNumberofClauses += cnf.getClauses().size();
		}
		return totalNumberofClauses;
	}
	
	
	/**
	 * 
	 * @param newSplotModel
	 * @return the number of common features using SAT 
	 * @throws Exception
	 */
	private List<String> getListOfCommonFeaturesSPLOTUsingSat(splar.core.fm.FeatureModel featureModel) throws Exception{
		// using sat solving
		FMReasoningWithSAT analyzer = new FMReasoningWithSAT("Default", featureModel, 60000);
		analyzer.init();
		return analyzer.allCoreFeatures(new HashMap<String, String>());
	}
	
	/**
	 * 
	 * @param newSplotModel
	 * @return the number of dead features using SAT 
	 * @throws Exception
	 */
	private List<String> getListOfDeadFeaturesSPLOTUsingSat(splar.core.fm.FeatureModel featureModel) throws Exception{
		// using sat solving
		FMReasoningWithSAT analyzer = new FMReasoningWithSAT("Default", featureModel, 60000);
		analyzer.init();
		return analyzer.allDeadFeatures(new HashMap<String, String>());
	}

	
	/** returns the number of feature, solo quelli necessari (senza dead e common). node count 
	 * 
	 * @param newSplotModel
	 * @return
	 * @throws Exception
	 */
	private long getNumberOfNodeSPLOT(splar.core.fm.FeatureModel featureModel)
			throws Exception {

		// create BDD variable order heuristic
		new FTPreOrderSortedECTraversalHeuristic("Pre-CL-MinSpan",
				featureModel, FTPreOrderSortedECTraversalHeuristic.FORCE_SORT);
		VariableOrderingHeuristic heuristic = VariableOrderingHeuristicsManager
				.createHeuristicsManager().getHeuristic("Pre-CL-MinSpan");
            
		// Creates the BDD reasoner
		ReasoningWithBDD reasoner = new FMReasoningWithBDD(featureModel,
				heuristic, 50000, 50000, 60000, "pre-order");
   
		// Initialize the reasoner (BDD is created at this moment)
		reasoner.init();
		// Use the reasoner
		return reasoner.getBDD().nodeCount();
	}
}
