package xxx;
import java.util.ArrayList;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

public class DiscoverGenerators {

	public static void main(String[] args) {
		IExtensionRegistry reg = Platform.getExtensionRegistry();
		IExtensionPoint ep = reg.getExtensionPoint("ctwedgeGenerators");
		IExtension[] extensions = ep.getExtensions();
		ArrayList contributors = new ArrayList();
		for (int i = 0; i < extensions.length; i++) {
			IExtension ext = extensions[i];
			IConfigurationElement[] ce = ext.getConfigurationElements();
			for (int j = 0; j < ce.length; j++) {
				// Object obj = ce[j].createExecutableExtension("class");
				// contributors.add(obj);
				System.out.println(ce[j].toString());
			}
		}
	}

}
