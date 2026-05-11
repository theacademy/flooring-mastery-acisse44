package com.sg.flooringmastery.ui;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class UserIOConsoleImpl implements UserIO {

    Scanner myScanner = new Scanner(System.in);

    @Override
    public void print(String msg) {
        System.out.println(msg);
    }

    @Override
    public String readString(String prompt) {
        System.out.println(prompt);
        return myScanner.nextLine();
    }

    @Override
    public int readInt(String prompt) {
        while (true) {
            System.out.println(prompt);
            try {
                return Integer.parseInt(myScanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    @Override
    public int readInt(String prompt, int min, int max) {
        int num;
        do {
            num = readInt(prompt);
        } while (num < min || num > max);
        return num;
    }

    @Override
    public BigDecimal readBigDecimal(String prompt) {
        while (true) {
            System.out.println(prompt);
            try {
                return new BigDecimal(myScanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a decimal number.");
            }
        }
    }

    @Override
    public LocalDate readLocalDate(String prompt) {
        while (true) {
            System.out.println(prompt);
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
                return LocalDate.parse(myScanner.nextLine(), formatter);
            } catch (Exception e) {
                System.out.println("Invalid date format. Please use MM-DD-YYYY.");
            }
        }
    }
}