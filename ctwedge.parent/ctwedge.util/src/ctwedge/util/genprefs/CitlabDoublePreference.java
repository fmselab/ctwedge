package ctwedge.util.genprefs; 
 
import java.util.prefs.Preferences; 
 
public class CitlabDoublePreference extends CitlabPreference<Double> { 
   
 
  public CitlabDoublePreference(String name) { 
    super(name,null,Boolean.FALSE); 
  } 
 
  @Override 
  public void storeValue(Preferences prefences) {     
    prefences.putDouble(getName(), getMyValue()); 
  } 
 
  @Override 
  public void loadValue(Preferences prefences) {     
    setMyValue(prefences.getDouble(getName(), getMyValue())); 
  } 
 
  @Override 
  public void setStringAsMyValue(String s) {   
    try { 
      setMyValue(Double.valueOf(s)); 
    } catch (NumberFormatException e) { 
      e.printStackTrace(); 
    }     
  }   
 
} 