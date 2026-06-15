package com.enviro.assessment.junior.asandile.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * DTO for withdrawal request
 *
 * @author Asandile
 * @version 1.0
 */
@Data
public class WithdrawalRequestDTO {

    @NotNull(message = "Investor ID is required")
    private UUID investorId;

    @NotNull(message = "Product ID is required")
    private UUID productId;

    @NotNull(message = "Product name is required")
    @NotBlank(message = "Product name cannot be blank")
    private String productName;

    @NotNull(message = "Withdrawal amount is required")
    @DecimalMin(value = "0.01", message = "Withdrawal amount must be greater than 0")
    @DecimalMax(value = "999999999.99", message = "Withdrawal amount exceeds maximum limit")
    private BigDecimal amount;

    @Size(max = 500, message = "Notes cannot exceed 500 characters")
    private String notes;
}