package com.enviro.assessment.junior.asandile.service;

import com.enviro.assessment.junior.asandile.dto.request.WithdrawalRequestDTO;
import com.enviro.assessment.junior.asandile.dto.response.WithdrawalResponseDTO;
import com.enviro.assessment.junior.asandile.exception.BusinessRuleException;
import com.enviro.assessment.junior.asandile.exception.ResourceNotFoundException;
import com.enviro.assessment.junior.asandile.mapper.WithdrawalMapper;
import com.enviro.assessment.junior.asandile.model.Investor;
import com.enviro.assessment.junior.asandile.model.Product;
import com.enviro.assessment.junior.asandile.model.Withdrawal;
import com.enviro.assessment.junior.asandile.model.enums.WithdrawalStatus;
import com.enviro.assessment.junior.asandile.repository.InvestorRepository;
import com.enviro.assessment.junior.asandile.repository.ProductRepository;
import com.enviro.assessment.junior.asandile.repository.WithdrawalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Service class for managing withdrawal operations
 *
 * @author Asandile
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class WithdrawalService {

    private final WithdrawalRepository withdrawalRepository;
    private final InvestorRepository investorRepository;
    private final ProductRepository productRepository;
    private final ValidationService validationService;
    private final WithdrawalMapper withdrawalMapper;

    /**
     * Create a new withdrawal request
     *
     * @param requestDTO withdrawal request data
     * @return created withdrawal response
     * @throws BusinessRuleException if validation fails
     * @throws ResourceNotFoundException if investor or product not found
     */
    public WithdrawalResponseDTO createWithdrawal(WithdrawalRequestDTO requestDTO)
            throws BusinessRuleException, ResourceNotFoundException {

        log.info("Creating withdrawal request for investor: {}", requestDTO.getInvestorId());

        try {
            // Fetch investor with portfolio
            Investor investor = investorRepository.findByIdWithPortfolio(requestDTO.getInvestorId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Investor not found with id: " + requestDTO.getInvestorId()
                    ));

            // Fetch product
            Product product = productRepository.findById(requestDTO.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Product not found with id: " + requestDTO.getProductId()
                    ));

            // Validate business rules
            validationService.validateWithdrawal(investor, product, requestDTO.getAmount());

            // Create withdrawal entity
            Withdrawal withdrawal = withdrawalMapper.toEntity(requestDTO);
            withdrawal.setInvestor(investor);
            withdrawal.setProductId(requestDTO.getProductId().toString());
            withdrawal.setProductName(product.getProductName());

            // Save withdrawal
            Withdrawal savedWithdrawal = withdrawalRepository.save(withdrawal);
            log.info("Withdrawal request created successfully with id: {}", savedWithdrawal.getId());

            return withdrawalMapper.toDto(savedWithdrawal);

        } catch (BusinessRuleException | ResourceNotFoundException e) {
            log.error("Withdrawal creation failed: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error during withdrawal creation", e);
            throw new RuntimeException("Failed to create withdrawal request", e);
        }
    }

    /**
     * Get all withdrawals for an investor
     *
     * @param investorId investor UUID
     * @return list of withdrawal responses
     * @throws ResourceNotFoundException if investor not found
     */
    @Transactional(readOnly = true)
    public List<WithdrawalResponseDTO> getInvestorWithdrawals(UUID investorId)
            throws ResourceNotFoundException {

        log.info("Fetching withdrawals for investor: {}", investorId);

        try {
            // Verify investor exists
            if (!investorRepository.existsById(investorId)) {
                throw new ResourceNotFoundException("Investor not found with id: " + investorId);
            }

            List<Withdrawal> withdrawals = withdrawalRepository.findByInvestorIdOrderByCreatedAtDesc(investorId);
            log.info("Found {} withdrawals for investor: {}", withdrawals.size(), investorId);

            return withdrawalMapper.toDtoList(withdrawals);

        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error fetching withdrawals for investor: {}", investorId, e);
            throw new RuntimeException("Failed to fetch withdrawal history", e);
        }
    }

    /**
     * Update withdrawal status (for admin processing)
     *
     * @param withdrawalId withdrawal UUID
     * @param status new status
     * @param rejectionReason reason for rejection (if applicable)
     * @return updated withdrawal response
     * @throws ResourceNotFoundException if withdrawal not found
     */
    public WithdrawalResponseDTO updateWithdrawalStatus(UUID withdrawalId, WithdrawalStatus status, String rejectionReason)
            throws ResourceNotFoundException {

        log.info("Updating withdrawal {} status to: {}", withdrawalId, status);

        try {
            Withdrawal withdrawal = withdrawalRepository.findById(withdrawalId)
                    .orElseThrow(() -> new ResourceNotFoundException("Withdrawal not found with id: " + withdrawalId));

            withdrawal.setStatus(status);
            withdrawal.setProcessedAt(LocalDateTime.now());

            if (status == WithdrawalStatus.REJECTED && rejectionReason != null) {
                withdrawal.setRejectionReason(rejectionReason);
            }

            Withdrawal updatedWithdrawal = withdrawalRepository.save(withdrawal);
            log.info("Withdrawal status updated successfully: {}", withdrawalId);

            return withdrawalMapper.toDto(updatedWithdrawal);

        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error updating withdrawal status: {}", withdrawalId, e);
            throw new RuntimeException("Failed to update withdrawal status", e);
        }
    }
}