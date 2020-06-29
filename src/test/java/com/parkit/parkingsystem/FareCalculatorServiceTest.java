package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.PromotionRecurringUserDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
class FareCalculatorServiceTest {

	private Ticket ticket;

	@Mock
	private static PromotionRecurringUserDAO promotionRecurringUser;
	@InjectMocks
	private static FareCalculatorService fareCalculatorService;

	@BeforeAll
	private static void init() {
		fareCalculatorService = new FareCalculatorService();
	}

	@BeforeEach
	private void setUpPerTest() {
		ticket = new Ticket();
	}

	@Test
	void givenCarStayForOneHour_whenCalculatingFare_thenFareEqualtoFareOneHour() {
		// GIVEN
		when(promotionRecurringUser.promotionRecurringUser("ABCDE")).thenReturn(0);
		LocalDateTime inTime = LocalDateTime.now().minusMinutes(60);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		// WHEN
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		// THEN
		fareCalculatorService.calculateFare(ticket, "ABCDE");
		assertEquals(Fare.CAR_RATE_PER_HOUR,ticket.getPrice());
	}

	@Test
	void givenBikeStayForOneHour_whenCalculatingFare_thenFareEqualtoFareOneHour() {
		// GIVEN
		when(promotionRecurringUser.promotionRecurringUser("ABCDE")).thenReturn(0);
		LocalDateTime inTime = LocalDateTime.now().minusMinutes(60);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
		// WHEN
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		// THEN
		fareCalculatorService.calculateFare(ticket, "ABCDE");
		assertEquals(Fare.BIKE_RATE_PER_HOUR, ticket.getPrice());
	}

	@Test
	void givenNoTypeVehicle_whenCalculatingFare_thenReturnException() {
		// GIVEN
		LocalDateTime inTime = LocalDateTime.now().minusMinutes(60);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, null, false);
		// WHEN
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		// THEN
		assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket, "ABCDE"));
	}

	@Test
	void givenEntryAfterExit_whenCalculatingFare_thenReturnException() {
		// GIVEN
		LocalDateTime inTime = LocalDateTime.now().plusMinutes(60);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
		// WHEN
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		// THEN
		assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket, "ABCDE"));
	}

	@Test
	void givenBikeStayForLessThanOneHour_whenCalculatingFare_thenFareEqualtoThreeQuarterOfPrice() {
		// GIVEN
		when(promotionRecurringUser.promotionRecurringUser("ABCDE")).thenReturn(0);
		LocalDateTime inTime = LocalDateTime.now().minusMinutes(45);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
		// WHEN
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		// THEN
		fareCalculatorService.calculateFare(ticket, "ABCDE");
		assertEquals((0.75 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
	}

	@Test
	void givenCarStayForLessThanOneHour_whenCalculatingFare_thenFareEqualtoThreeQuarterOfPrice() {
		// GIVEN
		when(promotionRecurringUser.promotionRecurringUser("ABCDE")).thenReturn(0);
		LocalDateTime inTime = LocalDateTime.now().minusMinutes(45);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		// WHEN
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		// THEN
		fareCalculatorService.calculateFare(ticket, "ABCDE");
		assertEquals(((double) Math.round(0.75 * Fare.CAR_RATE_PER_HOUR * 100) / 100), ticket.getPrice());

	}

	@Test
	void givenCarStayForOneDay_whenCalculatingFare_thenFareEqualtoFullDayPrice() {
		// GIVEN
		when(promotionRecurringUser.promotionRecurringUser("ABCDE")).thenReturn(0);
		LocalDateTime inTime = LocalDateTime.now();
		LocalDateTime outTime = LocalDateTime.now().plusDays(1);
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		// WHEN
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		// THEN
		fareCalculatorService.calculateFare(ticket, "ABCDE");
		assertEquals((24 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
	}

	@Test
	void givenBikeStayForOneDay_whenCalculatingFare_thenFareEqualtoFullDayPrice() {
		// GIVEN
		when(promotionRecurringUser.promotionRecurringUser("ABCDE")).thenReturn(0);
		LocalDateTime inTime = LocalDateTime.now();
		LocalDateTime outTime = LocalDateTime.now().plusDays(1);
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
		// WHEN
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		// THEN
		fareCalculatorService.calculateFare(ticket, "ABCDE");
		assertEquals((24 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
	}

	@Test
	void givenCarStayForThirtyMinutes_whenCalculatingFare_thenFareEqualtoZero() {
		// GIVEN
		LocalDateTime inTime = LocalDateTime.now();
		LocalDateTime outTime = LocalDateTime.now().plusMinutes(30);
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		// WHEN
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		// THEN
		fareCalculatorService.calculateFare(ticket, "ABCDE");
		assertEquals(0, ticket.getPrice());
	}

	@Test
	void givenBikeStayForThirtyMinutes_whenCalculatingFare_thenFareEqualtoZero() {
		// GIVEN
		LocalDateTime inTime = LocalDateTime.now();
		LocalDateTime outTime = LocalDateTime.now().plusMinutes(30);
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
		// WHEN
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		// THEN
		fareCalculatorService.calculateFare(ticket, "ABCDE");
		assertEquals(0, ticket.getPrice());
	}

	@Test
	void givenCarAlreadyUseService_WhenCalculatingFare_thenADiscountApplied() {
		// GIVEN
		when(promotionRecurringUser.promotionRecurringUser("ABCDE")).thenReturn(1);
		LocalDateTime inTime = LocalDateTime.now().minusMinutes(60);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		// WHEN
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		// THEN
		fareCalculatorService.calculateFare(ticket, "ABCDE");
		assertEquals(((double) Math.round(Fare.CAR_RATE_PER_HOUR * 0.95 * 100 ) / 100), ticket.getPrice());
	}
	@Test
	void givenBikeAlreadyUseService_WhenCalculatingFare_thenADiscountApplied() {
		// GIVEN
		when(promotionRecurringUser.promotionRecurringUser("ABCDE")).thenReturn(1);
		LocalDateTime inTime = LocalDateTime.now().minusMinutes(60);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
		// WHEN
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		// THEN
		fareCalculatorService.calculateFare(ticket, "ABCDE");
		assertEquals(((double) Math.round(Fare.BIKE_RATE_PER_HOUR * 0.95 * 100 ) / 100), ticket.getPrice());
	}
}
