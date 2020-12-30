package ctwedge.ui.views.generator;

import java.util.Collections;
import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.property.Properties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.jface.databinding.viewers.ViewerProperties;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import ctwedge.util.genprefs.CitlabDoublePreference;
import ctwedge.util.genprefs.CitlabIntPreference;
import ctwedge.util.genprefs.CitlabPreference;
import ctwedge.util.genprefs.CitlabPreferncesSet;

public class GenPropertiesDialog extends Dialog {
	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	private CitlabPreferncesSet genProperties;
	private Table table;
	private TableViewer tableViewer;
	private CCombo combo;
	private String preferenceToBeChanged;
	private Label lblNewLabel;
	private IsNumber verifierInt;
	private IsNumber verifierDouble;
	private Button btnCheckButton;

	public GenPropertiesDialog(Shell parentShell, CitlabPreferncesSet genProperties) {
		super(parentShell);
		if (genProperties != null) {
			this.genProperties = genProperties;

		}

		verifierInt = new IsNumber(false);
		verifierDouble = new IsNumber(true);
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);

		Composite composite = new Composite(container, SWT.NONE);
		GridData gd_composite = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_composite.heightHint = 203;
		gd_composite.widthHint = 423;
		composite.setLayoutData(gd_composite);
		TableColumnLayout tcl_composite = new TableColumnLayout();
		composite.setLayout(tcl_composite);
		table = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION);
		tableViewer = new TableViewer(table);

		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				combo.setVisible(true);
				btnCheckButton.setVisible(true);
				for (CitlabPreference<?> p : genProperties) {

					preferenceToBeChanged = lblNewLabel.getText();
					if (preferenceToBeChanged.toLowerCase().equals(p.getName().toLowerCase())) {
						btnCheckButton.setSelection(p.getIsEnabled());
						// remove all verifier
						combo.removeVerifyListener(verifierInt);
						combo.removeVerifyListener(verifierDouble);
						if (p instanceof CitlabIntPreference) {
							combo.setText(p.getMyValue().toString());
							combo.setEditable(true);
							combo.addVerifyListener(verifierInt);
						} else if (p instanceof CitlabDoublePreference) {
							combo.setText(p.getMyValue().toString());
							combo.setEditable(true);
							combo.addVerifyListener(verifierDouble);
						} else {
							// enum or boolean
							combo.setEditable(false);
							combo.deselectAll();
							// combo.setText(p.getMyValue().toString());
							if (p.getMyValue() != null)
								combo.select(combo.indexOf(p.getMyValue().toString()));

						}

						break;

					}

				}
			}
		});
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnNewColumn = tableViewerColumn.getColumn();
		tcl_composite.setColumnData(tblclmnNewColumn, new ColumnPixelData(136, true, true));
		tblclmnNewColumn.setText("Name");

		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnNewColumn_1 = tableViewerColumn_1.getColumn();
		tcl_composite.setColumnData(tblclmnNewColumn_1, new ColumnPixelData(134, true, true));
		tblclmnNewColumn_1.setText("Value");

		TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnIsenabled = tableViewerColumn_2.getColumn();
		tblclmnIsenabled.setText("isEnabled");
		tcl_composite.setColumnData(tblclmnIsenabled, new ColumnPixelData(150, true, true));

		Composite composite_1 = new Composite(container, SWT.NONE);
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		composite_1.setLayout(new GridLayout(2, false));

		lblNewLabel = new Label(composite_1, SWT.NONE);
		GridData gd_lblNewLabel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblNewLabel.widthHint = 254;
		lblNewLabel.setLayoutData(gd_lblNewLabel);
		lblNewLabel.setText("New Label");

		btnCheckButton = new Button(composite_1, SWT.CHECK);
		btnCheckButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				for (CitlabPreference<?> p : genProperties) {

					if (preferenceToBeChanged.toLowerCase().equals(p.getName().toLowerCase())) {
						if (btnCheckButton.getSelection())
							p.setIsEnabled(true);
						else
							p.setIsEnabled(false);

						break;

					}

				}
				tableViewer.refresh();

			}
		});
		btnCheckButton.setText("Enable");

		combo = new CCombo(composite_1, SWT.BORDER);
		GridData gd_combo = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_combo.widthHint = 251;
		combo.setLayoutData(gd_combo);

		combo.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				for (CitlabPreference<?> p : genProperties) {
					if (combo.getText() == null || combo.getText().trim().equals(""))
						break;
					preferenceToBeChanged = lblNewLabel.getText();
					if (preferenceToBeChanged.toLowerCase().equals(p.getName().toLowerCase())) {
						p.setStringAsMyValue(combo.getText());
						break;

					}

				}
				tableViewer.refresh();

			}
		});
		combo.setEditable(false);
		combo.setVisible(false);
		btnCheckButton.setVisible(false);
		new Label(composite_1, SWT.NONE);
		if (this.genProperties != null) {
			this.lblNewLabel.setText(this.genProperties.get(0).getName());

		}
		genProperties.loadPrefs();
		return container;
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		Button button = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				genProperties.storePrefs();
			}
		});
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
		initDataBindings();
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 465);
	}

	@SuppressWarnings("rawtypes")
	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		ObservableListContentProvider listContentProvider = new ObservableListContentProvider();
		IObservableMap[] observeMaps = PojoObservables.observeMaps(listContentProvider.getKnownElements(),
				CitlabPreference.class, new String[] { "name", "myValue", "isEnabled" });
		tableViewer.setLabelProvider(new ObservableMapLabelProvider(observeMaps));
		tableViewer.setContentProvider(listContentProvider);
		List<Object> a = Collections.emptyList();
		a.addAll(genProperties);
		IObservableList selfList = Properties.selfList(CitlabPreference.class).observe(a);
		tableViewer.setInput(selfList);
		//
		IObservableList itemsComboObserveWidget = WidgetProperties.items().observe(combo);
		IObservableValue observeSingleSelectionTableViewer = ViewerProperties.singleSelection().observe(tableViewer);
		IObservableList tableViewerDomainObserveDetailList = PojoProperties
				.list(CitlabPreference.class, "domain", List.class).observeDetail(observeSingleSelectionTableViewer);
		bindingContext.bindList(itemsComboObserveWidget, tableViewerDomainObserveDetailList, null, null);
		//
		IObservableValue observeTextLblNewLabelObserveWidget = WidgetProperties.text().observe(lblNewLabel);
		IObservableValue observeSingleSelectionTableViewer_1 = ViewerProperties.singleSelection().observe(tableViewer);
		IObservableValue tableViewerNameObserveDetailValue = PojoProperties
				.value(CitlabPreference.class, "name", String.class).observeDetail(observeSingleSelectionTableViewer_1);
		bindingContext.bindValue(observeTextLblNewLabelObserveWidget, tableViewerNameObserveDetailValue, null, null);
		//
		IObservableValue observeEnabledComboObserveWidget = WidgetProperties.enabled().observe(combo);
		IObservableValue observeSelectionBtnCheckButtonObserveWidget = WidgetProperties.selection()
				.observe(btnCheckButton);
		bindingContext.bindValue(observeEnabledComboObserveWidget, observeSelectionBtnCheckButtonObserveWidget, null,
				null);
		//
		return bindingContext;
	}
}

class IsNumber implements VerifyListener {

	private boolean acceptDecimalPoint;

	IsNumber(boolean acceptDecimalPoint) {
		this.acceptDecimalPoint = acceptDecimalPoint;
	}

	@Override
	public void verifyText(VerifyEvent ve) {
		if (ve.doit == false) {
			return;
		}
		// Validation for keys like Backspace, left arrow key, right arrow key
		// and del keys
		if (ve.character == SWT.BS || ve.keyCode == SWT.ARROW_LEFT || ve.keyCode == SWT.ARROW_RIGHT
				|| ve.keyCode == SWT.DEL) {
			ve.doit = true;
			return;
		}
		if (acceptDecimalPoint && ve.character == '.') {
			ve.doit = true;
			return;
		}
		if (!('0' <= ve.character && ve.character <= '9')) {
			ve.doit = false;
			return;
		}

	}

}
