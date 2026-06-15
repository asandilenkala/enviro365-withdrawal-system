package com.enviro.assessment.junior.asandile.mapper;

import com.enviro.assessment.junior.asandile.dto.response.PortfolioResponseDTO;
import com.enviro.assessment.junior.asandile.model.Investor;
import com.enviro.assessment.junior.asandile.model.Portfolio;
import com.enviro.assessment.junior.asandile.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * MapStruct mapper for Portfolio entity to DTO conversion
 *
 * @author Asandile
 * @version 1.0
 */
@Mapper(componentModel = "spring")
public interface PortfolioMapper {

    PortfolioMapper INSTANCE = Mappers.getMapper(PortfolioMapper.class);

    /**
     * Convert Portfolio entity to PortfolioResponseDTO
     *
     * @param portfolio portfolio entity
     * @return portfolio response DTO
     */
    @Mapping(target = "portfolioId", source = "id")
    @Mapping(target = "portfolioName", source = "portfolioName")
    @Mapping(target = "totalBalance", source = "totalBalance")
    @Mapping(target = "availableForWithdrawal", expression = "java(calculateAvailableForWithdrawal(portfolio))")
    @Mapping(target = "maxWithdrawalAmount", expression = "java(calculateMaxWithdrawal(portfolio))")
    @Mapping(target = "products", expression = "java(mapProducts(portfolio.getProducts()))")
    @Mapping(target = "investor", expression = "java(mapInvestor(portfolio.getInvestor()))")
    PortfolioResponseDTO toDto(Portfolio portfolio);

    /**
     * Map list of products to DTOs
     *
     * @param products list of products
     * @return list of product DTOs
     */
    default List<PortfolioResponseDTO.ProductDTO> mapProducts(List<Product> products) {
        if (products == null) return List.of();

        return products.stream()
                .map(product -> PortfolioResponseDTO.ProductDTO.builder()
                        .productId(product.getId())
                        .productName(product.getProductName())
                        .productType(product.getProductType().name())
                        .balance(product.getBalance())
                        .isRetirementProduct(product.getProductType().isRetirement())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * Map investor to investor summary DTO
     *
     * @param investor investor entity
     * @return investor summary DTO
     */
    default PortfolioResponseDTO.InvestorSummaryDTO mapInvestor(Investor investor) {
        if (investor == null) return null;

        return PortfolioResponseDTO.InvestorSummaryDTO.builder()
                .investorId(investor.getId())
                .fullName(investor.getFirstName() + " " + investor.getLastName())
                .email(investor.getEmail())
                .age(investor.getAge())
                .canWithdrawRetirement(investor.getAge() > 65)
                .build();
    }

    /**
     * Calculate available amount for withdrawal
     *
     * @param portfolio portfolio entity
     * @return available withdrawal amount
     */
    default BigDecimal calculateAvailableForWithdrawal(Portfolio portfolio) {
        if (portfolio.getProducts() == null) return BigDecimal.ZERO;

        return portfolio.getProducts().stream()
                .filter(product -> !product.getProductType().isRetirement())
                .map(Product::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Calculate maximum withdrawal amount (90% of non-retirement balance)
     *
     * @param portfolio portfolio entity
     * @return max withdrawal amount
     */
    default BigDecimal calculateMaxWithdrawal(Portfolio portfolio) {
        BigDecimal available = calculateAvailableForWithdrawal(portfolio);
        return available.multiply(new BigDecimal("0.90"));
    }
}