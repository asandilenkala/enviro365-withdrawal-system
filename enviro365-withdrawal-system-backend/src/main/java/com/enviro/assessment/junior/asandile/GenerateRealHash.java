package com.enviro.assessment.junior.asandile;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GenerateRealHash {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = "enviro365_2024";
        String hash = encoder.encode(password);
        System.out.println("=========================================");
        System.out.println("Password: " + password);
        System.out.println("Hash: " + hash);
        System.out.println("=========================================");
        System.out.println("Copy this SQL to H2 Console:");
        System.out.println();
        System.out.println("UPDATE users SET password = '" + hash + "' WHERE username = 'enviro_admin';");
        System.out.println();
        System.out.println("UPDATE users SET password = '" + hash + "' WHERE role = 'INVESTOR';");
    }
}