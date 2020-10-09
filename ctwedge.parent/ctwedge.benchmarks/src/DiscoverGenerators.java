
import java.util.ArrayList;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.InvalidRegistryObjectException;
import org.eclipse.core.runtime.Platform;
import org.junit.Test;

public class DiscoverGenerators {

	@Test
	public void temp() throws CoreException {
		IExtensionRegistry reg = Platform.getExtensionRegistry();
		IExtensionPoint ep = reg.getExtensionPoint("ctwedge.ui.ctwedgeGenerators");
		IExtension[] extensions = ep.getExtensions();
		ArrayList contributors = new ArrayList();
		for (int i = 0; i < extensions.length; i++) {
			IExtension ext = extensions[i];
			System.out.println(ext.getExtensionPointUniqueIdentifier());
			System.out.println(ext.getLabel());
			IConfigurationElement[] ce = ext.getConfigurationElements();
			for (int j = 0; j < ce.length; j++) {
				//contributors.add(obj);
				System.out.println(ce[j].toString());
			}
		}
	}
	
	@Test
	public void temp2() throws CoreException, ClassNotFoundException, InvalidRegistryObjectException {
		IExtensionRegistry reg = Platform.getExtensionRegistry();
		IExtensionPoint ep = reg.getExtensionPoint("ctwedge.ui.ctwedgeGenerators");
		IExtension[] extensions = ep.getExtensions();
		ArrayList contributors = new ArrayList();
		for (int i = 0; i < extensions.length; i++) {
			IExtension ext = extensions[i];
			IConfigurationElement[] ce = ext.getConfigurationElements();			
			
			for (IConfigurationElement e : ce) {
				
				try {
					Class c = Class.forName(e.getAttribute("GeneratorPrototype"));
					System.out.println(c);
				} catch (ClassNotFoundException exc) {
					System.err.println("Unable to find the class: " + e.getAttribute("GeneratorPrototype"));
				}
			}
			
		}
	}

}
