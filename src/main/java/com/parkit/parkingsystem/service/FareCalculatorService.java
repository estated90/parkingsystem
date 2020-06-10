package com.parkit.parkingsystem.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

	private static final Logger logger = LogManager.getLogger("TicketDAO");
	public DataBaseConfig dataBaseConfig = new DataBaseConfig();

	public void calculateFare(Ticket ticket, String vehicleRegNumber) {
		Connection con = null;
		boolean hasNext = false;
		if ((ticket.getOutTime() == null) || (ticket.getOutTime().isBefore(ticket.getInTime()))) {
			throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
		}
		LocalDateTime inHour = ticket.getInTime();
		LocalDateTime outHour = ticket.getOutTime();
		double duration = ChronoUnit.MINUTES.between(inHour, outHour);
		try {
			con = dataBaseConfig.getConnection();
			PreparedStatement ps = con.prepareStatement(DBConstants.GET_EXISTING_VEHICLE);
			ps.setString(1, vehicleRegNumber);
			ResultSet rs = ps.executeQuery();
			hasNext = rs.next();
		} catch (Exception ex) {
			logger.error("Error fetching if historic exist", ex);
		} finally {
			dataBaseConfig.closeConnection(con);
		}

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