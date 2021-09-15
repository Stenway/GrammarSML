package com.stenway.grammarsml;

import java.util.ArrayList;

public class TiGroup extends TokenItem {
	public final ArrayList<TokenItem> Items = new ArrayList<>();

	@Override
	public String toString() {
		String result = "(";
		boolean isFirst = true;
		for (TokenItem item : Items) {
			result += " " + item.toString();
		}
		return result + " )";
	}
}
