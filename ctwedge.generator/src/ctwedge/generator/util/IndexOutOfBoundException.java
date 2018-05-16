package ctwedge.generator.util;

import java.util.Map;

import ctwedge.ctWedge.Parameter;

public class IndexOutOfBoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public IndexOutOfBoundException(int n, Map<Parameter, Integer> offsets) {
		super("index " + n + " not found - current indexes " + offsets);
	}
}
