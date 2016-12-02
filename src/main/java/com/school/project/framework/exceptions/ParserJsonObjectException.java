package com.school.project.framework.exceptions;

@SuppressWarnings("serial")
public class ParserJsonObjectException extends SwpException {

	public ParserJsonObjectException() {
		 setErrorcode(PARSER_JSON_OBJECT_EXCEPTION);
	}

}
