package com.parkit.parkingsystem.integration.config;

import com.parkit.parkingsystem.config.DataBaseConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class DataBaseTestConfig extends DataBaseConfig {
	private static final Logger logger = LogManager.getLogger("DataBaseConfig");
	private static Connection connect;
	private static String localDir = System.getProperty("user.dir");
	private static String propertyFile = localDir + "\\src\\test\\resources\\config.properties";

	public static void setPropertyFile(String propertyFile) {
		DataBaseTestConfig.propertyFile = propertyFile;
	}

	public Connection getConnection() throws ClassNotFoundException, SQLException {
		String urlTest = null;
		String user = null;
		String password = null;
		try (InputStream input = new FileInputStream(propertyFile)) {
			Properties prop = new Properties();
			prop.load(input);
			urlTest = prop.getProperty("db.urlTest");
			user = prop.getProperty("db.user");
			password = prop.getProperty("db.password");
		} catch (IOException ex) {
			logger.error("The property file was not found");
		}
		Class.forName("com.mysql.cj.jdbc.Driver");
		if (connect == null) {
			try {
				connect = DriverManager.getConnection(urlTest, user, password);
				logger.info("Create DB connection in prod");
			} catch (SQLException e) {
				e.printStackTrace();
				logger.info("Fail to connect to the DB");
			}
		}
		return connect;
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
				ps = null;
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
				rs = null;
				logger.info("Closing Result Set");
			} catch (SQLException e) {
				logger.error("Error while closing result set", e);
			}
		}
	}
}
