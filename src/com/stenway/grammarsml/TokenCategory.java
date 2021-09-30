package com.stenway.grammarsml;

import java.util.ArrayList;

public class TokenCategory {
	public final String Id;
	public ArrayList<Token> Tokens = new ArrayList<>();
	
	public TokenCategory(String id) {
		Id = id;
	}
}
