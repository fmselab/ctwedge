package ctwedge.util;

import java.util.Map;

/** class representing a single test as a map from string (paramters) to their values (as strings)**/
public class Test extends Combination {
	
	public Test(Map<String,String> assignments) {
		super(assignments);
	}
	
	public Test() {}
	
}
