package com.enviro.assessment.junior.asandile.controller;

import com.enviro.assessment.junior.asandile.dto.request.FilterRequestDTO;
import com.enviro.assessment.junior.asandile.exception.AccessDeniedException;
import com.enviro.assessment.junior.asandile.service.ReportService;
import com.enviro.assessment.junior.asandile.service.UserContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST Controller for report generation operations
 *
 * @author Asandile
 * @version 1.0
 */
@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Report Generation", description = "Endpoints for generating CSV reports")
@Slf4j
public class ReportController {

    private final ReportService reportService;
    private final UserContext userContext;

    /**
     * Generate CSV report of withdrawals for an investor
     *
     * @param investorId investor UUID
     * @param filterDTO filter criteria
     * @return CSV file as downloadable response
     */
    @PostMapping("/withdrawals/investor/{investorId}")
    @Operation(summary = "Generate withdrawal report", description = "Generates CSV report of investor withdrawals")
    public ResponseEntity<byte[]> generateInvestorWithdrawalReport(
            @PathVariable UUID investorId,
            @RequestBody(required = false) FilterRequestDTO filterDTO) {

        log.info("REST request to generate withdrawal report for investor: {}", investorId);

        try {
            // Check access: Investors can only download their own reports
            if (userContext.isInvestor()) {
                UUID currentInvestorId = userContext.getCurrentInvestorId();
                if (currentInvestorId == null || !currentInvestorId.equals(investorId)) {
                    throw new AccessDeniedException("You can only download reports for your own portfolio");
                }
            }

            String csvContent = reportService.generateWithdrawalReport(investorId, filterDTO);

            byte[] csvBytes = csvContent.getBytes();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("text/csv"));
            headers.setContentDispositionFormData("attachment", "withdrawals_report_" + investorId + ".csv");
            headers.setContentLength(csvBytes.length);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(csvBytes);

        } catch (AccessDeniedException e) {
            log.error("Access denied: {}", e.getMessage());
            return ResponseEntity.status(403).build();
        } catch (Exception e) {
            log.error("Error generating report: {}", e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Generate CSV report of all withdrawals (admin only)
     *
     * @param filterDTO filter criteria
     * @return CSV file as downloadable response
     */
    @PostMapping("/withdrawals/all")
    @Operation(summary = "Generate all withdrawals report", description = "Generates CSV report of all withdrawals (admin only)")
    public ResponseEntity<byte[]> generateAllWithdrawalsReport(
            @RequestBody(required = false) FilterRequestDTO filterDTO) {

        log.info("REST request to generate all withdrawals report");

        try {
            if (!userContext.isAdmin()) {
                log.warn("Access denied: User is not admin");
                return ResponseEntity.status(403).build();
            }

            String csvContent = reportService.generateAllWithdrawalsReport(filterDTO);

            byte[] csvBytes = csvContent.getBytes();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("text/csv"));
            headers.setContentDispositionFormData("attachment", "all_withdrawals_report.csv");
            headers.setContentLength(csvBytes.length);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(csvBytes);

        } catch (Exception e) {
            log.error("Error generating all withdrawals report: {}", e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }
}