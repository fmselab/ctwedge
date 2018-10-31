package ctwedge.ui.wizard;

import org.eclipse.xtext.ui.wizard.IExtendedProjectInfo;
import org.eclipse.xtext.ui.wizard.IProjectCreator;
import org.eclipse.xtext.ui.wizard.XtextNewProjectWizard;

import com.google.inject.Inject;

public class CTWedgeNewProjectWizard extends XtextNewProjectWizard {

	private CTWedgeWizardNewProjectCreationPage mainPage;

	@Inject
	public CTWedgeNewProjectWizard(IProjectCreator projectCreator) {
		super(projectCreator);
		setWindowTitle("New CTWedge Project");
	}

	protected CTWedgeWizardNewProjectCreationPage getMainPage() {
		return mainPage;
	}

	/**
	 * Use this method to add pages to the wizard.
	 * The one-time generated version of this class will add a default new project page to the wizard.
	 */
	@Override
	public void addPages() {
		mainPage = createMainPage("basicNewProjectPage");
		mainPage.setTitle("CTWedge Project");
		mainPage.setDescription("Create a new CTWedge project.");
		addPage(mainPage);
	}

	protected CTWedgeWizardNewProjectCreationPage createMainPage(String pageName) {
		return new CTWedgeWizardNewProjectCreationPage(pageName);
	}

	/**
	 * Use this method to read the project settings from the wizard pages and feed them into the project info class.
	 */
	@Override
	protected IExtendedProjectInfo getProjectInfo() {
		CTWedgeProjectInfo projectInfo = new CTWedgeProjectInfo();
		projectInfo.setProjectName(mainPage.getProjectName());
		if (!mainPage.useDefaults()) {
			projectInfo.setLocationPath(mainPage.getLocationPath());
		}
		return projectInfo;
	}

}
