package com.enviro.assessment.junior.asandile.model.enums;

/**
 * Enum representing withdrawal request status
 *
 * @author Asandile
 * @version 1.0
 */
public enum WithdrawalStatus {
    PENDING("Pending Approval"),
    APPROVED("Approved"),
    REJECTED("Rejected"),
    PROCESSED("Processed"),
    CANCELLED("Cancelled");

    private final String description;

    WithdrawalStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}