package ctwedge.generator.pmedici;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


import ctwedge.ctWedge.CitModel;
import ctwedge.ctWedge.Constraint;
import ctwedge.ctWedge.Parameter;
import ctwedge.generator.util.ParameterSize;
import ctwedge.util.ModelUtils;
import ctwedge.util.ParameterValuesToInt;
import ctwedge.util.TestSuite;
import ctwedge.util.ext.ICTWedgeTranslTestGenerator;

public class PMediciCITGenerator extends ICTWedgeTranslTestGenerator{

	public PMediciCITGenerator() {
		super("PMedici");
	}

	@Override
	public TestSuite getTestSuite(CitModel loadModel, int t, boolean ignoreC) throws Exception {
		
	}
	
	// translation of the model to String
	@Override
	public String translateModel(CitModel sm, boolean ignoreConstraints) {

	}
}
