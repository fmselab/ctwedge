package ctwedge.generator.medici;

import static org.junit.Assert.assertNotNull;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
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

import ctwedge.ctWedge.CitModel;
import ctwedge.generator.util.Utility;
import ctwedge.util.TestSuite;
import ctwedge.util.ext.ICTWedgeModelProcessor;
import ctwedge.util.ext.ICTWedgeTestGenerator;

public class MediciCITGeneratorTest {

	private static final String model1 = "Model prova\nParameters:\n a: Boolean; b: Boolean; c: Boolean;\nConstraints:\n # a #\n";
	private static final String model2 = "Model prova\nParameters:\n a: Boolean; b: Boolean; c: Boolean;\nConstraints:\n # not a #\n";
	private static final String model3 = "Model prova\nParameters:\n a: Boolean; b: Boolean; c: Boolean;\nConstraints:\n # a or c or c#\n";
	
	private static final String model4 = "Model: model_0\n" + 
			"Parameters:\n" + 
			"A0: { V0,V4,V1,V2,V3 }\n" + 
			"A2: { V0,V1,V2,V3 }\n" + 
			"A3: { V0,V1,V2,V3,V4,V5,V6,V7,V8 }\n" + 
			"A4: { V0,V1,V2 }\n" + 
			"\n" + 
			"Constraints:\n" + 
			"# ((A0!=V4) || A2=V3) #\n";
	
	private static final String modelPhone = "Model Phone\r\n" + 
			" Parameters:\r\n" + 
			"   emailViewer : Boolean\r\n" + 
			"   textLines:  [ 25 .. 30 ]\r\n" + 
			"   display : {16MC, 8MC, BW}\r\n" + 
			"\r\n" + 
			" Constraints:\r\n" + 
			"   # emailViewer => textLines > 28 #";

	private static final String modelPhone2 = "Model Phone\r\n" + 
			" Parameters:\r\n" + 
			"   emailViewer : Boolean\r\n" + 
			"   textLines:  [ 25 .. 30 ]\r\n" + 
			"   display : {16MC, 8MC, BW}\r\n" + 
			"\r\n" + 
			" Constraints:\r\n" + 
			"   # emailViewer => textLines = 28 #";

	
	static MediciCITGenerator medici = new MediciCITGenerator(); 
	
	@Test
	public void test1() throws Exception {
		String s = medici.getTestSuite(Utility.loadModel(model1), 2, false).toString();
		System.out.println("Risultato:\n"+s);
	}
	@Test
	public void test2() throws Exception {
		String s = medici.getTestSuite(Utility.loadModel(model2), 2, false).toString();
		System.out.println("Risultato:\n"+s);
	}
	@Test
	public void test3() throws Exception {
		String s = medici.getTestSuite(Utility.loadModel(model3), 2, false).toString();
		System.out.println("Risultato:\n"+s);
	}

	@Test
	public void test4() throws Exception {
		String s = medici.getTestSuite(Utility.loadModel(model4), 2, false).toString();
		System.out.println("Risultato:\n"+s);
	}
	
	@Test(expected = RuntimeException.class)
	public void test_phone() throws Exception {
		medici.getTestSuite(Utility.loadModel(modelPhone), 2, false).toString();
	}
	
	@Test
	public void test_phoneCorrect() throws Exception {
		String s = medici.getTestSuite(Utility.loadModel(modelPhone2), 2, false).toString();
		System.out.println("Risultato:\n"+s);
	}
	
	@Test
	public void test_Storage5() throws Exception {
		CitModel loadModel = Utility.loadModelFromPath("../../ctwedge.benchmarks/models_test/Storage5.ctw");
		loadModel.getName();
		assertNotNull(loadModel);
		assertNotNull(loadModel.getName());
		String s = medici.getTestSuite(loadModel, 2, false).toString();
		System.out.println("Risultato:\n"+s);
	}
	@Test
	public void test_Model10() throws Exception {
		String modelpath = "../../ctwedge.benchmarks/models_test/model_10.ctw";
		CitModel loadModel = Utility.loadModelFromPath(modelpath);
		loadModel.getName();
		// Temp what if I use anothe rmethod with the validation
		ICTWedgeModelProcessor.getModel(modelpath);		
		assertNotNull(loadModel);
		assertNotNull(loadModel.getName());
		String s = medici.getTestSuite(loadModel, 2, false).toString();
		System.out.println("Risultato:\n"+s);
	}
		
	//	Usare Junit Plug-In test
	@Test
	public void test_bench() {
		try {
			MediciCITGenerator generator = new MediciCITGenerator();
			TestSuite ts = generator.getTestSuite(Utility.loadModel(model4), 2, false);
			System.out.println(ts.getTests().size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	@Test
	public void testFolder() {
		int errors = 0;
		String error_files = "";
		MediciCITGenerator generator = new MediciCITGenerator();
		List<File> fileList = new ArrayList<>();
		Path path = Paths.get("../../ctwedge.benchmarks/models_test");
		listFiles(path.toFile(), fileList);
		for (File file : fileList) {
			String model;
			try {
				if (generator.getTestSuite(Utility.loadModelFromPath(file.getPath()), 2, false) == null) {
					errors++;
					error_files += file.getName() + "\n";
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
		
	public class GeneratorExec implements Callable<TestSuite> {
		File model;
		ICTWedgeTestGenerator generator;
		
        public GeneratorExec(File model, ICTWedgeTestGenerator generator) {
			super();
			this.model = model;
			this.generator = generator;
		}

		@Override
        public TestSuite call() throws Exception {
            return  generator.getTestSuite(Utility.loadModelFromPath(model.getPath()), 2, false);
        }
    }
	
	@Test
	public void benchmark() {
		
		MediciCITGenerator generator = new MediciCITGenerator();
		
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
			try {
				ExecutorService executor = Executors.newSingleThreadExecutor();
				Future<TestSuite> ts_future = executor.submit(new GeneratorExec(file, generator));
				TestSuite result = null;
				try {
					//	Timeout 60 sec.
					result = ts_future.get(150, TimeUnit.SECONDS);
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
			sb_csv.append("\n");
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
		
		System.out.println("\n\n\n" + sb_csv.toString());
		
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

}
