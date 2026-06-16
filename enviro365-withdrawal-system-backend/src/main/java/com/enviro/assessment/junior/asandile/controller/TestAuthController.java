package com.enviro.assessment.junior.asandile.controller;

import com.enviro.assessment.junior.asandile.dto.response.ApiResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Test Controller for authentication verification
 *
 * @author Asandile
 * @version 1.0
 */
@RestController
@RequestMapping("/api/v1/test")
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Test", description = "Test endpoints")
@Slf4j
public class TestAuthController {

    @GetMapping("/auth-check")
    @Operation(summary = "Check authentication status")
    public ResponseEntity<ApiResponseDTO<Map<String, Object>>> checkAuth() {
        log.info("Checking authentication status");

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            Map<String, Object> response = new HashMap<>();
            response.put("authenticated", authentication != null && authentication.isAuthenticated());
            response.put("username", authentication != null ? authentication.getName() : null);
            response.put("principal", authentication != null ? authentication.getPrincipal() : null);
            response.put("authorities", authentication != null ? authentication.getAuthorities() : null);

            return ResponseEntity.ok(ApiResponseDTO.<Map<String, Object>>builder()
                    .success(true)
                    .message("Auth check successful")
                    .data(response)
                    .timestamp(LocalDateTime.now())
                    .build());

        } catch (Exception e) {
            log.error("Error checking auth: {}", e.getMessage());
            return ResponseEntity.status(500)
                    .body(ApiResponseDTO.<Map<String, Object>>builder()
                            .success(false)
                            .message("Error: " + e.getMessage())
                            .timestamp(LocalDateTime.now())
                            .build());
        }
    }
}