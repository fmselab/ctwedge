package ctwedge.generator.casa;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.Callable;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import ctwedge.util.ext.Utility;
import picocli.CommandLine;
import picocli.CommandLine.Parameters;

public class CASAConverterCLI  implements Callable<Integer> {

	@Parameters(index = "0", description = "The CTWedge model")
	String model = "";
	
	@Parameters(index = "1", description = "The strength")
	int strength = 2;
	
	public static void main(String[] args) {
		CASAConverterCLI converter = new CASAConverterCLI();
		int exitCode = new CommandLine(converter).execute(args);
		System.exit(exitCode);
	}

	@Override
	public Integer call() throws Exception {	
		File f = new File(model);		
		System.out.print(f.getName());
		ctwedge.ctWedge.CitModel citModel = Utility.loadModelFromPath(f.getAbsolutePath());
		ToCasaParametersExporter mygen = new ToCasaParametersExporter();
		BufferedWriter out;
		try {
			out = new BufferedWriter(new FileWriter(citModel.getName() + ".citmodel"));
			// get the model in casa
			CharSequence modelS = mygen.toCasaCode(citModel, strength);
			out.append(modelS);
			out.close();
			assert modelS.length() > 0;
			ConvertToAbstractID exporter = new ConvertToAbstractID(citModel);
			Logger.getLogger(ConvertToAbstractID.class).setLevel(Level.OFF);
			CharSequence constraints;
			try {
				constraints = exporter.translateConstraints();// can throw exception
				assert constraints != null;
				out = new BufferedWriter(new FileWriter(citModel.getName() + ".constraints"));
				out.append(constraints);
				out.close();
				System.out.println(" translation succeeded");
				return 0;
			} catch (Throwable e) {
				System.out.println(" translation failed: " + e.getClass() + " - " + e.getStackTrace());
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return -1;
	}

}
