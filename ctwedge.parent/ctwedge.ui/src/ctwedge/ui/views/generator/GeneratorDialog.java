/*******************************************************************************
 * Copyright (c) 2013 University of Bergamo - Italy
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *   Paolo Vavassori - initial API and implementation
 *   Angelo Gargantini - utils and architecture
 ******************************************************************************/
package ctwedge.ui.views.generator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wb.swt.SWTResourceManager;

import com.google.inject.Inject;

import ctwedge.ctWedge.CitModel;
import ctwedge.ctWedge.util.CtWedgeAdapterFactory;
import ctwedge.ui.utils.IsStoppedPolling;
import ctwedge.util.CTWedgeException;
import ctwedge.util.TestSuite;
import ctwedge.util.ext.ICTWedgeTestGenerator;

public class GeneratorDialog extends Dialog {

	private final class SafeGeneratorRunnable extends Job {
		private Boolean flagNotConvertable = false;
		private String trace;

		private SafeGeneratorRunnable() {
			super("Generation of the testuite");
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
					output = generator.generateTestsAndInfo(citModel, ignoreConstraints, nWise);
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

	private static final String SUPPORT_CONSTRAINTS = "support_constraints";
	private static final String SUPPORT_SEEDS = "support_seeds";
	private static final String SUPPORT_TESTGOALS = "support_testgoals";
	private static final String SUPPORT_GEN_PROPERTIES = "suppor_gen_properties";
	protected Object result;
	protected Shell shlGeneratorDialog;
	private CitModel citModel = null;
	private int nWise = 2;
	private TestSuite output;
	private ICTWedgeTestGenerator generator;
	private boolean supportConstraints;
	private boolean supportSeeds;
	private boolean supportTestGoals;
	private boolean supportProperties;
	protected boolean ignoreTestgoals;
	protected boolean ignoreSeeds;
	protected boolean ignoreConstraints;
	protected boolean useSimplifier = false;
	private String generatorName;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	@Inject
	Shell shell;

	public GeneratorDialog(Shell parent, int style, CitModel citModel, IConfigurationElement element) {

		super(parent, style);
		setGenerator(element);
		this.citModel = citModel;
		// CONSTRAINTS
		String attribute = element.getAttribute(SUPPORT_CONSTRAINTS);
		supportConstraints = (attribute != null) && attribute.equals(Boolean.TRUE.toString());
		// if it does not support constraints, ignore them
		ignoreConstraints = !supportConstraints;
		// SEEDS
		attribute = element.getAttribute(SUPPORT_SEEDS);
		supportSeeds = (attribute != null) && attribute.equals(Boolean.TRUE.toString());
		ignoreSeeds = !supportSeeds;
		// TEST GOALS
		attribute = element.getAttribute(SUPPORT_TESTGOALS);
		supportTestGoals = (attribute != null) && attribute.equals(Boolean.TRUE.toString());
		ignoreTestgoals = !supportTestGoals;
		// GEN PROPERTIES
		attribute = element.getAttribute(SUPPORT_GEN_PROPERTIES);
		supportProperties = (attribute != null) && attribute.equals(Boolean.TRUE.toString());
		//
		setText("Generator Dialog");
		// set the name
		generatorName = element.getAttribute("Algorithm");
	}

	private void btnGenerateSelected() {
		Job generation = new SafeGeneratorRunnable();
		generation.setPriority(Job.SHORT);
		generation.setUser(true);
		generation.schedule();
		shlGeneratorDialog.dispose();
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shlGeneratorDialog = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.MIN | SWT.MAX | SWT.APPLICATION_MODAL);
		shlGeneratorDialog.setSize(565, 282);
		shlGeneratorDialog.setText("Generator Dialog");
		shlGeneratorDialog.setLayout(new RowLayout(SWT.HORIZONTAL));

		Composite composite = new Composite(shlGeneratorDialog, SWT.NONE);
		composite.setLayoutData(new RowData(SWT.DEFAULT, 249));
		composite.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));

		Composite composite_1 = new Composite(shlGeneratorDialog, SWT.BORDER | SWT.NO_REDRAW_RESIZE);

		TreeViewer treeViewer = new TreeViewer(composite_1, SWT.BORDER);
		treeViewer.setAutoExpandLevel(2);
		Tree tree = treeViewer.getTree();
		tree.setBounds(0, 0, 257, 232);
		ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(
				ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		adapterFactory.addAdapterFactory(new ResourceItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new CtWedgeAdapterFactory());
		adapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());
		treeViewer.setContentProvider(new AdapterFactoryContentProvider(adapterFactory));

		treeViewer.setLabelProvider(new AdapterFactoryLabelProvider(adapterFactory));
		treeViewer.setInput(citModel.eResource().getResourceSet());

		Label lblAlgorithmLabel = new Label(composite, SWT.NONE);
		lblAlgorithmLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		lblAlgorithmLabel.setBounds(5, 10, 281, 39);
		lblAlgorithmLabel.setAlignment(SWT.CENTER);
		lblAlgorithmLabel.setFont(SWTResourceManager.getFont("Courier", 21, SWT.NORMAL));
		lblAlgorithmLabel.setText(generatorName);

