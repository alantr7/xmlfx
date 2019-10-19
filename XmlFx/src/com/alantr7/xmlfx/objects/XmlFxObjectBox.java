package com.alantr7.xmlfx.objects;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class XmlFxObjectBox extends XmlFxObjectContainer {
	
	public XmlFxObjectBox(String tag, String element) {
		super(tag, element, new VBox());
		if (tag.equalsIgnoreCase("vbox"))
			this.javaFxNode(new VBox());
		else if (tag.equalsIgnoreCase("hbox"))
			this.javaFxNode(new HBox());
	}

}
