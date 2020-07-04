package com.parkit.parkingsystem.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.PromotionRecurringUserDAO;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

	private PromotionRecurringUserDAO promotionRecurringUser = new PromotionRecurringUserDAO();

	public void calculateFare(Ticket ticket, String vehicleRegNumber) {

		if ((ticket.getOutTime() == null) || (ticket.getOutTime().isBefore(ticket.getInTime()))) {
			throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
		}
		LocalDateTime inHour = ticket.getInTime();
		LocalDateTime outHour = ticket.getOutTime();
		double duration = ChronoUnit.MINUTES.between(inHour, outHour);
		ParkingType vehicleType = ticket.getParkingSpot().getParkingType();
		switch (vehicleType) {
		case CAR: 
			calculateFareOfVehicle(ticket, vehicleRegNumber, vehicleType, duration);
			break;
		case BIKE: 
			calculateFareOfVehicle(ticket, vehicleRegNumber, vehicleType, duration);
			break;
		default:
			throw new IllegalArgumentException("Unkown Parking Type");
		}
	}

	private void calculateFareOfVehicle(Ticket ticket, String vehicleRegNumber, ParkingType vehicleType, double duration) {
		int hasNext = 0;
		double fareInUse = 0;
		if (duration <= 30) {
			ticket.setPrice(0);
		} else {
			hasNext = promotionRecurringUser.promotionRecurringUser(vehicleRegNumber);
			if (vehicleType == ParkingType.CAR) {
				fareInUse = (Fare.CAR_RATE_PER_HOUR / 60);
			} else {
				fareInUse = (Fare.BIKE_RATE_PER_HOUR / 60);
			}
			if (hasNext >= 1) {
				double d = (double) Math.round(duration * fareInUse * 0.95 * 100) / 100;
				ticket.setPrice(d);
			} else {
				double d = (double) Math.round(duration * fareInUse * 100) / 100;
				ticket.setPrice(d);
			}
		}
	}
}