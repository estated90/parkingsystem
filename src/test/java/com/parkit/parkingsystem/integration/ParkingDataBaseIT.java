package com.parkit.parkingsystem.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@TestMethodOrder(OrderAnnotation.class)
class ParkingDataBaseIT {
	private static ParkingSpotDAO parkingSpotDAO;
	private static TicketDAO ticketDAO;
	private static FareCalculatorService fareCalculatorService;
	private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();

	@Mock
	private static InputReaderUtil inputReaderUtil;

	@BeforeAll
	private static void setUp() throws Exception {
		parkingSpotDAO = new ParkingSpotDAO();
		parkingSpotDAO.setDataBaseConfig(dataBaseTestConfig);
		ticketDAO = new TicketDAO();
		ticketDAO.setDataBaseConfig(dataBaseTestConfig);
		new DataBasePrepareService();
		fareCalculatorService = new FareCalculatorService();
	}

	@BeforeEach
	private void setUpPerTest() throws Exception {
		when(inputReaderUtil.readSelection()).thenReturn(1);
		when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
	}

	@AfterAll
	private static void tearDown() {
		DataBasePrepareService clear = new DataBasePrepareService();
		clear.clearDataBaseEntries();
	}

	@Test
	@Order(1)
	void givenParkingACar_whenIncommingVehicle_thenDataUpdatedInDB() {
		ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		parkingService.processIncomingVehicle();
		try {
			Ticket ticket = ticketDAO.getTicket("ABCDEF");
			assertNotNull(ticket);
			assertNotEquals(1, parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR));
			assertEquals(2, parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR));
			ticket.setInTime(LocalDateTime.now().minusMinutes(60));
			ticketDAO.updateInTimeTicket(ticket);
			parkingService.processExitingVehicle();
			ticket = ticketDAO.getTicket("ABCDEF");
			assertNotEquals(0, ticket.getPrice());
			assertNotNull(ticket.getInTime());
			assertNotNull(ticket.getOutTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	@Order(2)
	void givenParkingSameCar_whenExitingVehicle_thenPromotion() {
		ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		parkingService.processIncomingVehicle();
		try {
			Ticket ticket2 = ticketDAO.getTicket("ABCDEF");
			LocalDateTime timeInUse2 = LocalDateTime.now();
			ticket2.setInTime(timeInUse2);
			ticketDAO.updateInTimeTicket(ticket2);
			ticket2.setOutTime(timeInUse2.plusHours(1));
			ticketDAO.updateTicket(ticket2);
			fareCalculatorService.calculateFare(ticket2);
			ticketDAO.updateTicket(ticket2);
			assertTrue(ticket2.getIsRecurring());
			assertEquals(((double) Math.round(Fare.CAR_RATE_PER_HOUR * 0.95 * 100) / 100), ticket2.getPrice());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
