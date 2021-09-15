package com.stenway.grammarsml;

import java.util.LinkedHashMap;

public class Tokens {
	private LinkedHashMap<String, Token> dictionary = new LinkedHashMap<>();

	public Tokens() {
		
	}
	
	public void add(Token rule) {
		String key = rule.Id.toLowerCase();
		if (dictionary.containsKey(key)) {
			throw new SmlGrammarException("Token with ID '"+rule.Id+"' already exists");
		}
		dictionary.put(key, rule);
	}
	
	public Token get(String id) {
		String key = id.toLowerCase();
		if (!dictionary.containsKey(key)) {
			throw new SmlGrammarException("Grammar does not contain a token with ID '"+id+"'");
		}
		return dictionary.get(key);
	}
	
	public boolean has(String id) {
		String key = id.toLowerCase();
		return dictionary.containsKey(key);
	}
	
	public Token[] all() {
		return dictionary.values().toArray(new Token[0]);
	}
}
