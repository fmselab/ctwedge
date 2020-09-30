import org.junit.Test;

import ctwedge.generator.pict.PICTGenerator;
import ctwedge.generator.util.Utility;
import ctwedge.util.TestSuite;
import ctwedge.util.validator.TestSuiteValidator;

public class TestSuiteValidatorTest {
	
	@Test
	public void test() {
			
			TestSuite ts = null;
			
			try {
				String model = "Model Concurrency\r\n" + 
						"\r\n" + 
						"Parameters:\r\n" + 
						"p1: { v1 v2 };\r\n" + 
						"p2: { v1 v2 };\r\n" + 
						"p3: { v1 v2 };\r\n" + 
						"p4: Boolean;\r\n" + 
						"p5: Boolean;\r\n" + 
						"\r\n" + 
						"Constraints:\r\n" + 
						"	# ( p3!=v1 OR p2!=v1 OR p5 OR p4 OR p1!=v1) #\r\n" + 
						"	# ( p1!=v2 OR p5!=true) #\r\n" + 
						"	# ( p2!=v1 OR p5 OR p4!=true OR p3!=v2 OR p1!=v1) #\r\n" + 
						"	# ( p5!=true OR p2!=v2) #\r\n" + 
						"	# ( p4 OR p3!=v2 OR p1!=v1) #\r\n" + 
						"	# ( p4!=true OR p1!=v2) #\r\n" + 
						"	# ( p3!=v1 OR p4!=true) #";
				PICTGenerator generator = new PICTGenerator();
				ts = generator.getTestSuite(Utility.loadModel(model), 2, false);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			TestSuiteValidator tsv = new TestSuiteValidator();
			tsv.setTestSuite(ts);
			System.out.println(tsv.howManyTestAreValid());
			System.out.println(tsv.howManyTuplesCovers());
		}
}