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
import ctwedge.ctWedge.CitModel;
import ctwedge.ctWedge.Parameter;
import ctwedge.ctWedge.impl.BoolImpl;
import ctwedge.ctWedge.impl.EnumerativeImpl;
import ctwedge.ctWedge.impl.RangeImpl;
import ctwedge.generator.util.Utility;
import ctwedge.util.TestSuite;
import ctwedge.util.ext.ICTWedgeTestGenerator;

public class BenchmarkTest {
	
	private List<String> excludedGen = new ArrayList<String>();
	
	static boolean saveOutputToFile = true;
	
	static int timeout_sec = 90;
		
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
            return (generator.getTestSuite(Utility.loadModel(model), 2, false));
        }
    }
	
	@Test
	public void bench_test() throws CoreException, ClassNotFoundException, InvalidRegistryObjectException {
		//	Generatori da escludere
		excludedGen.add("Medici");
		excludedGen.add("CASA");
		
		//	Recupero le istanze delle classi dei generatori
		IExtensionRegistry reg = Platform.getExtensionRegistry();
		IExtensionPoint ep = reg.getExtensionPoint("ctwedge.util.ctwedgeGenerators");
		IExtension[] extensions = ep.getExtensions();
		ArrayList<ICTWedgeTestGenerator> generators = new ArrayList<>();
		for (int i = 0; i < extensions.length; i++) {
			IConfigurationElement[] ce = extensions[i].getConfigurationElements();			
			for (IConfigurationElement e : ce) {
					Object o = e.createExecutableExtension("GeneratorPrototype");
					if(o instanceof ICTWedgeTestGenerator)
						if (!excludedGen.contains(((ICTWedgeTestGenerator)o).getGeneratorName()))
							generators.add((ICTWedgeTestGenerator) o);
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
		for (ICTWedgeTestGenerator gen : generators) sb.append("\t" + gen.getGeneratorName() + "\n");
		sb.append("# test: " + fileList.size() + "\n");
		sb.append("Timeout: " + timeout_sec + "sec.\n");
		sb.append("Results:\n");
		sb_csv.append("Model name;");
		for (ICTWedgeTestGenerator gen : generators) sb_csv.append(gen.getGeneratorName() + " #test;" + gen.getGeneratorName() + " time;");
		sb_csv.append("\n");
		
		long t_start = System.currentTimeMillis();
		
		//	Eseguo ogni file con tutti i generatori
		for (File file : fileList) {
			sb_csv.append(file.getName() + ";");
			sb.append("\n----- " + file.getName() + " ----- \n");
			for (ICTWedgeTestGenerator gen : generators) {
				String model;
				boolean error_detected = true;
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
						if (gen.executableProcess != null) {
							gen.executableProcess.destroy();
						}
						sb_csv.append("timeout;timeout;");
						sb.append("\t\t timeout \n");
						continue;
			        }
					if (result != null) {
						//	Verifico che il tempo sia stato registrato, altrimenti ritorna errore
						if (result.getGeneratorTime() <= 0)
							continue;
							
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
					error_detected = false;
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (error_detected) {
					sb_csv.append("null;null;");
					sb.append("\t\t null \n");
				}
			}
				
			sb_csv.append("\n");
		}
		
		long t_end = System.currentTimeMillis();
		
		//	Print risultati benchmark
		String data = new SimpleDateFormat("ddMMyyyy_HHmmss").format(Calendar.getInstance().getTime());
		System.out.println(sb.toString());
		System.out.println(sb_csv.toString());
		System.out.println("\n\n Benchmark execution time: " + (t_end-t_start)/1000.0 + "sec.");
		sb.append("Benchmark execution time: " + (t_end-t_start)/1000.0 + "sec.");

		//	Salvataggio benchmark in file txt e csv
		if (saveOutputToFile) {
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
	}
	
	@Test
	public void calculateComplexity() throws Exception {
		List<File> fileList = new ArrayList<>();
		//listFiles(new File("new_models/"), fileList);
		listFiles(new File("models_test/"), fileList);
		StringBuilder csv_sb = new StringBuilder();
		csv_sb.append("Model name;Parameter;Constraint\n");
		for (File file : fileList) {
			int parameters = 0;
			int constraints = 0;
			int and = 0;
			int or = 0;
			int implies = 0;
			int not = 0;
			String model = readFromFile(file);
			CitModel citModel = Utility.loadModel(model);
			for (Parameter p : citModel.getParameters()) {
				if (p instanceof EnumerativeImpl)
					parameters += ((EnumerativeImpl) p).getElements().size();
				if (p instanceof RangeImpl)
					parameters += ((RangeImpl) p).getEnd() - ((RangeImpl) p).getBegin();
				if (p instanceof BoolImpl)
					parameters += 2;
			}
			constraints += citModel.getConstraints().size();
			BufferedReader fin = new BufferedReader(new FileReader(file));
			String line = "";
			boolean constr_tag = false;
			while ((line = fin.readLine()) != null) {
				if (line.contains("Constraints:"))
					constr_tag = true;
				if (!constr_tag)
					continue;
				int lastIndex = 0;
				while(lastIndex != -1){
				    lastIndex = line.indexOf("||",lastIndex);
				    if(lastIndex != -1){
				        or ++;
				        lastIndex += 2;
				    }
				}
				lastIndex = 0;
				while(lastIndex != -1){
				    lastIndex = line.indexOf("&&",lastIndex);
				    if(lastIndex != -1){
				        and ++;
				        lastIndex += 2;
				    }
				}
				lastIndex = 0;
				while(lastIndex != -1){
				    lastIndex = line.indexOf("=>",lastIndex);
				    if(lastIndex != -1){
				        implies ++;
				        lastIndex += 2;
				    }
				}
				not += line.chars().filter(c -> c == '!').count();
			}
			csv_sb.append(file.getName() + ";" +
					parameters + ";" +
					(constraints + and + or + implies + not) + "\n");
			fin.close();
		}
		System.out.println(csv_sb.toString());
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
