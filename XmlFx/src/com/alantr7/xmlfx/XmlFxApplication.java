package com.alantr7.xmlfx;

import java.util.ArrayList;
import java.util.List;

import com.alantr7.alanapi1.Alan;
import com.alantr7.xmlfx.css.XmlFxCssClass;
import com.alantr7.xmlfx.css.XmlFxCssParser;
import com.alantr7.xmlfx.exception.XmlFxApplicationException;
import com.alantr7.xmlfx.objects.XmlFxObject;
import com.alantr7.xmlfx.objects.XmlFxObjectBox;
import com.alantr7.xmlfx.objects.XmlFxObjectContainer;

import javafx.application.Application;
import javafx.stage.Stage;

public abstract class XmlFxApplication extends Application {
	
	public enum XmlFxApplicationEvent {
		Creation_Started, Created
	}
	
	public enum XmlFxApplicationFindMethod {
		Xpath, Id
	}
	
	private XmlFxApplicationSettings settings = new XmlFxApplicationSettings();
	XmlFxObjectContainer mcontainer;
	
	private List<XmlFxCssClass> css = new ArrayList<>();
	
	public void create() throws Exception {
		long start = System.currentTimeMillis();
		if (settings.filePath == null)
			throw new XmlFxApplicationException(XmlFxApplicationException.FILE_NOT_SET);
		new XmlFxApplicationParser(this);
		
		if ((mcontainer = (XmlFxObjectContainer)
				this.findObject(XmlFxApplicationFindMethod.Id, "maincontainer")) == null)
			throw new XmlFxApplicationException(XmlFxApplicationException.NO_MAIN);
		
		System.out.println("Application Created in " + (System.currentTimeMillis() - start) + "ms");
	}
	public XmlFxObject findObject(XmlFxApplicationFindMethod method, String value) {
		switch (method) {
		case Xpath: {
			return xpathSearch(mcontainer, value);
		}
		case Id: {
			return idSearch(mcontainer, value);
		}
		}
		
		return null;
	}
	private XmlFxObject xpathSearch(XmlFxObjectContainer container, String path) {
		String[] paths = path.split("/");
		String next = paths[0];
		
		path = "";
		
		// REMOVE CURRENT OBJECT FROM PATH
		for (int i = 1; i < paths.length; i++) {
			path += paths[i] + "/";
		}
		
		// CHECK WHAT OBJECT TO FIND IF THERE ARE MULTIPLE
		int count = 0, found = 0;
		if (next.contains("[") && next.contains("]")) {
			count = Integer.parseInt(next.split("\\[")[1].split("\\]")[0]);
			next = next.split("\\[")[0];
		}
		
		// CHECK IF CONTAINER IS ALREADY APP. IF YES THEN SKIP
		if (next.equalsIgnoreCase("application"))
			return xpathSearch(container, path);
		
		// BEGIN SEARCHING THROUGH CONTAINER
		Alan.debug("[Xpath Search] Looking for " + next);
		for (int i = 0; i < container.children().size(); i++) {
			XmlFxObject child = container.children().get(i);
			
			// IF TAGS MATCH, RESULT PROBABLY FOUND
			if (child.tag().equalsIgnoreCase(next)) {
				Alan.debug("[Xpath Search] Tags matching. (" + child.tag() + ")");
				
				// CURRENT RESULT IS FOUND
				if (count == found) {
					// IF THERE IS NO MORE TO SEARCH, RESULT IS FOUND
					if (paths.length == 1)
						return container.children().get(i);
					else {
						// IF THERE IS MORE TO SEARCH, KEEP LOOKING.
						if (child instanceof XmlFxObjectContainer)
							return xpathSearch((XmlFxObjectContainer)child, path);
					}
				}
				count++;
			}
		}
		
		return null;
	}
	private XmlFxObject idSearch(XmlFxObjectContainer container, String value) {
		for (XmlFxObject obj : container.children()) {
			if (obj.id().equalsIgnoreCase(value))
				return obj;
			if (obj instanceof XmlFxObjectContainer) {
				XmlFxObject res = idSearch((XmlFxObjectContainer)obj, value);
				if (res != null) return res;
			}
		}
		return null;
	}
	
	public abstract void applicationEvent(XmlFxApplicationEvent event);
	
	Stage pstage;
	@Override
	public void start(Stage pstage) throws Exception {
		this.pstage = pstage;
		new XmlFxApplicationWindow(this);
	}
	
	public XmlFxApplicationSettings settings() {
		return this.settings;
	}
	
	void createObjects(XmlFxApplicationParserObject main) {
		this.mcontainer = new XmlFxObjectBox(main.tag, main.element);
		settings.width = Integer.parseInt(mcontainer.attribute("width"));
		settings.height = Integer.parseInt(mcontainer.attribute("height"));
		settings.title = mcontainer.attribute("title");
		addObjects(mcontainer, main);
	}
	void addObjects(XmlFxObjectContainer b, XmlFxApplicationParserObject pobj) {
		for (XmlFxApplicationParserObject child : pobj.children) {
			XmlFxObject obj = XmlFxObject.parse(child);
			b.add(obj);
			
			if (obj.tag().equalsIgnoreCase("style")) {
				String p = obj.attribute("path");
				new XmlFxCssParser(this, p);
			}
			
			if (obj.attribute("class").length() > 0) {
				XmlFxCssClass css = findCss(obj.attribute("class"));
				if (css != null) {
					Alan.debug("[Object Creation] Applied css class " + css.name() + " to " + obj.tag());
					obj.stylize(css.values());
				}
			}
			
			Alan.debug("[Object Creation] Created "  + obj.tag() + " and added it to " + b.tag());
			
			if (obj instanceof XmlFxObjectContainer)
				addObjects((XmlFxObjectContainer)obj, child);
		}
	}
	
	public void createCSS(XmlFxCssClass cl) {
		this.css.add(cl);
	}
	
	public XmlFxCssClass findCss(String name) {
		for (XmlFxCssClass c : this.css) {
			if (c.name().equalsIgnoreCase(name))
				return c;
		}
		return null;
	}
	
}
