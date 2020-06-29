package com.parkit.parkingsystem.config;

public class ParkingSpotDAOException extends Exception {

	public ParkingSpotDAOException() {
	}

	public ParkingSpotDAOException(String message) {
		super(message);
	}

	public ParkingSpotDAOException(Throwable cause) {
		super(cause);
	}
	
	public ParkingSpotDAOException(String message, Throwable cause) {
		super(message,cause);
	}
}
