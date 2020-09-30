package ctwedge.util.validator;

import java.util.ArrayList;
import ctwedge.ctWedge.*;

//Extracts all subsets of the given set
	/*
	 * Remarks: 
	 * This method uses a recursive algorithm to find the subsets.
	 * This is the algorithm: suppose we want to extract the subsets of
	 * 
	 * A = {a, b, c, ...}
	 * 
	 * First we separate the first element from A:
	 * 
	 * first-element: a
	 * B = {b, c, ...}
	 * 
	 * Now we use this recursive law:
	 * 
	 * The subsets of A are the collection of subsets of B,
	 * plus the collection of subsets of B once again, but this time
	 * the first element a is added to these subset:
	 * Subsets-Of (A) = Subsets-Of (B) + ({a} + Subsets-Of (B))
	 * 
	 * For example, if A has only two members:
	 * 
	 * A = {a, b}
	 * 
	 * then
	 * 
	 * first-element = a
	 * B = {b}.
	 * 
	 * The subsets of B:
	 * {}, {b}
	 * 
	 * The subsets of B, a added:
	 * {a}, {a, b}
	 * 
	 * Now the whole collection is
	 * Subsets of A: {}, {b}, {a}, {a, b}
	 * If the set is an empty set {}, this function
	 * returns the only subset: {}, and recursion terminates here.
	 * The input of this method, set, is an ArrayList<Integer>.
	 * It could be an ArrayList of any type because we have not 
	 * done any type specific operation on the elements.
	 * It could also be the generic type, E.
	 * 
	 * The output is an ArrayList of sets, That is the elements
	 * of ArrayList are ArrayList, themselves: ArrayList <ArrayList <E>>
	 * 
	 * */
public class SubSetMaker {
	public  static <T extends Constraint> ArrayList<ArrayList<T>> getSubsets(
			ArrayList<T> set) {

		ArrayList<ArrayList<T>> subsetCollection = new ArrayList<ArrayList<T>>();

		if (set.size() == 0) {
			subsetCollection.add(new ArrayList<T>());
		} else {
			ArrayList<T> reducedSet = new ArrayList<T>();

			reducedSet.addAll(set);

			T first = reducedSet.remove(0);
			ArrayList<ArrayList<T>> subsets = getSubsets(reducedSet);
			subsetCollection.addAll(subsets);

			subsets = getSubsets(reducedSet);

			for (ArrayList<T> subset : subsets) {
				subset.add(0, first);
			}
          
			subsetCollection.addAll(subsets);
		}
  
		return subsetCollection;
	}

}
