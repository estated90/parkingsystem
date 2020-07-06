package com.parkit.parkingsystem.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.SQLException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mock;

import com.parkit.parkingsystem.config.ParkingSpotDAOException;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.util.InputReaderUtil;

@TestMethodOrder(OrderAnnotation.class)
class ParkingSpotDAOTest {

	private static ParkingSpotDAO parkingSpotDAO;
	private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
	@Mock
	private static InputReaderUtil inputReaderUtil;

	@BeforeAll
	private static void setUp() throws Exception {
		parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
	}

	@AfterAll
	private static void tearDownClass() {
		DataBasePrepareService dataBasePrepareService = new DataBasePrepareService();
		dataBasePrepareService.clearDataBaseEntries();
	}

	@Test
	@Order(1)
	void givenNewCarEntering_whenAsked_thenReturnNextAvailableSpot() {
		try {
			int nextSpot = 0;
			nextSpot = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);
			assertEquals(1, nextSpot);

		} catch (ParkingSpotDAOException e) {
			e.printStackTrace();
		}
	}

	@Test
	@Order(2)
	void givenNewBikeEntering_whenAsked_thenReturnNextAvailableSpot() {
		try {
			int nextSpot = 0;
			nextSpot = parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE);
			assertEquals(4, nextSpot);

		} catch (ParkingSpotDAOException e) {
			e.printStackTrace();
		}
	}

	@Test
	@Order(3)
	public void testUpdateParking() throws SQLException {

		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		boolean isTrue = parkingSpotDAO.updateParking(parkingSpot);
		assertEquals(isTrue, Boolean.TRUE);

	}

}
