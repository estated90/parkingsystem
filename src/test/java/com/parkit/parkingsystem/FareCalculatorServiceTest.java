package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
public class FareCalculatorServiceTest {

	private static FareCalculatorService fareCalculatorService;
	private Ticket ticket;

	
	@Mock
	DBConstants GET_EXISTING_VEHICLE;
	
	@BeforeAll
	private static void setUp() {
		fareCalculatorService = new FareCalculatorService();
	}

	@BeforeEach
	private void setUpPerTest() {
		ticket = new Ticket();
	}

	@Test
	public void givenCarStayForOneHour_whenCalculatingFare_thenFareEqualtoFareOneHour() {
		// GIVEN
		LocalDateTime inTime = LocalDateTime.now().minusMinutes(60);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		// WHEN
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		// THEN
		fareCalculatorService.calculateFare(ticket);
		assertEquals(ticket.getPrice(), Fare.CAR_RATE_PER_HOUR);
	}

	@Test
	public void givenBikeStayForOneHour_whenCalculatingFare_thenFareEqualtoFareOneHour() {
		// GIVEN
		LocalDateTime inTime = LocalDateTime.now().minusMinutes(60);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
		// WHEN
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		// THEN
		fareCalculatorService.calculateFare(ticket);
		assertEquals(ticket.getPrice(), Fare.BIKE_RATE_PER_HOUR);
	}

	@Test
	public void givenNoTypeVehicle_whenCalculatingFare_thenReturnException() {
		// GIVEN
		LocalDateTime inTime = LocalDateTime.now().minusMinutes(60);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, null, false);
		// WHEN
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		// THEN
		assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
	}

	@Test
	public void givenEntryAfterExit_whenCalculatingFare_thenReturnException() {
		// GIVEN
		LocalDateTime inTime = LocalDateTime.now().plusMinutes(60);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
		// WHEN
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		// THEN
		assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
	}

	@Test
	public void givenBikeStayForLessThanOneHour_whenCalculatingFare_thenFareEqualtoThreeQuarterOfPrice() {
		// GIVEN
		LocalDateTime inTime = LocalDateTime.now().minusMinutes(45);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
		// WHEN
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		// THEN
		fareCalculatorService.calculateFare(ticket);
		assertEquals((0.75 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
	}

	@Test
	public void givenCarStayForLessThanOneHour_whenCalculatingFare_thenFareEqualtoThreeQuarterOfPrice() {
		// GIVEN
		LocalDateTime inTime = LocalDateTime.now().minusMinutes(45);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		// WHEN
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		// THEN
		fareCalculatorService.calculateFare(ticket);
		assertEquals((0.75 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
	}

	@Test
	public void givenCarStayForOneDay_whenCalculatingFare_thenFareEqualtoFullDayPrice() {
		// GIVEN
		LocalDateTime inTime = LocalDateTime.now();
		LocalDateTime outTime = LocalDateTime.now().plusDays(1);
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		// WHEN
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		// THEN
		fareCalculatorService.calculateFare(ticket);
		assertEquals((24 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
	}

	@Test
	public void givenBikeStayForOneDay_whenCalculatingFare_thenFareEqualtoFullDayPrice() {
		// GIVEN
		LocalDateTime inTime = LocalDateTime.now();
		LocalDateTime outTime = LocalDateTime.now().plusDays(1);
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
		// WHEN
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		// THEN
		fareCalculatorService.calculateFare(ticket);
		assertEquals((24 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
	}

	@Test
	public void givenCarStayForThirtyMinutes_whenCalculatingFare_thenFareEqualtoZero() {
		// GIVEN
		LocalDateTime inTime = LocalDateTime.now();
		LocalDateTime outTime = LocalDateTime.now().plusMinutes(30);
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		// WHEN
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		// THEN
		fareCalculatorService.calculateFare(ticket);
		assertEquals(0, ticket.getPrice());
	}

	@Test
	public void givenBikeStayForThirtyMinutes_whenCalculatingFare_thenFareEqualtoZero() {
		// GIVEN
		LocalDateTime inTime = LocalDateTime.now();
		LocalDateTime outTime = LocalDateTime.now().plusMinutes(30);
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
		// WHEN
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		// THEN
		fareCalculatorService.calculateFare(ticket);
		assertEquals(0, ticket.getPrice());
	}

	@Test
	public void givenVehicleAlreadyUseService_WhenCalculatingFare_thenADiscountApplied() {
		// GIVEN
		LocalDateTime inTime = LocalDateTime.now().minusMinutes(60);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		// WHEN
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		// THEN
		fareCalculatorService.calculateFare(ticket);
		assertEquals(ticket.getPrice() * (95 / 100), Fare.CAR_RATE_PER_HOUR);
	}
}
