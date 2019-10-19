package com.alantr7.xmlfx.css;

import java.io.File;

import com.alantr7.alanapi1.Alan;
import com.alantr7.alanapi1.file.TextFile;
import com.alantr7.xmlfx.XmlFxApplication;
import com.alantr7.xmlfx.exception.XmlFxApplicationException;

public class XmlFxCssParser {
	
	private XmlFxApplication app;
	public XmlFxCssParser(XmlFxApplication app, String path) {
		
		this.app = app;
		
		File f = new File(path);
		try {
			if (!f.exists()) {
				throw new XmlFxApplicationException(XmlFxApplicationException.CSS_FILE_NOT_FOUND);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		parse(f);
	}
	
	private void parse(File f) {
		TextFile tf = new TextFile(f);
		tf.read();
		
		String lines = "";
		for (String l : tf.getLines())
			lines += l;
		
		lines = lines.replace(" ", "").replace("	", "");
		String[] classes = lines.split("\\}");
		
		for (String cl : classes) {
			XmlFxCssClass c = new XmlFxCssClass(cl.split("\\{")[0]);
			String vals = cl.split("\\{")[1];
			
			Alan.debug("[CSS Parser] Created class " + c.name());
			
			String[] values = Alan.splitString(vals, ";");
			for (String v : values) {
				Alan.debug("[CSS Parser] Added " + v + " to class " + c.name());
				c.put(v.split(":")[0], v.split(":")[1]);
			}
			app.createCSS(c);
		}
		
	}
	
}
