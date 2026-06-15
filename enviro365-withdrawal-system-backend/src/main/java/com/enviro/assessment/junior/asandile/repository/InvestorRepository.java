package com.enviro.assessment.junior.asandile.repository;

import com.enviro.assessment.junior.asandile.model.Investor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for Investor entity operations
 *
 * @author Asandile
 * @version 1.0
 */
@Repository
public interface InvestorRepository extends JpaRepository<Investor, UUID> {

    /**
     * Find investor by email address
     *
     * @param email investor's email
     * @return Optional containing investor if found
     */
    Optional<Investor> findByEmail(String email);

    /**
     * Check if investor exists by email
     *
     * @param email investor's email
     * @return true if exists
     */
    boolean existsByEmail(String email);

    /**
     * Find investor with portfolio and products loaded
     *
     * @param id investor UUID
     * @return Optional containing investor with eager loaded portfolio
     */
    @Query("SELECT i FROM Investor i LEFT JOIN FETCH i.portfolio p LEFT JOIN FETCH p.products WHERE i.id = :id")
    Optional<Investor> findByIdWithPortfolio(@Param("id") UUID id);
}