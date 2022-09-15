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
import java.util.stream.Collectors;

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

	public MinimalityTestSuiteValidator(TestSuite ts) {
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
	class CoversMore implements Comparator<Map<Parameter, ?>> {

		/** The list map req. */
		private List<Map<Parameter, String>> listMapReq;

		/**
		 * Instantiates a new mycomp.
		 *
		 * @param listMapReq the list map req
		 */
		public CoversMore(List<Map<Parameter, String>> listMapReq) {
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
			int size1 = 0, size2 = 0;
			assert (!listMapReq.isEmpty());
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
			//System.out.println(size1 + " vs " + size2);
			return Integer.compare(size1, size2);
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
		assert ts.getStrength() > 0;
		return getRequirements(model, ts.getStrength());
	}

	/**
	 * 
	 * @param model
	 * @param strength 
	 * @return
	 */
	static List<Map<Parameter, String>> getRequirements(CitModel model, int strength) {
		Iterator<List<Pair<Parameter, String>>> reqs = ParameterSwitchToPairStrings.getTuples(model, strength);
		List<Map<Parameter, String>> ListMapReq = new ArrayList<Map<Parameter, String>>();
		// Trasformo la lista di liste in una map scindendo i pairelement in key
		// e value
		while (reqs.hasNext()) {
			List<Pair<Parameter, String>> req = reqs.next();
			Map<Parameter, String> map = new HashMap<Parameter, String>();
			for (Pair<Parameter, String> e : req) {
				map.put(e.getFirst(), e.getSecond());
				
			}
			if (logger.isDebugEnabled())
				logger.debug("adding tuple " + tupleToString(map));
			ListMapReq.add(map);
		}
		return ListMapReq;
	}

	private static List<String> tupleToString(Map<Parameter, ?> map) {
		return Collections.singletonList(""); //map.entrySet().stream().map(x -> (x.getKey().getName() + "=" + x.getValue())).collect(Collectors.toList());
	}

	/**
	 * it transforms the test suite a a set of maps.
	 *
	 * @return the seeds map
	 */
	private Set<Map<Parameter, String>> getSeedsMap() {
		Set<Map<Parameter, String>> testSuiteSet = new HashSet<Map<Parameter, String>>();
		for (Test t : ts.getTests()) {
			Map<Parameter, String> map = new HashMap<Parameter, String>();
			for (Entry<String, String> assignemnt : t.entrySet()) {
				String paramName = assignemnt.getKey();
				Optional<Parameter> p = ts.getModel().getParameters().stream()
						.filter(x -> x.getName().equals(paramName)).findFirst();
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
	 * @return It returns a reduced test suite that is one of the minimal ones
	 */
	public TestSuite reduceSize() {
		// create a copy that can be modified
		List<Map<Parameter, ?>> testSuiteSet = new ArrayList<Map<Parameter, ?>>();
		testSuiteSet.addAll(getSeedsMap());
		logger.debug("consider all the tests " + testSuiteSet.size());
		List<Map<Parameter, ?>> reducedTS = new ArrayList<Map<Parameter, ?>>();
		// get all the requirements
		List<Map<Parameter, String>> listMapReq = getRequirements();
		CoversMore m = new CoversMore(listMapReq);
		// till al the requirements are not covered by a test
		while (!listMapReq.isEmpty()) {
			if (testSuiteSet.isEmpty()) {
				logger.debug("no more tests");
				break;
			}
			logger.debug("taking the best test that sovers more -  number of requirements " + listMapReq.size());
			// take the best one (that covering most
			Map<Parameter, ?> best = Collections.max(testSuiteSet, m);
			testSuiteSet.remove(best);
			// count and mark as covered all that are covered by best
			int coveredTuples = 0;
			Iterator<Map<Parameter, String>> iReq = listMapReq.iterator();
			while (iReq.hasNext()) {
				Map<Parameter, String> next = iReq.next();
				if (covers(best, next)) {
					logger.debug("this requirement is covered:" + tupleToString(next) + " remove it");
					iReq.remove();
					coveredTuples++;
				}
			}
			if (coveredTuples == 0) {
				logger.debug("all the coverable requirements seem covered, exit - not covered tuples: " + listMapReq.size());
				break;
			}
			assert coveredTuples > 0;
			reducedTS.add(best);
			logger.debug("this test is necessary (" + tupleToString(best) + ") - covers " + coveredTuples);
		}
		logger.debug("all necessary tests are removed  - useless tests remaining: " + testSuiteSet.size());
		// create an empty test suite
		return new TestSuite(ts.getModel(),reducedTS);
//		// get the old tests
//		Set<Map<Parameter, String>> newTestSuite = getSeedsMap();
//		newTestSuite.removeAll(testSuiteSet);
//		for (Map<Parameter, ?> map : newTestSuite) {
//			Test test = new Test();
//			for (Entry<Parameter, ?> p : map.entrySet()) {
//				test.put(p.getKey().toString(), p.getValue().toString());
//			}
//			result.getTests().add(test);
//		}
	}

	/**
	 * Checks if is minimal. It is minimal if each test requirement is covered by one test
	 *
	 * @return the boolean
	 */
	public Boolean isMinimal() {
		Set<Map<Parameter, String>> testsSet = getSeedsMap();
		// System.out.println(testsSet);
		List<Map<Parameter, String>> listMapReq = getRequirements();
		// System.out.println(listMapReq);
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
		// the necesaary could be not the minimal set
		System.out.println("sufficent tests " + necessaryTests.size() + " actual size " + testsSet.size());
		return necessaryTests.size() == testsSet.size();
	}
}
