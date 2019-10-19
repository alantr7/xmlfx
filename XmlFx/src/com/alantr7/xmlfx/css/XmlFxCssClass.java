package com.alantr7.xmlfx.css;

import java.util.HashMap;

public class XmlFxCssClass {
	
	private HashMap<String, String> vals = new HashMap<>();
	private String name;
	
	public XmlFxCssClass(String name) {
		this.name = name;
	}
	
	public String get(String key) {
		return vals.get(key);
	}
	public String name() {
		return this.name;
	}
	public HashMap<String, String> values() {
		return this.vals;
	}
	
	void put(String k, String v) {
		vals.put(k, v);
	}
	
}
