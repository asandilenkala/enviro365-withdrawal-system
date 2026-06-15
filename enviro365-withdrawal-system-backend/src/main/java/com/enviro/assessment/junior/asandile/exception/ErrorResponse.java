package com.enviro.assessment.junior.asandile.exception;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Standard error response structure for exception handling
 *
 * @author Asandile
 * @version 1.0
 */
@Data
@Builder
public class ErrorResponse {

    private String errorCode;
    private String message;
    private String detail;
    private LocalDateTime timestamp;
    private String path;
    private String method;
    private Map<String, String> validationErrors;

    /**
     * Create a simple error response
     *
     * @param errorCode error code
     * @param message error message
     * @return ErrorResponse
     */
    public static ErrorResponse of(String errorCode, String message) {
        return ErrorResponse.builder()
                .errorCode(errorCode)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }
}