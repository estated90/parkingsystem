package com.parkit.parkingsystem.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class DataBaseConfig {
	private static final Logger logger = LogManager.getLogger("DataBaseConfig");
	private static Connection connect;
	private static boolean isTest = true;

	public static Connection getConnection() throws ClassNotFoundException, SQLException {
		String urlProd = null;
		String urlTest = null;
		String user = null;
		String password = null;
		String localDir = System.getProperty("user.dir");
		try (InputStream input = new FileInputStream(localDir + "\\src\\main\\resources\\config.properties")) {
			Properties prop = new Properties();
			// load a properties file
			prop.load(input);
			// get the property value and print it out
			urlProd = prop.getProperty("db.urlProd");
			urlTest = prop.getProperty("db.urlTest");
			user = prop.getProperty("db.user");
			password = prop.getProperty("db.password");
		} catch (IOException ex) {
			logger.error("The credential to access the DB were incorrect");
		}
		Class.forName("com.mysql.cj.jdbc.Driver");
		if (connect == null) {

			if (isTest) {
				try {
					connect = DriverManager.getConnection(urlProd, user, password);
					logger.info("Create DB connection in prod");
				} catch (SQLException e) {
					e.printStackTrace();
					logger.info("Fail to connect to the DB");
				}
			} else {
				try {
					connect = DriverManager.getConnection(urlTest, user, password);
					logger.info("Create DB connection in Test");
				} catch (SQLException e) {
					e.printStackTrace();
					logger.info("Fail to connect to the DB of testing");
				}
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

	public static boolean isTest() {
		return isTest;
	}

	public static void setTest(boolean isTest) {
		DataBaseConfig.isTest = isTest;
	}
}
