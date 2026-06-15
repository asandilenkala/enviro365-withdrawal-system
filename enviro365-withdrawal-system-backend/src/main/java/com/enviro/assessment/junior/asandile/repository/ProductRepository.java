package com.enviro.assessment.junior.asandile.repository;

import com.enviro.assessment.junior.asandile.model.Product;
import com.enviro.assessment.junior.asandile.model.enums.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Repository interface for Product entity operations
 *
 * @author Asandile
 * @version 1.0
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    /**
     * Find products by portfolio ID
     *
     * @param portfolioId portfolio UUID
     * @return list of products
     */
    List<Product> findByPortfolioId(UUID portfolioId);

    /**
     * Find products by type
     *
     * @param productType product type
     * @return list of products
     */
    List<Product> findByProductType(ProductType productType);

    /**
     * Get total balance of all products for a portfolio
     *
     * @param portfolioId portfolio UUID
     * @return total balance
     */
    @Query("SELECT COALESCE(SUM(p.balance), 0) FROM Product p WHERE p.portfolio.id = :portfolioId")
    BigDecimal getTotalBalanceByPortfolio(@Param("portfolioId") UUID portfolioId);

    /**
     * Get total balance of non-retirement products for a portfolio
     *
     * @param portfolioId portfolio UUID
     * @return total non-retirement balance
     */
    @Query("SELECT COALESCE(SUM(p.balance), 0) FROM Product p WHERE p.portfolio.id = :portfolioId AND p.productType != 'RETIREMENT'")
    BigDecimal getNonRetirementBalanceByPortfolio(@Param("portfolioId") UUID portfolioId);
}