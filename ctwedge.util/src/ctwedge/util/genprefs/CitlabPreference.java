package ctwedge.util.genprefs; 
 
import java.util.List; 
import java.util.prefs.Preferences; 
/** 
 *  
 * @author garganti 
 * 
 * @param <T> the kind of preference 
 */ 
public abstract class CitlabPreference<T> { 
  List<T> domain; 
  T myValue; 
  String name; 
  Boolean isEnabled; 
   
  public Boolean getIsEnabled() { 
    return isEnabled; 
  } 
  public void setIsEnabled(Boolean isEnabled) { 
    this.isEnabled = isEnabled; 
   
     
  } 
  public void setDomain(List<T> domain) { 
    this.domain = domain; 
  } 
  public CitlabPreference(String name, List<T> domain, Boolean isEnabled){ 
    this.name=name; 
    this.domain = domain; 
    this.isEnabled=isEnabled; 
  } 
  public final List<T> getDomain(){ 
    return domain; 
  } 
   
  public final T getMyValue(){ 
    return myValue; 
  }  
   
  public String getName() { 
    return name; 
  } 
    public final void setMyValue(T t){ 
      myValue = t; 
    } 
     
    public abstract void setStringAsMyValue(String s); 
     
     
    public void setName(String name) { 
    this.name = name; 
  } 
  public final void storePreference(Preferences prefences, Preferences pE){ 
    storeValue(prefences); 
    storeIsEnabled(pE); 
  } 
  public final void loadPreference(Preferences prefences, Preferences pE){ 
    loadValue(prefences); 
    loadIsEnabled(pE); 
  } 
 
  protected abstract void storeValue(Preferences prefences); 
  protected abstract void loadValue(Preferences prefences);    
   
  protected void storeIsEnabled(Preferences pE){ 
    pE.putBoolean(getName(), isEnabled); 
  } 
  protected void loadIsEnabled(Preferences pE){ 
    setIsEnabled(pE.getBoolean(getName(), false)); 
  } 
} 