package ctwedge.util.genprefs; 
 
import java.util.ArrayList; 
import java.util.prefs.Preferences; 
 
public class CitlabPreferncesSet extends ArrayList<CitlabPreference<?>> 
 
{ 
  String nodeName; 
 
  private static final long serialVersionUID = 1L; 
 
  Preferences prefernces; 
 
  Preferences pE; // save the status of each preference (is enabled or 
          // disabled) 
 
  public CitlabPreferncesSet(String nodeName) { 
    this.prefernces = Preferences.userRoot().node(nodeName); 
    this.pE = Preferences.userRoot().node(nodeName + "_ENABLED"); 
  } 
 
  public String getNodeName() { 
    return nodeName; 
  } 
 
  public Preferences getPrefences() { 
    return prefernces; 
  } 
 
  public void loadPrefs() { 
    for (CitlabPreference<?> p : this) { 
      p.loadPreference(this.prefernces, this.pE); 
 
    } 
  } 
 
  public void setNodeName(String nodeName) { 
    this.nodeName = nodeName; 
  } 
 
  /** return a preference given a name */ 
  public CitlabPreference<?> getPreferenceByName(String name) { 
    for (CitlabPreference<?> p : this) { 
      if (p.getName().equals(name)) 
        return p; 
    } 
    return null; 
  } 
 
  public void storePrefs() { 
 
    for (CitlabPreference<?> p : this) { 
      p.storePreference(this.prefernces, this.pE); 
    } 
  } 
 
} 