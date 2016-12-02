package com.school.project.framework.exceptions;

@SuppressWarnings("serial")
public class XmlToObjectFailException extends SwpException {

	public XmlToObjectFailException(String errormsg) {
		this.setErrorcode(XML_TO_OBJECT_EXCEPTION);
		this.setErrormsg(errormsg);
	}
	
}
