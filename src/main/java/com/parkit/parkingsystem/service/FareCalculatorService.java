package com.parkit.parkingsystem.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.dao.PromotionRecurringUserDAO;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

	private PromotionRecurringUserDAO promotionRecurringUser = new PromotionRecurringUserDAO();
	int hasNext = 0;

	public void calculateFare(Ticket ticket, String vehicleRegNumber) {

		if ((ticket.getOutTime() == null) || (ticket.getOutTime().isBefore(ticket.getInTime()))) {
			throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
		}
		LocalDateTime inHour = ticket.getInTime();
		LocalDateTime outHour = ticket.getOutTime();
		double fareCarPerMinute = (Fare.CAR_RATE_PER_HOUR / 60);
		double fareBikePerMinute = (Fare.BIKE_RATE_PER_HOUR / 60);
		double duration = ChronoUnit.MINUTES.between(inHour, outHour);
		if (duration <= 30) {
			ticket.setPrice(0);
		} else {
			hasNext = promotionRecurringUser.promotionRecurringUser(vehicleRegNumber);
			switch (ticket.getParkingSpot().getParkingType()) {
			case CAR: {
				if (hasNext >= 1) {
					double d = (double) Math.round(duration * fareCarPerMinute * 0.95 * 100) / 100;
					ticket.setPrice(d);
				} else {
					double d = (double) Math.round(duration * fareCarPerMinute * 100) / 100;
					ticket.setPrice(d);
				}
				break;
			}
			case BIKE: {
				if (hasNext >= 1) {
					double d = (double) Math.round(duration * fareBikePerMinute * 0.95 * 100) / 100;
					ticket.setPrice(d);
				} else {
					double d = (double) Math.round(duration * fareBikePerMinute * 100) / 100;
					ticket.setPrice(d);
				}
				break;
			}
			default:
				throw new IllegalArgumentException("Unkown Parking Type");
			}
		}

	}
}