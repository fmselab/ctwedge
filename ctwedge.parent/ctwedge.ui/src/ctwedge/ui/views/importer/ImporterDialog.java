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
package ctwedge.ui.views.importer;





import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.serializer.ISerializer;
import org.eclipse.xtext.util.CancelIndicator;
import org.eclipse.xtext.validation.CheckMode;
import org.eclipse.xtext.validation.IResourceValidator;
import org.eclipse.xtext.validation.Issue;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

import ctwedge.ctWedge.CitModel;
import ctwedge.ctWedge.util.CtWedgeAdapterFactory;
import ctwedge.util.CTWedgeException;
import ctwedge.util.ext.ICTWedgeImporter;
import ctwedge.util.ext.NotValidModelException;
import ctwedge.util.simplifier.Simplificator;


public class ImporterDialog extends Dialog {
	private Resource resource;
	private CitModel result;
	private IConfigurationElement element;
	private String fileExtension;
	private String path;
	private ISerializer SERIALIZER;
	private XtextResourceSet resourceSet;
	private Button btnNewButton_1;
	private Button btnSimplify;
	private StyledText preview;
	private TreeViewer treeViewer;
	

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public ImporterDialog(Shell parentShell, IConfigurationElement element) {
		super(parentShell);
		setShellStyle(SWT.CLOSE | SWT.MIN | SWT.MAX);
		this.element = element;
		Injector injector = Guice
				.createInjector(new ctwedge.CTWedgeRuntimeModule());
		resourceSet = injector.getInstance(XtextResourceSet.class);
		SERIALIZER = injector.getInstance(ISerializer.class);

	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 * @param lblNewLabel
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(null);
		preview = new StyledText(container, SWT.BORDER | SWT.V_SCROLL| SWT.H_SCROLL);
		preview.setDoubleClickEnabled(false);
		preview.setEditable(false);
		preview.setBounds(90, 10, 430, 308);
		if (element.getAttribute("OtherToolLanguage") != null)
			preview.setText(element.getAttribute("OtherToolLanguage")
					.toUpperCase());
		treeViewer = new TreeViewer(container, SWT.BORDER);
		Tree tree = treeViewer.getTree();
		tree.setBounds(545, 10, 248, 308);
		btnNewButton_1 = new Button(container, SWT.NONE);
		btnNewButton_1.setEnabled(false);
		btnNewButton_1.setGrayed(true);
		btnNewButton_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				PrjSelectionDialog prj = new PrjSelectionDialog(getShell(),
						SERIALIZER, result);
				prj.open();
				close();
			}
		});

		btnNewButton_1.setBounds(10, 71, 74, 41);
		btnNewButton_1.setText("Save/edit");
		btnSimplify = new Button(container, SWT.NONE);
		btnSimplify.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				result = new Simplificator(result).getSimplifiedVersion();
				result.setName(result.getName() + "_smp");
				fillPreview(result, preview, treeViewer, result.getName()
						+ ".citl");

			}
		});
		btnSimplify.setText("Simplify");
		btnSimplify.setGrayed(true);
		btnSimplify.setEnabled(false);
		btnSimplify.setBounds(10, 118, 74, 41);
		Button btnNewButton = new Button(container, SWT.NONE);
		btnNewButton.setBounds(10, 10, 74, 41);
		btnNewButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				openSelected();
			}
		});
		btnNewButton.setText("Open");

		return container;
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(832, 411);
	}

	private void initializedFileDialog(FileDialog fileDialog) {
		final String[] extensions = { "*.*" };
		fileDialog.setFilterExtensions(extensions);
		fileDialog.setFilterPath(Platform.getLocation().toOSString());

	}

	private void fillPreview(CitModel result, StyledText preview,
			TreeViewer treeViewer, String uri) {
      if(result==null){
    	  preview.setText("");
    	  if(treeViewer.getInput() != null)
    	  treeViewer.setInput(null);
    	  return;
      }
		if (resource == null)
			resource = resourceSet.createResource(URI.createFileURI(uri));
		else {
			resource.getContents().clear();
			resource.setURI(URI.createFileURI(uri));

		}
		resource.getContents().add(result);
		Injector injector = Guice
				.createInjector(new ctwedge.CTWedgeRuntimeModule());
		IResourceValidator validator = injector
				.getInstance(IResourceValidator.class);
		List<Issue> list = validator.validate(resource, CheckMode.ALL,CancelIndicator.NullImpl);
		if (list.isEmpty())
		{
			

			
			try {
				
				preview.setText(SERIALIZER.serialize(result));

			} catch (Exception e) {
				preview.setText("ERROR");
				// TODO Auto-generated catch block
				MessageDialog.openError(getShell(), "ERROR", e.toString());
			}

			ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(
					ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
			adapterFactory
					.addAdapterFactory(new ResourceItemProviderAdapterFactory());
			adapterFactory.addAdapterFactory(new CtWedgeAdapterFactory());
			adapterFactory
					.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());
			treeViewer.setContentProvider(new AdapterFactoryContentProvider(
					adapterFactory));

			treeViewer.setLabelProvider(new AdapterFactoryLabelProvider(
					adapterFactory));
			treeViewer.setInput(result.eResource().getResourceSet());
		}	
		else{
			NotValidModelException e = new NotValidModelException("THE MODEL CONTAINS ERRORS\n");
			for (Issue issue : list) {
				e.setTrace(issue.toString().concat(" "+e.getTrace()+"\n"));
				System.err.println(issue);
			}
			throw e;
		}
		
	}

	private void openSelected() {
		result=null;
		Job open = new Job("Model Importing") {
		private String trace="";
         @Inject Shell shell;
			@Override
			protected IStatus run(final IProgressMonitor monitor) {

				
					try {
						final Object o = element
								.createExecutableExtension("ImporterPrototype");

						if (o instanceof ICTWedgeImporter) {
							ISafeRunnable runnable = new ISafeRunnable() {

								@Override
								public void handleException(Throwable exception) {
									System.out.println("Exception in client");
								}

								@Override
								public void run() throws Exception {

									// ResourceSet resourceSet = new
									// ResourceSetImpl();
									// Register XML resource factory
									// resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("citl",
									// new XMIResourceFactoryImpl());
                                    
									try {
										result = ((ICTWedgeImporter) o)
												.importModel(path);
									} catch (CTWedgeException e){
										trace=e.getTrace();
										
									}catch (Exception e) {
										e.printStackTrace();
									}

									

								}
							};
							monitor.beginTask("IMPORTING",IProgressMonitor.UNKNOWN );
							SafeRunner.run(runnable);
							if (monitor.isCanceled())
								return Status.CANCEL_STATUS;
							monitor.done();
							monitor.setCanceled(true);
						}

					} catch (CoreException ex) {
						System.out.println(ex.getMessage());
					}

				
				Display.getDefault().asyncExec(new Runnable() {

					@Override
					public void run() {
						String name;
						if(result != null)
						 name =result.getName() + ".ctw" ;
						else name="";
						fillPreview(result, preview, treeViewer,
								name);
						if (result != null) {
							btnNewButton_1.setEnabled(true);
							btnSimplify.setEnabled(true);

							
							MessageDialog.openInformation(shell, "Complete", "Model correctly imported");
							
						} else{
							btnNewButton_1.setEnabled(false);
						btnSimplify.setEnabled(false);

						
						MessageDialog.openError(shell, "Error", "Model not importable \n"+trace);
					//		throw new RuntimeException(
						//			"Output null problem with  parsing");
						}

					}

				});
				return Status.OK_STATUS;
			}
		};
		FileDialog ComBFileSelection = new FileDialog(getShell());
		initializedFileDialog(ComBFileSelection);
		path = ComBFileSelection.open();
		if (path != null && !path.equals("")) {
		open.setPriority(Job.INTERACTIVE);
		open.setUser(true);

		open.schedule();}
	}
}
