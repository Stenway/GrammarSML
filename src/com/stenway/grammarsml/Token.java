package com.stenway.grammarsml;

public class Token {
	public final String Id;
	public final TiGroup Group = new TiGroup();
	
	public Token(String id) {
		Id = id;
	}

	@Override
	public String toString() {
		return "<" + Id + "> " + Group.toString();
	}
}
