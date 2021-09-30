package com.stenway.grammarsml;

import com.stenway.wsv.WsvSerializer;
import java.util.ArrayList;

public class Utils {
	public static String escapeChar(int codePoint) {
		String value = new String(new int[] {codePoint}, 0, 1);
		return escapeValue(value);
	}
	
	public static String escapeValue(String value) {
		StringBuilder sb = new StringBuilder();
		WsvSerializer.serializeValue(sb, value);
		return sb.toString();
	}

	public static String getCategoryCode(UnicodeCategory value) {
		switch(value) {
			case OTHER_CONTROL:				return "Cc";
			case OTHER_FORMAT:				return "Cf";
			case OTHER_PRIVATEUSE:			return "Co";
			case OTHER_SURROGATE:			return "Cs";
			case OTHER_NOTASSIGNED:			return "Cn";
			case LETTER_LOWERCASE:			return "Ll";
			case LETTER_MODIFIER:			return "Lm";
			case LETTER_OTHER:				return "Lo";
			case LETTER_TITLECASE:			return "Lt";
			case LETTER_UPPERCASE:			return "Lu";
			case MARK_SPACING:				return "Mc";
			case MARK_ENCLOSING:			return "Me";
			case MARK_NONSPACING:			return "Mn";
			case NUMBER_DECIMAL:			return "Nd";
			case NUMBER_LETTER:				return "Nl";
			case NUMBER_OTHER:				return "No";
			case PUNCTUATION_CONNECTOR:		return "Pc";
			case PUNCTUATION_DASH:			return "Pd";
			case PUNCTUATION_CLOSE:			return "Pe";
			case PUNCTUATION_FINALQUOTE:	return "Pf";
			case PUNCTUATION_INITIALQUOTE:	return "Pi";
			case PUNCTUATION_OTHER:			return "Po";
			case PUNCTUATION_OPEN:			return "Ps";
			case SYMBOL_CURRENCY:			return "Sc";
			case SYMBOL_MODIFIER:			return "Sk";
			case SYMBOL_MATH:				return "Sm";
			case SYMBOL_OTHER:				return "So";
			case SEPARATOR_LINE:			return "Zl";
			case SEPARATOR_PARAGRAPH:		return "Zp";
			case SEPARATOR_SPACE:			return "Zs";
		}
		throw new IllegalStateException();
	}
		
	public static UnicodeCategory getCategory(String name) {
		name = name.toLowerCase();
		if (name.equals("cc"))		{ return UnicodeCategory.OTHER_CONTROL; }
		else if (name.equals("cf")) { return UnicodeCategory.OTHER_FORMAT; }
		else if (name.equals("co")) { return UnicodeCategory.OTHER_PRIVATEUSE; }
		else if (name.equals("cs")) { return UnicodeCategory.OTHER_SURROGATE; }
		else if (name.equals("cn")) { return UnicodeCategory.OTHER_NOTASSIGNED; }
		else if (name.equals("ll")) { return UnicodeCategory.LETTER_LOWERCASE; }
		else if (name.equals("lm")) { return UnicodeCategory.LETTER_MODIFIER; }
		else if (name.equals("lo")) { return UnicodeCategory.LETTER_OTHER; }
		else if (name.equals("lt")) { return UnicodeCategory.LETTER_TITLECASE; }
		else if (name.equals("lu")) { return UnicodeCategory.LETTER_UPPERCASE; }
		else if (name.equals("mc")) { return UnicodeCategory.MARK_SPACING; }
		else if (name.equals("me")) { return UnicodeCategory.MARK_ENCLOSING; }
		else if (name.equals("mn")) { return UnicodeCategory.MARK_NONSPACING; }
		else if (name.equals("nd")) { return UnicodeCategory.NUMBER_DECIMAL; }
		else if (name.equals("nl")) { return UnicodeCategory.NUMBER_LETTER; }
		else if (name.equals("no")) { return UnicodeCategory.NUMBER_OTHER; }
		else if (name.equals("pc")) { return UnicodeCategory.PUNCTUATION_CONNECTOR; }
		else if (name.equals("pd")) { return UnicodeCategory.PUNCTUATION_DASH; }
		else if (name.equals("pe")) { return UnicodeCategory.PUNCTUATION_CLOSE; }
		else if (name.equals("pf")) { return UnicodeCategory.PUNCTUATION_FINALQUOTE; }
		else if (name.equals("pi")) { return UnicodeCategory.PUNCTUATION_INITIALQUOTE; }
		else if (name.equals("po")) { return UnicodeCategory.PUNCTUATION_OTHER; }
		else if (name.equals("ps")) { return UnicodeCategory.PUNCTUATION_OPEN; }
		else if (name.equals("sc")) { return UnicodeCategory.SYMBOL_CURRENCY; }
		else if (name.equals("sk")) { return UnicodeCategory.SYMBOL_MODIFIER; }
		else if (name.equals("sm")) { return UnicodeCategory.SYMBOL_MATH; }
		else if (name.equals("so")) { return UnicodeCategory.SYMBOL_OTHER; }
		else if (name.equals("zl")) { return UnicodeCategory.SEPARATOR_LINE; }
		else if (name.equals("zp")) { return UnicodeCategory.SEPARATOR_PARAGRAPH; }
		else if (name.equals("zs")) { return UnicodeCategory.SEPARATOR_SPACE; }
		throw new IllegalStateException();
	}
	
	private static void getTiGroups(ArrayList<TokenItem> items, ArrayList<TiGroup> groups) {
		for (TokenItem item : items) {
			if (item instanceof TiGroup) {
				TiGroup group = (TiGroup)item;
				groups.add(group);
				getTiGroups(group.Items, groups);
			} else if (item instanceof TiAlternative) {
				TiAlternative alternative = (TiAlternative)item;
				getTiGroups(alternative.Items, groups);
			} else if (item instanceof TiOccurrence) {
				TiOccurrence occurance = (TiOccurrence)item;
				if (occurance.Item instanceof TiGroup) {
					TiGroup group = (TiGroup)occurance.Item;
					groups.add(group);
					getTiGroups(group.Items, groups);
				}
			}
		}
	}
	
	public static TiGroup[] getTiSubGroups(Token token) {
		ArrayList<TiGroup> groups = new ArrayList<>();
		getTiGroups(token.Group.Items, groups);
		return groups.toArray(new TiGroup[0]);
	}
	
	private static void getRiGroups(ArrayList<RuleItem> items, ArrayList<RiGroup> groups) {
		for (RuleItem item : items) {
			if (item instanceof RiGroup) {
				RiGroup group = (RiGroup)item;
				groups.add(group);
				getRiGroups(group.Items, groups);
			} else if (item instanceof RiAlternative) {
				RiAlternative alternative = (RiAlternative)item;
				getRiGroups(alternative.Items, groups);
			} else if (item instanceof RiOccurrence) {
				RiOccurrence occurance = (RiOccurrence)item;
				if (occurance.Item instanceof RiGroup) {
					RiGroup group = (RiGroup)occurance.Item;
					groups.add(group);
					getRiGroups(group.Items, groups);
				}
			} else if (item instanceof RiNot) {
				RiNot not = (RiNot)item;
				if (not.Item instanceof RiGroup) {
					RiGroup group = (RiGroup)not.Item;
					groups.add(group);
					getRiGroups(group.Items, groups);
				}
			}
		}
	}
	
	public static RiGroup[] getRiSubGroups(Rule rule) {
		ArrayList<RiGroup> groups = new ArrayList<>();
		getRiGroups(rule.Group.Items, groups);
		return groups.toArray(new RiGroup[0]);
	}
}
