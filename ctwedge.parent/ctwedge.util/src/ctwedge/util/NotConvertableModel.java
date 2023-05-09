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
package ctwedge.util;

/** 
 * the model cannot be converted in the external language because some features 
 * are not supported 
 */ 
 
public class NotConvertableModel extends CTWedgeException { 
 
  private static final long serialVersionUID = 1L; 
 
  /** 
   *  
   * @param string message 
   */ 
  public NotConvertableModel(String string) { 
    super(string); 
  } 
 
} 