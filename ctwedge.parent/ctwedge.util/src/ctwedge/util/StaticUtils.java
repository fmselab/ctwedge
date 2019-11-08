package ctwedge.util;

public class StaticUtils {
	public static final StaticUtils INSTANCE = new StaticUtils();
	
	StaticUtils() {}
	
	/** check id str is a number (integer) possibly signed */
	public boolean isInteger(String str) {
		return str.matches("-?\\d+");
	}
	
	public boolean isNumber(String s) {
		return s.matches("(-|\\+)?[0-9]+(\\.[0-9]+)?"); 
	}

}
