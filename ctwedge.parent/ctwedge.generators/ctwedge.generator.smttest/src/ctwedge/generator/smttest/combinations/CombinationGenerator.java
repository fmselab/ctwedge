package ctwedge.generator.smttest.combinations;

import java.math.BigInteger;
import java.util.Iterator;

/**
 * Generator of combinations of numbers without repetitions. Adapted from the
 * code by Michael Gilleland http://www.merriampark.com/comb.htm
 * 
 * It is an iterator of int arrays, where every element is a combination
 */
final class CombinationGenerator implements Iterator<int[]> {

	private int[] a;
	private int n;
	private int r;
	private BigInteger numLeft;
	private BigInteger total;

	/**
	 * generates the r-combinations of n numbers, without repetition
	 * 
	 * @param n int
	 * @param r int
	 */
	public CombinationGenerator(int n, int r) {
		if (r > n) {
			throw new IllegalArgumentException();
		}
		if (n < 1) {
			throw new IllegalArgumentException();
		}
		this.n = n;
		this.r = r;
		a = new int[r];
		BigInteger nFact = getFactorial(n);
		BigInteger rFact = getFactorial(r);
		BigInteger nminusrFact = getFactorial(n - r);
		total = nFact.divide(rFact.multiply(nminusrFact));// total = n ! / (r !
															// * (n -r ) !)
		reset();
	}

	// ------
	// Reset
	// ------

	private void reset() {
		for (int i = 0; i < a.length; i++) {
			a[i] = i;
		}
		numLeft = new BigInteger(total.toString());
	}

	// ------------------------------------------------
	// Return number of combinations not yet generated
	// ------------------------------------------------

	/**
	 * Method getNumLeft.
	 * 
	 * @return BigInteger
	 */
	public BigInteger getNumLeft() {
		return numLeft;
	}

	// -----------------------------
	// Are there more combinations?
	// -----------------------------
	/**
	 * Method hasNext.
	 * 
	 * @return boolean
	 * @see java.util.Iterator#hasNext()
	 */

	@Override
	public boolean hasNext() {
		return numLeft.compareTo(BigInteger.ZERO) == 1;
	}

	// ------------------------------------
	// Return total number of combinations
	// ------------------------------------

	/**
	 * Method getTotal.
	 * 
	 * @return BigInteger
	 */
	public BigInteger getTotal() {
		return total;
	}

	// --------------------------------------------------------
	// Generate next combination (algorithm from Rosen p. 286)
	// --------------------------------------------------------
	/**
	 * Method next.
	 * 
	 * @return int[]
	 * @see java.util.Iterator#next()
	 */

	@Override
	public int[] next() {

		if (numLeft.equals(total)) {
			numLeft = numLeft.subtract(BigInteger.ONE);
			return a;
		}

		int i = r - 1;
		while (a[i] == n - r + i) {
			i--;
		}
		a[i] = a[i] + 1;
		for (int j = i + 1; j < r; j++) {
			a[j] = a[i] + j - i;
		}

		numLeft = numLeft.subtract(BigInteger.ONE);
		return a;

	}

	/**
	 * Method remove.
	 * 
	 * @see java.util.Iterator#remove()
	 */
	@Override
	public void remove() {
		throw new RuntimeException("remove not supported");
	}

	/**
	 * Method getFactorial.
	 * 
	 * @param n int
	 * @return BigInteger
	 */
	public static BigInteger getFactorial(int n) {
		BigInteger fact = BigInteger.ONE;
		for (int i = n; i > 1; i--) {
			fact = fact.multiply(new BigInteger(Integer.toString(i)));
		}
		return fact;
	}
}
