package com.stenway.grammarsml;

public class CiPreset extends CharsetItem {
	public int Value;
	
	public static final int Letter = 0;
	public static final int Digit = 1;
	public static final int Number = 2;
	
	public CiPreset(int value) {
		Value = value;
	}

	@Override
	public String toString() {
		if (Value == Letter) {
			return "Letter";
		} else if (Value == Digit) {
			return "Digit";
		} else if (Value == Number) {
			return "Number";
		}
		throw new IllegalStateException();
	}
}
