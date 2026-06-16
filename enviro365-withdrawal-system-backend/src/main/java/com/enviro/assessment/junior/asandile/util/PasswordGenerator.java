// util/PasswordGenerator.java
package com.enviro.assessment.junior.asandile.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Utility class for generating BCrypt encoded passwords
 * Only used for initial data setup, not for runtime
 *
 * @author Asandile
 * @version 1.0
 */
public class PasswordGenerator {

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * Generate BCrypt hash for a given raw password
     *
     * @param rawPassword the raw password to encode
     * @return BCrypt encoded password
     */
    public static String generateHash(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    /**
     * Verify if a raw password matches an encoded password
     *
     * @param rawPassword the raw password
     * @param encodedPassword the encoded password from database
     * @return true if matches
     */
    public static boolean verifyPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}