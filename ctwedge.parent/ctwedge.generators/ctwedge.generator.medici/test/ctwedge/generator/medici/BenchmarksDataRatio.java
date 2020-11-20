package ctwedge.generator.medici;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ctwedge.ctWedge.CitModel;
import ctwedge.generator.util.Utility;
import ctwedge.util.ext.ICTWedgeModelProcessor;

// use medici to compute the data about the benchmarks
public class BenchmarksDataRatio {

	@Test
	public void computeData() throws IOException {
		Path path = Paths.get("../../ctwedge.benchmarks");
		assertTrue(Files.exists(path));
		MediciCITGenerator gen = new MediciCITGenerator();
		FileWriter fw = new FileWriter("data.txt");
		Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

				//System.out.println(file.getFileName() + " " + Files.isRegularFile(file)
				//		+ file.getFileName().toString().endsWith(".ctw"));
				// if (!file.getFileName().toString().equals("model_10.ctw")) return FileVisitResult.CONTINUE;
				if (Files.isRegularFile(file) && file.getFileName().toString().endsWith(".ctw")) {
					System.out.println(file);
					// read as
					try {
						//CitModel loadModel = ICTWedgeModelProcessor.getModel();						
						CitModel loadModel = Utility.loadModelFromPath(file.toString());
						fw.write(file.getFileName().toString() + "\t");
						// convert to medici
						File model = new File("model.txt");
						FileWriter wf = new FileWriter(model);
						String translateModel = gen.translateModel(loadModel);
						wf.write(translateModel);
						System.out.println(translateModel);
						wf.close();
						// call medici
						List<String> command = new ArrayList<String>();
						command.add("./medici.exe");
						// model
						command.add("--m");
						command.add("model.txt");
						// do not generate
						command.add("--donotgenerate");
						System.out.println(command);
						// run
						ProcessBuilder pc = new ProcessBuilder(command);
						pc.command(command);
						pc.redirectError();
						Process p = pc.start();
						try {
							BufferedReader bri = new BufferedReader(new InputStreamReader(p.getInputStream()));
							String line;
							while ((line = bri.readLine()) != null) {
								System.out.println(line);
								// save to file
								if (line.contains("Cardinalita di partenza"))
									fw.write(line + "\t");
								if (line.contains("Cardinalita finale"))
									fw.write(line + "\t");
								if (line.contains("Generated tuples for 2-wise"))
									fw.write(line + "\t");
								if (line.contains("size dopo controllo copribilita"))
									fw.write(line + "\t");
							}
							bri.close();
							p.waitFor();
							System.out.println("command finished ");
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					} catch (Exception e) {
						fw.write(file.getFileName().toString() + "\t error:" + e.getMessage());
					}
					fw.write("\n");
					fw.flush();
					// return FileVisitResult.TERMINATE;
				}
				return FileVisitResult.CONTINUE;
			}
		});
		fw.close();
	}

}
