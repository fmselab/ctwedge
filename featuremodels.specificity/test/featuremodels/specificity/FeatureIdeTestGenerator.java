package featuremodels.specificity;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

import org.junit.Test;
import de.ovgu.featureide.fm.core.cli.ConfigurationGenerator;
import de.ovgu.featureide.fm.core.init.FMCoreLibrary;

public class FeatureIdeTestGenerator {

	
	@Test
	public String generaTest() throws FileNotFoundException {
		FMCoreLibrary.getInstance().install();
		
		ConfigurationGenerator  gen = new ConfigurationGenerator();
	
		return extracted(gen, "fmodels/Alternative.xml");
		
	}

	private String extracted(ConfigurationGenerator gen, String fmmodel) throws FileNotFoundException {
		String string = "tempts.txt";
		String[] args = {"-a", "incling", "-fm", fmmodel, "-o", string};
		gen.run(Arrays.asList(args));
		File text = new File(string);
	     
        //Creating Scanner instance to read File in Java
        Scanner scnr = new Scanner(text);
        String testSuiteCSV = "";
        while(scnr.hasNextLine()){
            String line = scnr.nextLine();
            line = line.substring(line.indexOf(";")+1);
            line = line.replaceAll("\\+", "true");
            line = line.replaceAll("\\-", "false");
            System.out.println("line " + " :" + line);
            testSuiteCSV += line + "\n";
        }   
        scnr.close();
        return testSuiteCSV;
	}
	
	
}
