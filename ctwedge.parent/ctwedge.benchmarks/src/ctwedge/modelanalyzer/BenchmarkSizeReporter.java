package ctwedge.modelanalyzer;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.eclipse.emf.common.util.EList;

import ctwedge.ctWedge.CitModel;
import ctwedge.ctWedge.Enumerative;
import ctwedge.ctWedge.Parameter;
import ctwedge.util.ParameterSize;
import ctwedge.util.ext.Utility;

public class BenchmarkSizeReporter {
	
	public static void main(String[] args) throws IOException {
		Stream<Path> walk = Files.walk(Paths.get("models_test"));
		Stream<CitModel> models = walk
		.filter(x -> x.getFileName().toString().endsWith(".ctw")) // include only valid files
		.map(m -> Utility.loadModelFromPath(m.toString())); // read
	
		models.forEach(z ->{
			BigInteger size = BigInteger.ONE;
			EList<Parameter> parameters = z.getParameters(); // get parameter list
			for (Parameter p : parameters) {
				int size_p = ParameterSize.eInstance.doSwitch(p);
				if ( size_p == 0) {
					throw new RuntimeException();
				}
				size = size.multiply(BigInteger.valueOf(size_p));
			}			
			System.out.println(z.getName() + " " + size);
		});
		walk.close();
	}

}
