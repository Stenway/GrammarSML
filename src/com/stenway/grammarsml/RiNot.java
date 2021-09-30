package com.stenway.grammarsml;

public class RiNot extends RuleItem {
	public RuleItem Item;
	
	public RiNot(RuleItem item) {
		Item = item;
	}
	
	@Override
	public String toString() {
		return "Not " + Item.toString();
	}
}
