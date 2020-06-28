package com.parkit.parkingsystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;

public class PromotionRecurringUserDAO {
	
	private DataBaseConfig dataBaseConfig = new DataBaseConfig();
	private static final Logger logger = LogManager.getLogger("PromotionRecurringUser");
	private Connection con = null;
	private PreparedStatement ps = null;
	private ResultSet rs = null;
	private int hasNext = 0;

	public int promotionRecurringUser(String vehicleRegNumber) {
		try {
			con = DataBaseConfig.getConnection();
			ps = con.prepareStatement(DBConstants.GET_EXISTING_VEHICLE, ResultSet.TYPE_SCROLL_SENSITIVE, 
                    ResultSet.CONCUR_UPDATABLE);
			ps.setString(1, vehicleRegNumber);
			ps.setString(2, vehicleRegNumber);
			rs = ps.executeQuery();
			rs.first();
			hasNext = rs.getInt("COUNT");
		} catch (Exception ex) {
			logger.error("Error fetching if historic exist", ex);
        }finally {
            dataBaseConfig.closeResultSet(rs);
            dataBaseConfig.closePreparedStatement(ps);
            DataBaseConfig.closeConnection(con);
        }
		return hasNext;
	}
}
