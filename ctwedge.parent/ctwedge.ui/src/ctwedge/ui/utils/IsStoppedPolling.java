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
package ctwedge.ui.utils; 

import org.eclipse.core.runtime.IProgressMonitor; 
import org.eclipse.core.runtime.IStatus; 
import org.eclipse.core.runtime.Status; 
import org.eclipse.core.runtime.jobs.Job;

import ctwedge.util.ext.ICTWedgeTestGenerator; 

public class IsStoppedPolling extends Job { 
	private IProgressMonitor monitorToCheck; 
	private ICTWedgeTestGenerator tg; 
	private Boolean stopped; 

	public IsStoppedPolling(String name, IProgressMonitor monitorToCheck, 
			Boolean stopped, ICTWedgeTestGenerator tg) { 
		super(name); 
		this.monitorToCheck = monitorToCheck; 
		this.tg = tg; 
		this.stopped = stopped; 
		// TODO Auto-generated constructor stub 
	} 

	@Override 
	protected IStatus run(IProgressMonitor monitor) { 
		// TODO Auto-generated method stub 
		while (!stopped) { 
			if (monitorToCheck.isCanceled()) { 
				try { 
					throw new InterruptedException(); 
				} catch (InterruptedException e) { 
					stopped = true; 
					tg.stopGeneration(); 
				} 
				return Status.CANCEL_STATUS; 

			} 
		} 
		return Status.CANCEL_STATUS; 
	} 
} 