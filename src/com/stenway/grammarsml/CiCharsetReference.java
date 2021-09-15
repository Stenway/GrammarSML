package com.stenway.grammarsml;

public class CiCharsetReference extends CharsetItem {
	public Charset Charset;
	
	public CiCharsetReference(Charset charset) {
		Charset = charset;
	}

	@Override
	public String toString() {
		return "<" + Charset.Id + ">";
	}
}
