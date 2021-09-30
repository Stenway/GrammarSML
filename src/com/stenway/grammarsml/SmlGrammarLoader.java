package com.stenway.grammarsml;

import com.stenway.sml.SmlAttribute;
import com.stenway.sml.SmlDocument;
import com.stenway.sml.SmlElement;
import com.stenway.sml.SmlNode;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class SmlGrammarLoader {
	SmlGrammar grammar;
	
	private CiChar getChar(ValueIterator values) {
		String value = values.get();
		int[] codePoints = value.codePoints().toArray();
		int charCodePoint = 0;
		if (codePoints.length == 1) {
			charCodePoint = codePoints[0];
		} else if (codePoints.length == 4 || codePoints.length == 6) {
			charCodePoint = Integer.parseInt(value, 16);
			if (charCodePoint < 0 || charCodePoint > 0x10FFFF) {
				throw new SmlGrammarException("Char '"+value+"' is out of range.");
			}
			if (charCodePoint >= 0xD800 && charCodePoint <= 0xDFFF) {
				throw new SmlGrammarException("Char '"+value+"' is a not allowed surrogate code point.");
			}
		} else {
			throw new SmlGrammarException("Char '"+value+"' is invalid.");
		}
		return new CiChar(charCodePoint);
	}
	
	private void loadCharset(SmlAttribute charsetAttribute, Charset charset) {
		ValueIterator values = new ValueIterator(charsetAttribute);
		boolean isExcept = false;
		while (true) {
			CharsetItem item = null;
			if (!values.hasValue()) {
				break;
			} else if (values.tryReadReference()) {
				String referencedCharsetId = values.LastReference;
				Charset referencedCharset = grammar.Charsets.get(referencedCharsetId);
				item = new CiCharsetReference(referencedCharset);
			} else if (values.tryRead("Except")) {
				if (isExcept || charset.Included.isEmpty()) {
					throw new SmlGrammarException("Invalid except");
				}
				isExcept = true;
				continue;
			} else if (values.tryRead("Range")) {
				CiChar fromChar = getChar(values);
				CiChar toChar = getChar(values);
				item = new CiRange(fromChar, toChar);
			} else if (values.tryRead("Any")) {
				item = new CiAny();
			} else if (values.tryRead("Letter")) {
				item = new CiPreset(CiPreset.Letter);
			} else if (values.tryRead("Digit")) {
				item = new CiPreset(CiPreset.Digit);
			} else if (values.tryRead("Category")) {
				String categoryName = values.get();
				UnicodeCategory category = Utils.getCategory(categoryName);
				item = new CiCategory(category);
			} else {
				item = getChar(values);
			}
			if (isExcept) {
				charset.Excluded.add(item);
			} else {
				charset.Included.add(item);
			}
		}
	}
	
	private void loadCharsets(SmlElement rootElement) {
		for (SmlElement charsElement : rootElement.elements("Chars")) {
			for (SmlAttribute charsetAttribute : charsElement.attributes()) {
				String id = getId(charsetAttribute.getName());
				Charset charset = grammar.Charsets.get(id);
				loadCharset(charsetAttribute, charset);
			}
		}
	}
	
	private void preloadCharsets(SmlElement rootElement) {
		for (SmlElement charsElement : rootElement.elements("Chars")) {
			for (SmlAttribute charsetAttribute : charsElement.attributes()) {
				String id = getId(charsetAttribute.getName());
				Charset charset = new Charset(id);
				grammar.Charsets.add(charset);
			}
		}
	}
	
	private TokenItem loadTokenItem(ValueIterator values) {
		if (!values.hasValue()) {
			throw new SmlGrammarException("Token value expected");
		} else if (values.tryRead("Optional")) {
			TokenItem item = loadTokenItem(values);
			return new TiOccurrence(0, 1, item);
		} else if (values.tryRead("Repeat+")) {
			TokenItem item = loadTokenItem(values);
			return new TiOccurrence(1, null, item);
		} else if (values.tryRead("Repeat*")) {
			TokenItem item = loadTokenItem(values);
			return new TiOccurrence(0, null, item);
		} else if (values.tryRead("Empty")) {
			return new TiString("", false);
		} else if (values.tryRead("String")) {
			String value = values.get();
			return new TiString(value, false);
		} else if (values.tryRead("CiString")) {
			String value = values.get();
			return new TiString(value, true);
		} else if (values.tryRead("(")) {
			TiGroup group = new TiGroup();
			loadTokenGroup(values, group, true);
			return group;
		} else if (values.tryRead("[")) {
			TiAlternative alternative = new TiAlternative();
			loadTokenAlternative(values, alternative);
			return alternative;
		} else if (values.tryReadReference()) {
			String referencedId = values.LastReference;
			Charset referencedCharset = grammar.Charsets.get(referencedId);
			return new TiCharsetReference(referencedCharset);
		}
		throw new SmlGrammarException("Invalid token value "+values.get());
	}
	
	private void loadTokenGroup(ValueIterator values, TiGroup group, boolean needsClosingParenthesis) {
		while (true) {
			if (!values.hasValue()) {
				if (needsClosingParenthesis) {
					throw new SmlGrammarException("Group not closed");
				}
				break;
			} else if (values.tryRead(")")) {
				if (!needsClosingParenthesis) {
					throw new SmlGrammarException("Invalid group closing parenthesis");
				}
				break;
			}
			TokenItem item = loadTokenItem(values);
			group.Items.add(item);
		}
	}
	
	private void loadTokenAlternative(ValueIterator values, TiAlternative alternative) {
		while (true) {
			if (!values.hasValue()) {
				throw new SmlGrammarException("Alternative not closed");
			} else if (values.tryRead("]")) {
				break;
			}
			TokenItem item = loadTokenItem(values);
			alternative.Items.add(item);
		}
	}
	
	private void loadToken(SmlAttribute tokenAttribute, Token token) {
		ValueIterator values = new ValueIterator(tokenAttribute);
		loadTokenGroup(values, token.Group, false);
	}
	
	private void loadTokens(SmlElement rootElement) {
		for (SmlElement tokensElement : rootElement.elements("Tokens")) {
			for (SmlAttribute tokenAttribute : tokensElement.attributes()) {
				String id = getId(tokenAttribute.getName());
				Token token = grammar.Tokens.get(id);
				loadToken(tokenAttribute, token);
			}
		}
	}
	
	private void preloadTokens(SmlElement rootElement) {
		for (SmlElement tokensElement : rootElement.elements("Tokens")) {
			for (SmlAttribute tokenAttribute : tokensElement.attributes()) {
				String id = getId(tokenAttribute.getName());
				if (grammar.Charsets.has(id)) {
					throw new SmlGrammarException("Token and charset with same id '"+id+"' not allowed");
				}
				Token token = new Token(id);
				grammar.Tokens.add(token);
			}
		}
	}
	
	private RuleItem loadRuleItem(ValueIterator values) {
		if (!values.hasValue()) {
			throw new SmlGrammarException("Rule value expected");
		} else if (values.tryRead("Optional")) {
			RuleItem item = loadRuleItem(values);
			return new RiOccurrence(0, 1, item);
		} else if (values.tryRead("Repeat+")) {
			RuleItem item = loadRuleItem(values);
			return new RiOccurrence(1, null, item);
		} else if (values.tryRead("Repeat*")) {
			RuleItem item = loadRuleItem(values);
			return new RiOccurrence(0, null, item);
		} else if (values.tryRead("(")) {
			RiGroup group = new RiGroup();
			loadRuleGroup(values, group, true);
			return group;
		} else if (values.tryRead("[")) {
			RiAlternative alternative = new RiAlternative();
			loadRuleAlternative(values, alternative);
			return alternative;
		} else if (values.tryRead("!")) {
			return new RiRequired();
		} else if (values.tryRead("Not")) {
			RuleItem item = loadRuleItem(values);
			return new RiNot(item);
		} else if (values.tryReadReference()) {
			String referencedId = values.LastReference;
			if (grammar.Tokens.has(referencedId)) {
				Token referencedToken = grammar.Tokens.get(referencedId);
				return new RiTokenReference(referencedToken);
			} else {
				Rule referencedRule = grammar.Rules.get(referencedId);
				return new RiRuleReference(referencedRule);
			}
		}
		throw new SmlGrammarException("Invalid rule value "+values.get());
	}
	
	private void loadRuleGroup(ValueIterator values, RiGroup group, boolean needsClosingParenthesis) {
		while (true) {
			if (!values.hasValue()) {
				if (needsClosingParenthesis) {
					throw new SmlGrammarException("Group not closed");
				}
				break;
			} else if (values.tryRead(")")) {
				if (!needsClosingParenthesis) {
					throw new SmlGrammarException("Invalid group closing parenthesis");
				}
				break;
			}
			RuleItem item = loadRuleItem(values);
			group.Items.add(item);
		}
	}
	
	private void loadRuleAlternative(ValueIterator values, RiAlternative alternative) {
		while (true) {
			if (!values.hasValue()) {
				throw new SmlGrammarException("Alternative not closed");
			} else if (values.tryRead("]")) {
				break;
			}
			RuleItem item = loadRuleItem(values);
			alternative.Items.add(item);
		}
	}
	
	private void loadRule(SmlAttribute ruleAttribute, Rule rule) {
		ValueIterator values = new ValueIterator(ruleAttribute);
		loadRuleGroup(values, rule.Group, false);
	}
	
	private void loadRules(SmlElement rootElement) {
		for (SmlElement rulesElement : rootElement.elements("Syntax")) {
			for (SmlAttribute ruleAttribute : rulesElement.attributes()) {
				String id = getId(ruleAttribute.getName());
				Rule rule = grammar.Rules.get(id);
				loadRule(ruleAttribute, rule);
			}
		}
	}
	
	private void preloadRules(SmlElement rootElement) {
		for (SmlElement rulesElement : rootElement.elements("Syntax")) {
			for (SmlAttribute ruleAttribute : rulesElement.attributes()) {
				String id = getId(ruleAttribute.getName());
				if (grammar.Tokens.has(id)) {
					throw new SmlGrammarException("Rule and token with same id '"+id+"' not allowed");
				}
				Rule rule = new Rule(id);
				grammar.Rules.add(rule);
			}
		}
	}
	
	private void loadTokenCategories(SmlElement rootElement) {
		for (SmlElement categoriesElement : rootElement.elements("TokenCategories")) {
			for (SmlAttribute categoryAttribute : categoriesElement.attributes()) {
				String id = categoryAttribute.getName();
				TokenCategory category = new TokenCategory(id);
				for (String fullTokenId : categoryAttribute.getValues()) {
					String tokenId = getId(fullTokenId);
					Token token = grammar.Tokens.get(tokenId);
					category.Tokens.add(token);
				}
				grammar.TokenCategories.add(category);
			}
		}
	}
	
	private String getId(String idStr) {
		if (!idStr.startsWith("<") || !idStr.endsWith(">")) {
			throw new SmlGrammarException("Invalid id '"+idStr+"'");
		}
		return idStr.substring(1, idStr.length()-1);
	}
	
	private void loadRootElement(SmlElement rootElement) {
		if (!rootElement.hasName("Grammar")) {
			throw new SmlGrammarException("Not a grammar file.");
		}
		preloadCharsets(rootElement);
		loadCharsets(rootElement);
		
		preloadTokens(rootElement);
		loadTokens(rootElement);
		
		preloadRules(rootElement);
		loadRules(rootElement);
		
		if (rootElement.hasElement("TokenCategories")) {
			loadTokenCategories(rootElement);
		}
		
		String rootRuleId = getId(rootElement.getString("RootRule"));
		grammar.RootRule = grammar.Rules.get(rootRuleId);
	}
	
	private ArrayList<Token> getRules(SmlAttribute attribute) {
		ArrayList<Token> result = new ArrayList<>();
		String[] values = attribute.getValues();
		for (String value : values) {
			String id = getId(value);
			Token rule = grammar.Tokens.get(id);
			result.add(rule);
		}
		return result;
	}
	
	public SmlGrammar parse(SmlDocument document) {
		grammar = new SmlGrammar();
		loadRootElement(document.getRoot());
		return grammar;
	}
	
	public SmlGrammar load(String filePath) throws IOException {
		SmlDocument document = SmlDocument.load(filePath);
		return parse(document);
	}
}
