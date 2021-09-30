package com.stenway.grammarsml;

import java.util.LinkedHashMap;

public class TokenCategories {
	private LinkedHashMap<String, TokenCategory> dictionary = new LinkedHashMap<>();

	public TokenCategories() {
		
	}
	
	public void add(TokenCategory category) {
		String key = category.Id.toLowerCase();
		if (dictionary.containsKey(key)) {
			throw new SmlGrammarException("Token category with ID '"+category.Id+"' already exists");
		}
		dictionary.put(key, category);
	}
	
	public TokenCategory get(String id) {
		String key = id.toLowerCase();
		if (!dictionary.containsKey(key)) {
			throw new SmlGrammarException("Grammar does not contain a token category with ID '"+id+"'");
		}
		return dictionary.get(key);
	}
	
	public boolean has(String id) {
		String key = id.toLowerCase();
		return dictionary.containsKey(key);
	}
	
	public TokenCategory[] all() {
		return dictionary.values().toArray(new TokenCategory[0]);
	}
}