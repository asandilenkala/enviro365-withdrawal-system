package com.enviro.assessment.junior.asandile.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

/**
 * User Response DTO for authentication responses
 *
 * @author Asandile
 * @version 1.0
 */
@Data
@Builder
public class UserResponseDTO {
    private UUID id;
    private String username;
    private String email;
    private String role;
    private UUID investorId;
    private String investorName;
    private boolean enabled;
}