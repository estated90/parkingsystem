package com.parkit.parkingsystem.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.config.Url;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

	private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
	private static ParkingSpotDAO parkingSpotDAO;
	private static TicketDAO ticketDAO;
	private static DataBasePrepareService dataBasePrepareService;
	private static Ticket ticket;

	@Mock
	private static InputReaderUtil inputReaderUtil;
	@Mock
	private static Url url;

	@BeforeAll
	private static void setUp() throws Exception {
		DataBaseConfig.isTest = false;
		parkingSpotDAO = new ParkingSpotDAO();
		parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
		ticketDAO = new TicketDAO();
		ticketDAO.dataBaseConfig = dataBaseTestConfig;
		dataBasePrepareService = new DataBasePrepareService();
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

	}

	@Test
	public void givenParkingACar_whenIncommingVehicle_thenImmatriculationInDB() {
		ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		parkingService.processIncomingVehicle();
		ticket = ticketDAO.getTicket("ABCDEF");
		assertEquals("ABCDEF", ticket.getVehicleRegNumber());
	}

	@Test
	public void givenTheUpdateToDB_whenUserEnterTheParking_thenNextAvailableSpotIsAvailable() {
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
		assertEquals(parkingNumberOrigin + 1, parkingNumber);
	}

	@Test
	public void givenVehicleExiting_whenExitConfirm_thenDBisupdatedWithOutTimeAndPrice() {
		ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		parkingService.processIncomingVehicle();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		parkingService.processExitingVehicle();
		parkingService.processIncomingVehicle();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		parkingService.processExitingVehicle();
		ParkingType parkingType = ParkingType.CAR;
		// TODO: check that the fare generated and out time are populated correctly in
		// the database
		assertEquals(1, parkingSpotDAO.getNextAvailableSlot(parkingType));
	}

	@Disabled
	@Test
	public void givenNewVihiculeInParking_whenEnteringAndConnectionFail_thenReturnError() {
		ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		try {
			when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
		} catch (Exception e) {
			e.printStackTrace();
		}
		parkingService.processIncomingVehicle();
		try {
			when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEFG");
		} catch (Exception e) {
			e.printStackTrace();
		}
		parkingService.processIncomingVehicle();
		try {
			when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEFGH");
		} catch (Exception e) {
			e.printStackTrace();
		}
		parkingService.processIncomingVehicle();
		try {
			when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEFGHI");
		} catch (Exception e) {
			e.printStackTrace();
		}
		parkingService.processIncomingVehicle();
		assertThrows(Exception.class, () -> parkingService.processIncomingVehicle());
	}
}
