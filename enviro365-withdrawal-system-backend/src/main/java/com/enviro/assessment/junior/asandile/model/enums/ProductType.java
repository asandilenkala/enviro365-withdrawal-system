package com.enviro.assessment.junior.asandile.model.enums;

/**
 * Enum representing different types of investment products
 *
 * @author Asandile
 * @version 1.0
 */
public enum ProductType {
    RETIREMENT("Retirement Fund"),
    SAVINGS("Savings Account"),
    INVESTMENT("Investment Portfolio"),
    BONDS("Government Bonds"),
    EQUITIES("Stock Equities");

    private final String displayName;

    ProductType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    /**
     * Checks if product is retirement type
     *
     * @return true if retirement product
     */
    public boolean isRetirement() {
        return this == RETIREMENT;
    }
}