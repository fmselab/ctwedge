package ctwedge.util;

import ctwedge.ctWedge.Parameter;

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
	
}
