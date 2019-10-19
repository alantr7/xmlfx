package com.alantr7.xmlfx;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.alantr7.alanapi1.Alan;
import com.alantr7.alanapi1.file.TextFile;
import com.alantr7.xmlfx.exception.XmlFxApplicationException;

public class XmlFxApplicationParser {

	private XmlFxApplicationSettings settings;
	private XmlFxApplication app;
	
	XmlFxApplicationParser(XmlFxApplication app) {

		this.app = app;
		this.settings = app.settings();
		try {
			parse(settings.filePath);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void parse(String path) throws Exception {

		if (!new File(path).exists()) {
			throw new XmlFxApplicationException(XmlFxApplicationException.FILE_NOT_FOUND);
		}

		TextFile tf = new TextFile(new File(path));
		tf.read();

		String lines = "";
		for (String l : tf.getLines())
			lines += l;

		List<XmlFxApplicationParserObject> open = new ArrayList<>();

		XmlFxApplicationParserObject opened = null;
		for (int i = 0; i < lines.length(); i++) {
			if (lines.charAt(i) == '<' && opened == null) {
				String tag = lines.substring(i).split("<")[1].split(">")[0];
				if (lines.charAt(i + 1) == '/') {
					tag = tag.substring(1);

					Alan.debug("Closing nearest element with tag: " + tag);

					for (int j = open.size() - 1; j >= 0; j--) {
						XmlFxApplicationParserObject obj = open.get(j);
						if (obj.tag.equalsIgnoreCase(tag) && !obj.closed) {
							obj.closed = true;
							try {
								obj.text = lines.substring(obj.end, i);
							} catch (Exception e) {
								
							}
							Alan.debug("Closed.");
							break;
						}
					}

					continue;
				} else {
					if (tag.contains(" "))
						tag = tag.split(" ")[0];
				}
				opened = new XmlFxApplicationParserObject();
				opened.start = i;
				opened.tag = tag;

				Alan.debug("Adding " + tag + " to nearest open object...");
				for (int j = open.size() - 1; j >= 0; j--) {
					XmlFxApplicationParserObject obj = open.get(j);
					if (!obj.closed) {
						obj.children.add(opened);
						Alan.debug("Added it to " + obj.tag);
						break;
					}
				}

				continue;
			}
			if (lines.charAt(i) == '>' && opened != null) {
				opened.end = i + 1;
				opened.element = lines.substring(opened.start, opened.end);
				open.add(opened);

				Alan.debug("Opened: " + opened.tag + ". Content: " + lines.substring(opened.start, opened.end));

				opened = null;
				continue;
			}
		}
		
		printXml(open.get(0), 0);
		app.createObjects(open.get(0));
	}

	void printXml(XmlFxApplicationParserObject c, int spaces) {
		for (int i = 0; i < spaces; i++) {
			System.out.print(" ");
		}
		System.out.println(c.tag + "(" + c.element + ")");
		if (c.children.size() > 0)
			for (XmlFxApplicationParserObject child : c.children) {
				printXml(child, spaces + 2);
			}
	}

}
