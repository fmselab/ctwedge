package ctwedge.util.ext;

import ctwedge.util.CTWedgeException;

/** when a model is read from a file but its contains severe errors (it is not valid 
 */ 
public class NotValidModelException extends CTWedgeException{ 
 
  /** 
   *  
   */ 
  private static final long serialVersionUID = -8827239646086972522L; 
 
  public NotValidModelException(String string) { 
    super(string); 
  } 
 
} 