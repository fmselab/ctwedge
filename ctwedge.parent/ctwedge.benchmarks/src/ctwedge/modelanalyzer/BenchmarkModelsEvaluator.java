package ctwedge.modelanalyzer;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import ctwedge.generator.util.Utility;

public class BenchmarkModelsEvaluator {
	
	
	
	   public static void main(String[] args) throws IOException {
	        Stream<Path> walk = Files.walk(Paths.get("models"));
	        long value =  walk
	        .filter(x -> x.getFileName().toString().endsWith(".ctw")) // include only valid files
	        .map(m -> Utility.loadModelFromPath(m.toString())) // read
	        .filter(m -> BooleanOnlyParameters.eInstance.process(m))
	        .count();
	        System.out.println(value);
	    }

	
}
