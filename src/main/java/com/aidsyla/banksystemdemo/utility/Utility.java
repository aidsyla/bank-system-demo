package com.aidsyla.banksystemdemo.utility;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Utility {

    public static int getIntInput(Scanner sc) {
        int choice = -1;
        boolean valid = false;

        while (!valid) {
            try {
                choice = sc.nextInt();
                valid = true; // valid input
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                sc.next(); // consume the invalid input
            }
        }

        return choice;
    }
}
