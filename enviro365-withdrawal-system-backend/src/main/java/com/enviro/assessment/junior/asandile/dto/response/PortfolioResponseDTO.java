package com.enviro.assessment.junior.asandile.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * DTO for portfolio response
 *
 * @author Asandile
 * @version 1.0
 */
@Data
@Builder
public class PortfolioResponseDTO {
    private UUID portfolioId;
    private String portfolioName;
    private BigDecimal totalBalance;
    private BigDecimal availableForWithdrawal;
    private BigDecimal maxWithdrawalAmount;
    private List<ProductDTO> products;
    private InvestorSummaryDTO investor;

    @Data
    @Builder
    public static class ProductDTO {
        private UUID productId;
        private String productName;
        private String productType;
        private BigDecimal balance;
        private boolean isRetirementProduct;
    }

    @Data
    @Builder
    public static class InvestorSummaryDTO {
        private UUID investorId;
        private String fullName;
        private String email;
        private int age;
        private boolean canWithdrawRetirement;
    }
}