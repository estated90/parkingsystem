package com.parkit.parkingsystem.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.util.InputReaderUtil;

@ExtendWith(MockitoExtension.class)
class InteractiveShellTest {

	@Mock
	InputReaderUtil inputReaderUtil;
	@Mock
	TicketDAO ticketDAO;
	@Mock
	ParkingService parkingService;
	@Mock
	static
	InteractiveShell classToTest;

	
	@BeforeAll
	static void init() {
		InteractiveShell classToTest = new InteractiveShell();
	}
	
	@Test
	void givenListOfChoice_whenEnteringValue_CallsService() throws IOException, URISyntaxException {
		InteractiveShell.loadInterface();
		verify(parkingService, Mockito.times(1)).processIncomingVehicle();
	}

}
