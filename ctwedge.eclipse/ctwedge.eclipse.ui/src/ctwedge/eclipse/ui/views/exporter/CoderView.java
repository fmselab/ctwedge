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
package ctwedge.eclipse.ui.views.exporter;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.osgi.framework.Bundle;

import ctwedge.ctWedge.CitModel;
import ctwedge.eclipse.ui.Activator;
import ctwedge.eclipse.ui.utils.ActiveResourceExtractor;
import ctwedge.eclipse.util.Constants;
import ctwedge.util.ext.ICTWedgeExporter;
import ctwedge.util.ext.ICTWedgeModelProcessor;

public class CoderView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = Activator.PLUGIN_ID+".views.CoderView";

	private TableViewer viewer;
	private Action doubleClickAction;
	private ArrayList<String> ExtensionsNames;
	private IConfigurationElement[] EX;
	private CitModel citModel = null;

	/*
	 * The content provider class is responsible for providing objects to the
	 * view. It can wrap existing objects in adapters or simply return objects
	 * as-is. These objects may be sensitive to the current input of the view,
	 * or ignore it and always show the same content (like Task List, for
	 * example).
	 */

	class ViewContentProvider implements IStructuredContentProvider {
		@Override
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}

		@Override
		public void dispose() {
		}

		@Override
		public Object[] getElements(Object parent) {
			EX = Platform.getExtensionRegistry().getConfigurationElementsFor(
					Constants.packagaName+".coders");
			ExtensionsNames = new ArrayList<String>();
			for (int i = 0; i < EX.length; i++) {
				if (EX[i] != null) {
					ExtensionsNames.add(EX[i].getAttribute("Name"));
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
			Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);

			ImageDescriptor myImage = ImageDescriptor.createFromURL(FileLocator
					.find(bundle, new Path("icons/logo16.png"), null));

			return myImage.createImage();
		}
	}

	//class NameSorter extends ViewerSorter {}

	/**
	 * The constructor.
	 */
	public CoderView() {
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	@Override
	public void createPartControl(Composite parent) {
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		//viewer.setSorter(new NameSorter());
		viewer.setInput(getViewSite());

		// Create the help context id for the viewer's control
		PlatformUI.getWorkbench().getHelpSystem()
				.setHelp(viewer.getControl(), Activator.PLUGIN_ID+".viewer");
		makeActions();
		hookDoubleClickAction();

	}

	private void makeActions() {

		doubleClickAction = new Action() {
		    //@Inject Shell shell;
			@Override
			public void run() {
				ISelection selection = viewer.getSelection();
				Object obj = ((IStructuredSelection) selection)
						.getFirstElement();
				final String file = ActiveResourceExtractor.citlFileSelect();

				if (file != null && !file.equals(""))
					try {
						for (final IConfigurationElement e : EX) {
							if (e.getAttribute("Name").equals(obj.toString())) {
								final Object o = e
										.createExecutableExtension("CoderPrototype");

								if (o instanceof ICTWedgeExporter) {
									ISafeRunnable runnable = new ISafeRunnable() {
										@Override
										public void handleException(
												Throwable exception) {
											System.out
													.println("Exception in client");
										}

										@Override
										public void run() throws Exception {
											citModel = ICTWedgeModelProcessor.getModel(file);

										}
									};
									SafeRunner.run(runnable);
									if (citModel != null) {
										CoderDialog coderDialog = new CoderDialog(
												viewer.getControl().getShell(),
												citModel, e,file.substring(0,file.lastIndexOf(File.separator)));
										coderDialog.open();

									} else
										showMessage("The ComB CitModel is not valid");
								}

							}
						}
					} catch (CoreException ex) {
						System.out.println(ex.getMessage());
					}
			}
		};
	}

	

	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});
	}

	private void showMessage(String message) {
		MessageDialog.openError(viewer.getControl().getShell(),
				"Loading citModel error", message);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}



}
