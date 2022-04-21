package ctwedge.util.genprefs; 
 
import java.util.List;
import java.util.prefs.Preferences; 
 
public class CitlabEnumPreference extends CitlabPreference<String> { 
 
   
    
  public CitlabEnumPreference(String name,List<String> domain) { 
    super(name,domain,Boolean.FALSE );     
  } 
 
  @Override 
  public void storeValue(Preferences prefences) { 
    prefences.put(getName(), getMyValue()); 
  } 
 
  @Override 
  public void loadValue(Preferences prefences) { 
    setMyValue(prefences.get(getName(), getMyValue())); 
  } 
 
  @Override 
  public void setStringAsMyValue(String s) { 
    setMyValue(s); 
  } 
   
} 