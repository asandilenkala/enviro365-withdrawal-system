package com.enviro.assessment.junior.asandile.repository;

import com.enviro.assessment.junior.asandile.model.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for Portfolio entity operations
 *
 * @author Asandile
 * @version 1.0
 */
@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, UUID> {

    /**
     * Find portfolio by investor ID
     *
     * @param investorId investor UUID
     * @return Optional containing portfolio if found
     */
    Optional<Portfolio> findByInvestorId(UUID investorId);

    /**
     * Find portfolio by investor ID with products loaded eagerly
     *
     * @param investorId investor UUID
     * @return Optional containing portfolio with products
     */
    @Query("SELECT p FROM Portfolio p LEFT JOIN FETCH p.products WHERE p.investor.id = :investorId")
    Optional<Portfolio> findByInvestorIdWithProducts(@Param("investorId") UUID investorId);

    /**
     * Check if portfolio exists for investor
     *
     * @param investorId investor UUID
     * @return true if portfolio exists
     */
    boolean existsByInvestorId(UUID investorId);
}