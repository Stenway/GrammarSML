package com.stenway.grammarsml;

public class CiCategory extends CharsetItem {
	public UnicodeCategory Value;
	
	public CiCategory(UnicodeCategory value) {
		Value = value;
	}

	@Override
	public String toString() {
		return "Category "+Utils.getCategoryCode(Value);
	}
}
