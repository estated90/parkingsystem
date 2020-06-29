package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.config.ParkingSpotDAOException;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ParkingSpotDAO {
	private static final Logger logger = LogManager.getLogger("ParkingSpotDAO");
	private DataBaseConfig dataBaseConfig = new DataBaseConfig();

	private Connection con = null;
	private PreparedStatement ps = null;
	private ResultSet rs = null;

	public int getNextAvailableSlot(ParkingType parkingType) throws ParkingSpotDAOException {

		int result = -1;
		try {
			con = DataBaseConfig.getConnection();
			ps = con.prepareStatement(DBConstants.GET_NEXT_PARKING_SPOT);
			ps.setString(1, parkingType.toString());
			rs = ps.executeQuery();
			if (rs.next()) {
				result = rs.getInt(1);
			}
		} catch (Exception ex) {
			logger.error("Error fetching next available slot", ex);
		} finally {
			dataBaseConfig.closeResultSet(rs);
			dataBaseConfig.closePreparedStatement(ps);
			DataBaseConfig.closeConnection(con);
		}
		return result;
	}

	public boolean updateParking(ParkingSpot parkingSpot) {
		// update the availability for that parking slot
		try {
			con = DataBaseConfig.getConnection();
			ps = con.prepareStatement(DBConstants.UPDATE_PARKING_SPOT);
			ps.setBoolean(1, parkingSpot.isAvailable());
			ps.setInt(2, parkingSpot.getId());
			int updateRowCount = ps.executeUpdate();
			return (updateRowCount == 1);
		} catch (Exception ex) {
			logger.error("Error updating parking info", ex);
			return false;
		} finally {
			dataBaseConfig.closePreparedStatement(ps);
			DataBaseConfig.closeConnection(con);
		}
	}

	public void setDataBaseConfig(DataBaseConfig dataBaseConfig) {
		this.dataBaseConfig = dataBaseConfig;
	}

}
