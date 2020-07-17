package com.parkit.parkingsystem.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.Scanner;

import org.junit.Rule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.rules.ExpectedException;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InputReaderUtilTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();
	 
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void givenUserEntry_whenInputProvided_thenReturnEntry() {
		InputReaderUtil inputReaderUtil = new InputReaderUtil();
		inputReaderUtil.setScan(new Scanner("1"));
		assertEquals(1, inputReaderUtil.readSelection());
	}
	
	@Test
	void givenUserEntry_whenFalseInputProvided_thenReturnException() {
		InputReaderUtil inputReaderUtil = new InputReaderUtil();
		inputReaderUtil.setScan(new Scanner("a"));
		inputReaderUtil.readSelection();
		exception.expect(NumberFormatException.class);
    	exception.expectMessage("Error reading input. Please enter valid number for proceeding further");
	}
	
	@Test
	void givenUserEntryVehicleRegNumber_whenInputProvided_thenReturnEntry() throws IOException {
		InputReaderUtil inputReaderUtil = new InputReaderUtil();
		inputReaderUtil.setScan(new Scanner("ABCDEF"));
		assertEquals("ABCDEF", inputReaderUtil.readVehicleRegistrationNumber());
	}
	

}
