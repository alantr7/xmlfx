package com.alantr7.xmlfx.exception;

public class XmlFxApplicationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5129560611590080584L;
	
	public static final int FILE_NOT_SET = 0, FILE_NOT_FOUND = 1, NO_MAIN = 2,
			CSS_FILE_NOT_FOUND = 10;
	
	public XmlFxApplicationException(int exception) {
		
		String message1 = "Error.", message2 = "", message3 = "";
		
		switch (exception) {
		case FILE_NOT_SET: {
			message1 = "XML File for FX Application has not been set.";
			break;
		}
		case FILE_NOT_FOUND: {
			message1 = "XML File for FX Application has not been found.";
			break;
		}
		case NO_MAIN: {
			message1 = "Error while parsing.";
			message2 = "There must be box with id 'maincontainer' under application tag.";
			break;
		}
		case CSS_FILE_NOT_FOUND: {
			message1 = "trying to find file.";
			message2 = "CSS file not found.";
		}
		}
		
		this.setStackTrace(new StackTraceElement[] {
				new StackTraceElement(message1, message2, message3, 0)
		});
	}

}
