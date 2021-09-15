package com.stenway.grammarsml;

import java.util.LinkedHashMap;

public class Charsets {
	private LinkedHashMap<String, Charset> dictionary = new LinkedHashMap<>();

	public Charsets() {
		
	}
	
	public void add(Charset charset) {
		String key = charset.Id.toLowerCase();
		if (dictionary.containsKey(key)) {
			throw new SmlGrammarException("Charset with ID '"+charset.Id+"' already exists");
		}
		dictionary.put(key, charset);
	}
	
	public Charset get(String id) {
		String key = id.toLowerCase();
		if (!dictionary.containsKey(key)) {
			throw new SmlGrammarException("Grammar does not contain a charset with ID '"+id+"'");
		}
		return dictionary.get(key);
	}
	
	public boolean has(String id) {
		String key = id.toLowerCase();
		return dictionary.containsKey(key);
	}
	
	public Charset[] all() {
		return dictionary.values().toArray(new Charset[0]);
	}
}
	
