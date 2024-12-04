package com.example.vehicleService.util;

import java.security.SecureRandom;
import java.util.Base64;

public class GenerateJwtSecret {
    public static void main(String[] args) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);
        System.out.println(Base64.getUrlEncoder().withoutPadding().encodeToString(bytes));
    }
}
