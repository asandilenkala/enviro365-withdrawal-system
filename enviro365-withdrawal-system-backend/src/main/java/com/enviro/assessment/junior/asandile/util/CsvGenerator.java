package com.enviro.assessment.junior.asandile.util;

import com.enviro.assessment.junior.asandile.model.Investor;
import com.enviro.assessment.junior.asandile.model.Withdrawal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for generating CSV content
 *
 * @author Asandile
 * @version 1.0
 */
@Component
@Slf4j
public class CsvGenerator {

    private static final String CSV_HEADER_WITHDRAWALS =
            "Withdrawal ID,Date,Product Name,Amount,Status,Rejection Reason,Processed Date";

    private static final String CSV_HEADER_FULL_REPORT =
            "Withdrawal ID,Date,Investor Name,Investor Email,Product Name,Amount,Status,Rejection Reason,Processed Date";

    /**
     * Generate CSV for investor withdrawals
     *
     * @param withdrawals list of withdrawals
     * @param investor investor information
     * @return CSV content as string
     */
    public String generateWithdrawalCsv(List<Withdrawal> withdrawals, Investor investor) {
        log.info("Generating CSV for {} withdrawals of investor: {}", withdrawals.size(), investor.getId());

        StringBuilder csv = new StringBuilder();

        // Add report header
        csv.append("Enviro365 Withdrawal Report\n");
        csv.append("Generated: ").append(DateUtil.format(java.time.LocalDateTime.now())).append("\n");
        csv.append("Investor: ").append(investor.getFirstName()).append(" ").append(investor.getLastName()).append("\n");
        csv.append("Email: ").append(investor.getEmail()).append("\n\n");

        // Add table header
        csv.append(CSV_HEADER_WITHDRAWALS).append("\n");

        // Add data rows
        for (Withdrawal withdrawal : withdrawals) {
            csv.append(formatWithdrawalRow(withdrawal)).append("\n");
        }

        // Add summary
        double totalAmount = withdrawals.stream()
                .filter(w -> w.getStatus().name().equals("APPROVED") || w.getStatus().name().equals("PROCESSED"))
                .mapToDouble(w -> w.getAmount().doubleValue())
                .sum();

        csv.append("\nSummary:\n");
        csv.append("Total Withdrawals: ").append(withdrawals.size()).append("\n");
        csv.append("Total Approved Amount: $").append(String.format("%.2f", totalAmount)).append("\n");

        return csv.toString();
    }

    /**
     * Generate CSV for all withdrawals (admin)
     *
     * @param withdrawals list of all withdrawals
     * @return CSV content as string
     */
    public String generateAllWithdrawalsCsv(List<Withdrawal> withdrawals) {
        log.info("Generating CSV for all {} withdrawals", withdrawals.size());

        StringBuilder csv = new StringBuilder();

        // Add report header
        csv.append("Enviro365 All Withdrawals Report\n");
        csv.append("Generated: ").append(DateUtil.format(java.time.LocalDateTime.now())).append("\n\n");

        // Add table header
        csv.append(CSV_HEADER_FULL_REPORT).append("\n");

        // Add data rows
        for (Withdrawal withdrawal : withdrawals) {
            csv.append(formatFullWithdrawalRow(withdrawal)).append("\n");
        }

        // Add summary
        long totalPending = withdrawals.stream().filter(w -> w.getStatus().name().equals("PENDING")).count();
        long totalApproved = withdrawals.stream().filter(w -> w.getStatus().name().equals("APPROVED")).count();
        long totalRejected = withdrawals.stream().filter(w -> w.getStatus().name().equals("REJECTED")).count();
        double totalAmount = withdrawals.stream()
                .mapToDouble(w -> w.getAmount().doubleValue())
                .sum();

        csv.append("\nSummary:\n");
        csv.append("Total Withdrawals: ").append(withdrawals.size()).append("\n");
        csv.append("Pending: ").append(totalPending).append("\n");
        csv.append("Approved: ").append(totalApproved).append("\n");
        csv.append("Rejected: ").append(totalRejected).append("\n");
        csv.append("Total Amount: $").append(String.format("%.2f", totalAmount)).append("\n");

        return csv.toString();
    }

    /**
     * Format withdrawal row for CSV
     *
     * @param withdrawal withdrawal entity
     * @return formatted CSV row
     */
    private String formatWithdrawalRow(Withdrawal withdrawal) {
        return String.join(",",
                escapeCsv(withdrawal.getId().toString()),
                escapeCsv(DateUtil.format(withdrawal.getCreatedAt())),
                escapeCsv(withdrawal.getProductName()),
                String.format("\"$%.2f\"", withdrawal.getAmount()),
                escapeCsv(withdrawal.getStatus().getDescription()),
                escapeCsv(withdrawal.getRejectionReason() != null ? withdrawal.getRejectionReason() : ""),
                escapeCsv(withdrawal.getProcessedAt() != null ? DateUtil.format(withdrawal.getProcessedAt()) : "")
        );
    }

    /**
     * Format full withdrawal row with investor info for CSV
     *
     * @param withdrawal withdrawal entity
     * @return formatted CSV row
     */
    private String formatFullWithdrawalRow(Withdrawal withdrawal) {
        Investor investor = withdrawal.getInvestor();

        return String.join(",",
                escapeCsv(withdrawal.getId().toString()),
                escapeCsv(DateUtil.format(withdrawal.getCreatedAt())),
                escapeCsv(investor.getFirstName() + " " + investor.getLastName()),
                escapeCsv(investor.getEmail()),
                escapeCsv(withdrawal.getProductName()),
                String.format("\"$%.2f\"", withdrawal.getAmount()),
                escapeCsv(withdrawal.getStatus().getDescription()),
                escapeCsv(withdrawal.getRejectionReason() != null ? withdrawal.getRejectionReason() : ""),
                escapeCsv(withdrawal.getProcessedAt() != null ? DateUtil.format(withdrawal.getProcessedAt()) : "")
        );
    }

    /**
     * Escape CSV special characters
     *
     * @param value string value to escape
     * @return escaped string
     */
    private String escapeCsv(String value) {
        if (value == null) return "\"\"";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}