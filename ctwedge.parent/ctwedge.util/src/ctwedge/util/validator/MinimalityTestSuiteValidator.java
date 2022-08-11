package ctwedge.util.validator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.emf.common.util.EList;
import org.osgi.service.prefs.Preferences;

import com.sun.jna.Native;
import com.sun.jna.Pointer;

import ctwedge.ctWedge.CitModel;
import ctwedge.ctWedge.Parameter;
import ctwedge.util.Pair;
import ctwedge.util.Test;
import ctwedge.util.TestSuite;

/**
 * Check the minimality and it reduces a test suite if possible
 */
public class MinimalityTestSuiteValidator extends TestSuiteAnalyzer {

	protected MinimalityTestSuiteValidator(TestSuite ts) {
		super(ts);
	}

	/** The Constant logger. */
	private static final Logger logger = Logger.getLogger(MinimalityTestSuiteValidator.class);

	// in the past this class was used as plugin so no constructor could be defined
	// and a set methdo was used instead
	// @Override
	// public void setTestSuite(TestSuite ts) {

	/**
	 * The Class Mycomp.
	 */
	class Mycomp implements Comparator<Map<Parameter, ?>> {

		/** The list map req. */
		private List<Map<Parameter, String>> listMapReq;

		/**
		 * Sets the req.
		 *
		 * @param listMapReq the list map req
		 */
		public void setReq(List<Map<Parameter, String>> listMapReq) {
			this.listMapReq = listMapReq;
		}

		/**
		 * Instantiates a new mycomp.
		 *
		 * @param listMapReq the list map req
		 */
		public Mycomp(List<Map<Parameter, String>> listMapReq) {
			this.listMapReq = listMapReq;
		}

		/**
		 * Compare.
		 *
		 * @param test1 the test 1
		 * @param test2 the test 2
		 * @return the int
		 */
		@Override
		public int compare(Map<Parameter, ?> test1, Map<Parameter, ?> test2) {
			// System.out.println(test1+" \n"+test2);
			Integer size1 = 0, size2 = 0;
			if (!listMapReq.isEmpty()) {
				Iterator<Map<Parameter, String>> iReq = listMapReq.iterator();
				while (iReq.hasNext()) {
					Map<Parameter, ?> actual = iReq.next();

					if (covers(test1, actual)) {
						size1++;
					}
					if (covers(test2, actual)) {
						size2++;
					}
				}
			}
			// System.out.println(size2);
			return size2.compareTo(size1);
		}
	}

	/**
	 * Covers.
	 *
	 * @param test        the test
	 * @param requirement the requirement
	 * @return true, if successful
	 */
	private boolean covers(Map<Parameter, ?> test, Map<Parameter, ?> requirement) {
		int counter = 0;
		for (Parameter p : requirement.keySet()) {
			if (requirement.get(p).equals(test.get(p)))
				counter++;
		}

		return counter == requirement.size();

	}

	/**
	 * Gets the requirements.
	 *
	 * @return the requirements
	 */
	public List<Map<Parameter, String>> getRequirements() {
		CitModel model = ts.getModel();
		Iterator<List<Pair<Parameter, String>>> reqs = ParameterSwitchToPairStrings.getTuples(model, ts.getStrength());

		List<Map<Parameter, String>> ListMapReq = new ArrayList<Map<Parameter, String>>();
		// Trasformo la lista di liste in una map scindendo i pairelement in key
		// e value
		while (reqs.hasNext()) {
			List<Pair<Parameter, String>> req = reqs.next();
			Map<Parameter, String> map = new HashMap<Parameter, String>();
			for (Pair<Parameter, String> e : req) {
				map.put(e.getFirst(), e.getSecond());

			}
			ListMapReq.add(map);

		}

		return ListMapReq;
	}

	/**
	 * Gets the seeds map.
	 *
	 * @return the seeds map
	 */
	private Set<Map<Parameter, String>> getSeedsMap() {
		Set<Map<Parameter, String>> testSuiteSet = new HashSet<Map<Parameter, String>>();
		for(Test t: ts.getTests()) {
			Map<Parameter, String> map = new HashMap<Parameter, String>();
			for(Entry<String, String> assignemnt: t.entrySet()) {
				String paramName = assignemnt.getKey();
				Optional<Parameter> p = ts.getModel().getParameters().stream().filter(x -> x.getName().equals(paramName)).findFirst();
				assert p.isPresent();
				map.put(p.get(), assignemnt.getValue());				
			}
			testSuiteSet.add(map);
		}
		return testSuiteSet;
	}

	/**
	 * Reduce size.
	 *
	 * @return the test suite
	 */
	// It returns a reduced test suite that can be minimal
	public TestSuite reduceSize() {
		ArrayList<Map<Parameter, ?>> testSuiteSet = new ArrayList<Map<Parameter, ?>>();
		testSuiteSet.addAll(getSeedsMap());
		List<Map<Parameter, String>> listMapReq = getRequirements();
		Iterator<Map<Parameter, ?>> iTest = testSuiteSet.iterator();
		Mycomp m = new Mycomp(listMapReq);
		Collections.sort(testSuiteSet, m);
		while (iTest.hasNext()) {
			Map<Parameter, ?> seed = iTest.next();
			int coveredTuples = 0;
			if (!listMapReq.isEmpty()) {
				Iterator<Map<Parameter, String>> iReq = listMapReq.iterator();
				while (iReq.hasNext()) {
					if (covers(seed, iReq.next())) {
						iReq.remove();
						coveredTuples++;
					}
				}
			}
			if (coveredTuples != 0) {
				iTest.remove();
				m.setReq(listMapReq);
				Collections.sort(testSuiteSet, m);
			}
			if (listMapReq.size() <= 0)
				break;
		}
		// create an empty test suite
		TestSuite result = TestSuite.copyAsEmpty(ts);
		// get the old tests
		Set<Map<Parameter, String>> newTestSuite = getSeedsMap();
		newTestSuite.removeAll(testSuiteSet);
		for (Map<Parameter, ?> map : newTestSuite) {
			Test test = new Test();
			for (Entry<Parameter, ?> p : map.entrySet()) {
				test.put(p.getKey().toString(), p.getValue().toString());
			}
			result.getTests().add(test);
		}
		return result;
	}

	/**
	 * Checks if is minimal.
	 *
	 * @return the boolean
	 */
	public Boolean isMinimal() {

		Set<Map<Parameter, String>> testsSet = getSeedsMap();
		List<Map<Parameter, String>> listMapReq = getRequirements();
		// set of tests that cover at least one tp uniqueley
		Set<Map<Parameter, ?>> necessaryTests = new HashSet<>();
		// take one tuple at the time
		for (Map<Parameter, String> currentTuple : listMapReq) {
			// find the set of tests covering currentReq
			Set<Map<Parameter, String>> testsCoveringTp = new HashSet<>();
			for (Map<Parameter, String> test : testsSet) {
				if (covers(test, currentTuple)) {
					testsCoveringTp.add(test);
				}
			}
			// if currentReq is uniquely covered by one test in covTp
			if (testsCoveringTp.size() == 1)
				necessaryTests.addAll(testsCoveringTp);
		}
		return necessaryTests.size() == testsSet.size();
	}
}
