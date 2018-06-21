package ctwedge.util;

import java.util.List;

public class Test {
	List<Assignment> assignments;
	
	public Test(List<Assignment> assignments) {
		this.assignments = assignments;
	}

	public List<Assignment> getAssignments() {
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
		for (int i=0; i<assignments.size(); i++) if (! assignments.get(i).equals(t.assignments.get(i))) return false;
		return true;
	}
}
