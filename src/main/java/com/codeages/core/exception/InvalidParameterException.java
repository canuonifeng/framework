package com.codeages.core.exception;

public class InvalidParameterException extends ServiceException {

	public InvalidParameterException() {
		super();
	}

	public InvalidParameterException(String message) {
		super(message);
	}

	public InvalidParameterException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidParameterException(Throwable cause) {
		super(cause);
	}

	public InvalidParameterException(String errorCode, String message) {
		super(message);
		this.code = errorCode;
	}

	public InvalidParameterException(String errorCode, String message, Throwable cause) {
		super(message, cause);
		this.code = errorCode;
	}

}
