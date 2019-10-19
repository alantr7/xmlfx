package com.alantr7.xmlfx.objects;

import java.util.HashMap;

import com.alantr7.alanapi1.Alan;
import com.alantr7.xmlfx.XmlFxApplicationParserObject;

import javafx.scene.Node;
import javafx.scene.layout.Region;

public abstract class XmlFxObject {
	
	private String element, tag;
	private Node javafxnode;
	
	public XmlFxObject(String tag, String element, Node javafxnode) {
		this.tag = tag;
		this.element = element;
		
		this.javafxnode = javafxnode;
		
		Region r = (Region)javafxnode;
		
		String s;
		if ((s = attribute("width")).length() > 0) {
			int w = Integer.parseInt(s);
			r.setMinWidth(w);
			r.setMaxWidth(w);
		}
		if ((s = attribute("height")).length() > 0) {
			int h = Integer.parseInt(s);
			r.setMinHeight(h);
			r.setMaxHeight(h);
		}
		
	}
	
	public String attribute(String attr) {
		String attrval = "";
		
		if (element.contains(attr)) {
			String value = element.split(attr)[1];
			
			// CHECK IF ATTRIBUTE HAS VALUE
			if (value.startsWith("=")) {
				value = value.substring(1);
				
				// CHECK IF ATTRIBUTE VALUE IS STRING
				if (value.startsWith("\""))
					value = value.substring(1);
				
				// FINDS VALUE END
				for (int i = 0; i < value.length(); i++) {
					if (value.charAt(i) == '\"') {
						break;
					} else attrval += value.charAt(i);
				}
			}
		}
		
		return attrval;
		
	}
	public String tag() {
		return this.tag;
	}
	public String id() {
		return attribute("id");
	}
	
	public static XmlFxObject parse(XmlFxApplicationParserObject obj) {
		switch (obj.tag.toLowerCase()) {
		case "label": {
			return new XmlFxObjectLabel(obj.element, obj.text);
		}
		case "vbox": case "hbox": {
			return new XmlFxObjectBox(obj.tag, obj.element);
		}
		case "button": {
			return new XmlFxObjectButton(obj.element, obj.text);
		}
		case "style": {
			return new XmlFxObject(obj.tag, obj.element, null) {};
		}
		}
		return null;
	}
	
	public void stylize(HashMap<String, String> style) {
		String finalStyle = "";
		for (String k : style.keySet()) {
			finalStyle += k + ":" + style.get(k) + ";";
		}
		this.javafxnode.setStyle(finalStyle);
		Alan.debug("[" + this.tag + "] Received style: " + finalStyle);
	}
	
	public Node javaFxNode() {
		return this.javafxnode;
	}
	void javaFxNode(Node n) {
		this.javafxnode = n;
	}
	
}
