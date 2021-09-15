package com.stenway.grammarsml;

public class RiTokenReference extends RuleItem {
	public Token Token;
	
	public RiTokenReference(Token token) {
		Token = token;
	}

	@Override
	public String toString() {
		return "<" + Token.Id + ">";
	}
}
