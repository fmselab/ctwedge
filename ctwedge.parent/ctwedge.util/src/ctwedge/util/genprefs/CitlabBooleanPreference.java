package ctwedge.util.genprefs; 
 
import java.util.Arrays;
import java.util.prefs.Preferences; 
 
public class CitlabBooleanPreference extends CitlabPreference<Boolean> { 
 
  public CitlabBooleanPreference(String name) { 
    super(name, Arrays.asList(Boolean.TRUE, Boolean.FALSE),Boolean.FALSE); 
  } 
 
  @Override 
  public void setStringAsMyValue(String t) { 
    setMyValue(Boolean.valueOf(t)); 
  } 
 
  @Override 
  public void storeValue(Preferences prefences) {     
    prefences.putBoolean(getName(), getMyValue()); 
     
  } 
 
  @Override 
  public void loadValue( Preferences prefences) {     
    setMyValue(prefences.getBoolean(getName(), getMyValue())); 
  } 
 
} 