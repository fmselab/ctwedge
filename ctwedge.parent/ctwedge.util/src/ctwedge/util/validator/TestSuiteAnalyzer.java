package ctwedge.util.validator;

import ctwedge.util.TestSuite;

/**
 * The Class TestSuiteAnalyzer perfoms the analtysis and returns a result of type T
 *
 * @param <T> the generic type
 */
public class TestSuiteAnalyzer<T> {
	
	
	protected TestSuite ts;

	protected TestSuiteAnalyzer(TestSuite ts) {
		this.ts = ts;
	}

}