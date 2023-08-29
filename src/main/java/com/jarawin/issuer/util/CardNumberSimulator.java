package com.jarawin.issuer.util;

import java.util.Random;

public class CardNumberSimulator {

    // Generate a random credit card number
    public static Long generateRandomCardNumber() {
        Random random = new Random();
        StringBuilder cardNumber = new StringBuilder();

        // Generate the first digit randomly (between 3 and 6)
        int firstDigit = random.nextInt(4) + 3;
        cardNumber.append(firstDigit);

        // Generate the remaining 15 digits randomly
        for (int i = 0; i < 15; i++) {
            int digit = random.nextInt(10);
            cardNumber.append(digit);
        }

        return Long.parseLong(cardNumber.toString());
    }

    // Simulate a card number based on a given prefix and length
    public static Long simulateCardNumber(String prefix, int length) {
        StringBuilder cardNumber = new StringBuilder(prefix);
        Random random = new Random();

        // Generate the remaining digits randomly
        for (int i = 0; i < length - prefix.length(); i++) {
            int digit = random.nextInt(10);
            cardNumber.append(digit);
        }

        return Long.parseLong(cardNumber.toString());
    }

    public static Long simulateCvv() {
        Random random = new Random();
        StringBuilder cvv = new StringBuilder();

        // Generate the first digit randomly (between 3 and 6)
        int firstDigit = random.nextInt(4) + 3;
        cvv.append(firstDigit);

        // Generate the remaining 2 digits randomly
        for (int i = 0; i < 2; i++) {
            int digit = random.nextInt(10);
            cvv.append(digit);
        }

        return Long.parseLong(cvv.toString());
    }

    // Example usage
    public static void main(String[] args) {
        // Generate a random credit card number
        Long randomCardNumber = generateRandomCardNumber();
        System.out.println("Random Card Number: " + randomCardNumber);

        // Simulate a card number with a given prefix and length
        Long simulatedCardNumber = simulateCardNumber("1234", 16);
        System.out.println("Simulated Card Number: " + simulatedCardNumber);

        // Simulate a cvv
        Long simulatedCvv = simulateCvv();
        System.out.println("Simulated Cvv: " + simulatedCvv);
    }
}
