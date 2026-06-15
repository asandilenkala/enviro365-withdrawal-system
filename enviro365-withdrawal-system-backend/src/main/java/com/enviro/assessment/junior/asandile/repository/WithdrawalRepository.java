package com.enviro.assessment.junior.asandile.repository;

import com.enviro.assessment.junior.asandile.model.Withdrawal;
import com.enviro.assessment.junior.asandile.model.enums.WithdrawalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Repository interface for Withdrawal entity operations
 *
 * @author Asandile
 * @version 1.0
 */
@Repository
public interface WithdrawalRepository extends JpaRepository<Withdrawal, UUID> {

    /**
     * Find all withdrawals for a specific investor
     *
     * @param investorId investor UUID
     * @return list of withdrawals ordered by creation date descending
     */
    List<Withdrawal> findByInvestorIdOrderByCreatedAtDesc(UUID investorId);

    /**
     * Find withdrawals by status
     *
     * @param status withdrawal status
     * @return list of withdrawals with given status
     */
    List<Withdrawal> findByStatus(WithdrawalStatus status);

    /**
     * Find withdrawals within date range
     *
     * @param startDate start of date range
     * @param endDate end of date range
     * @return list of withdrawals in date range
     */
    List<Withdrawal> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Get total withdrawal amount for an investor
     *
     * @param investorId investor UUID
     * @param status withdrawal status
     * @return total withdrawal amount
     */
    @Query("SELECT COALESCE(SUM(w.amount), 0) FROM Withdrawal w WHERE w.investor.id = :investorId AND w.status = :status")
    Double sumApprovedWithdrawalsByInvestor(@Param("investorId") UUID investorId, @Param("status") WithdrawalStatus status);
}