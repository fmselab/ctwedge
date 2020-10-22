package ctwedge.generator.benchmarks;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.InvalidRegistryObjectException;
import org.eclipse.core.runtime.Platform;
import org.junit.Test;

import ctwedge.generator.util.Benchmarkable;
import ctwedge.generator.util.Utility;
import ctwedge.util.TestSuite;
import ctwedge.util.validator.SMTTestSuiteValidator;

public class BenchmarkTest {
	
	static int timeout_sec = 120;
		
	public class GeneratorExec implements Callable<TestSuite> {
		String model;
		Benchmarkable generator;
		
        public GeneratorExec(String model, Benchmarkable generator) {
			super();
			this.model = model;
			this.generator = generator;
		}

		@Override
        public TestSuite call() throws Exception {
            return generator.benchmark_run(Utility.loadModel(model));
        }
    }
	
	@Test
	public void bench_test() throws CoreException, ClassNotFoundException, InvalidRegistryObjectException {
		
		//	Recupero le istanze delle classi dei generatori
		IExtensionRegistry reg = Platform.getExtensionRegistry();
		IExtensionPoint ep = reg.getExtensionPoint("ctwedge.util.ctwedgeGenerators");
		IExtension[] extensions = ep.getExtensions();
		ArrayList<Benchmarkable> generators = new ArrayList<>();
		for (int i = 0; i < extensions.length; i++) {
			IConfigurationElement[] ce = extensions[i].getConfigurationElements();			
			for (IConfigurationElement e : ce) {
					Object o = e.createExecutableExtension("GeneratorPrototype");
					if(o instanceof Benchmarkable)
						generators.add((Benchmarkable) o);
			}
		}
		
		//	Prendo la lista di tutti i file presenti
		List<File> fileList = new ArrayList<>();
		listFiles(new File("models_test/"), fileList);
		
		// Builder risultato
		StringBuilder sb = new StringBuilder();
		StringBuilder sb_csv = new StringBuilder();
		
		//	Descrizione benchmark
		sb.append("Data benchmark: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime()) + "\n");
		sb.append("Generator list:\n");
		for (Benchmarkable gen : generators) sb.append("\t" + gen.getGeneratorName() + "\n");
		sb.append("# test: " + fileList.size() + "\n");
		sb.append("Timeout: " + timeout_sec + "sec.\n");
		sb.append("Results:\n");
		sb_csv.append("Model name;");
		for (Benchmarkable gen : generators) sb_csv.append(gen.getGeneratorName() + " #test;" + gen.getGeneratorName() + " time;");
		sb_csv.append("\n");
		
		long t_start = System.currentTimeMillis();
		
		//	Eseguo ogni file con tutti i generatori
		for (File file : fileList) {
			sb_csv.append(file.getName() + ";");
			sb.append("\n----- " + file.getName() + " ----- \n");
			for (Benchmarkable gen : generators) {
				String model;
				try {
					sb.append("\t" + gen.getClass().getSimpleName() + "\n");
					model = readFromFile(file);
					ExecutorService executor = Executors.newSingleThreadExecutor();
					Future<TestSuite> ts_future = executor.submit(new GeneratorExec(model, gen));
					TestSuite result = null;
					try {
						result = ts_future.get(timeout_sec, TimeUnit.SECONDS);
					} catch (TimeoutException ex) {
						System.out.println("--- TIMEOUT---");
						ts_future.cancel(true);
						executor.shutdown();
						sb_csv.append("timeout;timeout;");
						sb.append("\t\t timeout \n");
						continue;
			        }
					if (result != null) {
						sb_csv.append(result.getTests().size() + ";" + result.getGeneratorTime() + ";");
						sb.append("\t\t #test = " + result.getTests().size() + "\n");
						sb.append("\t\t time = " + result.getGeneratorTime() + " millis." + "\n");
						
						// TODO
//						SMTTestSuiteValidator tsv = new SMTTestSuiteValidator();
//						sb.append("\n");
//						sb.append("\t\t #test_validi = " + tsv.howManyTestAreValid());
//						sb.append("\n");
//						sb.append("\t\t #tuple_coperte = " + tsv.howManyTuplesCovers());
//						sb.append("\n");
//						sb.append("\t\t isValid = " + tsv.isValid());
//						sb.append("\n");
//						sb.append("\t\t isComplete = " + tsv.isComplete());
//						sb.append("\n");
						
					} else {
						sb_csv.append("null;null;");
						sb.append("\t\t null \n");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			sb_csv.append("\n");
		}
		
		long t_end = System.currentTimeMillis();
		
		System.out.flush();
		
		String data = new SimpleDateFormat("ddMMyyyy_HHmmss").format(Calendar.getInstance().getTime());
		System.out.println(sb.toString());
		System.out.println(sb_csv.toString());
		System.out.println("\n\n Benchmark execution time: " + ((double)(t_end-t_start))/1000.0 + "sec.");
		sb.append("Benchmark execution time: " + (t_end-t_start)/1000.0 + "sec.");

		try {
			File out_desc = new File("bench_output/benchmark_" + data + ".txt");
			BufferedWriter writer_desc = new BufferedWriter(new FileWriter(out_desc));
			writer_desc.write(sb.toString());
			writer_desc.close();	
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
	
	public static String readFromFile(File f) throws Exception {
		StringBuilder sb = new StringBuilder();
		BufferedReader fin = new BufferedReader(new FileReader(f));
		String s = "";
		while ((s = fin.readLine()) != null)
			sb.append(s + "\n");
		fin.close();
		return sb.toString();
	}
	


}
