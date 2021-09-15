package com.stenway.grammarsml;

public class CiChar extends CharsetItem {
	public int Value;
	
	public CiChar(int value) {
		Value = value;
	}

	@Override
	public String toString() {
		return "Char " + Utils.escapeChar(Value);
	}
}
