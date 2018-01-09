package com.codeages.core.exception;

public class NotFoundException extends ServiceException {
	public NotFoundException() {
		super("404");
	}

	public NotFoundException(String message) {
		super("404",message);
	}

	public NotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotFoundException(Throwable cause) {
		super(cause);
	}

	public NotFoundException(String errorCode, String message) {
		super(message);
		this.code = errorCode;
	}

	public NotFoundException(String errorCode, String message, Throwable cause) {
		super(message, cause);
		this.code = errorCode;
	}
}
