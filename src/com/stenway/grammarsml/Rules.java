package com.stenway.grammarsml;

import java.util.LinkedHashMap;

public class Rules {
	private LinkedHashMap<String, Rule> dictionary = new LinkedHashMap<>();

	public Rules() {
		
	}
	
	public void add(Rule rule) {
		String key = rule.Id.toLowerCase();
		if (dictionary.containsKey(key)) {
			throw new SmlGrammarException("Rule with ID '"+rule.Id+"' already exists");
		}
		dictionary.put(key, rule);
	}
	
	public Rule get(String id) {
		String key = id.toLowerCase();
		if (!dictionary.containsKey(key)) {
			throw new SmlGrammarException("Grammar does not contain a rule with ID '"+id+"'");
		}
		return dictionary.get(key);
	}
	
	public boolean has(String id) {
		String key = id.toLowerCase();
		return dictionary.containsKey(key);
	}
	
	public Rule[] all() {
		return dictionary.values().toArray(new Rule[0]);
	}
}