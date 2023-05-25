package ctwedge.modelanalyzer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ctwedge.ctWedge.CitModel;
import ctwedge.util.ext.Utility;

public class BenchmarkModelsAnalyzer {

	public static void main(String[] args) throws IOException {
		extracted(BooleanOnlyParameters.eInstance);
		extracted(FobiddenTuples.eInstance);
		extracted(UseRelationalOperators.eInstance);
		extracted(new CTWedgeModelAnalyzer(){

			@Override
			public boolean process(CitModel model) {
				// TODO Auto-generated method stub
				return true;
			}});
		extracted(AlsoEnumerativeParameters.eInstance);
		extracted(AlsoIntegerParameters.eInstance);
		extracted(AllTheSameCardinality.eInstance);
		extracted(AllInCNF.eInstance);
		extracted(NoConstraints.eInstance);
	}

	private static void extracted(CTWedgeModelAnalyzer eInstance) throws IOException {
		Stream<Path> walk = Files.walk(Paths.get("models_test"));
		Stream<CitModel> models = walk
		.filter(x -> x.getFileName().toString().endsWith(".ctw")) // include only valid files
		.map(m -> Utility.loadModelFromPath(m.toString())) // read
		.filter(m -> eInstance.process(m));		
		List<String> modelsAsList = models.map(x->x.getName()).collect(Collectors.toList());
		System.out.println(modelsAsList.size());
		System.out.println(modelsAsList.stream().limit(10).collect(Collectors.toList()));
		walk.close();
	}

}
