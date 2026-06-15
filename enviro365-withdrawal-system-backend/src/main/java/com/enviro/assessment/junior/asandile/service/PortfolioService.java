package com.enviro.assessment.junior.asandile.service;

import com.enviro.assessment.junior.asandile.dto.response.PortfolioResponseDTO;
import com.enviro.assessment.junior.asandile.exception.ResourceNotFoundException;
import com.enviro.assessment.junior.asandile.mapper.PortfolioMapper;
import com.enviro.assessment.junior.asandile.model.Investor;
import com.enviro.assessment.junior.asandile.model.Portfolio;
import com.enviro.assessment.junior.asandile.repository.InvestorRepository;
import com.enviro.assessment.junior.asandile.repository.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service class for portfolio management operations
 *
 * @author Asandile
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final InvestorRepository investorRepository;
    private final PortfolioMapper portfolioMapper;

    /**
     * Get portfolio by investor ID
     *
     * @param investorId investor UUID
     * @return portfolio response DTO
     * @throws ResourceNotFoundException if investor or portfolio not found
     */
    @Transactional(readOnly = true)
    public PortfolioResponseDTO getPortfolioByInvestorId(UUID investorId) throws ResourceNotFoundException {
        log.info("Fetching portfolio for investor: {}", investorId);

        Investor investor = investorRepository.findByIdWithPortfolio(investorId)
                .orElseThrow(() -> new ResourceNotFoundException("Investor", investorId.toString()));

        Portfolio portfolio = investor.getPortfolio();
        if (portfolio == null) {
            throw new ResourceNotFoundException("Portfolio not found for investor: " + investorId);
        }

        // Update total balance
        portfolio.setTotalBalance(portfolio.calculateTotalBalance());

        return portfolioMapper.toDto(portfolio);
    }

    /**
     * Get all portfolios (admin)
     *
     * @return list of portfolio response DTOs
     */
    @Transactional(readOnly = true)
    public List<PortfolioResponseDTO> getAllPortfolios() {
        log.info("Fetching all portfolios");

        List<Portfolio> portfolios = portfolioRepository.findAll();

        return portfolios.stream()
                .map(portfolio -> {
                    portfolio.setTotalBalance(portfolio.calculateTotalBalance());
                    return portfolioMapper.toDto(portfolio);
                })
                .collect(Collectors.toList());
    }
}