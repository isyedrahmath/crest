package com.capital.one.home.directs.utility;

public class OpusException extends Exception{

	private Integer statusCode;
	private String message;
	
	public OpusException(Integer statusCode, String message) {
		super(message);
		this.statusCode = statusCode;
		this.message = message;
	}

	public Integer getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
	
}
