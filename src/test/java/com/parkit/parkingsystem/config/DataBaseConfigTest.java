package com.parkit.parkingsystem.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.SQLException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.parkit.parkingsystem.constants.DBConstants;

@TestMethodOrder(OrderAnnotation.class)
class DataBaseConfigTest {

	private java.sql.Connection con = null;

	@AfterEach
	void tearDown() throws Exception {

	}

	@Test
	@Order(1)
	void testGetConnection() {
		try {
			con = DataBaseConfig.getConnection();
			assertNotNull(con);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	@Test
	@Order(2)
	void testCloseConnection() {
		try {
			con = DataBaseConfig.getConnection();
			DataBaseConfig.closeConnection(con);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		assertThrows(SQLException.class, () -> {
			con.prepareStatement(DBConstants.GET_NEXT_PARKING_SPOT);
		});
	}



	@Test
	void testClosePreparedStatement() {
		fail("Not yet implemented");
	}

	@Test
	void testCloseResultSet() {
		fail("Not yet implemented");
	}

	@Test
	void testObject() {
		fail("Not yet implemented");
	}

	@Test
	void testGetClass() {
		fail("Not yet implemented");
	}

	@Test
	void testHashCode() {
		fail("Not yet implemented");
	}

	@Test
	void testEquals() {
		fail("Not yet implemented");
	}

	@Test
	void testClone() {
		fail("Not yet implemented");
	}

	@Test
	void testToString() {
		fail("Not yet implemented");
	}

	@Test
	void testNotify() {
		fail("Not yet implemented");
	}

	@Test
	void testNotifyAll() {
		fail("Not yet implemented");
	}

	@Test
	void testWait() {
		fail("Not yet implemented");
	}

	@Test
	void testWaitLong() {
		fail("Not yet implemented");
	}

	@Test
	void testWaitLongInt() {
		fail("Not yet implemented");
	}

	@Test
	void testFinalize() {
		fail("Not yet implemented");
	}

}
