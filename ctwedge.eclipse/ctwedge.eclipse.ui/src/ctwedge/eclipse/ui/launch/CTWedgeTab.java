package ctwedge.eclipse.ui.launch;

import java.util.ArrayList;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import ctwedge.ctWedge.CitModel;
import ctwedge.eclipse.util.Constants;

public class CTWedgeTab extends AbstractLaunchConfigurationTab {

	public static final String IGNORE_CONSTRAINTS = "ignoreConstraints";
	public static final String GENERATOR2 = "generator";
	public static final String STRENGTH = "strength";

	protected Object result;
	// private String generatorName = "ACTS";
	private Combo combo;
	private Button btnIgnoreConstraints;
	private Spinner spinner;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.debug.ui.ILaunchConfigurationTab#createControl(org.eclipse.swt.
	 * widgets.Composite)
	 */
	public void createControl(Composite parent) {
		//Font font = parent.getFont();

		// Composite comp = new Composite(parent, SWT.NONE);
		// setControl(comp);
		// GridLayout topLayout = new GridLayout();
		// topLayout.verticalSpacing = 0;
		// topLayout.numColumns = 3;
		// comp.setLayout(topLayout);
		// comp.setFont(font);
		//
		// createVerticalSpacer(comp, 3);
		//
		// Label programLabel = new Label(comp, SWT.NONE);
		// programLabel.setText("&Program:");
		// GridData gd = new GridData(GridData.BEGINNING);
		// programLabel.setLayoutData(gd);
		// programLabel.setFont(font);
		//
		// fProgramText = new Text(comp, SWT.SINGLE | SWT.BORDER);
		// gd = new GridData(GridData.FILL_HORIZONTAL);
		// fProgramText.setLayoutData(gd);
		// fProgramText.setFont(font);
		// fProgramText.addModifyListener(new ModifyListener() {
		// public void modifyText(ModifyEvent e) {
		// updateLaunchConfigurationDialog();
		// }
		// });
		//
		// fProgramButton = createPushButton(comp, "&Browse...", null); //$NON-NLS-1$
		// fProgramButton.addSelectionListener(new SelectionAdapter() {
		// public void widgetSelected(SelectionEvent e) {
		// browsePDAFiles();
		// }
		// });

		Composite composite = new Composite(parent, SWT.NONE);
		
		composite.setLayoutData(new RowData(SWT.DEFAULT, 249));
		//composite.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));

		SelectionListener defaultSelectionListener = new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				updateLaunchConfigurationDialog();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				updateLaunchConfigurationDialog();
			}
		};

		Label lblAlgorithmLabel = new Label(composite, SWT.NONE);
		//lblAlgorithmLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		lblAlgorithmLabel.setBounds(5, 10, 281, 39);
		lblAlgorithmLabel.setAlignment(SWT.CENTER);
		//lblAlgorithmLabel.setFont(SWTResourceManager.getFont("Courier", 21, SWT.NORMAL));
		// lblAlgorithmLabel.setText(generatorName);
		lblAlgorithmLabel.setText("CTWedge configuration");

		// Create a dropdown Combo
		Composite composite_generator = new Composite(composite, SWT.NONE);
		composite_generator.setBounds(54, 120, 300, 39);
		composite_generator.setLayout(null);
		Label lblGenerator = new Label(composite_generator, SWT.NONE);
		lblGenerator.setLocation(3, 5);
		lblGenerator.setSize(128, 28);
		//lblGenerator.setFont(SWTResourceManager.getFont("Sans Serif", 14, SWT.NORMAL));
		//lblGenerator.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_DARK_SHADOW));
		lblGenerator.setText("Generation Algorithm");
		combo = new Combo(composite_generator, SWT.NONE);
		//combo.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		combo.setBounds(140, 0, 80, 39);
		// combo.setFont(SWTResourceManager.getFont("Courier", 21, SWT.NORMAL));
		ArrayList<String> extensionsNames = new ArrayList<String>();
		IConfigurationElement[] eXgenerator = Platform.getExtensionRegistry()
				.getConfigurationElementsFor(Constants.packagaName + ".ctwedgeGenerators");
		for (int i = 0; i < eXgenerator.length; i++) {
			if (eXgenerator[i] != null) {
				extensionsNames.add(eXgenerator[i].getAttribute("Name"));
			}
		}
		combo.setItems(extensionsNames.toArray(new String[0]));
		combo.addSelectionListener(defaultSelectionListener);

		Composite composite_uses = new Composite(composite, SWT.NONE);
		composite_uses.setBounds(54, 86, 203, 99);
		composite_uses.setLayout(null);
		btnIgnoreConstraints = new Button(composite_uses, SWT.CHECK);
		btnIgnoreConstraints.setBounds(3, 3, 196, 16);
		btnIgnoreConstraints.setText("Ignore constraints");
		btnIgnoreConstraints.addSelectionListener(defaultSelectionListener);

		Composite composite_nwise = new Composite(composite, SWT.NONE);
		composite_nwise.setBounds(54, 47, 203, 39);
		spinner = new Spinner(composite_nwise, SWT.BORDER);
		//spinner.setFont(SWTResourceManager.getFont("Segoe UI", 13, SWT.NORMAL));
		spinner.setLocation(85, 5);
		spinner.setSize(108, 28);
		//spinner.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_RED));
		spinner.setMinimum(1);
		spinner.setSelection(2);
		//if (citModel != null)
		//	spinner.setMaximum(citModel.getParameters().size());
		Label lblNewLabel = new Label(composite_nwise, SWT.NONE);
		lblNewLabel.setLocation(3, 5);
		lblNewLabel.setSize(108, 28);
		//lblNewLabel.setFont(SWTResourceManager.getFont("Sans Serif", 14, SWT.NORMAL));
		//lblNewLabel.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_DARK_SHADOW));
		lblNewLabel.setText("N-WISE");
		spinner.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent arg0) {
				updateLaunchConfigurationDialog();
			}
		});
		setControl(composite);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.debug.ui.ILaunchConfigurationTab#setDefaults(org.eclipse.debug.
	 * core.ILaunchConfigurationWorkingCopy)
	 */
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		System.out.println("Setting defaults...");
		configuration.setAttribute(STRENGTH, 2);
		configuration.setAttribute(GENERATOR2, "ACTS");
		configuration.setAttribute(IGNORE_CONSTRAINTS, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.debug.ui.ILaunchConfigurationTab#initializeFrom(org.eclipse.debug
	 * .core.ILaunchConfiguration)
	 */
	public void initializeFrom(ILaunchConfiguration configuration) {
		// String program = null;
		// program = "CTWedge"; //
		// configuration.getAttribute(DebugCorePlugin.ATTR_PDA_PROGRAM, (String) null);
		// if (program != null) {
		// fProgramText.setText(program);
		// }
		try {
			spinner.setSelection(configuration.getAttribute(STRENGTH, 2));
			combo.setText(configuration.getAttribute(GENERATOR2, combo.getText()));
			btnIgnoreConstraints
					.setSelection(configuration.getAttribute(IGNORE_CONSTRAINTS, btnIgnoreConstraints.getSelection()));
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.debug.ui.ILaunchConfigurationTab#performApply(org.eclipse.debug.
	 * core.ILaunchConfigurationWorkingCopy)
	 */
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		// String program = fProgramText.getText().trim();
		// if (program.length() == 0) {
		// program = null;
		// }
		// #ifdef ex1
		// # // TODO: Exercise 1 - update the launch configuration with the path to
		// # // currently specified program
		// #else
		// configuration.setAttribute(DebugCorePlugin.ATTR_PDA_PROGRAM, program);
		// #endif

		// perform resource mapping for contextual launch
		// IResource[] resources = null;
		// if (program != null) {
		// IPath path = new Path(program);
		// IResource res = ResourcesPlugin.getWorkspace().getRoot().findMember(path);
		// if (res != null) {
		// resources = new IResource[] { res };
		// }
		// }
		// configuration.setMappedResources(resources);

		System.out.println("Performing apply...");
		configuration.setAttribute(STRENGTH, spinner.getSelection());
		configuration.setAttribute(GENERATOR2, combo.getText());
		configuration.setAttribute(IGNORE_CONSTRAINTS, btnIgnoreConstraints.getSelection());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#getName()
	 */
	public String getName() {
		return "CTWedge";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.debug.ui.ILaunchConfigurationTab#isValid(org.eclipse.debug.core.
	 * ILaunchConfiguration)
	 */
	public boolean isValid(ILaunchConfiguration launchConfig) {
		// setErrorMessage(null);
		// setMessage(null);
		// String text = fProgramText.getText();
		// #ifdef ex1
		// # // TODO: Exercise 1 - validate the currently specified program exists and
		// is not
		// # // empty, providing the user with feedback.
		// #else
		// if (text.length() > 0) {
		// IPath path = new Path(text);
		// if (ResourcesPlugin.getWorkspace().getRoot().findMember(path) == null) {
		// setErrorMessage("Specified program does not exist");
		// return false;
		// }
		// } else {
		// setMessage("Specify a program");
		// }
		// // #endif
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#getImage()
	 */
	public Image getImage() {
		return null; // return
						// DebugUIPlugin.getDefault().getImageRegistry().get(DebugUIPlugin.IMG_OBJ_PDA);
	}
}