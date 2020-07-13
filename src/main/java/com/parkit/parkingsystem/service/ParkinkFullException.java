package com.parkit.parkingsystem.service;

public class ParkinkFullException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ParkinkFullException() {
	}

	public ParkinkFullException(String message) {
		super(message);
	}

	public ParkinkFullException(Throwable cause) {
		super(cause);
	}
	
	public ParkinkFullException(String message, Throwable cause) {
		super(message,cause);
	}
}
