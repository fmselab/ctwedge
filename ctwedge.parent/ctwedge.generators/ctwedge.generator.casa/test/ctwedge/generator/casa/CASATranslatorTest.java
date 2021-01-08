package ctwedge.generator.casa;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.Test;

import ctwedge.generator.util.Utility;
import ctwedge.util.TestSuite;
import ctwedge.util.ext.ICTWedgeTestGenerator;

public class CASATranslatorTest {
	
	@Test
	public void test() throws Exception {
		ctwedge.ctWedge.CitModel citModel = Utility.loadModel("Model Phone\nParameters:\n emailViewer : Boolean\n textLines:  [ 25 .. 30 ]\n display : {16MC, 8MC, BW}\n\n Constraints:\n  # textLines=28 #\n");	
		//System.out.println(new CASATranslator().getTestSuite(m, 2, false));
		ToCasaParametersExporter mygen = new ToCasaParametersExporter();
		// get the model in casa
		CharSequence modelS = mygen.toCasaCode(citModel, 2);
		System.out.println(modelS);

		ConvertToAbstractID exporter = new ConvertToAbstractID(citModel);
		CharSequence constraints = exporter.translateConstraints(); // can throw exception
		assert constraints != null;
		System.out.println(constraints);
	}
	
	public class GeneratorExec implements Callable<TestSuite> {
		String model;
		ICTWedgeTestGenerator generator;
		
        public GeneratorExec(String model, ICTWedgeTestGenerator generator) {
			super();
			this.model = model;
			this.generator = generator;
		}

		@Override
        public TestSuite call() throws Exception {
            return generator.getTestSuite(Utility.loadModel(model), 2, false);
        }
    }
	
	@Test
	public void benchmark() {
		
		CASATranslator generator = new CASATranslator();
		
		// Builder risultato
		StringBuilder sb_csv = new StringBuilder();
		
		int errors = 0;
		int timeouts = 0;
		String error_files = "";
		String timeout_files = "";
		
		sb_csv.append("Model name;");
		sb_csv.append(generator.getGeneratorName() + " #test;" + generator.getGeneratorName() + " time;");
		sb_csv.append("\n");
		
		
		List<File> fileList = new ArrayList<>();
		Path path = Paths.get("../../ctwedge.benchmarks/models_test");
		listFiles(path.toFile(), fileList);
		for (File file : fileList) {
			sb_csv.append(file.getName() + ";");
			System.out.println("*************************************** " + file.getName());
			String model;
			try {
				model = Files.readString(file.toPath());
				ExecutorService executor = Executors.newSingleThreadExecutor();
				Future<TestSuite> ts_future = executor.submit(new GeneratorExec(model, generator));
				TestSuite result = null;
				try {
					//	Timeout 60 sec.
					result = ts_future.get(60, TimeUnit.SECONDS);
				} catch (TimeoutException ex) {
					System.out.println("--- TIMEOUT---");
					ts_future.cancel(true);
					executor.shutdown();
					if (generator.executableProcess != null) {
						generator.executableProcess.destroy();
					}
					timeouts++;
					timeout_files += file.getName() + "\n";
					sb_csv.append("timeout;timeout;");
					continue;
		        }
				if (result == null) {
					sb_csv.append("null;null;");
					errors++;
					error_files += file.getName() + "\n";ts_future.cancel(true);
					executor.shutdown();
				} else {
					sb_csv.append(result.getTests().size() + ";" + result.getGeneratorTime() + ";");
					System.out.println("\t #tests: " + result.getTests().size());
				}
			} catch (Exception e) {
				sb_csv.append("null;null;");
				errors++;
				error_files += file.getName() + "\n";
				e.printStackTrace();
			}
		}
		System.out.println("******\n"
				+ "Test eseguiti: " + fileList.size() + "\n"
				+ "Errori: " + errors);
		System.out.println("******\n"
				+ "File con errore: \n" + error_files);
		System.out.println("******\n"
				+ "Timeout: " + timeouts);
		System.out.println("******\n"
				+ "File con timeout: \n" + timeout_files);
		
		String data = new SimpleDateFormat("ddMMyyyy_HHmmss").format(Calendar.getInstance().getTime());
		
		try {	
			File out_csv = new File("bench_output/benchmark_" + data + ".csv");
			BufferedWriter writer_csv = new BufferedWriter(new FileWriter(out_csv));
			writer_csv.write(sb_csv.toString());	
			writer_csv.close();
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	public void listFiles(File folder, List<File> fileList) {
		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()	&& (listOfFiles[i].getName().endsWith(".citw") || listOfFiles[i].getName().endsWith(".ctw"))) {
				fileList.add(listOfFiles[i]);
			} else if (listOfFiles[i].isDirectory()) {
				listFiles(listOfFiles[i], fileList);
			}
		}
	}
	
}
