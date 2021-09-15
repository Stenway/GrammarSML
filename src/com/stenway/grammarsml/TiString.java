package com.stenway.grammarsml;

public class TiString extends TokenItem {
	public String Value;
	public boolean IgnoreCase;
	
	public TiString(String value, boolean ignoreCase) {
		Value = value;
		IgnoreCase = ignoreCase;
	}

	@Override
	public String toString() {
		if (Value.length() == 0) {
			return "Empty";
		}
		String prefix = "String";
		if (IgnoreCase) {
			prefix = "CiString";
		}
		return prefix + " " + Utils.escapeValue(Value);
	}
}
