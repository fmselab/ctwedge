package ctwedge.util;

import ctwedge.ctWedge.Parameter;

@Deprecated
public class Assignment {
	Parameter parameter;
	String value;
	
	public Assignment(Parameter parameter, String value) {
		super();
		this.parameter = parameter;
		this.value = value;
	}
	public Parameter getParameter() {
		return parameter;
	}
	public void setParameter(Parameter parameter) {
		this.parameter = parameter;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return parameter.getName()+":"+value;
	}
	
	/** It is very important to detect equal combinations */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Assignment)) return false;
//		System.out.print("C");
		return toString().equals(((Assignment)obj).toString());
	}
	
}
