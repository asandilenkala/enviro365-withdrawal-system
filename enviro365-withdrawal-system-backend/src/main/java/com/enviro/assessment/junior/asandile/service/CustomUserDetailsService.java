package com.enviro.assessment.junior.asandile.service;

import com.enviro.assessment.junior.asandile.model.User;
import com.enviro.assessment.junior.asandile.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Custom UserDetailsService for database authentication
 *
 * @author Asandile
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Loading user by username: {}", username);

        try {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> {
                        log.error("User not found with username: {}", username);
                        return new UsernameNotFoundException("User not found with username: " + username);
                    });

            log.info("User found: {} with role: {}", user.getUsername(), user.getRole());
            return user;
        } catch (UsernameNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error loading user: {}", e.getMessage());
            throw new RuntimeException("Error loading user", e);
        }
    }
}