package com.gelitix.backend.users.service.impl;

import java.security.SecureRandom;

public class RandomStringGenerator {

    // Define the characters allowed in the random string
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    // SecureRandom is preferred over Random for cryptographic security
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generateRandomString(int length) {
        // StringBuilder to store the generated random string
        StringBuilder sb = new StringBuilder(length);

        // Loop to generate each character in the random string
        for (int i = 0; i < length; i++) {
            int randomIndex = RANDOM.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(randomIndex));
        }

        return sb.toString();
    }

    public static void main(String[] args) {
        // Generate a 6-character random string
        String randomString = generateRandomString(6);
        System.out.println("Random String: " + randomString);
    }
}