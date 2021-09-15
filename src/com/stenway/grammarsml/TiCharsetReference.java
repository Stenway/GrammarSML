package com.stenway.grammarsml;

public class TiCharsetReference extends TokenItem {
	public Charset Charset;
	
	public TiCharsetReference(Charset charset) {
		Charset = charset;
	}

	@Override
	public String toString() {
		return "<" + Charset.Id + ">";
	}
}
