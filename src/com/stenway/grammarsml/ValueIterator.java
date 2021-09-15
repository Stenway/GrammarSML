package com.stenway.grammarsml;

import com.stenway.sml.SmlAttribute;

public class ValueIterator {
	String[] values;
	int index = 0;
	
	public ValueIterator(SmlAttribute attribute) {
		values = attribute.getValues();
	}
	
	public boolean hasValue() {
		return index < values.length;
	}
	
	public String LastReference;
	
	public boolean tryReadReference() {
		if (!hasValue()) {
			return false;
		}
		String curValue = values[index];
		if (curValue == null || !curValue.startsWith("<") || !curValue.endsWith(">")) {
			return false;
		}
		LastReference = curValue.substring(1, curValue.length()-1);
		index++;
		
		return true;
	}
	
	public boolean tryRead(String value) {
		if (!hasValue()) {
			return false;
		}
		String curValue = values[index];
		if (curValue == null || !curValue.equalsIgnoreCase(value)) {
			return false;
		}
		index++;
		return true;
	}
	
	public String get() {
		if (!hasValue()) {
			throw new SmlGrammarException("No value available");
		}
		String value = values[index];
		if (value == null) {
			throw new SmlGrammarException("Null value not allowed");
		}
		index++;
		return value;
	}

	@Override
	public String toString() {
		if (hasValue()) {
			return values[index];
		} else {
			return "";
		}
	}
}
