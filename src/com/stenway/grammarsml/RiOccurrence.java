package com.stenway.grammarsml;

public class RiOccurrence extends RuleItem {
	public Integer Min;
	public Integer Max;
	public RuleItem Item;
	
	public RiOccurrence(Integer min, Integer max, RuleItem item) {
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
