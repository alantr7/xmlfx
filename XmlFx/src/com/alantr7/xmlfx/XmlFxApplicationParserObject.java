package com.alantr7.xmlfx;

import java.util.ArrayList;
import java.util.List;

public class XmlFxApplicationParserObject {
	
	public String tag;
	public int start, end, endContent;
	public boolean closed = false;
	
	public String element, text;
	
	List<XmlFxApplicationParserObject> children = new ArrayList<>();
	
}
