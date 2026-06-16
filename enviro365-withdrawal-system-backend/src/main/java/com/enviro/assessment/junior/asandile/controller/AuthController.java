package com.enviro.assessment.junior.asandile.controller;

import com.enviro.assessment.junior.asandile.dto.request.LoginRequestDTO;
import com.enviro.assessment.junior.asandile.dto.response.ApiResponseDTO;
import com.enviro.assessment.junior.asandile.dto.response.LoginResponseDTO;
import com.enviro.assessment.junior.asandile.model.User;
import com.enviro.assessment.junior.asandile.repository.UserRepository;
import com.enviro.assessment.junior.asandile.service.SessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * REST Controller for authentication operations
 *
 * @author Asandile
 * @version 1.0
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", allowCredentials = "true")
@Tag(name = "Authentication", description = "Endpoints for user authentication")
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final SessionService sessionService;

    /**
     * Authenticate user and create session
     *
     * @param loginRequest login credentials
     * @return ResponseEntity with session information
     */
    @PostMapping("/login")
    @Operation(summary = "Login user", description = "Authenticates user and creates a session")
    public ResponseEntity<ApiResponseDTO<LoginResponseDTO>> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        log.info("Login attempt for user: {}", loginRequest.getUsername());

        try {
            User user = userRepository.findByUsername(loginRequest.getUsername()).orElse(null);
            if (user == null) {
                log.warn("User not found: {}", loginRequest.getUsername());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponseDTO.<LoginResponseDTO>builder()
                                .success(false)
                                .message("Invalid username or password")
                                .timestamp(LocalDateTime.now())
                                .build());
            }

            // Authenticate
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            // IMPORTANT: Set the authentication in the SecurityContext with the actual User object
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Create session
            String sessionId = sessionService.createSession(user);

            // Build response
            LoginResponseDTO response = LoginResponseDTO.builder()
                    .userId(user.getId())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .role(user.getRole())
                    .investorId(user.getInvestor() != null ? user.getInvestor().getId() : null)
                    .investorName(user.getInvestor() != null ?
                            user.getInvestor().getFirstName() + " " + user.getInvestor().getLastName() : null)
                    .sessionId(sessionId)
                    .authenticated(true)
                    .message("Login successful")
                    .build();

            log.info("Login successful for user: {}", user.getUsername());

            return ResponseEntity.ok()
                    .header("X-Session-Id", sessionId)
                    .body(ApiResponseDTO.<LoginResponseDTO>builder()
                            .success(true)
                            .message("Login successful")
                            .data(response)
                            .timestamp(LocalDateTime.now())
                            .build());

        } catch (BadCredentialsException e) {
            log.error("Bad credentials for user {}: {}", loginRequest.getUsername(), e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponseDTO.<LoginResponseDTO>builder()
                            .success(false)
                            .message("Invalid username or password")
                            .timestamp(LocalDateTime.now())
                            .build());
        } catch (Exception e) {
            log.error("Login failed for user {}: {}", loginRequest.getUsername(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponseDTO.<LoginResponseDTO>builder()
                            .success(false)
                            .message("Login failed: " + e.getMessage())
                            .timestamp(LocalDateTime.now())
                            .build());
        }
    }

    /**
     * Logout user and invalidate session
     *
     * @param sessionId the session ID to invalidate
     * @return ResponseEntity with logout status
     */
    @PostMapping("/logout")
    @Operation(summary = "Logout user", description = "Invalidates user session")
    public ResponseEntity<ApiResponseDTO<Void>> logout(@RequestHeader(value = "X-Session-Id", required = false) String sessionId) {
        log.info("Logout request received");

        try {
            if (sessionId != null) {
                sessionService.invalidateSession(sessionId);
                log.info("Session invalidated: {}", sessionId);
            }

            // Clear security context
            SecurityContextHolder.clearContext();

            return ResponseEntity.ok(ApiResponseDTO.<Void>builder()
                    .success(true)
                    .message("Logout successful")
                    .timestamp(LocalDateTime.now())
                    .build());

        } catch (Exception e) {
            log.error("Logout failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.<Void>builder()
                            .success(false)
                            .message("Logout failed: " + e.getMessage())
                            .timestamp(LocalDateTime.now())
                            .build());
        }
    }

    /**
     * Get current user information using session
     *
     * @param sessionId the session ID
     * @return ResponseEntity with user information
     */
    @GetMapping("/user-info")
    @Operation(summary = "Get current user info", description = "Returns information about the authenticated user")
    public ResponseEntity<ApiResponseDTO<LoginResponseDTO>> getCurrentUser(
            @RequestHeader(value = "X-Session-Id", required = false) String sessionId) {

        log.info("REST request to get current user info");

        try {
            if (sessionId == null || !sessionService.isValidSession(sessionId)) {
                log.warn("Invalid or missing session ID");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponseDTO.<LoginResponseDTO>builder()
                                .success(false)
                                .message("Invalid or expired session")
                                .timestamp(LocalDateTime.now())
                                .build());
            }

            SessionService.UserSession session = sessionService.getSession(sessionId);
            if (session == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponseDTO.<LoginResponseDTO>builder()
                                .success(false)
                                .message("Session not found")
                                .timestamp(LocalDateTime.now())
                                .build());
            }

            LoginResponseDTO response = LoginResponseDTO.builder()
                    .userId(session.getUserId())
                    .username(session.getUsername())
                    .email(session.getEmail())
                    .role(session.getRole())
                    .investorId(session.getInvestorId())
                    .investorName(session.getInvestorName())
                    .sessionId(sessionId)
                    .authenticated(true)
                    .message("User info retrieved successfully")
                    .build();

            return ResponseEntity.ok(ApiResponseDTO.<LoginResponseDTO>builder()
                    .success(true)
                    .message("User info retrieved successfully")
                    .data(response)
                    .timestamp(LocalDateTime.now())
                    .build());

        } catch (Exception e) {
            log.error("Error retrieving user info: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.<LoginResponseDTO>builder()
                            .success(false)
                            .message("Error retrieving user info: " + e.getMessage())
                            .timestamp(LocalDateTime.now())
                            .build());
        }
    }
}