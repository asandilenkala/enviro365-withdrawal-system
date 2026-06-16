package com.enviro.assessment.junior.asandile.controller;

import com.enviro.assessment.junior.asandile.dto.response.ApiResponseDTO;
import com.enviro.assessment.junior.asandile.dto.response.PortfolioResponseDTO;
import com.enviro.assessment.junior.asandile.exception.ResourceNotFoundException;
import com.enviro.assessment.junior.asandile.model.User;
import com.enviro.assessment.junior.asandile.service.PortfolioService;
import com.enviro.assessment.junior.asandile.service.UserContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
 * REST Controller for portfolio management operations
 *
 * @author Asandile
 * @version 1.0
 */
@RestController
@RequestMapping("/api/v1/portfolios")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Portfolio Management", description = "Endpoints for managing investor portfolios")
@Slf4j
public class PortfolioController {

    private final PortfolioService portfolioService;
    private final UserContext userContext;

    /**
     * Get portfolio by investor ID (with role-based access control)
     *
     * @param investorId the investor's UUID
     * @return ResponseEntity containing portfolio details
     */
    @GetMapping("/investor/{investorId}")
    @Operation(summary = "Get portfolio by investor ID", description = "Retrieves complete portfolio details for a specific investor")
    @ApiResponse(responseCode = "200", description = "Portfolio retrieved successfully")
    @ApiResponse(responseCode = "403", description = "Access denied")
    @ApiResponse(responseCode = "404", description = "Investor not found")
    public ResponseEntity<ApiResponseDTO<PortfolioResponseDTO>> getPortfolioByInvestorId(
            @PathVariable UUID investorId) {

        log.info("REST request to get portfolio for investor: {}", investorId);

        try {
            User currentUser = userContext.getCurrentUser();
            String userRole = currentUser.getRole();

            // Check access: ADMIN can view any, INVESTOR can only view their own
            if ("INVESTOR".equals(userRole)) {
                if (currentUser.getInvestor() == null ||
                        !currentUser.getInvestor().getId().equals(investorId)) {
                    log.warn("Access denied for user {} to investor {}", currentUser.getUsername(), investorId);
                    throw new AccessDeniedException("You can only access your own portfolio");
                }
            }

            PortfolioResponseDTO portfolio = portfolioService.getPortfolioByInvestorId(investorId);

            return ResponseEntity.ok(ApiResponseDTO.<PortfolioResponseDTO>builder()
                    .success(true)
                    .message("Portfolio retrieved successfully")
                    .data(portfolio)
                    .timestamp(LocalDateTime.now())
                    .build());

        } catch (AccessDeniedException e) {
            log.error("Access denied: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponseDTO.<PortfolioResponseDTO>builder()
                            .success(false)
                            .message("Access denied: " + e.getMessage())
                            .timestamp(LocalDateTime.now())
                            .build());
        } catch (ResourceNotFoundException e) {
            log.error("Resource not found: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error retrieving portfolio: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.<PortfolioResponseDTO>builder()
                            .success(false)
                            .message("Error retrieving portfolio: " + e.getMessage())
                            .timestamp(LocalDateTime.now())
                            .build());
        }
    }

    /**
     * Get all portfolios - ADMIN only
     *
     * @return ResponseEntity containing list of portfolio summaries
     */
    @GetMapping
    @Operation(summary = "Get all portfolios", description = "Retrieves summary of all portfolios (admin only)")
    @ApiResponse(responseCode = "200", description = "Portfolios retrieved successfully")
    @ApiResponse(responseCode = "403", description = "Access denied")
    public ResponseEntity<ApiResponseDTO<List<PortfolioResponseDTO>>> getAllPortfolios() {

        log.info("REST request to get all portfolios");

        try {
            if (!userContext.isAdmin()) {
                log.warn("Access denied for user to view all portfolios");
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponseDTO.<List<PortfolioResponseDTO>>builder()
                                .success(false)
                                .message("Access denied. Admin role required")
                                .timestamp(LocalDateTime.now())
                                .build());
            }

            List<PortfolioResponseDTO> portfolios = portfolioService.getAllPortfolios();

            return ResponseEntity.ok(ApiResponseDTO.<List<PortfolioResponseDTO>>builder()
                    .success(true)
                    .message("Portfolios retrieved successfully")
                    .data(portfolios)
                    .timestamp(LocalDateTime.now())
                    .build());

        } catch (Exception e) {
            log.error("Error retrieving all portfolios: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.<List<PortfolioResponseDTO>>builder()
                            .success(false)
                            .message("Error retrieving portfolios: " + e.getMessage())
                            .timestamp(LocalDateTime.now())
                            .build());
        }
    }
}