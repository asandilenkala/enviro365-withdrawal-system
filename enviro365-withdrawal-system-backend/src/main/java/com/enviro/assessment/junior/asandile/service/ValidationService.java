package com.enviro.assessment.junior.asandile.service;

import com.enviro.assessment.junior.asandile.exception.BusinessRuleException;
import com.enviro.assessment.junior.asandile.model.Investor;
import com.enviro.assessment.junior.asandile.model.Portfolio;
import com.enviro.assessment.junior.asandile.model.Product;
import com.enviro.assessment.junior.asandile.model.enums.ProductType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Service for validating withdrawal business rules
 *
 * @author Asandile
 * @version 1.0
 */
@Service
@Slf4j
public class ValidationService {

    private static final BigDecimal MAX_WITHDRAWAL_PERCENTAGE = new BigDecimal("0.90");
    private static final int RETIREMENT_AGE_THRESHOLD = 65;

    /**
     * Validate withdrawal request against all business rules
     *
     * @param investor the investor making the withdrawal
     * @param product the product to withdraw from
     * @param amount the withdrawal amount
     * @throws BusinessRuleException if any validation fails
     */
    public void validateWithdrawal(Investor investor, Product product, BigDecimal amount)
            throws BusinessRuleException {

        log.info("Validating withdrawal for investor: {}, product: {}, amount: {}",
                investor.getId(), product.getId(), amount);

        // Rule 1: Retirement withdrawal age check
        validateRetirementAge(investor, product);

        // Rule 2: Withdrawal must not exceed balance
        validateSufficientBalance(product, amount);

        // Rule 3: Withdrawal must not exceed 90% of balance (for non-retirement)
        validateMaxPercentage(product, amount);

        log.info("Withdrawal validation passed for investor: {}", investor.getId());
    }

    /**
     * Validate retirement product withdrawal age requirement
     *
     * @param investor the investor
     * @param product the product
     * @throws BusinessRuleException if age requirement not met
     */
    private void validateRetirementAge(Investor investor, Product product)
            throws BusinessRuleException {

        if (product.getProductType() == ProductType.RETIREMENT) {
            int age = investor.getAge();
            if (age <= RETIREMENT_AGE_THRESHOLD) {
                throw new BusinessRuleException(
                        "RETIREMENT_AGE_ERROR",
                        String.format("Retirement withdrawal only allowed if age > %d. Current age: %d",
                                RETIREMENT_AGE_THRESHOLD, age)
                );
            }
        }
    }

    /**
     * Validate sufficient balance for withdrawal
     *
     * @param product the product
     * @param amount withdrawal amount
     * @throws BusinessRuleException if insufficient balance
     */
    private void validateSufficientBalance(Product product, BigDecimal amount)
            throws BusinessRuleException {

        if (amount.compareTo(product.getBalance()) > 0) {
            throw new BusinessRuleException(
                    "INSUFFICIENT_BALANCE",
                    String.format("Withdrawal amount (%.2f) exceeds product balance (%.2f)",
                            amount, product.getBalance())
            );
        }
    }

    /**
     * Validate withdrawal does not exceed 90% of balance for non-retirement products
     *
     * @param product the product
     * @param amount withdrawal amount
     * @throws BusinessRuleException if exceeds 90% limit
     */
    private void validateMaxPercentage(Product product, BigDecimal amount)
            throws BusinessRuleException {

        // Retirement products have different rules, handled by age validation
        if (product.getProductType() == ProductType.RETIREMENT) {
            return;
        }

        BigDecimal maxAllowed = product.getBalance().multiply(MAX_WITHDRAWAL_PERCENTAGE);

        if (amount.compareTo(maxAllowed) > 0) {
            throw new BusinessRuleException(
                    "EXCEEDS_MAX_PERCENTAGE",
                    String.format("Withdrawal amount (%.2f) exceeds 90%% of balance (%.2f)",
                            amount, maxAllowed)
            );
        }
    }
}