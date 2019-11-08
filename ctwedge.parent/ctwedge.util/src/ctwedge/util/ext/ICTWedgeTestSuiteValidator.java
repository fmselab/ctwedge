package ctwedge.util.ext;

import ctwedge.util.TestSuite;

public abstract class ICTWedgeTestSuiteValidator { 
  protected TestSuite ts; 
 
  public ICTWedgeTestSuiteValidator() { 
   
  } 
  public void setTestSuite(TestSuite ts){ 
    this.ts=ts; 
  } 
    public abstract Boolean isValid (); 
    public abstract Boolean isComplete (); 
    public abstract Boolean isMinimal (); 
} 