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
	private Connection connect;
	private static String localDir = System.getProperty("user.dir");
	private static String propertyFile = localDir + "\\src\\main\\resources\\config.properties";

	public Connection getConnection() throws ClassNotFoundException, SQLException {
		String urlProd = null;
		String user = null;
		String password = null;

		try (InputStream input = new FileInputStream(propertyFile)) {
			Properties prop = new Properties();
			prop.load(input);
			urlProd = prop.getProperty("db.urlProd");
			user = prop.getProperty("db.user");
			password = prop.getProperty("db.password");
		} catch (IOException ex) {
			logger.error("The property file was not found");
		}
		Class.forName("com.mysql.cj.jdbc.Driver");
		if (connect == null) {
			try {
				connect = DriverManager.getConnection(urlProd, user, password);
				logger.info("Create DB connection in prod");
			} catch (SQLException e) {
				logger.info("Fail to connect to the DB");
			}
		}
		return connect;
	}

	public static void setPropertyFile(String propertyFile) {
		DataBaseConfig.propertyFile = propertyFile;
	}

	public void closeConnection(Connection con) {
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
