package com.alantr7.xmlfx.objects;

import java.util.ArrayList;
import java.util.List;

import com.alantr7.alanapi1.Alan;

import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

public abstract class XmlFxObjectContainer extends XmlFxObject {
	
	public XmlFxObjectContainer(String tag, String element, Parent parent) {
		super(tag, element, parent);
	}

	private List<XmlFxObject> children = new ArrayList<>();
	public List<XmlFxObject> children() {
		return this.children;
	}
	
	public void add(XmlFxObject obj) {
		Platform.runLater(() -> {
			if (this.tag().equalsIgnoreCase("application"))
				return;
			Alan.debug("Adding " + obj.javaFxNode().getClass().getName() + " to " + 
					this.javaFxNode().getClass().getName());
			((Pane)this.javaFxNode()).getChildren().add(obj.javaFxNode());
		});
		children().add(obj);
	}
	
}
