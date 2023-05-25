package ctwedge.util;

import ctwedge.ctWedge.Bool;
import ctwedge.ctWedge.Enumerative;
import ctwedge.ctWedge.Range;
import ctwedge.ctWedge.util.CtWedgeSwitch;

/**
 * Given a parameter returns the number of values it can take
 * 
 * @author garganti
 * 
 */
public class ParameterSize extends CtWedgeSwitch<Integer> {

	public static final ParameterSize eInstance = new ParameterSize();

	private ParameterSize() {
	}

	@Override
	public Integer caseBool(Bool object) {
		return 2;
	}

	@Override
	public Integer caseEnumerative(Enumerative enume) {
		return enume.getElements().size();
	}

	@Override
	public Integer caseRange(Range r) {
		int delta = r.getEnd() - r.getBegin();
		if (r.getStep() != 0)
			return delta / r.getStep() + 1;
		else
			return delta + 1;
	}

}
