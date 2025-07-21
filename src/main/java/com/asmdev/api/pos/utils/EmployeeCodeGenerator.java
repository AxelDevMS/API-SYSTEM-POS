package com.asmdev.api.pos.utils;

import java.security.SecureRandom;

public class EmployeeCodeGenerator {

    private static final String PREFIX = "EMP";
    private static final String SUFFIX = "POS";
    private static final int RANDOM_LENGTH = 9;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom random = new SecureRandom();

    public static String generateCodeEmployee(){
        StringBuilder randomPart = new StringBuilder(RANDOM_LENGTH);
        for (int i = 0; i < RANDOM_LENGTH; i++){
            int index = random.nextInt(CHARACTERS.length());
            randomPart.append(CHARACTERS.charAt(index));
        }
        return String.format("%s-%s-%s", PREFIX, randomPart, SUFFIX);
    }

}
