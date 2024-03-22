package com.app.exception;

public class InvalidInputException extends RuntimeException{

	private static final long serialVersionUID = -345194873075164201L;

	private String id;
	
	public InvalidInputException(String id, String message) {
		super(message);
		this.id = id;
	}
	
	public InvalidInputException(String message) {
		super(message);
	}
}
