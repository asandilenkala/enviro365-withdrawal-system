package com.enviro.assessment.junior.asandile.model.enums;

/**
 * Currency enum for monetary values across the system
 *
 * @author Asandile
 * @version 1.0
 */
public enum Currency {
    USD("US Dollar", "$"),
    EUR("Euro", "€"),
    GBP("British Pound", "£"),
    ZAR("South African Rand", "R"),
    CAD("Canadian Dollar", "C$"),
    AUD("Australian Dollar", "A$");

    private final String displayName;
    private final String symbol;

    Currency(String displayName, String symbol) {
        this.displayName = displayName;
        this.symbol = symbol;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getSymbol() {
        return symbol;
    }

    /**
     * Format amount with currency symbol
     *
     * @param amount the amount to format
     * @return formatted string
     */
    public String format(double amount) {
        return String.format("%s%.2f", symbol, amount);
    }
}