package com.enviro.assessment.junior.asandile.service;

import com.enviro.assessment.junior.asandile.dto.response.PortfolioResponseDTO;
import com.enviro.assessment.junior.asandile.exception.ResourceNotFoundException;
import com.enviro.assessment.junior.asandile.model.Investor;
import com.enviro.assessment.junior.asandile.model.Portfolio;
import com.enviro.assessment.junior.asandile.model.Product;
import com.enviro.assessment.junior.asandile.model.enums.ProductType;
import com.enviro.assessment.junior.asandile.repository.InvestorRepository;
import com.enviro.assessment.junior.asandile.repository.PortfolioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PortfolioServiceTest {

    @Mock
    private PortfolioRepository portfolioRepository;

    @Mock
    private InvestorRepository investorRepository;

    @InjectMocks
    private PortfolioService portfolioService;

    private UUID investorId;
    private Investor investor;
    private Portfolio portfolio;
    private List<Product> products;

    @BeforeEach
    void setUp() {
        investorId = UUID.randomUUID();

        investor = new Investor();
        investor.setId(investorId);
        investor.setFirstName("John");
        investor.setLastName("Smith");
        investor.setEmail("john.smith@email.com");

        portfolio = new Portfolio();
        portfolio.setId(UUID.randomUUID());
        portfolio.setPortfolioName("Test Portfolio");
        portfolio.setInvestor(investor);

        products = new ArrayList<>();
        Product product1 = new Product();
        product1.setId(UUID.randomUUID());
        product1.setProductName("Test Fund");
        product1.setProductType(ProductType.EQUITIES);
        product1.setBalance(BigDecimal.valueOf(100000));
        product1.setPortfolio(portfolio);
        products.add(product1);

        portfolio.setProducts(products);
        investor.setPortfolio(portfolio);
    }

    @Test
    void getPortfolioByInvestorId_ShouldReturnPortfolio() {
        // Arrange
        when(investorRepository.findByIdWithPortfolio(investorId))
                .thenReturn(Optional.of(investor));

        // Act
        PortfolioResponseDTO result = portfolioService.getPortfolioByInvestorId(investorId);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getPortfolioName()).isEqualTo("Test Portfolio");
        assertThat(result.getTotalBalance()).isEqualTo(BigDecimal.valueOf(100000));
    }

    @Test
    void getPortfolioByInvestorId_WhenInvestorNotFound_ShouldThrowException() {
        // Arrange
        when(investorRepository.findByIdWithPortfolio(investorId))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> portfolioService.getPortfolioByInvestorId(investorId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Investor not found");
    }

    @Test
    void getAllPortfolios_ShouldReturnAllPortfolios() {
        // Arrange
        List<Portfolio> portfolios = List.of(portfolio);
        when(portfolioRepository.findAll()).thenReturn(portfolios);

        // Act
        List<PortfolioResponseDTO> result = portfolioService.getAllPortfolios();

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getPortfolioName()).isEqualTo("Test Portfolio");
    }
}