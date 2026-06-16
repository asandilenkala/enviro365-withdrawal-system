package com.enviro.assessment.junior.asandile.service;

import com.enviro.assessment.junior.asandile.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service for managing user sessions
 *
 * @author Asandile
 * @version 1.0
 */
@Service
@Slf4j
public class SessionService {

    private final Map<String, UserSession> activeSessions = new ConcurrentHashMap<>();
    private final Map<UUID, String> userToSessionMap = new ConcurrentHashMap<>();

    /**
     * User session data
     */
    public static class UserSession {
        private final UUID userId;
        private final String username;
        private final String email;
        private final String role;
        private final UUID investorId;
        private final String investorName;
        private final long createdAt;
        private long lastAccessedAt;

        public UserSession(User user) {
            this.userId = user.getId();
            this.username = user.getUsername();
            this.email = user.getEmail();
            this.role = user.getRole();
            this.investorId = user.getInvestor() != null ? user.getInvestor().getId() : null;
            this.investorName = user.getInvestor() != null ?
                    user.getInvestor().getFirstName() + " " + user.getInvestor().getLastName() : null;
            this.createdAt = System.currentTimeMillis();
            this.lastAccessedAt = System.currentTimeMillis();
        }

        public void updateLastAccessed() {
            this.lastAccessedAt = System.currentTimeMillis();
        }

        public UUID getUserId() { return userId; }
        public String getUsername() { return username; }
        public String getEmail() { return email; }
        public String getRole() { return role; }
        public UUID getInvestorId() { return investorId; }
        public String getInvestorName() { return investorName; }
        public long getCreatedAt() { return createdAt; }
        public long getLastAccessedAt() { return lastAccessedAt; }

        public boolean isExpired(long timeoutMillis) {
            return System.currentTimeMillis() - lastAccessedAt > timeoutMillis;
        }
    }

    /**
     * Create a new session for a user
     *
     * @param user the authenticated user
     * @return session ID
     */
    public String createSession(User user) {
        try {
            String sessionId = UUID.randomUUID().toString();
            UserSession session = new UserSession(user);

            activeSessions.put(sessionId, session);
            userToSessionMap.put(user.getId(), sessionId);

            log.info("Session created for user: {}", user.getUsername());
            return sessionId;
        } catch (Exception e) {
            log.error("Error creating session: {}", e.getMessage());
            throw new RuntimeException("Failed to create session", e);
        }
    }

    /**
     * Get session by session ID
     *
     * @param sessionId the session ID
     * @return UserSession if found and valid
     */
    public UserSession getSession(String sessionId) {
        try {
            if (sessionId == null) {
                return null;
            }

            UserSession session = activeSessions.get(sessionId);
            if (session == null) {
                return null;
            }

            // Update last accessed time
            session.updateLastAccessed();
            return session;
        } catch (Exception e) {
            log.error("Error retrieving session: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Get session by user ID
     *
     * @param userId the user ID
     * @return UserSession if found and valid
     */
    public UserSession getSessionByUserId(UUID userId) {
        try {
            String sessionId = userToSessionMap.get(userId);
            if (sessionId == null) {
                return null;
            }
            return getSession(sessionId);
        } catch (Exception e) {
            log.error("Error retrieving session by user ID: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Validate if a session is valid
     *
     * @param sessionId the session ID
     * @return true if session exists and is not expired
     */
    public boolean isValidSession(String sessionId) {
        try {
            UserSession session = getSession(sessionId);
            if (session == null) {
                return false;
            }

            // Session timeout: 24 hours
            long timeoutMillis = 24 * 60 * 60 * 1000L;
            return !session.isExpired(timeoutMillis);
        } catch (Exception e) {
            log.error("Error validating session: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Invalidate a session
     *
     * @param sessionId the session ID
     */
    public void invalidateSession(String sessionId) {
        try {
            UserSession session = activeSessions.remove(sessionId);
            if (session != null) {
                userToSessionMap.remove(session.getUserId());
                log.info("Session invalidated for user: {}", session.getUsername());
            }
        } catch (Exception e) {
            log.error("Error invalidating session: {}", e.getMessage());
        }
    }

    /**
     * Invalidate all sessions for a user
     *
     * @param userId the user ID
     */
    public void invalidateUserSessions(UUID userId) {
        try {
            String sessionId = userToSessionMap.remove(userId);
            if (sessionId != null) {
                activeSessions.remove(sessionId);
                log.info("All sessions invalidated for user ID: {}", userId);
            }
        } catch (Exception e) {
            log.error("Error invalidating user sessions: {}", e.getMessage());
        }
    }

    /**
     * Get current user ID from session
     *
     * @param sessionId the session ID
     * @return user ID if session is valid
     */
    public UUID getCurrentUserId(String sessionId) {
        UserSession session = getSession(sessionId);
        return session != null ? session.getUserId() : null;
    }
}