package com.parkit.parkingsystem.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;

class TicketDAOTest {
	private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
	private static TicketDAO ticketDAO;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		ticketDAO = new TicketDAO();
		ticketDAO.setDataBaseConfig(dataBaseTestConfig);
	}

	@AfterAll
	private static void tearDown() {
		DataBasePrepareService clear = new DataBasePrepareService();
		clear.clearDataBaseEntries();
	}

	@Test
	void givenSavedTicked_whenSavingTicket_thenTicketInDB() {
		Ticket ticket = new Ticket();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		ticket.setParkingSpot(parkingSpot);
		ticket.setVehicleRegNumber("ABCDEF");
		ticket.setPrice(Fare.CAR_RATE_PER_HOUR);
		LocalDateTime inTime = LocalDateTime.now(ZoneId.systemDefault());
		ticket.setInTime(inTime);
		boolean isFalse = ticketDAO.saveTicket(ticket);
		assertEquals(isFalse, Boolean.FALSE);
	}

	@Test
	public void givenSavingTicket_whenNoTicketProvided_thenReturnException() throws SQLException {
		Ticket ticket = null;
		assertThrows(NullPointerException.class, () -> ticketDAO.saveTicket(ticket));
	}

	@Test
	public void givenNeedToUpdateTicket_whenInputingNewData_thenUpdateTicket()
			throws SQLException, ClassNotFoundException, IOException {
		Ticket ticket = new Ticket();
		ticket.setPrice(Fare.CAR_RATE_PER_HOUR);
		LocalDateTime outTime = LocalDateTime.now(ZoneId.systemDefault());
		ticket.setOutTime(outTime);
		boolean isTrue = ticketDAO.updateTicket(ticket);
		assertEquals(isTrue, Boolean.TRUE);

	}

	@Test
	public void givenUpdatingTicket_whenNoTicketProvided_thenReturnException() throws SQLException {
		Ticket ticket = null;
		assertThrows(NullPointerException.class, () -> ticketDAO.updateTicket(ticket));
	}
}
