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

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final InvestorRepository investorRepository;
    private final PortfolioMapper portfolioMapper;

    @Transactional(readOnly = true)
    public PortfolioResponseDTO getPortfolioByInvestorId(UUID investorId) throws ResourceNotFoundException {
        log.info("Fetching portfolio for investor: {}", investorId);

        try {
            Investor investor = investorRepository.findByIdWithPortfolio(investorId)
                    .orElseThrow(() -> {
                        log.error("Investor not found with id: {}", investorId);
                        return new ResourceNotFoundException("Investor", investorId.toString());
                    });

            Portfolio portfolio = investor.getPortfolio();
            if (portfolio == null) {
                log.error("Portfolio not found for investor: {}", investorId);
                throw new ResourceNotFoundException("Portfolio not found for investor: " + investorId);
            }

            portfolio.setTotalBalance(portfolio.calculateTotalBalance());
            log.info("Portfolio retrieved successfully for investor: {}", investorId);

            return portfolioMapper.toDto(portfolio);

        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error fetching portfolio for investor {}: {}", investorId, e.getMessage());
            throw new RuntimeException("Failed to fetch portfolio: " + e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    public List<PortfolioResponseDTO> getAllPortfolios() {
        log.info("Fetching all portfolios");

        try {
            List<Portfolio> portfolios = portfolioRepository.findAll();

            List<PortfolioResponseDTO> result = portfolios.stream()
                    .map(portfolio -> {
                        portfolio.setTotalBalance(portfolio.calculateTotalBalance());
                        return portfolioMapper.toDto(portfolio);
                    })
                    .collect(Collectors.toList());

            log.info("Retrieved {} portfolios", result.size());
            return result;

        } catch (Exception e) {
            log.error("Error fetching all portfolios: {}", e.getMessage());
            throw new RuntimeException("Failed to fetch all portfolios: " + e.getMessage(), e);
        }
    }
}