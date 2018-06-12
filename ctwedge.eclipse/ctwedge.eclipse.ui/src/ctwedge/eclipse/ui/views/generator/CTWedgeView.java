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
package ctwedge.eclipse.ui.views.generator;

import java.util.ArrayList;
import java.util.List;

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
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.wb.swt.SWTResourceManager;
import org.osgi.framework.Bundle;

import com.google.inject.Inject;

import ctwedge.ctWedge.CitModel;
import ctwedge.eclipse.ui.Activator;
import ctwedge.eclipse.ui.utils.ActiveResourceExtractor;
import ctwedge.eclipse.ui.views.exporter.CoderDialog;
import ctwedge.eclipse.util.Constants;
import ctwedge.util.Pair;
import ctwedge.util.ext.ICTWedgeExporter;
import ctwedge.util.ext.ICTWedgeImporter;
import ctwedge.util.ext.ICTWedgeModelProcessor;
import ctwedge.util.ext.ICTWedgeTestGenerator;

public class CTWedgeView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = Activator.PLUGIN_ID+".views.generator.CTWEdgeView";

	private TableViewer viewer;
	private Action doubleClickAction;
	private ArrayList<String> ExtensionsNames;

	private Table table_1;
	private Button IgnoreConstraints;
	private CheckboxTableViewer checkboxTableViewer;
	private static final String SUPPORT_CONSTRAINTS = "support_constraints";
	private String file;
	private Button btnRunGen;
	private IConfigurationElement[] EXgenerator;
	private IConfigurationElement[] EXcoder;
	private IConfigurationElement[] EXimporter;

	private Label fileLabel;

	private Spinner spinner;

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
			
			ExtensionsNames = new ArrayList<String>();
			EXimporter = Platform.getExtensionRegistry()
					.getConfigurationElementsFor(Constants.packagaName+".importers");
			for (int i = 0; i < EXimporter.length; i++) {
				if (EXimporter[i] != null) {
					ExtensionsNames.add(EXimporter[i].getAttribute("Name"));
				}
			}
			EXgenerator = Platform.getExtensionRegistry()
					.getConfigurationElementsFor(Constants.packagaName+".ctwedgeGenerators");
		
			for (int i = 0; i < EXgenerator.length; i++) {
				if (EXgenerator[i] != null) {
					
					ExtensionsNames.add(EXgenerator[i].getAttribute("Name"));
				}
			}
			EXcoder = Platform.getExtensionRegistry()
					.getConfigurationElementsFor(Constants.packagaName+".coders");
			for (int i = 0; i < EXcoder.length; i++) {
				if (EXcoder[i] != null) {
					ExtensionsNames.add(EXcoder[i].getAttribute("Name"));
				}
			}
			return ExtensionsNames.toArray();

		}
	}

	class ViewContentProviderPorfolio implements IStructuredContentProvider {

		@Override
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}

		@Override
		public void dispose() {
		}

		@Override
		public Object[] getElements(Object parent) {
			EXgenerator = Platform.getExtensionRegistry()
					.getConfigurationElementsFor(Constants.packagaName+".ctwedgeGenerators");
			ExtensionsNames = new ArrayList<String>();
			for (int i = 0; i < EXgenerator.length; i++) {
				if (EXgenerator[i] != null) {
					ExtensionsNames.add(EXgenerator[i].getAttribute("Name"));
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
			return getImage(obj);// getImage(obj);
		}

		@Override
		public Image getImage(Object obj) {
			
			ImageDescriptor myImage=null;
			Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);
			for ( IConfigurationElement e : EXgenerator) {
				if (e.getAttribute("Name").equals(obj.toString())) {
					Object o = null;
					try {
						o = e.createExecutableExtension("GeneratorPrototype");
					} catch (CoreException e1) {
					
						e1.printStackTrace();
					}

					if (o instanceof ICTWedgeTestGenerator) {
						myImage = ImageDescriptor.createFromURL(FileLocator
								.find(bundle, new Path("icons/g.gif"), null));
						break;
						
					}
				}
			}
			if(EXcoder != null)
			for ( IConfigurationElement e : EXcoder) {
				if (e.getAttribute("Name").equals(obj.toString())) {
					Object o = null;
					try {
						o = e.createExecutableExtension("CoderPrototype");
					} catch (CoreException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					if (o instanceof ICTWedgeExporter) {
						myImage = ImageDescriptor.createFromURL(FileLocator
								.find(bundle, new Path("icons/logo.png"), null));
						break;
						
					}
				}
			}
			if(EXimporter != null)
			for (IConfigurationElement e : EXimporter) {
				if (e.getAttribute("Name").equals(obj.toString())) {
					Object o = null;
					try {
						o = e.createExecutableExtension("ImporterPrototype");
					} catch (CoreException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					if (o instanceof ICTWedgeImporter) {
						myImage = ImageDescriptor.createFromURL(FileLocator
								.find(bundle, new Path("icons/i.gif"), null));
						break;
						
					}
				}
			}
			
		return myImage.createImage();
	}
	}
	class CheckedViewLabelProvider extends LabelProvider implements ITableLabelProvider{

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);
			ImageDescriptor myImage = ImageDescriptor.createFromURL(FileLocator
					.find(bundle, new Path("icons/g.gif"), null));
			return myImage.createImage();
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			// TODO Auto-generated method stub
			return getText(element);
		}
		
	}
	//class NameSorter extends ViewerSorter {}

	/**
	 * The constructor.
	 */
	public CTWedgeView() {

	}

	@Override
	public void showBusy(boolean busy) {
		super.showBusy(busy);
		if (busy)
			setPartName("I'm doing a job right now...");
		else
			setPartName("Generator View");
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	@Override
	public void createPartControl(Composite parent) {

		parent.setLayout(new FillLayout());

		// Create the ScrolledComposite to scroll horizontally and vertically
	    ScrolledComposite scrolledComp = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);

	    // Create a child composite for your content
	    Composite content = new Composite(scrolledComp, SWT.NONE);
	    content.setLayout(new FillLayout() );

	    
	    // add content to scrolled composite
	    scrolledComp.setContent(content);

	    // Set the minimum size (in this case way too large)
	    scrolledComp.setMinSize(400, 400);

	    // Expand both horizontally and vertically
	    scrolledComp.setExpandHorizontal(true);
	    scrolledComp.setExpandVertical(true);
		TabFolder tabFolder = new TabFolder(content, SWT.NONE);
		tabFolder.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		GridData gd_tabFolder = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_tabFolder.heightHint = 523;
		gd_tabFolder.widthHint = 473;
		tabFolder.setLayoutData(gd_tabFolder);

		TabItem tbtmNewItem = new TabItem(tabFolder, SWT.NONE);
		tbtmNewItem.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID,
				"icons/g.gif"));
		TabItem portItem = new TabItem(tabFolder, SWT.NONE);
		portItem.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID,
				"icons/logo.png"));
		portItem.setText("Portfolio");
		tbtmNewItem.setText("Citlab Extensions");
		viewer = new TableViewer(tabFolder, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL);
		Table table = viewer.getTable();

		tbtmNewItem.setControl(table);

		Composite composite = new Composite(tabFolder, SWT.NONE);
		portItem.setControl(composite);
		composite.setBackground(SWTResourceManager
				.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		composite.setLayout(new FormLayout());

		checkboxTableViewer = CheckboxTableViewer.newCheckList(composite,
				SWT.BORDER | SWT.FULL_SELECTION);
		checkboxTableViewer.addCheckStateListener(new ICheckStateListener() {
			public void checkStateChanged(CheckStateChangedEvent event) {

				boolean portfolioSupportCon = true;
				if (checkboxTableViewer.getCheckedElements().length == 0) {
					IgnoreConstraints.setSelection(true);
					IgnoreConstraints.setEnabled(false);
				}
				for (Object o : checkboxTableViewer.getCheckedElements()) {
					for (final IConfigurationElement element : EXgenerator) {
						if (element.getAttribute("Name").equals(o.toString())) {
							String attribute = element
									.getAttribute(SUPPORT_CONSTRAINTS);
							boolean supportConstraints = (attribute != null)
									&& attribute.equals(Boolean.TRUE.toString());
							// if it does not support constraints, ignore them
							portfolioSupportCon = (portfolioSupportCon && supportConstraints);
						}
					}
				}

				IgnoreConstraints.setSelection(!portfolioSupportCon);
				IgnoreConstraints.setEnabled(portfolioSupportCon);
			}
		});

		checkboxTableViewer.addDoubleClickListener(new IDoubleClickListener() {
			@Inject
			Shell shell;

			public void doubleClick(DoubleClickEvent event) {

				ISelection selection = checkboxTableViewer.getSelection();
				Object objPortfolio = ((IStructuredSelection) selection)
						.getFirstElement();
				try {
					for (final IConfigurationElement e : EXgenerator) {
						if (e.getAttribute("Name").equals(
								objPortfolio.toString())) {
							final Object o = e
									.createExecutableExtension("GeneratorPrototype");

							if (o instanceof ICTWedgeTestGenerator) {
								ISafeRunnable runnable = new ISafeRunnable() {
									@Override
									public void handleException(
											Throwable exception) {
										System.out
												.println("Exception in client");
									}

									@Override
									public void run() throws Exception {
										if (((ICTWedgeTestGenerator) o)
												.getGenProperties() != null) {
											GenPropertiesDialog dialogP = new GenPropertiesDialog(
													shell,
													((ICTWedgeTestGenerator) o)
															.getGenProperties());

											dialogP.open();
										}

									}
								};
								SafeRunner.run(runnable);

							}
						}
					}
				} catch (CoreException ex) {
					System.out.println(ex.getMessage());
				}
			}
		});
		table_1 = checkboxTableViewer.getTable();
		FormData fd_table_1 = new FormData();
		fd_table_1.bottom = new FormAttachment(0, 523);
		fd_table_1.right = new FormAttachment(0, 303);
		fd_table_1.top = new FormAttachment(0);
		fd_table_1.left = new FormAttachment(0);
		table_1.setLayoutData(fd_table_1);

		Composite composite_1 = new Composite(composite, SWT.NONE);
		composite_1.setLayout(null);
		FormData fd_composite_1 = new FormData();
		fd_composite_1.right = new FormAttachment(100, -10);
		fd_composite_1.left = new FormAttachment(table_1, 6);
		fd_composite_1.top = new FormAttachment(0, 98);
		fd_composite_1.bottom = new FormAttachment(0, 420);
		composite_1.setLayoutData(fd_composite_1);

		IgnoreConstraints = new Button(composite_1, SWT.CHECK);
		IgnoreConstraints.setBounds(5, 5, 136, 24);
		IgnoreConstraints.setEnabled(false);
		IgnoreConstraints.setSelection(true);
		IgnoreConstraints.setText("Ignore constraints");

		fileLabel = new Label(composite, SWT.NONE);
		fileLabel.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLUE));
		fileLabel.setAlignment(SWT.CENTER);
		fileLabel.setFont(SWTResourceManager.getFont("Sans", 16, SWT.BOLD));
		FormData fd_fileLabel = new FormData();
		fd_fileLabel.top = new FormAttachment(0, 34);
		fd_fileLabel.left = new FormAttachment(table_1, 3);
		fd_fileLabel.right = new FormAttachment(100);
		fileLabel.setLayoutData(fd_fileLabel);

		spinner = new Spinner(composite, SWT.BORDER);
		fd_fileLabel.bottom = new FormAttachment(100, -458);
		spinner.setEnabled(false);
		FormData fd_spinner = new FormData();
		fd_spinner.top = new FormAttachment(fileLabel, 6);
		fd_spinner.right = new FormAttachment(composite_1, 0, SWT.RIGHT);
		spinner.setLayoutData(fd_spinner);

		final Button simplify = new Button(composite_1, SWT.CHECK);
		simplify.setBounds(5, 92, 74, 24);
		simplify.setText("Simplify");

		Button btnChooseModel = new Button(composite_1, SWT.NONE);
		btnChooseModel.setBounds(5, 139, 136, 27);
		btnChooseModel.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				file = ActiveResourceExtractor.citlFileSelect();
				if (file != null && file.trim() != "") {
					btnRunGen.setEnabled(true);

					spinner.setEnabled(true);
					if (ICTWedgeModelProcessor.getModel(file) != null) {
						spinner.setMinimum(2);
						spinner.setSelection(2);
						spinner.setMaximum(ICTWedgeModelProcessor.getModel(file)
								.getParameters().size());
						fileLabel.setText(ICTWedgeModelProcessor.getModel(file)
								.getName());
					}

				}
			}
		});
		btnChooseModel.setText("Model...");

		btnRunGen = new Button(composite_1, SWT.NONE);
		btnRunGen.setBounds(5, 193, 136, 44);
		btnRunGen.setEnabled(false);
		btnRunGen.addSelectionListener(new SelectionAdapter() {
			@Inject
			Shell shell;

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (checkboxTableViewer.getCheckedElements().length <= 0) {
					MessageDialog.openInformation(shell, "GENERATOR SELECTION",
							"Select one or more generators?");

					return;
				}

				ISafeRunnable runnable = new ISafeRunnable() {

					@Override
					public void handleException(Throwable exception) {
						System.out.println("Exception in client");
					}

					@Override
					public void run() throws Exception {
						System.out.println("I am running....");
						if (file != null && file.trim() != "") {
							List<Pair<ICTWedgeTestGenerator, CitModel>> list = modeAnGeneratorlExtractor(ICTWedgeModelProcessor
									.getModel(file));
							CitLabPortfolio portfolio = new CitLabPortfolio(IgnoreConstraints
											.getSelection(), spinner
											.getSelection());
							portfolio.run(list);
						}
					}
				};
				SafeRunner.run(runnable);

			}
		});
		btnRunGen.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		btnRunGen.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLUE));
		btnRunGen.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID,
				"icons/wizard.gif"));
		btnRunGen.setText("Generate");

		Label lblNwise = new Label(composite, SWT.NONE);
		lblNwise.setAlignment(SWT.CENTER);
		FormData fd_lblNwise = new FormData();
		fd_lblNwise.right = new FormAttachment(table_1, 84, SWT.RIGHT);
		fd_lblNwise.top = new FormAttachment(fileLabel, 12);
		fd_lblNwise.bottom = new FormAttachment(composite_1, -6);
		fd_lblNwise.left = new FormAttachment(table_1, 6);
		lblNwise.setLayoutData(fd_lblNwise);
		lblNwise.setText("N-WISE");
		checkboxTableViewer
				.setContentProvider(new ViewContentProviderPorfolio());
		checkboxTableViewer.setLabelProvider(new CheckedViewLabelProvider());             
		checkboxTableViewer.setInput(getViewSite());
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setInput(getViewSite());

		// Create the help context id for the viewer's control
		PlatformUI.getWorkbench().getHelpSystem()
				.setHelp(viewer.getControl(), Activator.PLUGIN_ID+".viewer");
		makeActions();
		hookDoubleClickAction();

	}

	private void makeActions() {
		doubleClickAction = new Action() {
		

			@Override
			public void run() {
				ISelection selection = viewer.getSelection();
				Object obj = ((IStructuredSelection) selection)
						.getFirstElement();
				if (!ImporterRun(obj)) {
					final String file = ActiveResourceExtractor
							.citlFileSelect();

					if (file != null && !file.equals(""))
						try {
							modelExtractorGen(obj, file);
							modelExtractorCoder(obj, file);
						} catch (CoreException ex) {
							System.out.println(ex.getMessage());
						}
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

	private void modelExtractorGen(final Object obj, final String file)
			throws CoreException {
		BusyIndicator.showWhile(Display.getCurrent(), new Runnable() {
			private CitModel citModel;

			@Override
			public void run() {

				ISafeRunnable runnable = null;
				for (final IConfigurationElement e : EXgenerator) {
					if (e.getAttribute("Name").equals(obj.toString())) {
						Object o = null;
						try {
							o = e.createExecutableExtension("GeneratorPrototype");
						} catch (CoreException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

						if (o instanceof ICTWedgeTestGenerator) {

							runnable = new ISafeRunnable() {

								@Override
								public void handleException(Throwable exception) {
									System.out.println("Exception in client");
								}

								@Override
								public void run() throws Exception {
									citModel = ICTWedgeModelProcessor
											.getModel(file);

								}
							};

							SafeRunner.run(runnable);
							if (citModel != null) {

								GeneratorDialog genDialog = new GeneratorDialog(
										viewer.getControl().getShell(), 0,
										citModel, e);
								genDialog.open();

							} else
								showMessage("The ComB CitModel is not valid");
							break;

						}

					}
				}

			}
		});

	}

	private void modelExtractorCoder(final Object obj, final String file)
			throws CoreException {
		BusyIndicator.showWhile(Display.getCurrent(), new Runnable() {
			private CitModel citModel;

			@Override
			public void run() {

				ISafeRunnable runnable = null;
				for (final IConfigurationElement e : EXcoder) {
					if (e.getAttribute("Name").equals(obj.toString())) {
						Object o = null;
						try {
							o = e.createExecutableExtension("CoderPrototype");
						} catch (CoreException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

						if (o instanceof ICTWedgeExporter) {

							runnable = new ISafeRunnable() {

								@Override
								public void handleException(Throwable exception) {
									System.out.println("Exception in client");
								}

								@Override
								public void run() throws Exception {
									citModel = ICTWedgeModelProcessor
											.getModel(file);

								}
							};

							SafeRunner.run(runnable);
							if (citModel != null) {

								CoderDialog coderDialog = new CoderDialog(
										viewer.getControl().getShell(),
										citModel, e, file);
								coderDialog.open();

							} else
								showMessage("The ComB CitModel is not valid");
							break;

						}

					}
				}

			}
		});
	}

	private boolean ImporterRun(final Object obj) {
		Boolean is = false;

		for (final IConfigurationElement e : EXimporter) {
			if (e.getAttribute("Name").equals(obj.toString())) {
				/*Object o = null;
				try {
					o = e.createExecutableExtension("ImporterPrototype");
				} catch (CoreException e1) {
					e1.printStackTrace();
				}
				 MR TODO if (o instanceof ICitLabImporter) {
					ImporterDialog importerDialog = new ImporterDialog(viewer
							.getControl().getShell(), e);
					importerDialog.open();

				} else*/
					showMessage("The importer class is not valid");
				is = true;
				break;
			}

		}

		return is;
	}

	private List<Pair<ICTWedgeTestGenerator, CitModel>> modeAnGeneratorlExtractor(
			final CitModel citModel) throws CoreException {

		List<Pair<ICTWedgeTestGenerator, CitModel>> list = new ArrayList<>();
		for (Object checked : checkboxTableViewer.getCheckedElements()) {
			for (final IConfigurationElement e : EXgenerator) {
				Pair<ICTWedgeTestGenerator, CitModel> pair;
				if (e.getAttribute("Name").equals(checked.toString())) {
					final Object o = e
							.createExecutableExtension("GeneratorPrototype");

					if (o instanceof ICTWedgeTestGenerator) {

						if (citModel != null) {
							pair = new Pair<ICTWedgeTestGenerator, CitModel>(
									(ICTWedgeTestGenerator) o, citModel);
							list.add(pair);

						} else
							showMessage("The ComB CitModel is not valid");

					}
				}

			}

		}

		return list;
	}

}
