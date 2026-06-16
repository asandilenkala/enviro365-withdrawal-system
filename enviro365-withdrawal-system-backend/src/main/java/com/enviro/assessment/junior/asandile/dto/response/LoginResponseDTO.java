package com.enviro.assessment.junior.asandile.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

/**
 * Login Response DTO containing user session information
 *
 * @author Asandile
 * @version 1.0
 */
@Data
@Builder
public class LoginResponseDTO {
    private UUID userId;
    private String username;
    private String email;
    private String role;
    private UUID investorId;
    private String investorName;
    private String sessionId;
    private boolean authenticated;
    private String message;
}