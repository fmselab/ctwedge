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

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

import ctwedge.util.TestSuite;

public class OutputDialog extends Dialog {
	private Table table;
	private TestSuite inputlist;
	private Text text;
	private Text text_1;
	private Text text_2;

	/**
	 * Create the dialog.
	 * 
	 * @param <T>
	 * @param parentShell
	 */
	public OutputDialog(Shell parentShell, TestSuite output) {
		super(parentShell);
		setBlockOnOpen(false);
		
		setShellStyle(SWT.BORDER | SWT.CLOSE | SWT.MIN | SWT.MAX | SWT.RESIZE);
		this.inputlist = output;
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(1, false));

		table = new Table(container, SWT.BORDER | SWT.FULL_SELECTION);
		table.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, true, true, 1, 1));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
        
		/*MR TODO TableColumn[] columnOfTheModel = new TableColumn[inputlist.getTests().get(0).getAssignments()
		 	.size()+1];
		 

		columnOfTheModel[0] = new TableColumn(table, SWT.NONE);
		columnOfTheModel[0].setText("Test");
		*/
		int n = 1;
		/*MR TODO for (Assignment i :inputlist.getTests().get(0).getAssignments()) {
			columnOfTheModel[n] = new TableColumn(table, SWT.NONE);
			columnOfTheModel[n].setText(i.getParameter().getName());
			n++;
		}*/
		fillTable(table);
		
	    
		Composite composite = new Composite(container, SWT.NONE);
		GridData gd_composite = new GridData(SWT.CENTER, SWT.TOP, false, false, 1, 1);
		gd_composite.heightHint = 57;
		gd_composite.widthHint = 710;
		composite.setLayoutData(gd_composite);
		
		Label lblTime = new Label(composite, SWT.NONE);
		lblTime.setBounds(10, 10, 49, 15);
		lblTime.setText("Name:");
		
		text = new Text(composite, SWT.BORDER);
		text.setEditable(false);
		text.setBounds(65, 7, 271, 21); 
		text.setText(this.inputlist.getGeneratorName());
		
		text_1 = new Text(composite, SWT.BORDER);
		text_1.setEditable(false);
		text_1.setBounds(65, 33, 271, 21);
		text_1.setText((String.valueOf(this.inputlist.getGeneratorTime())));
		Label lblTime_1 = new Label(composite, SWT.NONE);
		lblTime_1.setText("Time:");
		lblTime_1.setBounds(9, 39, 49, 15);
		
		Label labelSize = new Label(composite, SWT.NONE);
		labelSize.setText("Size:");
		labelSize.setBounds(342, 11, 49, 15);
		
		text_2 = new Text(composite, SWT.BORDER);
		text_2.setEditable(false);
		text_2.setBounds(397, 8, 271, 21);
		//MR TODO text_2.setText((String.valueOf(this.inputlist.getTests().size())));
		
		Button btnNewButton = new Button(composite, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
			    /*MR TODO CombCounterJob cjob = new CombCounterJob("comb", inputlist.getModel());
				cjob.setUser(true);
				cjob.schedule();
				*/
				
			}
		});
		btnNewButton.setBounds(397, 33, 271, 25);
		btnNewButton.setText("Count all possible configurations");
		/* MR TODO for (int i = 0, n1 = columnOfTheModel.length; i < n1; i++) {
			columnOfTheModel[i].pack();
		}*/

		return container;
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		Button export_button = createButton(parent, IDialogConstants.OK_ID, "Export",
				true);
		Button close_button = createButton(parent, IDialogConstants.CLOSE_ID,
				IDialogConstants.CLOSE_LABEL, true);
		close_button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				close();
			}
		});
		export_button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				buttonSelected(event);
			}

			
		});
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(744, 583);
	}

	private void fillTable(Table table) {
        //TODO MR removed, to implement
		/*int testnumber=0;
		for (Test test : inputlist.getTests()) {
			testnumber++;
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(0, Integer.toString(testnumber));
			item.setForeground(0, SWTResourceManager.getColor(SWT.COLOR_RED));
			int i=1;
			for (Assignment assignment : test.getAssignments()) {
				item.setText(i, assignment.getValue());
				i++;

			}
		}
		table.setRedraw(true);
		
		*/
	}
	private void buttonSelected(SelectionEvent event) {
		
		TestSuiteExporterDialog tExDialog = new TestSuiteExporterDialog(this.getShell(),inputlist);
		tExDialog.open();
		this.close();
	}
}
