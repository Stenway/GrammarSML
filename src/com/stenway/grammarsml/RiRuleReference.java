package com.stenway.grammarsml;

public class RiRuleReference extends RuleItem {
	public Rule Rule;
	
	public RiRuleReference(Rule rule) {
		Rule = rule;
	}

	@Override
	public String toString() {
		return "<" + Rule.Id + ">";
	}
}
