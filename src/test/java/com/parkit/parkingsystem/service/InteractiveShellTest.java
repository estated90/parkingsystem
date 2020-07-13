package com.parkit.parkingsystem.service;

import static org.easymock.EasyMock.expect;
import static org.mockito.Mockito.verify;
import static org.powermock.api.easymock.PowerMock.mockStatic;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.util.InputReaderUtil;

@RunWith(PowerMockRunner.class)
@PrepareForTest(InteractiveShell.class)
class InteractiveShellTest {

	private static InteractiveShell instance;
	@Mock
	private static ParkingService parkingService;
	@Mock
	private static InputReaderUtil inputReaderUtil;
	@Mock
	private static ParkingSpotDAO parkingSpotDAO;
	@Mock
	private static TicketDAO ticketDAO;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		instance = new InteractiveShell();
		parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void test() {
		mockStatic(InputReaderUtil.class);
		expect(inputReaderUtil.readSelection()).andReturn(1);
		verify(parkingService, Mockito.times(1)).processIncomingVehicle();
	}

}
