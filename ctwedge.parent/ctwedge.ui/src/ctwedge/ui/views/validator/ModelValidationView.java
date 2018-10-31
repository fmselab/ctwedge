package ctwedge.ui.views.validator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;

import ctwedge.ui.utils.Constants;

import org.eclipse.swt.layout.FillLayout;

public class ModelValidationView extends ViewPart {
	private String secondaryId;
	StyledText styledText;
	public ModelValidationView() {
		
	}
	@Override
	public void init(IViewSite site) throws PartInitException {
		super.init(site);
		secondaryId = site.getSecondaryId();
		setPartName(secondaryId.replace(Constants.packagaName+".ui.modelvalidationSecondaryID", "")
				.replace("__", ":"));

	}

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

		Composite composite = new Composite(content, SWT.NONE);
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		styledText = new StyledText(composite, SWT.BORDER);
		styledText.setSelectionForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		styledText.setSelectionBackground(SWTResourceManager.getColor(SWT.COLOR_YELLOW));
		styledText.setLeftMargin(2);
		styledText.setEditable(false);
		styledText.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		styledText.setFont(SWTResourceManager.getFont("Latin Modern Roman", 13, SWT.BOLD));
		
		
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	public void setReport(String output) {
		styledText.setText(output);
		
	}
}
