package com.stenway.grammarsml;

import java.util.ArrayList;

public class Charset {
	public final String Id;
	public final ArrayList<CharsetItem> Included = new ArrayList<>();
	public final ArrayList<CharsetItem> Excluded = new ArrayList<>();
	
	public Charset(String id) {
		Id = id;
	}

	@Override
	public String toString() {
		String result = "";
		boolean isFirst = true;
		for (CharsetItem item : Included) {
			if (isFirst) {
				isFirst = false;
			} else {
				result += " ";
			}
			result += item.toString();
		}
		if (!Excluded.isEmpty()) {
			result += " Except";
			for (CharsetItem item : Excluded) {
				result += item.toString();
			}
		}
		return result;
	}
}
