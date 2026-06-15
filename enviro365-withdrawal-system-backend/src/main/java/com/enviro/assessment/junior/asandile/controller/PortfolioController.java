package com.enviro.assessment.junior.asandile.controller;

import com.enviro.assessment.junior.asandile.dto.response.ApiResponseDTO;
import com.enviro.assessment.junior.asandile.dto.response.PortfolioResponseDTO;
import com.enviro.assessment.junior.asandile.exception.ResourceNotFoundException;
import com.enviro.assessment.junior.asandile.service.PortfolioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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

    /**
     * Get portfolio details by investor ID
     *
     * @param investorId the investor's UUID
     * @return ResponseEntity containing portfolio details
     */
    @GetMapping("/investor/{investorId}")
    @Operation(summary = "Get portfolio by investor ID", description = "Retrieves complete portfolio details for a specific investor")
    @ApiResponse(responseCode = "200", description = "Portfolio retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Investor not found")
    public ResponseEntity<ApiResponseDTO<PortfolioResponseDTO>> getPortfolioByInvestorId(
            @PathVariable UUID investorId) {

        log.info("REST request to get portfolio for investor: {}", investorId);

        PortfolioResponseDTO portfolio = portfolioService.getPortfolioByInvestorId(investorId);

        return ResponseEntity.ok(ApiResponseDTO.<PortfolioResponseDTO>builder()
                .success(true)
                .message("Portfolio retrieved successfully")
                .data(portfolio)
                .timestamp(LocalDateTime.now())
                .build());
    }

    /**
     * Get summary of all portfolios (admin endpoint)
     *
     * @return ResponseEntity containing list of portfolio summaries
     */
    @GetMapping
    @Operation(summary = "Get all portfolios", description = "Retrieves summary of all portfolios (admin only)")
    public ResponseEntity<ApiResponseDTO<List<PortfolioResponseDTO>>> getAllPortfolios() {

        log.info("REST request to get all portfolios");

        List<PortfolioResponseDTO> portfolios = portfolioService.getAllPortfolios();

        return ResponseEntity.ok(ApiResponseDTO.<List<PortfolioResponseDTO>>builder()
                .success(true)
                .message("Portfolios retrieved successfully")
                .data(portfolios)
                .timestamp(LocalDateTime.now())
                .build());
    }
}