package com.stenway.grammarsml;

import java.util.ArrayList;

public class RiGroup extends RuleItem {
	public final ArrayList<RuleItem> Items = new ArrayList<>();

	@Override
	public String toString() {
		String result = "(";
		boolean isFirst = true;
		for (RuleItem item : Items) {
			result += " " + item.toString();
		}
		return result + " )";
	}
}