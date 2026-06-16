package com.enviro.assessment.junior.asandile;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordVerifier {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String rawPassword = "enviro365_2024";
        String encodedPassword = "$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5E4b4UuBzG4qGZ5zjBqW3Kq";

        boolean matches = encoder.matches(rawPassword, encodedPassword);
        System.out.println("Password matches: " + matches);
        // Should print: Password matches: true

        // Generate new encoded password if needed
        String newEncoded = encoder.encode("enviro365_2024");
        System.out.println("New encoded password: " + newEncoded);
    }
}