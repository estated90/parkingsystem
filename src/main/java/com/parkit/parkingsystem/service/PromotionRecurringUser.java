package com.parkit.parkingsystem.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;

public class PromotionRecurringUser {
	
	Connection con = null;
	boolean hasNext = false;
	public DataBaseConfig dataBaseConfig = new DataBaseConfig();
	private static final Logger logger = LogManager.getLogger("PromotionRecurringUser");

	public boolean promotionRecurringUser(String vehicleRegNumber) {
		try {
			con = dataBaseConfig.getConnection();
			PreparedStatement ps = con.prepareStatement(DBConstants.GET_EXISTING_VEHICLE);
			ps.setString(1, vehicleRegNumber);
			ResultSet rs = ps.executeQuery();
			hasNext = rs.next();
		} catch (Exception ex) {
			logger.error("Error fetching if historic exist", ex);
		}
		return hasNext;
	}
}
