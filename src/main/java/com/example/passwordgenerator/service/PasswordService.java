package com.example.passwordgenerator.service;

import java.security.SecureRandom;

public class PasswordService {
    private static final String NUMBERS = "0123456789";
    private static final String LETTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String SYMBOLS = "!@#$%^&*()_-+=<>?/{}[]|";

    public String generatePassword(int length, int complexity) {
        String characters = NUMBERS;

        if (complexity >= 2) {
            characters += LETTERS;
        }
        if (complexity >= 3) {
            characters += SYMBOLS;
        }

        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            password.append(characters.charAt(index));
        }

        return password.toString();
    }
}
