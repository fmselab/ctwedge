package ctwedge.util.validator;

import org.junit.Test;

public class CTWedgeVTest {

	@Test
	public void test() {
		CTWedgeV.main(new String[] { "--m", "/Users/andrea/Desktop/model.ctw", 
				"--ts", "/Users/andrea/Desktop/ts.csv",
				"--t", "2", "--min", "--val", "--com" });
	}

}
