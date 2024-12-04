package com.example.vehicleService.dto;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordEncoderGenerator {
    public static void main(String[] args) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.out.println(passwordEncoder.encode("admin") + "    dcm");
        //$2a$10$jUMJ6pB2A8s22WXjg2Da5uJbJPmr0vfrMUzObsYPsjl3mo5GnZ5Wy
    }
}