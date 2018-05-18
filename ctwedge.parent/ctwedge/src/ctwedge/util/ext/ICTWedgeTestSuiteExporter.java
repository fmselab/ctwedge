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

import ctwedge.util.TestSuite;

/** 
 * The Class ICitLabTestSuiteExporter. 
 */ 
public abstract class ICTWedgeTestSuiteExporter { 
      
  /** 
   * Instantiates a new i cit lab test suite exporter. 
   */ 
  public ICTWedgeTestSuiteExporter() { 
     
  } 
   
  /** 
   * Generate output. 
   * 
   * @param input the input 
   * @param FileName the file name 
   */ 
  public abstract void generateOutput(TestSuite input,String FileName); 
   
 
} 
