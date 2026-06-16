package com.enviro.assessment.junior.asandile.service;

import com.enviro.assessment.junior.asandile.exception.BusinessRuleException;
import com.enviro.assessment.junior.asandile.model.Investor;
import com.enviro.assessment.junior.asandile.model.Product;
import com.enviro.assessment.junior.asandile.model.enums.ProductType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class ValidationServiceTest {

    private ValidationService validationService;
    private Investor investor;
    private Product product;

    @BeforeEach
    void setUp() {
        validationService = new ValidationService();

        investor = new Investor();
        investor.setId(UUID.randomUUID());
        investor.setDateOfBirth(LocalDateTime.of(1970, 1, 1, 0, 0)); // Age ~54

        product = new Product();
        product.setId(UUID.randomUUID());
        product.setProductName("Test Fund");
        product.setBalance(BigDecimal.valueOf(100000));
    }

    @Test
    void validateWithdrawal_WhenRetirementAndAgeUnder65_ShouldThrowException() {
        // Arrange
        product.setProductType(ProductType.RETIREMENT);
        BigDecimal amount = BigDecimal.valueOf(50000);

        // Act & Assert
        assertThatThrownBy(() -> validationService.validateWithdrawal(investor, product, amount))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("Retirement withdrawal only allowed if age > 65");
    }

    @Test
    void validateWithdrawal_WhenAmountExceedsBalance_ShouldThrowException() {
        // Arrange
        product.setProductType(ProductType.EQUITIES);
        BigDecimal amount = BigDecimal.valueOf(150000); // Exceeds balance

        // Act & Assert
        assertThatThrownBy(() -> validationService.validateWithdrawal(investor, product, amount))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("exceeds product balance");
    }

    @Test
    void validateWithdrawal_WhenAmountExceeds90Percent_ShouldThrowException() {
        // Arrange
        product.setProductType(ProductType.EQUITIES);
        BigDecimal amount = BigDecimal.valueOf(95000); // 95% of balance

        // Act & Assert
        assertThatThrownBy(() -> validationService.validateWithdrawal(investor, product, amount))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("exceeds 90% of balance");
    }
}