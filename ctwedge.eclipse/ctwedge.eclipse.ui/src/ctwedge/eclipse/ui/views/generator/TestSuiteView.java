/**
 * 
 */
package ctwedge.eclipse.ui.views.generator;

import java.util.Map.Entry;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import com.google.inject.Inject;

import ctwedge.eclipse.util.Constants;
import ctwedge.util.Test;
import ctwedge.util.TestSuite;
import ctwedge.util.ext.ICTWedgeTestSuiteValidator;

/**
 * @author vavas726
 * 
 */
public class TestSuiteView extends ViewPart {
	@Inject
	Shell shell;
	private Table table;
	private TableViewer tableViewer;
	//private String secondaryId;
	private Text text;
	private Text text_1;
	private Text text_2;
	private TestSuite inputlist;
	private TableColumnLayout layout;
	private StyledText styledText;
	private Composite composite_2;
	private Button button;
	private Button button_1;

	public TestSuiteView() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init(IViewSite site) throws PartInitException {
		super.init(site);
		//secondaryId = site.getSecondaryId();
		//setPartName(secondaryId.replace(Activator.PLUGIN_ID+".ui.outputSecondaryID", "").replace("__", ":"));
		setPartName("CTWedge TestSuite");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets
	 * .Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {

		parent.setLayout(new FillLayout(SWT.HORIZONTAL));
		 ScrolledComposite scrolledComp = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);

		    // Create a child composite for your content
		    Composite content = new Composite(scrolledComp, SWT.NONE);
		    content.setLayout(new FillLayout() );

		    
		    // add content to scrolled composite
		    scrolledComp.setContent(content);

		    // Set the minimum size (in this case way too large)
		    scrolledComp.setMinSize(600, 400);

		    // Expand both horizontally and vertically
		    scrolledComp.setExpandHorizontal(true);
		    scrolledComp.setExpandVertical(true);

		Composite composite_1 = new Composite(content, SWT.NONE);

		tableViewer = new TableViewer(composite_1, SWT.BORDER
				| SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL);
		table = tableViewer.getTable();

		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		layout = new TableColumnLayout();
		composite_1.setLayout(layout);
		Composite composite = new Composite(content, SWT.NONE);
		composite.setLayout(new FormLayout());

		Label lblTime = new Label(composite, SWT.NONE);
		FormData fd_lblTime = new FormData();
		fd_lblTime.right = new FormAttachment(0, 59);
		fd_lblTime.top = new FormAttachment(0, 10);
		fd_lblTime.left = new FormAttachment(0, 10);
		lblTime.setLayoutData(fd_lblTime);
		lblTime.setText("Name:");

		text = new Text(composite, SWT.BORDER);
		FormData fd_text = new FormData();
		fd_text.bottom = new FormAttachment(0, 28);
		fd_text.right = new FormAttachment(0, 336);
		fd_text.top = new FormAttachment(0, 7);
		fd_text.left = new FormAttachment(0, 65);
		text.setLayoutData(fd_text);
		text.setEditable(false);

		text_1 = new Text(composite, SWT.BORDER);
		FormData fd_text_1 = new FormData();
		fd_text_1.top = new FormAttachment(text, 11);
		fd_text_1.left = new FormAttachment(text, 0, SWT.LEFT);
		fd_text_1.right = new FormAttachment(0, 336);
		text_1.setLayoutData(fd_text_1);
		text_1.setEditable(false);

		Label lblTime_1 = new Label(composite, SWT.NONE);
		fd_lblTime.bottom = new FormAttachment(lblTime_1, -6);
		FormData fd_lblTime_1 = new FormData();
		fd_lblTime_1.bottom = new FormAttachment(text_1, 0, SWT.BOTTOM);
		fd_lblTime_1.right = new FormAttachment(0, 58);
		fd_lblTime_1.top = new FormAttachment(0, 39);
		fd_lblTime_1.left = new FormAttachment(0, 9);
		lblTime_1.setLayoutData(fd_lblTime_1);
		lblTime_1.setText("Time:");

		Label labelSize = new Label(composite, SWT.NONE);
		FormData fd_labelSize = new FormData();
		fd_labelSize.left = new FormAttachment(0, 10);
		fd_labelSize.top = new FormAttachment(lblTime_1, 6);
		fd_labelSize.bottom = new FormAttachment(100, -376);
		labelSize.setLayoutData(fd_labelSize);
		labelSize.setText("Size:");

		text_2 = new Text(composite, SWT.BORDER);
		fd_labelSize.right = new FormAttachment(text_2, -6);
		fd_text_1.bottom = new FormAttachment(text_2, -16);
		FormData fd_text_2 = new FormData();
		fd_text_2.top = new FormAttachment(0, 76);
		fd_text_2.right = new FormAttachment(text, 0, SWT.RIGHT);
		fd_text_2.left = new FormAttachment(0, 65);
		text_2.setLayoutData(fd_text_2);
		text_2.setEditable(false);

		composite_2 = new Composite(composite, SWT.NONE);
		composite_2.setLayout(new FillLayout(SWT.HORIZONTAL));
		FormData fd_composite_2 = new FormData();
		fd_composite_2.right = new FormAttachment(text, 0, SWT.RIGHT);
		fd_composite_2.top = new FormAttachment(0, 93);
		fd_composite_2.left = new FormAttachment(text, 0, SWT.LEFT);
		composite_2.setLayoutData(fd_composite_2);

		styledText = new StyledText(composite, SWT.BORDER);
		fd_text_2.bottom = new FormAttachment(styledText, -60);
		IConfigurationElement[] exporters = Platform.getExtensionRegistry()
				.getConfigurationElementsFor("citlab.core.TestSuiteExporters");
		if (exporters.length > 0) {
			button = new Button(composite_2, SWT.NONE);
			button.setText("Export");
			button.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					TestSuiteExporterDialog tExDialog = new TestSuiteExporterDialog(
							shell, inputlist);
					tExDialog.open();

				}

			});
		}
		final IConfigurationElement[] validator = Platform
				.getExtensionRegistry().getConfigurationElementsFor(
						Constants.packagaName+".TestSuiteValidator");
		if (validator.length > 0) {
			button_1 = new Button(composite_2, SWT.NONE);
			button_1.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					try {
						final Object o = validator[0]
								.createExecutableExtension("TsVProto");

						if (o instanceof ICTWedgeTestSuiteValidator) {
							Job validatorJob = new Job("TestSuite Validation") {

								Boolean isValid;
								Boolean isComplete;
								Boolean isMinimal;
								@Inject
								Shell shell;
								private boolean error = false;
								private boolean errorJna = false;
								String trace = "GENERIC ERROR";

								@Override
								protected IStatus run(IProgressMonitor monitor) {
									ISafeRunnable runnable = new ISafeRunnable() {
										@Override
										public void handleException(
												Throwable exception) {

											if (exception instanceof java.lang.UnsatisfiedLinkError) {

												trace = exception.getMessage();
												errorJna = true;
												// exception.printStackTrace();
											}
											if (exception instanceof java.lang.OutOfMemoryError) {
												trace = " EXCEEDED MEMORY LIMIT";
												// exception.printStackTrace();
											}
										
                                            trace=exception.getCause().toString();
											error = true;

										}

										@Override
										public void run() throws Exception {

											// compute the project directory
											((ICTWedgeTestSuiteValidator) o)
													.setTestSuite(inputlist);
											isValid = ((ICTWedgeTestSuiteValidator) o)
													.isValid();
											isComplete = ((ICTWedgeTestSuiteValidator) o)
													.isComplete();
											isMinimal = ((ICTWedgeTestSuiteValidator) o)
													.isMinimal();
										}
									};
									monitor.beginTask("TestSuite Validator",
											IProgressMonitor.UNKNOWN);
									SafeRunner.run(runnable);
									if (monitor.isCanceled())
										return Status.CANCEL_STATUS;
									monitor.done();
									monitor.setCanceled(true);
									Display.getDefault().syncExec(
											new Runnable() {

												@Override
												public void run() {
													if (error) {
														MessageDialog
																.openError(
																		shell,
																		"NOT CORRECTLY VALIDATED",
																		"Validation problem "
																				+ trace);
														if (errorJna) {
															MessageDialog
																	.openInformation(
																			shell,
																			"Locate the missing dll",
																			"Please select the location of the missing dll");

															DirectoryDialog dialog = new DirectoryDialog(
																	Display.getCurrent()
																			.getActiveShell());
															Preferences node = InstanceScope.INSTANCE
																	.getNode("citlab.core.jna");
															node.put(
																	"dir",
																	dialog.open());
															try {
																// forces the
																// application
																// to save the
																// preferences
																node.flush();
															} catch (BackingStoreException e) {
																e.printStackTrace();
															}
															MessageDialog
																	.openInformation(
																			shell,
																			"Re-run the tool",
																			"Run the tool again to make the changes effective");

														}
													} else
														MessageDialog
																.openInformation(
																		shell,
																		"VALIDATE PROCESS ENDED",
																		" VALID: "
																				+ isValid
																				+ "\n COMPLETE: "
																				+ isComplete
																				+ "\n MINIMAL: "
																				+ isMinimal);

												}

											});

									return Status.OK_STATUS;
								}
							};
							validatorJob.setUser(true);
							validatorJob.schedule();

						}

					} catch (CoreException ex) {
						System.out.println(ex.getMessage());
					}

				}

			});
			button_1.setText("Validate");
		}
		styledText.setEditable(false);
		styledText.setAlwaysShowScrollBars(false);
		FormData fd_styledText = new FormData();
		fd_styledText.bottom = new FormAttachment(0, 259);
		fd_styledText.right = new FormAttachment(text, 0, SWT.RIGHT);
		fd_styledText.top = new FormAttachment(0, 157);
		fd_styledText.left = new FormAttachment(text, 0, SWT.LEFT);
		styledText.setLayoutData(fd_styledText);

	}

	// TODO Auto-generated method stub

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	public void setTestsuite(final TestSuite inputlist) {
		this.inputlist = inputlist;
		
		if (inputlist.getTests().size()==0) {
			text_2.setText("The generated test suite is empty. An error occurred or the specified model is infeasible.");
		} else {
			TableColumn[] columnOfTheModel = new TableColumn[this.inputlist
					.getTests().get(0).size() + 1];
			TableViewerColumn[] tableViewerColumn = new TableViewerColumn[this.inputlist
					.getTests().get(0).size() + 1];
	
			columnOfTheModel[0] = new TableColumn(table, SWT.NONE);
			columnOfTheModel[0].setText("Test");
	
			int n = 1;
			
			for (Entry<String,String> i : this.inputlist.getTests().get(0).entrySet()) {
				tableViewerColumn[n] = new TableViewerColumn(tableViewer, SWT.NONE);
	
				columnOfTheModel[n] = tableViewerColumn[n].getColumn();
	
				columnOfTheModel[n].setText(i.getKey());
				n++;
			}
			int testnumber = 0;
			for (Test test : this.inputlist.getTests()) {
				testnumber++;
				TableItem item = new TableItem(table, SWT.NONE);
				item.setText(0, Integer.toString(testnumber));
				item.setForeground(0, SWTResourceManager.getColor(SWT.COLOR_RED));
				int i = 1;
				for (Entry<String,String> assignment : test.entrySet()) {
					item.setText(i, assignment.getValue());
					i++;
	
				}
			}
	
			for (int i = 0, n1 = columnOfTheModel.length; i < n1; i++) {
				columnOfTheModel[i].pack();//
				layout.setColumnData(columnOfTheModel[i], new ColumnWeightData(10));
	
			}
			table.setRedraw(true);
			// table.pack();
			table.redraw();
			text_2.setText((String.valueOf(this.inputlist.getTests().size())));
			text_1.setText((String.valueOf(this.inputlist.getGeneratorTime())));
			text.setText(this.inputlist.getGeneratorName());
	
			styledText.setText(this.getPartName() + "\n" + "\n" + "N-WISE= "
					+ inputlist.getStrength());
		}
	}
}
