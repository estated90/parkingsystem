package com.parkit.parkingsystem.service;


import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

	public PromotionRecurringUser getRecurringUser = new PromotionRecurringUser();
	boolean hasNext = false;

	public void calculateFare(Ticket ticket, String vehicleRegNumber) {

		if ((ticket.getOutTime() == null) || (ticket.getOutTime().isBefore(ticket.getInTime()))) {
			throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
		}
		LocalDateTime inHour = ticket.getInTime();
		LocalDateTime outHour = ticket.getOutTime();
		double duration = ChronoUnit.MINUTES.between(inHour, outHour);
		hasNext = getRecurringUser.promotionRecurringUser(vehicleRegNumber);
		
		if (duration <= 30) {
			ticket.setPrice(0);
		} else {
			switch (ticket.getParkingSpot().getParkingType()) {
			case CAR: {
				double fareCarPerMinute = (Fare.CAR_RATE_PER_HOUR / 60);
				if (hasNext) {
					ticket.setPrice(duration * fareCarPerMinute * 0.95);
				} else {
					ticket.setPrice(duration * fareCarPerMinute);
				}
				break;
			}
			case BIKE: {
				double fareBikePerMinute = (Fare.BIKE_RATE_PER_HOUR / 60);
				if (hasNext) {
					ticket.setPrice(duration * fareBikePerMinute * 0.95);
				} else {
					ticket.setPrice(duration * fareBikePerMinute);
				}
				break;
			}
			default:
				throw new IllegalArgumentException("Unkown Parking Type");
			}
		}

	}
}