package com.parkit.parkingsystem.config;

public class AccessToDBFailException extends Exception {
	
	private static final long serialVersionUID = 6553112893566666654L;

	public AccessToDBFailException() {
	}

	public AccessToDBFailException(String message) {
		super(message);
	}

	public AccessToDBFailException(Throwable cause) {
		super(cause);
	}
	
	public AccessToDBFailException(String message, Throwable cause) {
		super(message,cause);
	}
}
