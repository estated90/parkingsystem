package com.parkit.parkingsystem.dao;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.config.ParkingSpotDAOException;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.util.InputReaderUtil;

class ParkingSpotDAOTest {

	//private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
	private static ParkingSpotDAO parkingSpotDAO;
	//private static TicketDAO ticketDAO;
	private static DataBasePrepareService dataBasePrepareService;

	@Mock
	private static InputReaderUtil inputReaderUtil;

	@BeforeAll
	private static void setUp() throws Exception {
		DataBaseConfig.setTest(false);
		parkingSpotDAO = new ParkingSpotDAO();
		//parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
		//ticketDAO = new TicketDAO();
		//ticketDAO.dataBaseConfig = dataBaseTestConfig;
		dataBasePrepareService = new DataBasePrepareService();
	}

	@BeforeEach
	private void setUpPerTest() throws Exception {
		//when(inputReaderUtil.readSelection()).thenReturn(1);
		//when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
		dataBasePrepareService.clearDataBaseEntries();
	}

	@AfterAll
	private static void tearDown() {
		DataBasePrepareService clear = new DataBasePrepareService();
		clear.clearDataBaseEntries();
	}
	
	@Test
	void givenNewCarEntering_whenAsked_thenReturnNextAvailableSpot() {
		try {
			int nextSpot = 0;
			nextSpot = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);
			assertEquals(1,nextSpot);
			
		} catch (ParkingSpotDAOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	void givenNewBikeEntering_whenAsked_thenReturnNextAvailableSpot() {
		try {
			int nextSpot = 0;
			nextSpot = parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE);
			assertEquals(4,nextSpot);
			
		} catch (ParkingSpotDAOException e) {
			e.printStackTrace();
		}
	}
	
	@Disabled
	@Test
	void givenTheNeedOfAParkingNumber_whenParkingIsFull_thenReturnError() throws ParkingSpotDAOException {
		Connection connection = null;
		Exception exception = null;
		try {
			connection = DataBaseTestConfig.getConnection();
			// set parking entries to available
			connection.prepareStatement("update parking set available = false").execute();
			exception = assertThrows(ParkingSpotDAOException.class, () -> {parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);});
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			assertTrue(exception.getMessage().contains("Parking is full"));
			DataBaseTestConfig.closeConnection(connection);
		}
		
		
	}

}
