package com.parkit.parkingsystem.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class DataBaseConfig {
	private static final Logger logger = LogManager.getLogger("DataBaseConfig");
	private static String url = "jdbc:mysql://localhost:3306/prod";
	private static String urlTest = "jdbc:mysql://localhost:3306/test";
	private static String user = "root";
	private static String passwd = "rootroot";
	private static Connection connect;
	public static boolean isTest = true;

	public static Connection getConnection() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		if (connect == null) {
			try {
				if (isTest == true){
					connect = DriverManager.getConnection(url, user, passwd);
					logger.info("Create DB connection in prod");
				} else {
					connect = DriverManager.getConnection(urlTest, user, passwd);
					logger.info("Create DB connection in Test");
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return connect;
	}

	public static void closeConnection(Connection con) {
		if (con != null) {
			try {
				con.close();
				connect = null;
				logger.info("Closing DB connection");
			} catch (SQLException e) {
				logger.error("Error while closing connection", e);
			}
		}
	}

	public void closePreparedStatement(PreparedStatement ps) {
		if (ps != null) {
			try {
				ps.close();
				logger.info("Closing Prepared Statement");
			} catch (SQLException e) {
				logger.error("Error while closing prepared statement", e);
			}
		}
	}

	public void closeResultSet(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
				logger.info("Closing Result Set");
			} catch (SQLException e) {
				logger.error("Error while closing result set", e);
			}
		}
	}
}
