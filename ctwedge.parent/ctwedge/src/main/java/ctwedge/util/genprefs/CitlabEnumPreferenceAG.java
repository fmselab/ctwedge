package ctwedge.util.genprefs; 
 
import java.util.List; 
import java.util.prefs.Preferences; 
 
public class CitlabEnumPreferenceAG<T extends Enum<T>> extends CitlabPreference<T> { 
 
   
    
  public CitlabEnumPreferenceAG(String name,List<T> domain) { 
    super(name,domain,Boolean.FALSE); 
  } 
 
 
  @Override 
  public void setStringAsMyValue(String s) { 
    // TODO Auto-generated method stub 
     
  } 
 
 
  @Override 
  protected void storeValue(Preferences prefences) { 
    // TODO Auto-generated method stub 
     
  } 
 
 
  @Override 
  protected void loadValue(Preferences prefences) { 
    // TODO Auto-generated method stub 
     
  } 
 
} 