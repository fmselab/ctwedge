package ctwedge.util;

import java.util.Map;
import java.util.Map.Entry;

public class Test {
	public final Map<String,String> assignments;
	
	public Test(Map<String,String> assignments) {
		this.assignments = assignments;
	}

	public Map<String,String> getAssignments() {
		return assignments;
	}
	
	@Override
	public String toString() {
		return assignments.toString();
	}
	
	/**
	 * NB: we suppose that the assignments in the two tests, are ordered.
	 */
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Test)) return false;
		Test t = (Test)o;
		if (t.assignments.size()!=assignments.size()) return false;
		//for (int i=0; i<assignments.size(); i++) if (! assignments.get(i).equals(t.assignments.get(i))) return false;
		for (Entry<String,String> a : assignments.entrySet()) if (!a.getValue().equals(t.assignments.get(a.getKey()))) return false;
		return true;
	}
}