		Button btnGenerate = new Button(composite, SWT.NONE);
		btnGenerate.setBounds(173, 191, 94, 26);
		btnGenerate.setText("Generate");

		Composite composite_uses = new Composite(composite, SWT.NONE);
		composite_uses.setBounds(54, 86, 203, 99);
		composite_uses.setLayout(null);

		final Button btnIgnoreConstraints = new Button(composite_uses, SWT.CHECK);
		btnIgnoreConstraints.setBounds(3, 3, 196, 16);
		btnIgnoreConstraints.setText("Ignore constraints");
		btnIgnoreConstraints.setEnabled(supportConstraints);
		btnIgnoreConstraints.setSelection(ignoreConstraints);
		btnIgnoreConstraints.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ignoreConstraints = btnIgnoreConstraints.getSelection();
			}
		});

		final Button btnIgnoreSeeds = new Button(composite_uses, SWT.CHECK);
		btnIgnoreSeeds.setBounds(3, 22, 196, 16);
		btnIgnoreSeeds.setText("Ignore seeds");
		btnIgnoreSeeds.setEnabled(supportSeeds);
		btnIgnoreSeeds.setSelection(ignoreSeeds);
		btnIgnoreSeeds.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ignoreSeeds = btnIgnoreSeeds.getSelection();
			}
		});
		final Button btnIgnoreTestGoals = new Button(composite_uses, SWT.CHECK);
		btnIgnoreTestGoals.setBounds(3, 41, 196, 16);
		btnIgnoreTestGoals.setText("Ignore test goals");
		btnIgnoreTestGoals.setEnabled(supportTestGoals);
		btnIgnoreTestGoals.setSelection(ignoreTestgoals);

		btnIgnoreTestGoals.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ignoreTestgoals = btnIgnoreTestGoals.getSelection();
			}
		});
		final Button btnSimplify = new Button(composite_uses, SWT.CHECK);
		btnSimplify.setBounds(2, 62, 93, 16);
		btnSimplify.setText("Simplify");
		btnSimplify.setSelection(useSimplifier);

		btnSimplify.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				useSimplifier = btnSimplify.getSelection();
			}
		});

		Composite composite_nwise = new Composite(composite, SWT.NONE);
		composite_nwise.setBounds(54, 47, 203, 39);

		final Spinner spinner = new Spinner(composite_nwise, SWT.BORDER);
		spinner.setFont(SWTResourceManager.getFont("Segoe UI", 13, SWT.NORMAL));
		spinner.setLocation(85, 5);
		spinner.setSize(108, 28);
		spinner.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_RED));
		spinner.setMinimum(2);
		spinner.setSelection(2);
		if (citModel != null)
			spinner.setMaximum(citModel.getParameters().size());

		Label lblNewLabel = new Label(composite_nwise, SWT.NONE);
		lblNewLabel.setLocation(3, 5);
		lblNewLabel.setSize(108, 28);
		lblNewLabel.setFont(SWTResourceManager.getFont("Sans Serif", 14, SWT.NORMAL));
		lblNewLabel.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_DARK_SHADOW));

		lblNewLabel.setText("N-WISE");

		Button btnSetProperties = new Button(composite, SWT.NONE);
		btnSetProperties.setBounds(45, 191, 127, 26);
		btnSetProperties.setEnabled(supportProperties);

		btnSetProperties.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				GenPropertiesDialog genD = new GenPropertiesDialog(shell, generator.getGenProperties());
				genD.open();
			}
		});
		btnSetProperties.setText("Set Properties");
		spinner.addListener(SWT.Modify, new Listener() {
			private void btnSpinnerChanged(Event event) {
				nWise = spinner.getSelection();

			}

			@Override
			public void handleEvent(Event event) {
				btnSpinnerChanged(event);
			}
		});
		btnIgnoreTestGoals.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				ignoreTestgoals = btnIgnoreTestGoals.getSelection();
			}
		});
		btnIgnoreSeeds.addListener(SWT.Selection, new Listener() {

			private void btnUseTheSeedsChanged(Event event) {
				ignoreSeeds = btnIgnoreSeeds.getSelection();

			}

			@Override
			public void handleEvent(Event event) {
				btnUseTheSeedsChanged(event);

			}

		});
		btnIgnoreConstraints.addListener(SWT.Selection, new Listener() {

			private void btnUseTheConstraintsChanged(Event event) {
				ignoreConstraints = btnIgnoreConstraints.getSelection();

			}

			@Override
			public void handleEvent(Event event) {
				btnUseTheConstraintsChanged(event);

			}

		});

		btnGenerate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {

				btnGenerateSelected();

			}

		});

		// shlGeneratorDialog.setLayout(gl_shlGeneratorDialog);

	}

	/**
	 * Open the dialog.
	 * 
	 * @return the result
	 */
	public Object open() {
		createContents();
		shlGeneratorDialog.open();
		shlGeneratorDialog.layout();
		Display display = getParent().getDisplay();
		while (!shlGeneratorDialog.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	// the the selected generator from the element
	private void setGenerator(IConfigurationElement element) {
		try {
			Object o = element.createExecutableExtension("GeneratorPrototype");
			if (o instanceof ICTWedgeTestGenerator) {
				generator = (ICTWedgeTestGenerator) o;
			} else {
				System.err.println("generator not registered");
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
}
