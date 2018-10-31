package ctwedge.ui.views.generator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import ctwedge.ctWedge.CitModel;
import ctwedge.ui.Activator;
import ctwedge.ui.utils.Statistics;
import ctwedge.ui.utils.IsStoppedPolling;
import ctwedge.util.Pair;
import ctwedge.util.TestSuite;
import ctwedge.util.ext.CTWedgeException;
import ctwedge.util.ext.ICTWedgeTestGenerator;

/**
 * compare the generation and produces a file for wiki google
 * 
 * @author Paolo Vavassori
 * 
 */
public class CitLabPortfolio {
	private boolean ignoreConstraints;
	private int nWise;
	private ICTWedgeTestGenerator generator;
	private CitModel citModel;

	private static final int NRuns = 1;

	public CitLabPortfolio(boolean ignoreConstraints, int nwise) {
		this.ignoreConstraints = ignoreConstraints;
		this.nWise = nwise;
	}

	public void run(List<Pair<ICTWedgeTestGenerator, CitModel>> generators) {
		System.out.println(":::::::::::::::::::" + generators.size());
		// do not validate the models
		// ModelsFromFilesUtils.VALIDATE = false;
		// init the file where to save
		// File resultFile = new File(path);
		// FileOutputStream fileOutputStream = null;
		// try {
		// fileOutputStream = new FileOutputStream(resultFile);
		// } catch (FileNotFoundException e3) {
		// // TODO Auto-generated catch block
		// e3.printStackTrace();
		// }
		// PrintStream StreamOutput= new PrintStream(new BufferedOutputStream(
		// fileOutputStream));
		//
		//
		//
		//
		// printInfo(StreamOutput);
		// print header of table
		// StreamOutput.print("|| *model* \t");
		// StreamOutput.print("|| *generator* \t");
		// StreamOutput.print("|| *time avg.*\t");
		// StreamOutput.print("|| *time stddev.*\t");
		// StreamOutput.print("|| *size avg.*\t");
		// StreamOutput.print("|| *size stddev.*\t");
		// StreamOutput.println("||");

		Map<String, Pair<Double, Double>> dataSizeTime = new HashMap<>();
		for (Pair<ICTWedgeTestGenerator, CitModel> generator : generators) {
			String name = generator.getSecond().getName();
			dataSizeTime.put(name, new Pair<Double, Double>(0.0, 0.0));
		}

		try {
			generate(generators, NRuns, dataSizeTime);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// print results
		// StreamOutput
		// .print("\nsummary over the specs correclty solved by all the generators\n");
		// StreamOutput
		// .print("||generator || sum mean of size || sum mean of time ||\n");
		// for (Iterator<Entry<String, Pair<Double, Double>>> iterator =
		// dataSizeTime
		// .entrySet().iterator(); iterator.hasNext();) {
		// Entry<String, Pair<Double, Double>> e = iterator.next();
		// StreamOutput.print("|| " + e.getKey() + " || "
		// + e.getValue().getFirst() + " || "
		// + e.getValue().getSecond() + " ||\n");
		// }
		// //
		// StreamOutput.close();

	}

	// private static void printInfo(PrintStream fileStdOutput) {
	// // header
	// fileStdOutput.println("=== benchmarking with constraints===\n");
	// DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	// Date date = new Date();
	// fileStdOutput.println("`date of execution: " + dateFormat.format(date)
	// + "`\n");
	// fileStdOutput.println("`number of runs: " + NRuns + "`\n");
	// //
	// fileStdOutput
	// .println("`CASA: Covering Arrays by Simulated Annealing (CASA) 1.1`\n");
	// fileStdOutput.println("`ACTS: acts_cmd_beta_v2_r2.8.jar`\n");
	// fileStdOutput
	// .println("`medici: v1: (20, 25, 5, 200) - v2: (30, 50, 10, 500)`\n");
	// }

	static final String data = "\t||%g||%g\t";

	// generate

	private void generate(List<Pair<ICTWedgeTestGenerator, CitModel>> generators, int nt,
			Map<String, Pair<Double, Double>> dataSizeTime) throws InterruptedException {
		// write model name
		// printOutput.print(String.format(
		// generators.get(0).getSecond().getName(), generators.get(0)
		// .getSecond().getName()));
		double[] sizes = new double[nt];
		double[] times = new double[nt];
		// boolean first = true;
		//
		Map<String, Pair<Double, Double>> dataRun = new HashMap<>();
		// generate
		for (Pair<ICTWedgeTestGenerator, CitModel> pair : generators) {
			generator = pair.getFirst();
			assert generator != null;
			citModel = pair.getSecond();
			assert citModel != null;
			boolean error = false;
			// if (first)
			// first = false;
			// else
			// skip first column
			// printOutput.print("||\t");
			// printOutput.print("|| " + pair.getSecond());
			for (int i = 0; i < nt; i++) {
				System.out.println(
						"**** model " + pair.getSecond().getName() + " run: " + i + " generator: " + pair.getSecond());
				try {

					Job generation = new SafeGeneratorRunnable(generator, citModel);
					generation.setPriority(Job.LONG);
					generation.setUser(true);
					generation.schedule();

					// sizes[i] = test.getTests().size();
					// times[i] = test.getGeneratorTime();
				} catch (Throwable e) {
					// printOutput.print("\t|| ERROR " + e.getClass() +
					// "||");
					error = true;
					e.printStackTrace();
					break;
				}
			}
			if (!error) {
				// time
				Statistics timesS = new Statistics(times);
				double meanTime = timesS.getMean();
				// printOutput.print(String.format(data, meanTime,
				// timesS.getStdDev()));
				// // size
				Statistics sizesS = new Statistics(sizes);
				double meanSize = sizesS.getMean();
				// // printOutput.print(String.format(data, meanSize,
				// sizesS.getStdDev()));
				dataRun.put(pair.getSecond().getName(), new Pair<Double, Double>(meanSize, meanTime));

			}
			// printOutput.println("||");
			// printOutput.flush();
			Thread.sleep(1000);
		}
		// add this data if alla completed
		if (dataRun.entrySet().size() == generators.size()) {
			for (Pair<ICTWedgeTestGenerator, CitModel> generator : generators) {
				String name = generator.getSecond().getName();
				Pair<Double, Double> tillNow = dataSizeTime.get(name);
				Pair<Double, Double> pair = dataRun.get(name);
				dataSizeTime.put(name, new Pair<Double, Double>(tillNow.getFirst() + pair.getFirst(),
						tillNow.getSecond() + pair.getSecond()));
			}
		}
		return;
	}

	private class SafeGeneratorRunnable extends Job {
		private Boolean flagNotConvertable = false;
		private String trace;
		private TestSuite output;
		// private PrintStream printOutput;

		public SafeGeneratorRunnable(ICTWedgeTestGenerator generator, CitModel citmodel) {
			super("Generation of the testuite " + generator.getGeneratorName());
			// this.printOutput = printOutput;

		}

		@Override
		protected IStatus run(final IProgressMonitor monitor) {

			ISafeRunnable runnable = new ISafeRunnable() {

				@Override
				public void handleException(Throwable exception) {
					if (exception instanceof CTWedgeException) {
						flagNotConvertable = true;
						trace = generator.getGeneratorName() + "  \n" + exception.getMessage() + "  \n";
						// e.printStackTrace();
					}
					if (exception instanceof java.lang.OutOfMemoryError) {
						trace = " EXCEEDED MEMORY LIMIT";
						// e.printStackTrace();
					}
				}

				@Override
				public void run() {
					System.out.println("A. I am running....");
					output = generator.generateTestsAndInfo(citModel, ignoreConstraints, nWise);
					System.out.println("B. Output generated " + output);

				}
			};

			monitor.beginTask("Generation", IProgressMonitor.UNKNOWN);
			IsStoppedPolling stopper = new IsStoppedPolling("", monitor, false, generator);
			stopper.setSystem(true);
			stopper.schedule();

			SafeRunner.run(runnable);

			if (monitor.isCanceled())
				return Status.CANCEL_STATUS;
			monitor.done();
			monitor.setCanceled(true);
			Display.getDefault().syncExec(new Runnable() {
				@Inject
				Shell shell;

				@Override
				public void run() {
					if (flagNotConvertable) {
						MessageDialog.openError(shell, "ERROR", "MODEL NOT TRANSLATABLE \n" + trace);
					}

					if (output != null) {

						try {
							DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
							Date date = new Date();

							TestSuiteView view = (TestSuiteView) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
									.getActivePage().showView(Activator.PLUGIN_ID + ".output",
											Activator.PLUGIN_ID + ".outputSecondaryID" + output.getModel().getName()
													+ "  " + output.getGeneratorName() + "   "
													+ dateFormat.format(date).replace(":", "__"),
											2);

							view.setTestsuite(output);
							// synchronized (printOutput) {
							// // printOutput.print(run + "\t");
							// // write model name
							// printOutput.print(output.getModel().getName()
							// + "\t");
							// // generate
							// printOutput.print(generator.getGeneratorName()
							// + "\t");
							// printOutput.print(output.getTests().size()
							// + "\t");
							// printOutput.print(output.getGeneratorTime()
							// + "\t");
							// printOutput.print(System.currentTimeMillis());
							// printOutput.println();
							// printOutput.flush();
							//
							// }

						} catch (PartInitException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					} else {
						if (!flagNotConvertable) {
							MessageDialog.openError(shell, "ERROR", "Generation ERROR \n" + trace);

							throw new RuntimeException("Output null problem with citModel parsing");
						}
					}

				}

			});

			return Status.OK_STATUS;
		}
	}
}
