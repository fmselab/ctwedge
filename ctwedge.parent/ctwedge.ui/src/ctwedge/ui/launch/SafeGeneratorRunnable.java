package ctwedge.ui.launch;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import ctwedge.ctWedge.CitModel;
import ctwedge.ui.views.generator.TestSuiteView;
import ctwedge.ui.utils.IsStoppedPolling;
import ctwedge.util.CTWedgeException;
import ctwedge.util.TestSuite;
import ctwedge.util.ext.ICTWedgeTestGenerator;

public class SafeGeneratorRunnable extends Job {
	private Boolean flagNotConvertable = false;
	private String trace;

	private SafeGeneratorRunnable() {
		super("Generation of the testuite");
	}
	
	private int nWise;
	private CitModel citModel;
	private boolean ignoreConstraints;
	public TestSuite output;
	private String generatorName;
	private ICTWedgeTestGenerator generator;

	public SafeGeneratorRunnable(String name, int nWise, CitModel citModel, boolean ignoreConstraints,
			String generatorName, ICTWedgeTestGenerator generator) {
		super(name);
		this.nWise = nWise;
		this.citModel = citModel;
		this.ignoreConstraints = ignoreConstraints;
		this.generatorName = generatorName;
		this.generator = generator;
	}

	@Override
	protected IStatus run(final IProgressMonitor monitor) {

		ISafeRunnable runnable = new ISafeRunnable() {

			@Override
			public void handleException(Throwable exception) {
				if (exception instanceof CTWedgeException) {
					flagNotConvertable = true;
					trace = exception.getMessage();
					// e.printStackTrace();
				}
				if (exception instanceof java.lang.OutOfMemoryError) {
					trace = generatorName.toUpperCase() + " EXCEEDED MEMORY LIMIT";
					// e.printStackTrace();
				}
			}

			@Override
			public void run() {

				/*
				 * MR:if (useSimplifier) { Simplificator simp = new Simplificator(citModel);
				 * output = generator.generateTestsAndInfo( simp.getSimplifiedVersion(),
				 * ignoreConstraints, ignoreSeeds, ignoreTestgoals, nWise); SemanticPreserver SP
				 * = new SemanticPreserver( output, simp); output = SP.preserve(); } else
				 */ 
				System.out.println("In safe runner");
					output = generator.generateTestsAndInfo(citModel, ignoreConstraints, nWise);
				System.out.println("End of safe runner "+output);
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
				if (flagNotConvertable)
					MessageDialog.openError(shell, "ERROR", "MODEL NOT TRANSLATABLE \n" + trace);
				if (output != null) {
					try {
						DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
						Date date = new Date();

						TestSuiteView view = (TestSuiteView) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
								.getActivePage().showView(ctwedge.ui.Activator.PLUGIN_ID + ".output",
										ctwedge.ui.Activator.PLUGIN_ID + ".outputSecondaryID"
												+ output.getModel().getName() + "  " + output.getGeneratorName()
												+ "   " + dateFormat.format(date).replace(":", "__"),
										IWorkbenchPage.VIEW_ACTIVATE);

						view.setTestsuite(output);

					} catch (PartInitException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// OutputDialog outD = new OutputDialog(shlGeneratorDialog, output);
					// outD.open();
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
