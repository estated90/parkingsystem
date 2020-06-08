package com.parkit.parkingsystem.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().isBefore(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        LocalDateTime inHour = ticket.getInTime();
        LocalDateTime outHour = ticket.getOutTime();

        //TODO: Some tests are failing here. Need to check if this logic is correct
        double duration = ChronoUnit.MINUTES.between(inHour, outHour);
        double fareCarPerMinute = (Fare.CAR_RATE_PER_HOUR / 60);
        double fareBikePerMinute = (Fare.BIKE_RATE_PER_HOUR / 60);


        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                ticket.setPrice(duration * fareCarPerMinute);
                break;
            }
            case BIKE: {
                ticket.setPrice(duration * fareBikePerMinute);
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }
    }
}