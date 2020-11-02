
import ctwedge.generator.pict.PICTGenerator;
import ctwedge.generator.util.Benchmarkable;
import ctwedge.generator.util.Utility;
import ctwedge.util.TestSuite;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.Test;


public class PictTest {
	
	@Test
	public void test() {
		try {
			String model = "Model Concurrency\r\n" + 
					"\r\n" + 
					"Parameters:\r\n" + 
					"p1: { v1 v2 };\r\n" + 
					"p2: { v1 v2 };\r\n" + 
					"p3: { v1 v2 };\r\n" + 
					"p4: Boolean;\r\n" + 
					"p5: Boolean;\r\n" + 
					"\r\n" + 
					"Constraints:\r\n" + 
					"	# ( p3!=v1 OR p2!=v1 OR p5 OR p4 OR p1!=v1) #\r\n" + 
					"	# ( p1!=v2 OR p5!=true) #\r\n" + 
					"	# ( p2!=v1 OR p5 OR p4!=true OR p3!=v2 OR p1!=v1) #\r\n" + 
					"	# ( p5!=true OR p2!=v2) #\r\n" + 
					"	# ( p4 OR p3!=v2 OR p1!=v1) #\r\n" + 
					"	# ( p4!=true OR p1!=v2) #\r\n" + 
					"	# ( p3!=v1 OR p4!=true) #";
			PICTGenerator generator = new PICTGenerator();
			//	Test con constraint
			generator.getTestSuite(Utility.loadModel(model), 2, false);
			//	Test senza constraint
			generator.getTestSuite(Utility.loadModel(model), 2, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
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
	public void testFolder2() {
		int errors = 0;
		int timeouts = 0;
		String error_files = "";
		String timeout_files = "";
		PICTGenerator generator = new PICTGenerator();
		List<File> fileList = new ArrayList<>();
		listFiles(new File("new_models/"), fileList);
		for (File file : fileList) {
			System.out.println("*************************************** " + file.getName());
			String model;
			try {
				model = readFromFile(file);
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
					generator.destroyProcess();
					timeouts++;
					timeout_files += file.getName() + "\n";
					continue;
		        }
				if (result == null) {
					errors++;
					error_files += file.getName() + "\n";ts_future.cancel(true);
					executor.shutdown();
				} else {
					System.out.println("\t #tests: " + result.getTests().size());
				}
			} catch (Exception e) {
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
				+ "Test timeout: " + fileList.size() + "\n"
				+ "Timeour: " + timeouts);
		System.out.println("******\n"
				+ "File con timeout: \n" + timeout_files);
	}
	
	@Test
	public void testFolder() {
		PICTGenerator generator = new PICTGenerator();
		List<File> fileList = new ArrayList<>();
		listFiles(new File("models/"), fileList);
		for (File file : fileList) {
			String model;
			try {
				model = readFromFile(file);
				generator.getTestSuite(Utility.loadModel(model), 2, false);
			} catch (Exception e) {
				e.printStackTrace();
			}
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
