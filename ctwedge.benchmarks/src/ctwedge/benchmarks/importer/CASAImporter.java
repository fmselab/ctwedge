package ctwedge.benchmarks.importer;

import java.util.ArrayList;
import java.util.List;

public class CASAImporter {
	
	List<Integer> offsets = new ArrayList<>();
	List<Integer> values = new ArrayList<>(); // read by the 
	
	public static CASAImporter instance = new CASAImporter();
	private CASAImporter() {}
	
	/*int getParamIndex(int value) {
		for (int i=0; i<offsets.size(); i++) if (offsets.get(i)>value) return i-1;
		if (offsets.get(offsets.size()-1)<=value) return offsets.size()-1;
		return -1; // parameter not found
	}
	
	int getValueIndex(int value) {
		int paramIndex = getParamIndex(value);
		if (paramIndex<0) return -1;
		return value - offsets.get(paramIndex);
	}*/
	
	int getParam(int idx) {
		for (int i=0; i<offsets.size(); i++) {
			if (offsets.get(i)>idx) return i-1;
		}
		return offsets.size()-1;
	}
	
	int getValue(int idx) {
		return idx-offsets.get(getParam(idx));
	}
	
	boolean isBooleanParam(int param) {
		return values.get(param)==2;
	}
	
	/** a simple importer */
	public String importFromCASA(String modelName, String casaModel) {
		offsets.clear(); values.clear();
		StringBuilder sb = new StringBuilder();
		String[] st = casaModel.replace("\n\n","\n").split("\n");
		//int t = Integer.parseInt(st[0]); // the strength of the test suite (useless)
		int nparams = Integer.parseInt(st[1]); // the number of parameters
		int count=0;
		String vals[] = st[2].split(" ");
		sb.append("Model "+modelName+"\n\nParameters:\n");
		for (int i=0; i<nparams; i++) {
			int nvalues = Integer.parseInt(vals[i]);
			offsets.add(count);
			values.add(nvalues);
			count+=nvalues;
			
			// add parameter to sb
			if (nvalues==2) sb.append("p"+(i+1)+": Boolean\n");
			else {
				sb.append("p"+(i+1)+": { ");
				for (int j=1; j<=nvalues; j++) sb.append("v"+j+" ");
				sb.append("}\n");
			}
		}
		
		// add constraints to sb
		int nconstr = Integer.parseInt(st[3]);
		sb.append("\nConstraints:\n");
		for (int i=0; i<nconstr; i++) {
			StringBuilder constraint = new StringBuilder();
			int nterms = Integer.parseInt(st[4+i*2]);
			String[] terms = st[5+i*2].replace("  "," ").split(" ");
			for (int j=0; j<nterms; j++) {
				if (j>0) constraint.append(" or ");
				boolean positive = terms[j*2].equals("+");
				int idx = Integer.parseInt(terms[(j*2)+1]);
				String param = "p"+(getParam(idx)+1);
				int value = getValue(idx)+1;
				if (isBooleanParam(getParam(idx))) {
					if ((value==2 && !positive) || (value==1 && positive)) constraint.append("!");
					constraint.append(param);
				} else {
					constraint.append(param + (positive?"==":"!=") + "v"+value);
				}
				constraint.append("");
			}
			sb.append(" # "+ constraint.toString() +" #\n");
		}
		
		return sb.toString();
	}
}
