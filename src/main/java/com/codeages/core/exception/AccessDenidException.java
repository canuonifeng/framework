package com.codeages.core.exception;

public class AccessDenidException extends ServiceException {
	public AccessDenidException() {
		super();
	}

	public AccessDenidException(String message) {
		super(message);
	}

	public AccessDenidException(String message, Throwable cause) {
		super(message, cause);
	}

	public AccessDenidException(Throwable cause) {
		super(cause);
	}

	public AccessDenidException(String errorCode, String message) {
		super(message);
		this.code = errorCode;
	}

	public AccessDenidException(String errorCode, String message, Throwable cause) {
		super(message, cause);
		this.code = errorCode;
	}
}
