package com.enviro.assessment.junior.asandile.dto.response;

import lombok.Builder;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

/**
 * Generic API Response DTO for standardizing API responses
 *
 * @author Asandile
 * @version 1.0
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponseDTO<T> {

    private boolean success;
    private String message;
    private String errorCode;
    private T data;
    private LocalDateTime timestamp;

    /**
     * Create a success response with data
     *
     * @param data the response data
     * @param message success message
     * @return ApiResponseDTO
     */
    public static <T> ApiResponseDTO<T> success(T data, String message) {
        return ApiResponseDTO.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Create an error response
     *
     * @param message error message
     * @param errorCode error code
     * @return ApiResponseDTO
     */
    public static <T> ApiResponseDTO<T> error(String message, String errorCode) {
        return ApiResponseDTO.<T>builder()
                .success(false)
                .message(message)
                .errorCode(errorCode)
                .timestamp(LocalDateTime.now())
                .build();
    }
}