package ctwedge.fmtester;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import ctwedge.ctWedge.CitModel;
import ctwedge.generator.acts.ACTSTranslator;
import ctwedge.importer.featureide.FeatureIdeImporterBoolean;
import ctwedge.util.ModelUtils;
import ctwedge.util.Test;
import ctwedge.util.TestSuite;

/**
 * {@link DistanceCalculator} gives methods to calculate the distances between:
 * <ul>
 * <li>test suites</li>
 * <li>test cases</li>
 * <li>feature values</li>
 * </ul>
 * 
 * </br>
 * The main() method generates combinatorial tests in .csv files for all models in the <i>evolutionModels</i> folder. 
 * 
 */
public class DistancesCalculator {
	
	/*
	 * TestSuite is a List of Test
	 * each Test is a Map<key,value> with Map<feature,value>
	 * in particular Map<key,value> is implemented as TreeMap<String,String>
	 * 
	 * */

	public static int testCasesDist(Test t, Test tp) {
	
		// Set guarantee that there won't be duplicates values in "features" set
		Set<String> features = new TreeSet<String>();
		features.addAll(t.keySet());
		features.addAll(tp.keySet());
		
		int testCasesDist=0;
		
		for(String s : features) {
			// get returns the value to which the specified key is mapped
			// or null if this map contains no mapping for the key
			testCasesDist += DistancesCalculator.featDist(t.get(s), tp.get(s));
		}
		
		return testCasesDist;
		
	};
	
	/**
	 * Compares the values of feature f with fp
	 * 
	 * @param f the feature value of T
	 * @param fp (=f') the feature value of T'
	 * @return 0 if the values of f and fp are the same, 1 otherwise
	 */
	public static int featDist(String f, String fp) {
		if(f==null || fp == null || !f.equals(fp))
			return 1;
		else
			return 0;
	}
	
	static TestSuite getTestSuite(String pathToFM) {
		// import Feature Model .xml
		FeatureIdeImporterBoolean importer = new FeatureIdeImporterBoolean();
		CitModel model = importer.importModel(pathToFM);
		ModelUtils mu = new ModelUtils(model);
		System.out.println(mu.serializeToString());
		// generate boolean TS with ACTS (pair-wise)
		ACTSTranslator acts = new ACTSTranslator();
		return acts.getTestSuite(model, 2, false);
	}

}
