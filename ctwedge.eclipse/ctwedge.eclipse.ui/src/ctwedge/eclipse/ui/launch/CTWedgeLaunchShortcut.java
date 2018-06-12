package ctwedge.eclipse.ui.launch;

import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.IDebugModelPresentation;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;

public class CTWedgeLaunchShortcut implements ILaunchShortcut {

	// attribute file path
	protected static final String ATTR_FILEPATH = "ATTR_FILE_PATH";

	
	@Override
	public void launch(ISelection selection, String mode) {
		// must be a structured selection with one file selected
		IFile file = (IFile) ((IStructuredSelection) selection).getFirstElement();
		// check for an existing launch config for the pda file
		findAndLaunch(mode, file);
		//new CTwedgeLaunchConfigurationDelegate().launch(getLastConfiguration(), arg0.toString(), (ILaunch)null, (IProgressMonitor)null);
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.debug.ui.ILaunchShortcut#launch(org.eclipse.ui.IEditorPart,
	 * java.lang.String)
	 */
	public void launch(IEditorPart editor, String mode) {
		System.out.println("Launch from editor");
		IEditorInput input = editor.getEditorInput();
		IFile file = (IFile) input.getAdapter(IFile.class);
		if (file != null) {
			findAndLaunch(mode, file);
		}
	}
	
	/**
	 * @author mradavelli
	 * https://stackoverflow.com/questions/39052708/eclipse-plugin-get-launch-configurations-tree-list-in-a-dialog
	 */
	public ILaunchConfiguration getLastConfiguration() {
		ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
		ILaunchConfigurationType [] allTypes = manager.getLaunchConfigurationTypes();
		for (ILaunchConfigurationType t : allTypes) {
			System.out.println(t.getIdentifier()+" "+t.getName()+" "+t.getPluginIdentifier());
		}
		try {
			ILaunchConfiguration[] configs = manager.getLaunchConfigurations(allTypes[0]);
			if (configs!=null && configs.length>0) {
				return configs[0];
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}
		//TODO create a new configuration
		return null;
	}

	protected ILaunchConfiguration chooseConfiguration(List<ILaunchConfiguration> configList) {
		IDebugModelPresentation labelProvider = DebugUITools.newDebugModelPresentation();
		ElementListSelectionDialog dialog = new ElementListSelectionDialog(Display.getCurrent().getActiveShell(),
				labelProvider);
		dialog.setElements(configList.toArray());

		dialog.setMessage("&Select existing configuration:"/*
															 * LauncherMessages.
															 * JavaLaunchShortcut_2
															 */);
		dialog.setMultipleSelection(false);
		int result = dialog.open();
		labelProvider.dispose();
		if (result == Window.OK) {
			return (ILaunchConfiguration) dialog.getFirstResult();
		}
		return null;
	}


	private void findAndLaunch(String mode, IFile file) {
		ILaunchConfigurationWorkingCopy workingCopy;
		ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
		ILaunchConfigurationType type = launchManager.getLaunchConfigurationType("CTwedge.generator");
		String filepath = file.getRawLocation().makeAbsolute().toOSString();
		System.out.println(filepath);
		try {
			ILaunchConfiguration[] configurations = launchManager.getLaunchConfigurations(type);
			int candidateCount = configurations.length;
			ILaunchConfiguration configuration = null;
			if (candidateCount == 1) {
				configuration = (ILaunchConfiguration) configurations[0];
			} else if (candidateCount > 1) {
				configuration = chooseConfiguration(Arrays.asList(configurations));

			} else if (candidateCount == 0) {
				workingCopy = type.newInstance(null, "new");
				workingCopy.setAttribute(ATTR_FILEPATH, filepath);
				// workingCopy.setMappedResources(new IResource[] { file });
				configuration = workingCopy.doSave();
				//DebugUITools.launch(configuration, mode);
				//return;
			}
			workingCopy = configuration.getWorkingCopy();
			workingCopy.setAttribute(ATTR_FILEPATH, filepath);
			configuration = workingCopy.doSave();
			DebugUITools.launch(configuration, mode);
			return;
		} catch (CoreException e) {
			return;
		}
	}


}

