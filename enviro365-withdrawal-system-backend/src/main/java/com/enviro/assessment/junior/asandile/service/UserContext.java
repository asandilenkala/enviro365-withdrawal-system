// service/UserContext.java
package com.enviro.assessment.junior.asandile.service;

import com.enviro.assessment.junior.asandile.model.User;
import com.enviro.assessment.junior.asandile.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * User Context Service for accessing current user information across the application
 *
 * @author Asandile
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class UserContext {

    private final UserRepository userRepository;
    private final SessionService sessionService;

    /**
     * Get current authenticated user
     *
     * @return User entity
     * @throws SecurityException if user not authenticated
     */
    public User getCurrentUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null) {
                log.error("No authentication found in SecurityContext");
                throw new SecurityException("User not authenticated");
            }

            // Check if principal is already a User object
            if (authentication.getPrincipal() instanceof User) {
                User user = (User) authentication.getPrincipal();
                log.debug("User found in authentication principal: {}", user.getUsername());
                return user;
            }

            // If principal is a String (username), look up the user
            String username = authentication.getName();
            if (username != null && !username.equals("anonymousUser")) {
                log.debug("Looking up user by username: {}", username);
                User user = userRepository.findByUsernameWithInvestor(username)
                        .orElseThrow(() -> {
                            log.error("User not found with username: {}", username);
                            return new SecurityException("User not found");
                        });
                log.debug("User found: {}", user.getUsername());
                return user;
            }

            // Try to get from session
            String sessionId = getSessionIdFromRequest();
            if (sessionId != null) {
                SessionService.UserSession session = sessionService.getSession(sessionId);
                if (session != null) {
                    User user = userRepository.findById(session.getUserId())
                            .orElseThrow(() -> new SecurityException("User not found with ID: " + session.getUserId()));
                    log.debug("User found from session: {}", user.getUsername());
                    return user;
                }
            }

            throw new SecurityException("User not authenticated");
        } catch (SecurityException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error getting current user: {}", e.getMessage(), e);
            throw new SecurityException("Failed to get current user", e);
        }
    }

    /**
     * Get current authenticated user ID
     *
     * @return user ID
     * @throws SecurityException if user not authenticated
     */
    public UUID getCurrentUserId() {
        try {
            User user = getCurrentUser();
            return user.getId();
        } catch (SecurityException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error getting current user ID: {}", e.getMessage());
            throw new SecurityException("Failed to get current user ID", e);
        }
    }

    /**
     * Get current user email
     *
     * @return user email
     * @throws SecurityException if user not authenticated
     */
    public String getCurrentUserEmail() {
        try {
            User user = getCurrentUser();
            return user.getEmail();
        } catch (SecurityException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error getting current user email: {}", e.getMessage());
            throw new SecurityException("Failed to get current user email", e);
        }
    }

    /**
     * Get current user role
     *
     * @return user role or null if not authenticated
     */
    public String getCurrentUserRole() {
        try {
            User user = getCurrentUser();
            return user.getRole();
        } catch (SecurityException e) {
            return null;
        } catch (Exception e) {
            log.error("Error getting current user role: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Get current user's investor ID (if investor role)
     *
     * @return investor ID or null
     */
    public UUID getCurrentInvestorId() {
        try {
            User user = getCurrentUser();
            if (user.getInvestor() != null) {
                return user.getInvestor().getId();
            }
            return null;
        } catch (SecurityException e) {
            return null;
        } catch (Exception e) {
            log.error("Error getting current investor ID: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Check if current user is authenticated
     *
     * @return true if authenticated
     */
    public boolean isAuthenticated() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated() &&
                    !"anonymousUser".equals(authentication.getPrincipal())) {
                return true;
            }

            String sessionId = getSessionIdFromRequest();
            return sessionId != null && sessionService.isValidSession(sessionId);
        } catch (Exception e) {
            log.error("Error checking authentication: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Check if current user has admin role
     *
     * @return true if admin
     */
    public boolean isAdmin() {
        String role = getCurrentUserRole();
        return "ADMIN".equals(role);
    }

    /**
     * Check if current user has investor role
     *
     * @return true if investor
     */
    public boolean isInvestor() {
        String role = getCurrentUserRole();
        return "INVESTOR".equals(role);
    }

    /**
     * Get session ID from request header
     *
     * @return session ID or null
     */
    private String getSessionIdFromRequest() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                String sessionId = request.getHeader("X-Session-Id");
                if (sessionId != null && !sessionId.isEmpty()) {
                    return sessionId;
                }
                // Also try to get from parameter (for testing)
                sessionId = request.getParameter("sessionId");
                if (sessionId != null && !sessionId.isEmpty()) {
                    return sessionId;
                }
            }
            return null;
        } catch (Exception e) {
            log.error("Error getting session ID from request: {}", e.getMessage());
            return null;
        }
    }
}