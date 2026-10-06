package com.globallogic.sistema.gestion.utils;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class RandomPasswordGenerator {
    private static final String CHAR_POOL = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+";

    public String generateRandomPassword(int length){
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(CHAR_POOL.length());
            password.append(CHAR_POOL.charAt(index));
        }

        return password.toString();
    }
}
