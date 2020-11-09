/******************************************************************************* 
 * Copyright (c) 2013 University of Bergamo - Italy 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 *  
 * Contributors: 
 *   Paolo Vavassori - initial API and implementation 
 *   Angelo Gargantini - utils and architecture 
 ******************************************************************************/
package ctwedge.util.ext;

import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.xtext.util.CancelIndicator;
import org.eclipse.xtext.validation.CheckMode;
import org.eclipse.xtext.validation.IResourceValidator;
import org.eclipse.xtext.validation.Issue;

import com.google.inject.Injector;

import ctwedge.CTWedgeStandaloneSetupGenerated;
import ctwedge.ctWedge.CitModel;

/**
 * all the classes that process in a way or another the citModel
 * 
 * @author garganti
 * 
 */
public class ICTWedgeModelProcessor {

	/**
	 * Gets the citModel from a file in filePath
	 * 
	 * @param filePath
	 *            the file path
	 * @return the citModel
	 */
	public static final CitModel getModel(String filePath) throws NotValidModelException {
		Injector injector = new CTWedgeStandaloneSetupGenerated().createInjectorAndDoEMFRegistration();
		ResourceSet resourceSet = new ResourceSetImpl();
		Resource resource;
		resource = resourceSet.getResource(URI.createFileURI(filePath), true);
		IResourceValidator validator = injector.getInstance(IResourceValidator.class);
		List<Issue> list = validator.validate(resource, CheckMode.ALL, CancelIndicator.NullImpl);
		if (list.isEmpty())
			return (CitModel) resource.getContents().get(0);
		else {
			NotValidModelException e = new NotValidModelException("THE MODEL CONTAINS ERRORS\n");
			for (Issue issue : list) {
				e.setTrace(issue.toString().concat(" " + e.getTrace() + "\n"));
				System.err.println(issue);
			}
			throw e;
		}
	}

}