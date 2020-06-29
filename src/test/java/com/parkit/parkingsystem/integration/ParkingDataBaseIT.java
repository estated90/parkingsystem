package com.parkit.parkingsystem.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.awaitility.Awaitility.await;

import java.util.concurrent.Callable;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.config.ParkingSpotDAOException;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;

@ExtendWith(MockitoExtension.class)
class ParkingDataBaseIT {

	//private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
	private static ParkingSpotDAO parkingSpotDAO;
	private static TicketDAO ticketDAO;
	private static Ticket ticket;

	@Mock
	private static InputReaderUtil inputReaderUtil;

	@BeforeAll
	private static void setUp() throws Exception {
		DataBaseConfig.setTest(false);
		parkingSpotDAO = new ParkingSpotDAO();
		parkingSpotDAO.setDataBaseConfig(new DataBaseTestConfig());
		ticketDAO = new TicketDAO();
		ticketDAO.setDataBaseConfig(new DataBaseTestConfig());
		new DataBasePrepareService();
	}

	@BeforeEach
	private void setUpPerTest() throws Exception {
		when(inputReaderUtil.readSelection()).thenReturn(1);
		when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
		//dataBasePrepareService.clearDataBaseEntries();
	}
	
	@AfterEach
	private void closeParkingSpot() {
		ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		parkingService.processExitingVehicle();
	}

	@AfterAll
	private static void tearDown() {
		DataBasePrepareService clear = new DataBasePrepareService();
		clear.clearDataBaseEntries();
	}

	@Test
	void givenParkingACar_whenIncommingVehicle_thenImmatriculationInDB() {
		ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		parkingService.processIncomingVehicle();
		ticket = ticketDAO.getTicket("ABCDEF");
		assertEquals("ABCDEF", ticket.getVehicleRegNumber());
	}

	@Test
	void givenTheUpdateToDB_whenUserEnterTheParking_thenNextAvailableSpotIsAvailable() throws ParkingSpotDAOException {
		// GIVEN
		ParkingService parkingService = 	new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		ParkingType parkingType = 			ParkingType.CAR;
		int parkingNumber = 				0;
		int parkingNumberOrigin = 			0;
		parkingNumberOrigin = parkingSpotDAO.getNextAvailableSlot(parkingType);
		parkingService.processIncomingVehicle();
		// WHEN
		parkingNumber = parkingSpotDAO.getNextAvailableSlot(parkingType);
		// THEN
		assertEquals(parkingNumberOrigin+1, parkingNumber);
	}

	@Test
	void givenVehicleExiting_whenExitConfirm_thenDBisupdatedWithOutTimeAndPrice() throws ParkingSpotDAOException {
		ParkingType parkingType = 		ParkingType.CAR;
		ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		parkingService.processIncomingVehicle();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		parkingService.processExitingVehicle();
		assertEquals(1, parkingSpotDAO.getNextAvailableSlot(parkingType));
	}

}
