package ctwedge.generator.smttest.combinations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import ctwedge.ctWedge.CitModel;
import ctwedge.ctWedge.Parameter;
import ctwedge.generator.smttest.util.Pair;
import ctwedge.generator.util.ParameterElementsGetterAsStrings;

// A the type of the parameters
// B the type of the values of the parameters
public class TupleGenerator {

	public static Iterator<List<Pair<Integer, Integer>>> getAllKWiseCombination(CitModel m, int strength) {
		Map<Integer, List<Integer>> map = new HashMap<>();
		int[] bounds = new int[m.getParameters().size()];
		int i=0;
		
		for (Parameter p : m.getParameters()) {
			ArrayList<String> paramValues = new ArrayList<String>(ParameterElementsGetterAsStrings.instance.doSwitch(p));
			bounds[i] = paramValues.size();
		}
		
		for (int param = 0; param < bounds.length; param++) {
			Vector<Integer> values = new Vector<>();
			for (int val = 0; val < bounds[param]; val++) {
				values.add(val);
			}
			map.put(param, values);
		}
		return getAllKWiseCombination(map, strength);
	}

	/**
	 * given a list of list of variables and their elements, and a k-wise it returns
	 * the k-wise combinations of the elements da covertire la mappa in lista di
	 * coppie??? 
	 * 
	 * @return
	 */
	static public <S, T> Iterator<List<Pair<S, T>>> getAllKWiseCombination(final Map<S, List<T>> varElements, int k) {
		// get the list of variables
		List<S> vars = new ArrayList<S>(varElements.keySet());
		// build all the pairs
		List<List<Pair<S, T>>> problem = new ArrayList<List<Pair<S, T>>>();
		// for var
		for (S var : vars) {
			List<Pair<S, T>> vi = new ArrayList<Pair<S, T>>();
			for (T ele : varElements.get(var)) {
				vi.add(new Pair<S, T>(var, ele));
			}
			problem.add(vi);
		}
		return getAllKWiseCombination(problem, k);
	}

	/**
	 * given a list of list of elements, and a k-wise it returns the k-wise
	 * combinations of the elements to be used with pairs if one wants to keep track
	 * of variables.
	 *
	 * @param <T>      the generic type
	 * @param elements the elements
	 * @param k        the k
	 * @return the all k wise combination
	 */
	static public <T> Iterator<List<T>> getAllKWiseCombination(List<List<T>> elements, int k) {
		// build the combinator for the variables
		final CombinationGeneratorList<List<T>> gen = new CombinationGeneratorList<List<T>>(elements, k);

		return new Iterator<List<T>>() {
			// current combination which is iterated
			private Iterator<List<T>> currentCombinationIterator;

			@Override
			public boolean hasNext() {
				return gen.hasNext() || currentCombinationIterator.hasNext();
			}

			@Override
			public List<T> next() {
				if (currentCombinationIterator == null || !currentCombinationIterator.hasNext()) {
					// get the next combination
					List<List<T>> vs = gen.next();
					TupleIterator<T> lE = new TupleIterator<T>(vs);
					currentCombinationIterator = lE;
				}
				return currentCombinationIterator.next();
			}

			@Override
			public void remove() {
				throw new RuntimeException("remove not supported");

			}
		};
	}

}
