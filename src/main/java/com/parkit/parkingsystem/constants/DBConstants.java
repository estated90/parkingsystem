package com.parkit.parkingsystem.constants;

public class DBConstants {

	private DBConstants() {
		throw new IllegalStateException("Utility class");
	}

	public static final String GET_NEXT_PARKING_SPOT = "select min(PARKING_NUMBER) from parking where AVAILABLE = true and TYPE = ?";
	public static final String UPDATE_PARKING_SPOT = "update parking set available = ? where PARKING_NUMBER = ?";

	public static final String SAVE_TICKET = "insert into ticket(PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME, RECURRING_USER) "
			+ "values(?,?,?,?,?,?)";
	public static final String UPDATE_TICKET = "update ticket set PRICE=?, OUT_TIME=? where ID=?";
	public static final String GET_TICKET = "SELECT t.PARKING_NUMBER, t.ID, t.PRICE, t.IN_TIME, t.OUT_TIME, t.RECURRING_USER, p.TYPE FROM ticket t,parking p "
			+ "WHERE p.parking_number = t.parking_number AND t.VEHICLE_REG_NUMBER=? ORDER BY t.ID DESC LIMIT 1";
	public static final String GET_EXISTING_VEHICLE = "SELECT EXISTS (SELECT * FROM ticket t WHERE t.VEHICLE_REG_NUMBER = ? AND t.OUT_TIME IS NOT NULL)";
	public static final String SET_UPDATE_TO_TICKET_INTEGRATION_TEST = "UPDATE TICKET SET IN_TIME=? WHERE ID=?";
}
