package ctwedge.util;

import java.util.Map;

/** class representing a single test as a map from string (parameters' names) to their values (as strings)**/
public class Test extends Combination {
	
	public Test(Map<String,String> assignments) {
		super(assignments);
	}
	
	public Test() {}

	@Override
	public boolean equals(Object t) {
		Test t1 = (Test) t;
		boolean different = false;
		for (java.util.Map.Entry<String, String> entry : this.entrySet()) {
			for (java.util.Map.Entry<String, String> entry2 : t1.entrySet()) {
				if (!this.get(entry.getKey()).equals(t1.get(entry2.getKey()))) {
					different = true;
				}
			}
			
		}
		return !different;
	}
	
}
