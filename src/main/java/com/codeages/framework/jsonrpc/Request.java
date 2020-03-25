package com.codeages.framework.jsonrpc;

import java.io.Serializable;
import java.util.concurrent.ThreadLocalRandom;

public class Request implements Serializable {

	private static final long serialVersionUID = 1L;

	private Double id = ThreadLocalRandom.current().nextDouble(10000, 10000000);
;
	private String jsonrpc = "2.0";
	private String method;
	private Object[] params;

	public String getJsonrpc() {
		return jsonrpc;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Object[] getParams() {
		return params;
	}

	public void setParams(Object[] params) {
		this.params = params;
	}

	public Double getId() {
		return id;
	}

	public void setId(Double id) {
		this.id = id;
	}

}
