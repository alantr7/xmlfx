package com.alantr7.xmlfx.objects;

import javafx.scene.control.Label;

public class XmlFxObjectLabel extends XmlFxObject {
	
	private String text;
	
	public XmlFxObjectLabel(String element) {
		super("label", element, new Label());
	}
	public XmlFxObjectLabel(String element, String text) {
		this(element);
		
		((Label)this.javaFxNode()).setText(text);
	}
	
	public String text() {
		return this.text;
	}

}
