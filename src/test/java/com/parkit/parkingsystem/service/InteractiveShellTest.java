package com.parkit.parkingsystem.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.DriverManager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.util.InputReaderUtil;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DriverManager.class)
@ExtendWith(MockitoExtension.class)
class InteractiveShellTest {
	
	@Mock
	InteractiveShell interactiveShell;
	@Mock
	private static ParkingService parkingService;
	@Mock
	private static InputReaderUtil inputReaderUtil;
	@Mock
	private static ParkingSpotDAO parkingSpotDAO;
	@Mock
	private static TicketDAO ticketDAO;

	@BeforeEach
	void setUpPerTest() {
		when(inputReaderUtil.readSelection()).thenReturn(1);
		
		parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
	}

	@AfterEach
	void tearDown() throws Exception {
	}
	@Disabled
	@Test
	void testLoadInterface() throws Exception {
		when(inputReaderUtil.readSelection()).thenReturn(1);
		InteractiveShell.loadInterface();
		verify(parkingService, Mockito.times(1)).processIncomingVehicle();
	}

}
