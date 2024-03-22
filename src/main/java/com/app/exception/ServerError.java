package com.app.exception;

public class ServerError extends RuntimeException {

	private static final long serialVersionUID = 5000415880461274195L;

	private String id;

	public ServerError(String id, String message) {
		super(message);
		this.id = id;
	}

	public ServerError(String message) {
		super(message);
	}

}
