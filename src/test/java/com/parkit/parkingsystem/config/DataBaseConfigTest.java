package com.parkit.parkingsystem.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.Rule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.rules.ExpectedException;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mock;

import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.service.FareCalculatorService;
import com.parkit.parkingsystem.util.InputReaderUtil;

@TestMethodOrder(OrderAnnotation.class)
class DataBaseConfigTest {

	private java.sql.Connection con = null;
	private java.sql.PreparedStatement ps = null;
	private static DataBaseConfig dataBaseConfig = new DataBaseConfig();
    @Mock
	private static InputReaderUtil inputReaderUtil;

    @Rule
    public ExpectedException exception = ExpectedException.none();
    
	@BeforeAll
	private static void setUp() throws Exception {
        new DataBasePrepareService();
        new FareCalculatorService();
	}
	
	
	@AfterEach
	void tearDown() throws Exception {

	}

	@Test
	@Order(1)
	void testGetConnection() {
		try {
			con = dataBaseConfig.getConnection();
			assertNotNull(con);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	@Test
	@Order(2)
	void testCloseConnection() {
		try {
			con = dataBaseConfig.getConnection();
			dataBaseConfig.closeConnection(con);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		assertThrows(SQLException.class, () -> {
			con.prepareStatement(DBConstants.GET_NEXT_PARKING_SPOT);
		});
	}
	
	@Test
	void testgetConnectionError () {
		DataBaseConfig.setPropertyFile("\\src\\main\\resources\\config.propertie");
		try {
			dataBaseConfig.getConnection();
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        exception.expect(IOException.class);
    	exception.expectMessage("The property file was not found");
	}
	
}
