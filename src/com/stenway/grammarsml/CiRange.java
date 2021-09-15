package com.stenway.grammarsml;

public class CiRange extends CharsetItem {
	public CiChar From;
	public CiChar To;
	
	public CiRange(CiChar from, CiChar to) {
		From = from;
		To = to;
	}
	
	@Override
	public String toString() {
		return "Range " + Utils.escapeChar(From.Value) + " " + Utils.escapeChar(To.Value);
	}
}
