package ctwedge.generator.benchmarks;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
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

public class BenchmarkTest {
		
	public static class GeneratorExec implements Callable<TestSuite> {
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
		IExtensionPoint ep = reg.getExtensionPoint("ctwedge.ui.ctwedgeGenerators");
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
		
		//	Eseguo ogni file con tutti i generatori
		for (File file : fileList) {
			//System.out.println("\n----- " + file.getName() + " -----");
			sb.append("\n----- " + file.getName() + " -----");
			sb.append("\n");
			for (Benchmarkable gen : generators) {
				String model;
				try {
					//System.out.println("\t" + gen.getClass().getSimpleName());
					sb.append("\t" + gen.getClass().getSimpleName());
					sb.append("\n");
					model = readFromFile(file);
					//TestSuite result = gen.benchmark_run(Utility.loadModel(model));
					Future<TestSuite> ts_future = Executors.newSingleThreadExecutor().submit(new GeneratorExec(model, gen));
					TestSuite result = null;
					try {
						result = ts_future.get(30, TimeUnit.SECONDS);
					} catch (TimeoutException ex) {
						ts_future.cancel(true);
						sb.append("\t\t timeout");
						sb.append("\n");
						continue;
			        }
					if (result != null) {
						//System.out.println("\t\t #test = " + result.getTests().size());
						//System.out.println("\t\t time = " + result.getGeneratorTime() + " millis.");
						sb.append("\t\t #test = " + result.getTests().size());
						sb.append("\n");
						sb.append("\t\t time = " + result.getGeneratorTime() + " millis.");
						sb.append("\n");
					} else {
						//System.out.println("\t\t null");
						sb.append("\t\t null");
						sb.append("\n");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		System.out.println(sb.toString());
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
