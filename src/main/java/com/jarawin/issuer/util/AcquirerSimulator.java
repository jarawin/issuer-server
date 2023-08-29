package com.jarawin.issuer.util;

import java.util.UUID;

public class AcquirerSimulator {
    public static String generateAcquirerId() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().toUpperCase().replace("-", "");
    }

    public static void main(String[] args) {
        String acquirerId = generateAcquirerId();
        System.out.println("Generated Acquirer ID: " + acquirerId);
    }
}
