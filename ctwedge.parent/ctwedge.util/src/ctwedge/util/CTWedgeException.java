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
 
public abstract class CTWedgeException extends RuntimeException { 
 
  private static final long serialVersionUID = 1L; 
 
  private String trace ; 
   
  /** 
   *  
   * @param string message 
   */ 
  public CTWedgeException(String string) { 
    super(string); 
  } 
 
  public String getTrace() { 
    return trace; 
  } 
 
  public void setTrace(String trace) { 
    this.trace = trace; 
  } 
} 