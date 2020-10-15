package ctwedge.generator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.InvalidRegistryObjectException;
import org.eclipse.core.runtime.Platform;

import ctwedge.util.ext.ICTWedgeTestGenerator;

//import ctwedge.generator.pict.PICTGenerator;

public class DiscoverGenerators {


	// funziona anche via web?
	// senza plugins/OSGI?
	static public ICTWedgeTestGenerator getGenerator(String generatorName) throws CoreException, ClassNotFoundException, InvalidRegistryObjectException {
		IExtensionRegistry reg = Platform.getExtensionRegistry();
		IExtensionPoint ep = reg.getExtensionPoint("ctwedge.ui.ctwedgeGenerators");
		IExtension[] extensions = ep.getExtensions();
		for (int i = 0; i < extensions.length; i++) {
			IExtension ext = extensions[i];
			IConfigurationElement[] ce = ext.getConfigurationElements();	
			System.out.println("Checking: " + ext.getLabel());
			if (ext.getLabel().equalsIgnoreCase(generatorName)) {			
				for (IConfigurationElement e : ce) {
					Object o = e.createExecutableExtension("GeneratorPrototype");
					System.out.println("Found " + o);
					if (o instanceof ICTWedgeTestGenerator)
						return (ICTWedgeTestGenerator)o;
				}
			}
		}
		return null;
	}

}
