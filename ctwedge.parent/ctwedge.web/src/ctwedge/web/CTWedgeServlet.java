/*
 * generated by Xtext 2.34.0
 */
package ctwedge.web;

import com.google.inject.Injector;
import jakarta.servlet.annotation.WebServlet;
import org.eclipse.xtext.util.DisposableRegistry;
import org.eclipse.xtext.web.servlet.XtextServlet;

/**
 * Deploy this class into a servlet container to enable DSL-specific services.
 */
@WebServlet(name = "XtextServices", urlPatterns = "/xtext-service/*")
public class CTWedgeServlet extends XtextServlet {

	private static final long serialVersionUID = 1L;

	DisposableRegistry disposableRegistry;

	@Override
	public void init() throws jakarta.servlet.ServletException {
		super.init();
		Injector injector = new CTWedgeWebSetup().createInjectorAndDoEMFRegistration();
		this.disposableRegistry = injector.getInstance(DisposableRegistry.class);
	}

	@Override
	public void destroy() {
		if (disposableRegistry != null) {
			disposableRegistry.dispose();
			disposableRegistry = null;
		}
		super.destroy();
	}

}
