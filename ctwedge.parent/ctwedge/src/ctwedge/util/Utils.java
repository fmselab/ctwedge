package ctwedge.util;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;

import ctwedge.ctWedge.AndExpression;
import ctwedge.ctWedge.AtomicPredicate;
import ctwedge.ctWedge.Bool;
import ctwedge.ctWedge.CitModel;
import ctwedge.ctWedge.Element;
import ctwedge.ctWedge.Enumerative;
import ctwedge.ctWedge.EqualExpression;
import ctwedge.ctWedge.Expression;
import ctwedge.ctWedge.ImpliesExpression;
import ctwedge.ctWedge.ModMultDiv;
import ctwedge.ctWedge.OrExpression;
import ctwedge.ctWedge.Parameter;
import ctwedge.ctWedge.PlusMinus;
import ctwedge.ctWedge.Range;
import ctwedge.ctWedge.RelationalExpression;



public class Utils {
		
	public enum Type {UNKNOWN_TYPE,RANGE_TYPE, ENUMERATIVE_TYPE,ELEMENT_TYPE,BOOLEAN_TYPE,NUMBER_TYPE, ERROR}
	
	public CitModel my;
	public HashMap<String,Type> params=new HashMap<>();
	public HashMap<String,List<String>> enums=new HashMap<>();
	public HashMap<String,int[]> ranges=new HashMap<>();
	public Set<String> elems = new HashSet<>();
	
	public Utils(CitModel my) {
		this.my=my;		
		for (Parameter p : my.getParameters()) {
			params.put(p.getName(), getType(p));
			if (p instanceof Enumerative) {
				List<String> s = new ArrayList<String>();
				for (Element e : ((Enumerative)p).getElements()) {
					s.add(e.getName());
				}
				elems.addAll(s);
				enums.put(p.getName(), s);
			}
			if (p instanceof Range) {
				ranges.put(p.getName(), new int[] {((Range)p).getBegin(), ((Range)p).getEnd()});
			}
		}
	}
		
	public Type getType(Expression e) {
		System.out.println("Expression "+e+" ... ");
		if (e instanceof AtomicPredicate) {
			if (isNumber(((AtomicPredicate) e).getName())) return Type.NUMBER_TYPE;
			if (((AtomicPredicate) e).getBoolConst()!=null) return Type.BOOLEAN_TYPE;
			return getType(((AtomicPredicate) e).getName());
		}
		if (e.eContents().isEmpty()) return Type.ERROR;
		if (e instanceof ImpliesExpression && ((ImpliesExpression) e).getOp()!=null) return Type.BOOLEAN_TYPE;
		if (e instanceof OrExpression && ((OrExpression) e).getRight()!=null) return Type.BOOLEAN_TYPE;
		if (e instanceof AndExpression && ((AndExpression) e).getRight()!=null) return Type.BOOLEAN_TYPE;
		if (e instanceof EqualExpression && ((EqualExpression) e).getRight()!=null) return Type.BOOLEAN_TYPE;
		if (e instanceof RelationalExpression && ((RelationalExpression) e).getRight()!=null) return Type.BOOLEAN_TYPE;
		if (e instanceof PlusMinus && ((PlusMinus) e).getRight()!=null) return Type.NUMBER_TYPE;
		if (e instanceof ModMultDiv && ((ModMultDiv) e).getRight()!=null) return Type.NUMBER_TYPE;
		//if (e instanceof NotExpression && ((NotExpression) e).getPredicate()!=null) return "Boolean";
		
		for (EObject o : e.eContents()) {
			System.out.println("Content: "+o);
		}
		
		for (EObject o : e.eContents()) if (o instanceof Expression) return getType((Expression)o);
		return Type.ERROR;
	}
	
	// check id str is a number (integer) possibly signed
	public static boolean isNumber(String str) {
		return str.matches("-?\\d+");
	}

	public Type getType(String variableName) {
		for (Parameter p : my.getParameters()) {
			if (variableName.equals(p.getName())) {
				return getType(p);
			}
		}
		if (elems.contains(variableName)) return Type.ELEMENT_TYPE;
		if (isNumber(variableName)) return Type.NUMBER_TYPE;
		return Type.ERROR;
	}
	
	public boolean isUnknownSymbol(String variableName) {
		return getType(variableName)==Type.ERROR || getType(variableName)==Type.UNKNOWN_TYPE;
	}
	
	public Type getType(Parameter p) {
		if (p instanceof Bool) return Type.BOOLEAN_TYPE;
		if (p instanceof Enumerative) return Type.ENUMERATIVE_TYPE;
		if (p instanceof Range) return Type.RANGE_TYPE;
		return Type.UNKNOWN_TYPE;
	}
	
	public boolean belongToEnum(String enumerative, String element) {
		List<String> elements = enums.get(enumerative);
		if (elements==null) return false;
		return elements.contains(element);
	}
	
	
	// --- Copied from CitLab
	
	/**
	 * Method getFactorial.
	 * 
	 * @param n
	 *            int
	 * @return BigInteger
	 */
	public static BigInteger getFactorial(int n) {
		BigInteger fact = BigInteger.ONE;
		for (int i = n; i > 1; i--) {
			fact = fact.multiply(new BigInteger(Integer.toString(i)));
		}
		return fact;
	}
	
	/**
	 * given a list of list of n elements, returns an iterator over all the n-wise combinations of the elements
	 * to be used with pairs if one wants to keep track of variables.
	 *
	 * @param <T> the generic type
	 * @param elements the elements
	 * @return iterator of all the combinations
	 */
	static public <T> Iterator<List<T>> getAllCombinations(List<List<T>> elements) {
		return getAllKWiseCombination(elements, elements.size());
	}

	
	/**
	 * given a list of list of elements, and a k-wise it returns the k-wise combinations of the elements
	 * to be used with pairs if one wants to keep track of variables.
	 *
	 * @param <T> the generic type
	 * @param elements the elements
	 * @param k the k
	 * @return the all k wise combination
	 */
	static public <T> Iterator<List<T>> getAllKWiseCombination(List<List<T>> elements, int k) {
		// build the combinator for the variables
		final CombinationGeneratorList<List<T>> gen = new CombinationGeneratorList<List<T>>(elements, k);

		return new Iterator<List<T>>() {
			// current combination which is iterated
			private Iterator<List<T>> currentCombinationIterator;

			
			public boolean hasNext() {
				return gen.hasNext() || currentCombinationIterator.hasNext();
			}

		
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
	
	/**
	 * given a list of list of variables and their elements, and a k-wise it returns the k-wise combinations of the elements
	 * da covertire la mappa in lista di coppie??? TODO AG
	 * T potrebbero essere anche non omogenei cosa succede??? 
	 * 
	 * @return
	 */
	static public <S,T> Iterator<List<Pair<S,T>>> getAllKWiseCombination(final Map<S,List<T>> varElements, int k) {
		// get the list of variables
		List<S> vars = new ArrayList<S>(varElements.keySet());
		// build all the pairs
		List<List<Pair<S, T>>> problem = new ArrayList<List<Pair<S, T>>>();
		// for var
		for (S var: vars) {
			List<Pair<S, T>> vi = new ArrayList<Pair<S, T>>();
			for (T ele: varElements.get(var)) {
				vi.add(new Pair<S, T>(var, ele));
			}
			problem.add(vi);
		}
		return getAllKWiseCombination(problem, k);
	}
	
}
