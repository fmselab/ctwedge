package ctwedge.util;

/**
 * Copied from CitLab
 * @author marcoradavelli
 *
 * @param <S>
 * @param <T>
 */
public class Pair<S,T>{
	
	private S first;
	private T second;
	
	public Pair(S s, T a) {
		first = s; second = a;
	}
	
	@Override
	public String toString() {
		return "[" + first.toString()+ ", "+ second.toString() + "]";
	}

	public S getFirst() {
		return first;
	}

	public T getSecond() {
		return second;
	}	
}