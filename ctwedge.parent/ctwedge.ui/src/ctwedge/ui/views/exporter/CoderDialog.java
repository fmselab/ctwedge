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
package ctwedge.ui.views.exporter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.wb.swt.SWTResourceManager;

import com.google.inject.Inject;

import ctwedge.ctWedge.CitModel;
import ctwedge.util.ext.CTWedgeException;
import ctwedge.util.ext.ICTWedgeExporter;

public class CoderDialog extends Dialog {
	private CitModel citModel;
	private IConfigurationElement element;
	private Boolean constraintUse = false;
	private int nWise = 2;
	private final String modelPath;

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 * @param element
	 * @param file
	 */
	public CoderDialog(Shell parentShell, CitModel citModel,
			IConfigurationElement element, String path) {
		super(parentShell);
		this.citModel = citModel;
		this.element = element;
		this.modelPath = path;
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(null);

		CLabel lblNewLabel = new CLabel(container, SWT.NONE);
		lblNewLabel.setBounds(15, 59, 615, 302);
		lblNewLabel.setRightMargin(0);
		lblNewLabel.setLeftMargin(0);
		lblNewLabel.setTopMargin(0);
		lblNewLabel
				.setFont(SWTResourceManager.getFont("Ubuntu", 7, SWT.NORMAL));
		lblNewLabel.setBackground(SWTResourceManager
				.getColor(SWT.COLOR_LIST_BACKGROUND));
		if (citModel.eResource().getURI().path() != null) {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			@SuppressWarnings("rawtypes")
			Map options = new HashMap();
			try {
				citModel.eResource().save(outputStream, options);
				lblNewLabel.setText(outputStream.toString());

			} catch (IOException e) {
				lblNewLabel.setText("ERROR");
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		Label lblNewLabel_1 = new Label(container, SWT.NONE);
		lblNewLabel_1.setFont(SWTResourceManager.getFont("Courier", 21, SWT.NORMAL));
		lblNewLabel_1.setBounds(25, 10, 103, 31);
		lblNewLabel_1.setText("CONVERT");

		Label lblNewLabel_2 = new Label(container, SWT.NONE);
		lblNewLabel_2.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_FOREGROUND));
		lblNewLabel_2.setFont(SWTResourceManager.getFont("Courier", 21, SWT.NORMAL));
		lblNewLabel_2.setBounds(139, 10, 189, 31);
		if (element.getAttribute("OtherToolLanguage") != null)
			lblNewLabel_2.setText(element.getAttribute("OtherToolLanguage")
					.toUpperCase());
		final Spinner spinner = new Spinner(container, SWT.BORDER);
		spinner.setMinimum(2);
		spinner.setBounds(78, 370, 103, 26);
		if (citModel != null)
			spinner.setMaximum(citModel.getParameters().size());
		spinner.setSelection(2);
		spinner.addListener(SWT.Modify, new Listener() {
			@Override
			public void handleEvent(Event event) {
				btnSpinnerChanged(event);
			}

			private void btnSpinnerChanged(Event event) {
				nWise = spinner.getSelection();

			}
		});

		Label lblNwise = new Label(container, SWT.NONE);
		lblNwise.setFont(SWTResourceManager.getFont("Sans Serif", 14, SWT.NORMAL));
		lblNwise.setBounds(15, 375, 57, 26);
		lblNwise.setText("N-WISE");

		final Button btnCheckButton = new Button(container, SWT.CHECK);
		btnCheckButton.setFont(SWTResourceManager.getFont("Sans Serif", 14, SWT.NORMAL));
		btnCheckButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnCheckButton.setBounds(198, 367, 169, 26);
		btnCheckButton.setText("Use Constraints");
		btnCheckButton.setSelection(false);
		btnCheckButton.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				btnUseTheConstraintsChanged(event);

			}

			private void btnUseTheConstraintsChanged(Event event) {
				constraintUse = btnCheckButton.getSelection();

			}

		});
		return container;
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		Button button = createButton(parent, IDialogConstants.OK_ID,
				IDialogConstants.OK_LABEL, true);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {

				btnGenerateSelected(event);

			}

		});

		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);

	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(640, 480);
	}

	private void btnGenerateSelected(SelectionEvent event) {
		try {
			final Object o = element
					.createExecutableExtension("CoderPrototype");

			if (o instanceof ICTWedgeExporter) {
				Job coderjob = new Job("Model translation") {
					@Inject
					Shell shell;
					private boolean error = false;
					String trace="GENERIC ERROR";
					@Override
					protected IStatus run(IProgressMonitor monitor) {
						ISafeRunnable runnable = new ISafeRunnable() {
							@Override
							public void handleException(Throwable exception) {
								
								if(exception instanceof CTWedgeException ) {
								
									trace=exception.getMessage();
									//exception.printStackTrace();
									}
							if(exception instanceof java.lang.OutOfMemoryError ){
									trace=" EXCEEDED MEMORY LIMIT";
									//exception.printStackTrace();
							}

				
							error = true;
						
							}

							@Override
							public void run() throws Exception {
								// compute the project directory
								((ICTWedgeExporter) o).convertModel(citModel,constraintUse, nWise,modelPath);
							}
						};
						monitor.beginTask("Generation", IProgressMonitor.UNKNOWN);
						SafeRunner.run(runnable);
						if (monitor.isCanceled())
							return Status.CANCEL_STATUS;
						monitor.done();
						monitor.setCanceled(true);
						Display.getDefault().syncExec(new Runnable() {
							

							@Override
							public void run() {
								if(error){
									MessageDialog.openError(Display.getCurrent().getActiveShell(), "NOT CORRECTLY TRANSLATED", "MODEL NOT CORRECTLY TRANSLATED "+trace);
								}else MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "CORRECTLY TRANSLATED", "MODEL CORRECTLY TRANSLATED");
								
									IWorkspace workspace = ResourcesPlugin.getWorkspace();
									IWorkspaceRoot root = workspace.getRoot();
									 List<IProject> prjs = Arrays.asList(root.getProjects());
									 if (prjs !=null)
									 for (IProject p : prjs){
										 try {
											p.refreshLocal(IResource.DEPTH_INFINITE, null);
										} catch (CoreException e1) {
											// TODO Auto-generated catch block
											e1.printStackTrace();
										}
									 }
									
								}
							
							});
						
						return Status.OK_STATUS;
					}
				};
				coderjob.setUser(true);
				coderjob.schedule();
				
			}

		} catch (CoreException ex) {
			System.out.println(ex.getMessage());
		}

	}
}
