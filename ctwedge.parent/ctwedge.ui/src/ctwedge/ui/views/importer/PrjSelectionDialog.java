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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.xtext.serializer.ISerializer;

import ctwedge.ctWedge.CitModel;


public class PrjSelectionDialog extends Dialog {
	private List<IProject> prjs;
	private Table table_1;
	private TableViewer tableViewer;
	private String projectName= "newProject";
	private Text text;
	
	private ISerializer SERIALIZER;
	private CitModel result;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public PrjSelectionDialog(Shell parentShell, ISerializer serializer,CitModel model) {
		
		super(parentShell);
		this.SERIALIZER=serializer;
		this.result=model;
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		 prjs= Arrays.asList(root.getProjects());
	}
    
	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		final Button btnRadioButton_1 = new Button(container, SWT.CHECK);
		btnRadioButton_1.setText("Create Project");
		text = new Text(container, SWT.BORDER);
		
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		final Button btnRadioButton = new Button(container, SWT.CHECK);
		tableViewer = new TableViewer(container, SWT.BORDER | SWT.FULL_SELECTION);
		table_1 = tableViewer.getTable();
		table_1.setHeaderVisible(true);
		table_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		btnRadioButton.setText("Select Project");
		btnRadioButton_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btnRadioButton_1.setSelection(true);
				btnRadioButton.setSelection(false);
				table_1.setEnabled(false);
				text.setEnabled(true);
			}
		});
		btnRadioButton_1.setSelection(true);
		btnRadioButton.setSelection(false);
		table_1.setEnabled(false);
		text.setEnabled(true);
		
		
		btnRadioButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btnRadioButton_1.setSelection(false);
				btnRadioButton.setSelection(true);
				table_1.setEnabled(true);
				text.setEnabled(false);
			}
		});
		
		
		
		
		TableColumn tblclmnName = new TableColumn(table_1, SWT.NONE);
		tblclmnName.setWidth(100);
		tblclmnName.setText("Name");
		
		TableColumn tblclmnNewColumn = new TableColumn(table_1, SWT.NONE);
		tblclmnNewColumn.setWidth(403);
		tblclmnNewColumn.setText("Path");
		
		final Button btnNewButton = new Button(container, SWT.NONE);
		btnNewButton.setEnabled(false);
		GridData gd_btnNewButton = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd_btnNewButton.widthHint = 223;
		btnNewButton.setLayoutData(gd_btnNewButton);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				  
		  
		          
				try {
					saveModel(text.getText());
					
					close();
				} catch (Exception e1) {
					MessageBox m =new MessageBox(getShell(),SWT.ERROR);
					m.setMessage("Project name not Valid");
					m.open();
					e1.printStackTrace();
				}}
				
			
		});
		btnNewButton.setText("OK");
		text.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if(text.getText()!=null && (text.getText().length() == 0 || text.getText().charAt(0) != ' ') && !text.getText().equals(""))
				btnNewButton.setEnabled(true); else btnNewButton.setEnabled(false); 
			}
		});
		return container;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		initDataBindings();
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(532, 483);
	}
	@SuppressWarnings("rawtypes")
	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
//		ObservableListContentProvider listContentProvider = new ObservableListContentProvider();
//		IObservableMap[] observeMaps = PojoObservables.observeMaps(listContentProvider.getKnownElements(), IProject.class, new String[]{"name", "locationURI.path"});
//		tableViewer.setLabelProvider(new ObservableMapLabelProvider(observeMaps));
//		tableViewer.setContentProvider(listContentProvider);
//		//
//		WritableList writableList = new WritableList(prjs, IProject.class);
//		tableViewer.setInput(writableList);
//		//
//		IObservableValue tableViewerObserveSingleSelection = ViewersObservables.observeSingleSelection(tableViewer);
//		IObservableValue tableViewerAccessibleObserveDetailValue = PojoObservables.observeDetailValue(tableViewerObserveSingleSelection, "name", String.class);
//		IObservableValue textTextObserveValue = PojoObservables.observeValue(text, "text");
//		bindingContext.bindValue(tableViewerAccessibleObserveDetailValue, textTextObserveValue, null, null);
		//
		return bindingContext;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public void saveModel(String prjName){
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		IProject project = root.getProject(prjName);
		IFolder folder = project.getFolder("src");
		IFile file = folder.getFile(result.getName()+".citl");
		if (!project.exists())
			try {
				project.create(null);
			} catch (CoreException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		if (!project.isOpen())
			try {
				project.open(null);
			} catch (CoreException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		if (!folder.exists())
			try {
				folder.create(IResource.NONE, true, null);
			} catch (CoreException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		byte[] bytes = SERIALIZER
				.serialize(result).getBytes();
	
		InputStream source = new ByteArrayInputStream(bytes);
		try {
			if(!file.exists())
			file.create(source, IResource.NONE, null);
			else{
				file.delete(true, null);
			file.create(source, IResource.FORCE, null);}

		} catch (CoreException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
       try {
    	   IWorkbench wb = PlatformUI.getWorkbench();
    	   IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
		IDE.openEditor(win.getActivePage(), file);
	} catch (PartInitException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
     
		
	}
}
