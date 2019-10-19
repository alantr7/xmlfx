package com.alantr7.xmlfx;

import javafx.scene.Parent;
import javafx.scene.Scene;

public class XmlFxApplicationWindow {
	
	private XmlFxApplication app;
	XmlFxApplicationWindow(XmlFxApplication app) {
		this.app = app;
		
		create();
	}
	
	void create() {
		Scene scene = new Scene((Parent)app.mcontainer.javaFxNode());
		app.pstage.setScene(scene);
		
		app.pstage.setWidth(app.settings().width);
		app.pstage.setHeight(app.settings().height);
		
		app.pstage.setTitle(app.settings().title);
		
		app.pstage.show();
	}
	
}
