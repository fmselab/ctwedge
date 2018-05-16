package ctwedge.util.genprefs; 
 
import java.util.prefs.Preferences; 
 
public class CitlabIntPreference extends CitlabPreference<Integer> { 
   
 
  public CitlabIntPreference(String name) { 
    super(name,null,Boolean.FALSE); 
  } 
 
  @Override 
  public void storeValue(Preferences prefences) {     
    prefences.putInt(getName(), getMyValue()); 
  } 
 
  @Override 
  public void loadValue(Preferences prefences) {     
    setMyValue(prefences.getInt(getName(), getMyValue())); 
  } 
 
  @Override 
  public void setStringAsMyValue(String s) {   
    try { 
      setMyValue(Integer.valueOf(s)); 
    } catch (NumberFormatException e) { 
      e.printStackTrace(); 
    }     
  }   
 
} 