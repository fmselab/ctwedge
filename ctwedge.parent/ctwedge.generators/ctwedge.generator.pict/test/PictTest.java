
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ctwedge.ctWedge.CitModel;
import ctwedge.generator.pict.PICTGenerator;
import ctwedge.util.TestSuite;
import ctwedge.util.ext.Utility;

public class PictTest {

	@Test
	public void test() {
		try {
			String model = "Model Concurrency\r\n" + "\r\n" + "Parameters:\r\n" + "p1: { v1 v2 };\r\n"
					+ "p2: { v1 v2 };\r\n" + "p3: { v1 v2 };\r\n" + "p4: Boolean;\r\n" + "p5: Boolean;\r\n" + "\r\n"
					+ "Constraints:\r\n" + "	# ( p3!=v1 OR p2!=v1 OR p5 OR p4 OR p1!=v1) #\r\n"
					+ "	# ( p1!=v2 OR p5!=true) #\r\n" + "	# ( p2!=v1 OR p5 OR p4!=true OR p3!=v2 OR p1!=v1) #\r\n"
					+ "	# ( p5!=true OR p2!=v2) #\r\n" + "	# ( p4 OR p3!=v2 OR p1!=v1) #\r\n"
					+ "	# ( p4!=true OR p1!=v2) #\r\n" + "	# ( p3!=v1 OR p4!=true) #";
			PICTGenerator generator = new PICTGenerator();
			// Test con constraint
			generator.getTestSuite(Utility.loadModel(model), 2, false);
			// Test senza constraint
			generator.getTestSuite(Utility.loadModel(model), 2, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testFolder() {
		PICTGenerator generator = new PICTGenerator();
		List<File> fileList = new ArrayList<>();
		Path path = Paths.get("../../ctwedge.benchmarks/models_test");
		listFiles(path.toFile(), fileList);
		for (File file : fileList) {
			String model;
			try {
				model = Files.readString(file.toPath());
				generator.getTestSuite(Utility.loadModel(model), 2, false);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	public void test1() {
		String pathname = "examples/ADD_BOOLC_1.ctw";
		extracted(pathname);
	}

	@Test
	public void test2() {
		String pathname = "examples/ADD_BOOLC_0.ctw";
		extracted(pathname);
	}

	private void extracted(String pathname) {
		PICTGenerator generator = new PICTGenerator();
		try {
			File model = new File(pathname);
			assert model.exists();
			CitModel loadModel = Utility.loadModelFromPath(model.getAbsolutePath());
			TestSuite ts = generator.getTestSuite(loadModel, 2, false);
			System.out.println(ts.getTests().size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void listFiles(File folder, List<File> fileList) {
		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()
					&& (listOfFiles[i].getName().endsWith(".citw") || listOfFiles[i].getName().endsWith(".ctw"))) {
				fileList.add(listOfFiles[i]);
			} else if (listOfFiles[i].isDirectory()) {
				listFiles(listOfFiles[i], fileList);
			}
		}
	}

	@Test
	public void convertCTComp() throws IOException {
		Path path = Paths.get("E:\\GitHub\\CIT_Benchmark_Generator\\Benchmarks_CITCompetition_2022\\CTWedge\\");
		Files.walk(path).filter(Files::isRegularFile).map(Path::toFile).filter(x -> x.getName().endsWith(".ctw"))
				.forEach(x -> {
					System.out.println(x.getName());
					ctwedge.ctWedge.CitModel citModel = Utility.loadModelFromPath(x.getAbsolutePath());
					BufferedWriter out;
					try {
						out = new BufferedWriter(new FileWriter(
								"E:\\GitHub\\CIT_Benchmark_Generator\\Benchmarks_CITCompetition_2022\\PICT\\"
										+ citModel.getName() + ".txt"));
						PICTGenerator translator = new PICTGenerator();
						out.append(translator.translateModel(citModel, false));
						out.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
	}
	
	@Test
	public void convertCTCompFollowUp() throws IOException {
		Path path = Paths.get("E:\\GitHub\\CIT_Benchmark_Generator\\Benchmarks_FollowUp_CITCompetition_2022\\CTWedge\\");
		Files.walk(path).filter(Files::isRegularFile).map(Path::toFile).filter(x -> x.getName().endsWith(".ctw"))
				.forEach(x -> {
					System.out.println(x.getName());
					ctwedge.ctWedge.CitModel citModel = Utility.loadModelFromPath(x.getAbsolutePath());
					BufferedWriter out;
					try {
						out = new BufferedWriter(new FileWriter(
								"E:\\GitHub\\CIT_Benchmark_Generator\\Benchmarks_FollowUp_CITCompetition_2022\\PICT\\"
										+ citModel.getName() + ".txt"));
						PICTGenerator translator = new PICTGenerator();
						out.append(translator.translateModel(citModel, false));
						out.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
	}

	@Test
	public void convertCTComp2023() throws IOException {
		Path path = Paths.get("E:\\GitHub\\CIT_Benchmark_Generator\\Benchmarks_CITCompetition_2023\\CTWedge\\");
		Files.walk(path).filter(Files::isRegularFile).map(Path::toFile).filter(x -> x.getName().endsWith(".ctw"))
				.forEach(x -> {
					System.out.println(x.getName());
					ctwedge.ctWedge.CitModel citModel = Utility.loadModelFromPath(x.getAbsolutePath());
					BufferedWriter out;
					try {
						out = new BufferedWriter(new FileWriter(
								"E:\\GitHub\\CIT_Benchmark_Generator\\Benchmarks_CITCompetition_2023\\PICT\\"
										+ citModel.getName() + ".txt"));
						PICTGenerator translator = new PICTGenerator();
						out.append(translator.translateModel(citModel, false));
						out.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
	}
	
	@Test
	public void convertCTComp2024() throws IOException {
		Path path = Paths.get("C:\\Users\\Andrea_PC\\Desktop\\CTCompetition2024\\CTWedge");
		Files.walk(path).filter(Files::isRegularFile).map(Path::toFile).filter(x -> x.getName().endsWith(".ctw") && x.getName().startsWith("INDUSTRIAL_"))
				.forEach(x -> {
					System.out.println(x.getName());
					ctwedge.ctWedge.CitModel citModel = Utility.loadModelFromPath(x.getAbsolutePath());
					BufferedWriter out;
					try {
						out = new BufferedWriter(new FileWriter(
								"C:\\Users\\Andrea_PC\\Desktop\\CTCompetition2024\\PICT\\"
										+ citModel.getName() + "_pict.txt"));
						PICTGenerator translator = new PICTGenerator();
						out.append(translator.translateModel(citModel, false));
						out.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
	}

}
