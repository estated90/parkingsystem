package com.parkit.parkingsystem.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.matchers.JUnitMatchers;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.parkit.parkingsystem.config.ParkingSpotDAOException;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.util.InputReaderUtil;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ParkingServiceTest {

	@Mock
    private static ParkingService parkingService;
    @Mock
    private static InputReaderUtil inputReaderUtil;
    @Mock
    private static ParkingSpotDAO parkingSpotDAO;
    @Mock
    private static TicketDAO ticketDAO;
    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    @BeforeEach
    void setUpPerTest() throws IOException, ParkingSpotDAOException {
        when(inputReaderUtil.readSelection()).thenReturn(1);
		when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
		Ticket ticket = new Ticket();
		ticket.setInTime(LocalDateTime.now(ZoneId.systemDefault()).minusHours(1));
		ticket.setParkingSpot(parkingSpot);
		ticket.setVehicleRegNumber("ABCDEF");
		when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
		when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);
		when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);

		when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);

		parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
    }
    
    @Test
    void processExitingVehicleTest(){
        parkingService.processExitingVehicle();
        verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
    }
    
    @Test
    void processIncomingVehicle() throws Exception{
        parkingService.processIncomingVehicle();
        verify(inputReaderUtil, Mockito.times(1)).readVehicleRegistrationNumber();
        verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
        verify(ticketDAO, Mockito.times(1)).saveTicket(any());
    }
    
    @Test
    void givenEnteringParking_whenParkingIsFull_thenReturnError() throws Exception{
    	when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(-1);
    	parkingService.processIncomingVehicle();
    	exception.expect(ParkinkFullException.class);
    	exception.expectMessage("Unable to process incoming vehicle");
    }
    
    @Test
    void givenGettingAvailableSpot_whenParkingIsFull_thenReturnError() throws Exception{
    	when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(-1);
    	parkingService.getNextParkingNumberIfAvailable();
    	exception.expect(ParkinkFullException.class);
    	exception.expectMessage("Error fetching next available parking slot");
    }
    
    @Test
    void givenBikeEntering_whenIncomingVehicle_thenAssertMethodAreCalled() throws Exception{
    	when(inputReaderUtil.readSelection()).thenReturn(2);
        parkingService.processIncomingVehicle();
        verify(inputReaderUtil, Mockito.times(1)).readVehicleRegistrationNumber();
        verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
        verify(ticketDAO, Mockito.times(1)).saveTicket(any());
    }
    @Test
    void givenEntering_whenIncomingVehicleGiveNoAnswer_thenReturnIllegalArgument() throws Exception{
    	when(inputReaderUtil.readSelection()).thenReturn(3);
        parkingService.processIncomingVehicle();
        exception.expect(IllegalArgumentException.class);
    	exception.expectMessage("Incorrect input provided");
    }
}
