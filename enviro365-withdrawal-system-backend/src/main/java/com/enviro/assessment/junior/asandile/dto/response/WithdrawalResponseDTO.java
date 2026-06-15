package com.enviro.assessment.junior.asandile.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for withdrawal response
 *
 * @author Asandile
 * @version 1.0
 */
@Data
@Builder
public class WithdrawalResponseDTO {

    private UUID withdrawalId;
    private BigDecimal amount;
    private String formattedAmount;
    private String productId;
    private String productName;
    private String status;
    private String statusDescription;
    private String rejectionReason;
    private String investorName;
    private LocalDateTime createdAt;
    private LocalDateTime processedAt;
    private boolean canBeProcessed;
}