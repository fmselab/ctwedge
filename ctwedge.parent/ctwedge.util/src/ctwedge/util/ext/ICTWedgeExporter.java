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
 
import ctwedge.ctWedge.CitModel; 
 
/** exports a combinatorial citlab model into the notation of another tool */  
 
public abstract class ICTWedgeExporter extends ICTWedgeModelProcessor { 
 
  /** 
   * it converts a citModel in another language 
   * 
   * @param citModel the citModel 
   * @param constraintUse the constraint use 
   * @param nWise the n wise 
   * @param path the directory where to save the files 
   */ 
  public abstract void convertModel(CitModel citModel, Boolean constraintUse, int nWise, String path) throws NotConvertableModel; 
 
} 