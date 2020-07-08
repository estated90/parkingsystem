package com.parkit.parkingsystem.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

	public void calculateFare(Ticket ticket) {

		if ((ticket.getOutTime() == null) || (ticket.getOutTime().isBefore(ticket.getInTime()))) {
			throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
		}
		LocalDateTime inHour = ticket.getInTime();
		LocalDateTime outHour = ticket.getOutTime();
		double duration = ChronoUnit.MINUTES.between(inHour, outHour);
		ParkingType vehicleType = ticket.getParkingSpot().getParkingType();
		switch (vehicleType) {
		case CAR: 
			calculateFareOfVehicle(ticket, vehicleType, duration);
			break;
		case BIKE: 
			calculateFareOfVehicle(ticket, vehicleType, duration);
			break;
		default:
			throw new IllegalArgumentException("Unkown Parking Type");
		}
	}

	private void calculateFareOfVehicle(Ticket ticket, ParkingType vehicleType, double duration) {
		double fareInUse = 0;
		if (duration <= 30) {
			ticket.setPrice(0);
		} else {
			if (vehicleType == ParkingType.CAR) {
				fareInUse = (Fare.CAR_RATE_PER_HOUR / 60);
			} else {
				fareInUse = (Fare.BIKE_RATE_PER_HOUR / 60);
			}
			if (ticket.getIsRecurring()) {
				double d = (double) Math.round(duration * fareInUse * 0.95 * 100) / 100;
				ticket.setPrice(d);
			} else {
				double d = (double) Math.round(duration * fareInUse * 100) / 100;
				ticket.setPrice(d);
			}
		}
	}
}