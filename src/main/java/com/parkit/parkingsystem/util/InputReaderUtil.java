package com.parkit.parkingsystem.util;

import java.io.IOException;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class InputReaderUtil {

    private Scanner scan = new Scanner(System.in, "UTF-8");
    private final Logger logger = LogManager.getLogger("InputReaderUtil");

    public int readSelection() {
        try {
            return Integer.parseInt(scan.nextLine());
        }catch(NumberFormatException  e){
            logger.error("Error while reading user input from Shell", e);
            System.out.println("Error reading input. Please enter valid number for proceeding further");
            return -1;
        }
    }

    public String readVehicleRegistrationNumber() throws IOException {
        try {
            String vehicleRegNumber= scan.nextLine();
            if(vehicleRegNumber == null || vehicleRegNumber.trim().length()==0) {
                throw new IOException("Invalid input provided");
            }
            return vehicleRegNumber;
        }catch(IOException e){
            logger.error("Error while reading user input from Shell", e);
            System.out.println("Error reading input. Please enter a valid string for vehicle registration number");
            throw e;
        }
    }

	public void setScan(Scanner scan) {
		this.scan = scan;
	}


}
