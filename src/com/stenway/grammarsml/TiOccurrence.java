package com.stenway.grammarsml;

public class TiOccurrence extends TokenItem {
	public Integer Min;
	public Integer Max;
	public TokenItem Item;
	
	public TiOccurrence(Integer min, Integer max, TokenItem item) {
		Min = min;
		Max = max;
		Item = item;
	}
	
	public boolean isOptional() {
		return Min != null && Max != null && Min == 0 && Max == 1;
	}
	
	public boolean isRepeatPlus() {
		return Min != null && Max == null && Min == 1;
	}
	
	public boolean isRepeatAsterisk() {
		return Min != null && Max == null && Min == 0;
	}
	
	@Override
	public String toString() {
		String prefix = "";
		if (isOptional()) {
			prefix = "Optional";
		} else if (isRepeatPlus()) {
			prefix = "Repeat+";
		} else if (isRepeatAsterisk()) {
			prefix = "Repeat*";
		}
		return prefix + " " + Item.toString();
	}
}
