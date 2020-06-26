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
	public static boolean isTest = false;

	public static Connection getConnection() throws ClassNotFoundException, SQLException {

		try (InputStream input = new FileInputStream(
				"E:\\Documents\\GitHub\\parkingsystem\\src\\main\\resources\\config.properties")) {
			Properties prop = new Properties();
			// load a properties file
			prop.load(input);
			// get the property value and print it out
			String urlProd = prop.getProperty("db.urlProd");
			String urlTest = prop.getProperty("db.urlTest");
			String user = prop.getProperty("db.user");
			String password = prop.getProperty("db.password");

			Class.forName("com.mysql.cj.jdbc.Driver");
			if (connect == null) {
				try {
					if (isTest == true) {
						connect = DriverManager.getConnection(urlProd, user, password);
						logger.info("Create DB connection in prod");
					} else {
						connect = DriverManager.getConnection(urlTest, user, password);
						logger.info("Create DB connection in Test");
					}

				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException ex) {
			logger.error("The credential to access the DB were incorrect");
		}
		return connect;
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
