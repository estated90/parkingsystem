package com.parkit.parkingsystem.dao;

import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

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
		parkingSpotDAO = new ParkingSpotDAO();
		//parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
		//ticketDAO = new TicketDAO();
		//ticketDAO.dataBaseConfig = dataBaseTestConfig;
		dataBasePrepareService = new DataBasePrepareService();
	}

	@BeforeEach
	private void setUpPerTest() throws Exception {
		when(inputReaderUtil.readSelection()).thenReturn(1);
		when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
		dataBasePrepareService.clearDataBaseEntries();
	}

	@AfterAll
	private static void tearDown() {
		DataBasePrepareService clear = new DataBasePrepareService();
		clear.clearDataBaseEntries();
	}
	@Disabled
	@Test
	void givenTheNeedOfAParkingNumber_whenParkingIsFull_thenReturnError() throws ParkingSpotDAOException {
		Connection connection = null;
		
		try {
			connection = DataBaseTestConfig.getConnection();
			// set parking entries to available
			connection.prepareStatement("update parking set available = false").execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DataBaseTestConfig.closeConnection(connection);
		}
		Assertions.assertThrows(ParkingSpotDAOException.class, () -> {parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);});
	}

}
