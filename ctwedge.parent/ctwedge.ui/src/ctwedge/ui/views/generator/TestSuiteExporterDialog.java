/*******************************************************************************
- * Copyright (c) 2013 University of Bergamo - Italy
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

import java.util.ArrayList;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import ctwedge.util.TestSuite;
import ctwedge.util.ext.ICTWedgeTestSuiteExporter;


public class TestSuiteExporterDialog extends Dialog {
	private Table table;
	private TestSuite input;
	private ArrayList<String> ExtensionsNames;
	private IConfigurationElement[] EX;
	private CheckboxTableViewer checkboxTableViewer;

	class ViewContentProvider implements IStructuredContentProvider {

		@Override
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}

		@Override
		public void dispose() {
		}

		@Override
		public Object[] getElements(Object parent) {
			EX = Platform.getExtensionRegistry().getConfigurationElementsFor("ctwedge.ui.ctwedgeExporters");
//					Activator.PLUGIN_ID+".TestSuiteExporters");
			ExtensionsNames = new ArrayList<String>();
			for (int i = 0; i < EX.length; i++) {
				if (EX[i] != null) {
					ExtensionsNames.add(EX[i].getAttribute("ExporterName"));
				}
			}

			return ExtensionsNames.toArray();

		}
	}

	class ViewLabelProvider extends LabelProvider implements
			ITableLabelProvider {
		@Override
		public String getColumnText(Object obj, int index) {
			return getText(obj);
		}

		@Override
		public Image getColumnImage(Object obj, int index) {
			return getImage(obj);
		}

		@Override
		public Image getImage(Object obj) {
			return PlatformUI.getWorkbench().getSharedImages()
					.getImage(ISharedImages.IMG_OBJ_ELEMENT);
		} 
	}

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public TestSuiteExporterDialog(Shell parentShell, TestSuite input) {
		super(parentShell);
		this.input = input;
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);

		checkboxTableViewer = CheckboxTableViewer.newCheckList(container,
				SWT.BORDER | SWT.FULL_SELECTION);
		checkboxTableViewer.setContentProvider(new ViewContentProvider());
		checkboxTableViewer.setLabelProvider(new ViewLabelProvider());
		checkboxTableViewer.setInput(getShell());
		table = checkboxTableViewer.getTable();
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		return container;
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		Button btnsave = createButton(parent, IDialogConstants.PROCEED_ID,
				IDialogConstants.PROCEED_LABEL, true);
		btnsave.setText("&Save");
		btnsave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				buttonSelected(e);
			}
		});
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	protected void buttonSelected(SelectionEvent e) {
		boolean aborted=false;
		Object[] exportersChecked = checkboxTableViewer.getCheckedElements();
		if (exportersChecked.length != 0) {
			for (int index = 0; index < EX.length; index++) {
				for (int check = 0; check < exportersChecked.length; check++) {
					if (EX[index].getAttribute("ExporterName").equals(
							exportersChecked[check]))
						try {
							final String fileExt = EX[index]
									.getAttribute("FileExtension");
							FileDialog fileDialog = new FileDialog(getShell(),
									SWT.SAVE);

							final String[] extensions = { fileExt };
							fileDialog.setFilterExtensions(extensions);
							fileDialog.setFilterPath(Platform.getLocation()
									.toOSString());

							final String fileName = fileDialog.open();
							final Object o = EX[index]
									.createExecutableExtension("ITSPrototype");
							if (o instanceof ICTWedgeTestSuiteExporter) {
								ISafeRunnable runnable = new ISafeRunnable() {
									@Override
									public void handleException(
											Throwable exception) {
										System.out
												.println("Exception in client");
									}

									@Override
									public void run() throws Exception {
										((ICTWedgeTestSuiteExporter) o).generateOutput(input,
												fileName);

									}
								};
								if (fileName !=null && !fileName.equals("")){
								SafeRunner.run(runnable);
								aborted=false;
								}else
									aborted=true;
							}
						} catch (CoreException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

				}
			}
			if(!aborted){
			MessageDialog.openInformation(this.getContents().getShell(), "",
					"Exporting completed");
			checkboxTableViewer.setAllChecked(false);
			this.getContents().getShell().dispose();
			}
		}else MessageDialog.openError(this.getContents().getShell(), "",
				"Select one or more exporters");
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 300);
	}

}
