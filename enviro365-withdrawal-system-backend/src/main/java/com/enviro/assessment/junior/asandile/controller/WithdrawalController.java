package com.enviro.assessment.junior.asandile.controller;

import com.enviro.assessment.junior.asandile.dto.request.WithdrawalRequestDTO;
import com.enviro.assessment.junior.asandile.dto.response.ApiResponseDTO;
import com.enviro.assessment.junior.asandile.dto.response.WithdrawalResponseDTO;
import com.enviro.assessment.junior.asandile.exception.BusinessRuleException;
import com.enviro.assessment.junior.asandile.exception.ResourceNotFoundException;
import com.enviro.assessment.junior.asandile.model.User;
import com.enviro.assessment.junior.asandile.model.enums.WithdrawalStatus;
import com.enviro.assessment.junior.asandile.service.UserContext;
import com.enviro.assessment.junior.asandile.service.WithdrawalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * REST Controller for withdrawal management operations
 *
 * @author Asandile
 * @version 1.0
 */
@RestController
@RequestMapping("/api/v1/withdrawals")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Withdrawal Management", description = "Endpoints for managing withdrawal requests")
@Slf4j
public class WithdrawalController {

    private final WithdrawalService withdrawalService;
    private final UserContext userContext;

    /**
     * Create a new withdrawal request
     *
     * @param requestDTO withdrawal request data
     * @return ResponseEntity containing created withdrawal
     */
    @PostMapping
    @Operation(summary = "Create withdrawal request", description = "Submits a new withdrawal request with validation")
    @ApiResponse(responseCode = "201", description = "Withdrawal request created successfully")
    @ApiResponse(responseCode = "400", description = "Validation failed or business rule violation")
    @ApiResponse(responseCode = "403", description = "Access denied")
    public ResponseEntity<ApiResponseDTO<WithdrawalResponseDTO>> createWithdrawal(
            @Valid @RequestBody WithdrawalRequestDTO requestDTO) {

        log.info("REST request to create withdrawal for investor: {}", requestDTO.getInvestorId());

        try {
            User currentUser = userContext.getCurrentUser();

            if (userContext.isInvestor()) {
                if (currentUser.getInvestor() == null ||
                        !currentUser.getInvestor().getId().equals(requestDTO.getInvestorId())) {
                    throw new AccessDeniedException("You can only create withdrawals for your own account");
                }
            }

            WithdrawalResponseDTO withdrawal = withdrawalService.createWithdrawal(requestDTO);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponseDTO.<WithdrawalResponseDTO>builder()
                            .success(true)
                            .message("Withdrawal request created successfully")
                            .data(withdrawal)
                            .timestamp(LocalDateTime.now())
                            .build());

        } catch (AccessDeniedException e) {
            log.error("Access denied: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponseDTO.<WithdrawalResponseDTO>builder()
                            .success(false)
                            .message("Access denied: " + e.getMessage())
                            .timestamp(LocalDateTime.now())
                            .build());
        } catch (BusinessRuleException | ResourceNotFoundException e) {
            log.error("Withdrawal creation failed: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error during withdrawal creation", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.<WithdrawalResponseDTO>builder()
                            .success(false)
                            .message("Failed to create withdrawal: " + e.getMessage())
                            .timestamp(LocalDateTime.now())
                            .build());
        }
    }

    /**
     * Get withdrawal history for an investor
     *
     * @param investorId the investor's UUID
     * @return ResponseEntity containing list of withdrawals
     */
    @GetMapping("/investor/{investorId}")
    @Operation(summary = "Get withdrawal history", description = "Retrieves all withdrawal requests for an investor")
    @ApiResponse(responseCode = "200", description = "Withdrawals retrieved successfully")
    @ApiResponse(responseCode = "403", description = "Access denied")
    public ResponseEntity<ApiResponseDTO<List<WithdrawalResponseDTO>>> getWithdrawalHistory(
            @PathVariable UUID investorId) {

        log.info("REST request to get withdrawal history for investor: {}", investorId);

        try {
            User currentUser = userContext.getCurrentUser();

            if (userContext.isInvestor()) {
                if (currentUser.getInvestor() == null ||
                        !currentUser.getInvestor().getId().equals(investorId)) {
                    throw new AccessDeniedException("You can only view your own withdrawal history");
                }
            }

            List<WithdrawalResponseDTO> withdrawals = withdrawalService.getInvestorWithdrawals(investorId);

            return ResponseEntity.ok(ApiResponseDTO.<List<WithdrawalResponseDTO>>builder()
                    .success(true)
                    .message("Withdrawal history retrieved successfully")
                    .data(withdrawals)
                    .timestamp(LocalDateTime.now())
                    .build());

        } catch (AccessDeniedException e) {
            log.error("Access denied: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponseDTO.<List<WithdrawalResponseDTO>>builder()
                            .success(false)
                            .message("Access denied: " + e.getMessage())
                            .timestamp(LocalDateTime.now())
                            .build());
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error retrieving withdrawal history: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.<List<WithdrawalResponseDTO>>builder()
                            .success(false)
                            .message("Error retrieving withdrawal history: " + e.getMessage())
                            .timestamp(LocalDateTime.now())
                            .build());
        }
    }

    /**
     * Update withdrawal status (admin only)
     *
     * @param withdrawalId withdrawal UUID
     * @param status new status
     * @param rejectionReason rejection reason (optional)
     * @return updated withdrawal
     */
    @PutMapping("/{withdrawalId}/status")
    @Operation(summary = "Update withdrawal status", description = "Updates the status of a withdrawal request (admin only)")
    @ApiResponse(responseCode = "200", description = "Status updated successfully")
    @ApiResponse(responseCode = "403", description = "Access denied")
    public ResponseEntity<ApiResponseDTO<WithdrawalResponseDTO>> updateWithdrawalStatus(
            @PathVariable UUID withdrawalId,
            @RequestParam String status,
            @RequestParam(required = false) String rejectionReason) {

        log.info("REST request to update withdrawal {} status to: {}", withdrawalId, status);

        try {
            if (!userContext.isAdmin()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponseDTO.<WithdrawalResponseDTO>builder()
                                .success(false)
                                .message("Access denied. Admin role required")
                                .timestamp(LocalDateTime.now())
                                .build());
            }

            WithdrawalStatus withdrawalStatus;
            try {
                withdrawalStatus = WithdrawalStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BusinessRuleException("INVALID_STATUS", "Invalid status value: " + status);
            }

            WithdrawalResponseDTO withdrawal = withdrawalService.updateWithdrawalStatus(
                    withdrawalId, withdrawalStatus, rejectionReason);

            return ResponseEntity.ok(ApiResponseDTO.<WithdrawalResponseDTO>builder()
                    .success(true)
                    .message("Withdrawal status updated successfully")
                    .data(withdrawal)
                    .timestamp(LocalDateTime.now())
                    .build());

        } catch (BusinessRuleException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error updating withdrawal status: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.<WithdrawalResponseDTO>builder()
                            .success(false)
                            .message("Error updating withdrawal status: " + e.getMessage())
                            .timestamp(LocalDateTime.now())
                            .build());
        }
    }
}