package com.parkit.parkingsystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;

public class PromotionRecurringUserDAO {
	
	private Connection con = null;
	private PreparedStatement ps = null;
	private ResultSet rs = null;
	private int hasNext = 0;
	private DataBaseConfig dataBaseConfig = new DataBaseConfig();
	private static final Logger logger = LogManager.getLogger("PromotionRecurringUser");

	public int promotionRecurringUser(String vehicleRegNumber) {
		try {
			con = dataBaseConfig.getConnection();
			ps = con.prepareStatement(DBConstants.GET_EXISTING_VEHICLE);
			ps.setString(1, vehicleRegNumber);
			rs = ps.executeQuery();
			if (rs.first()) {
				hasNext = rs.getInt("COUNT");
			}
			//hasNext = 
		} catch (Exception ex) {
			logger.error("Error fetching if historic exist", ex);
        }finally {
            dataBaseConfig.closeResultSet(rs);
            dataBaseConfig.closePreparedStatement(ps);
            dataBaseConfig.closeConnection(con);
        }
		return hasNext;
	}
}
