package com.stenway.grammarsml;

public class Rule {
	public final String Id;
	public final RiGroup Group = new RiGroup();
	
	public Rule(String id) {
		Id = id;
	}

	@Override
	public String toString() {
		return "<" + Id + "> " + Group.toString();
	}
}
