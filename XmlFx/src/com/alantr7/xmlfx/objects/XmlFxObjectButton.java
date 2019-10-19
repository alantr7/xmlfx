package com.alantr7.xmlfx.objects;

import javafx.scene.control.Button;

public class XmlFxObjectButton extends XmlFxObject {

	public XmlFxObjectButton(String element) {
		super("button", element, new Button());
	}
	public XmlFxObjectButton(String element, String text) {
		this(element);
		((Button)this.javaFxNode()).setText(text);
	}
	
	public void setOnMouseClicked(Runnable r) {
		((Button)this.javaFxNode()).setOnAction((e) -> {
			r.run();
		});
	}

}
