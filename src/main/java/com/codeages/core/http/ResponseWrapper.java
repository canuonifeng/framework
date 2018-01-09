package com.codeages.core.http;

import java.io.Serializable;

public class ResponseWrapper<T> implements Serializable {

	private T body;
	private String status = "200";
	private String message = "";

	public ResponseWrapper() {
		
	}
	
	public ResponseWrapper(T body) {
		this.body = body;
	}

	public String getStatus() {
		return status;
	}

	public T getBody() {
		return body;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setBody(T body) {
		this.body = body;
	}
}