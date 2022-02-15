package ctwedge.generator.smttest.combinations;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * computes the list of combinations of the elements given in the list. Every
 * combination is a list of the original elements
 * 
 * @author garganti
 * 
 * @param <T>
 */
public class CombinationGeneratorList<T> implements Iterator<List<T>>, Iterable<List<T>> {

	private CombinationGenerator generator;

	private List<T> elements;

	/**
	 * the number of elements must be >= n and n >= 1 otherwise throws an exception
	 */
	public CombinationGeneratorList(List<T> elements, int n) {
		generator = new CombinationGenerator(elements.size(), n);
		this.elements = elements;
	}

	@Override
	public List<T> next() {
		int[] nextComb = generator.next();
		List<T> result = new ArrayList<T>();
		for (int i : nextComb)
			result.add(elements.get(i));
		return result;
	}

	@Override
	public boolean hasNext() {
		return generator.hasNext();
	}

	@Override
	public void remove() {
		throw new RuntimeException("not supported");
	}

	@Override
	public Iterator<List<T>> iterator() {
		return this;
	}
	
	
	
}