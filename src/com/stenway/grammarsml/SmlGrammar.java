package com.stenway.grammarsml;

import com.stenway.sml.SmlDocument;
import java.io.IOException;
import java.util.ArrayList;

public class SmlGrammar {
	public Charsets Charsets = new Charsets();
	public Tokens Tokens = new Tokens();
	public Rules Rules = new Rules();
	public TokenCategories TokenCategories = new TokenCategories();
	public Rule RootRule;
	
	public static SmlGrammar parse(String content) throws IOException {
		SmlDocument document = SmlDocument.parse(content);
		return new SmlGrammarLoader().parse(document);
	}
	
	public static SmlGrammar load(String filePath) throws IOException {
		return new SmlGrammarLoader().load(filePath);
	}
}
