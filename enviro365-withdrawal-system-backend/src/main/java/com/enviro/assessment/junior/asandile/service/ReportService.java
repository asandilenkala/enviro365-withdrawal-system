package com.enviro.assessment.junior.asandile.service;

import com.enviro.assessment.junior.asandile.dto.request.FilterRequestDTO;
import com.enviro.assessment.junior.asandile.exception.ResourceNotFoundException;
import com.enviro.assessment.junior.asandile.model.Investor;
import com.enviro.assessment.junior.asandile.model.Withdrawal;
import com.enviro.assessment.junior.asandile.repository.InvestorRepository;
import com.enviro.assessment.junior.asandile.repository.WithdrawalRepository;
import com.enviro.assessment.junior.asandile.util.CsvGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service class for report generation operations
 *
 * @author Asandile
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ReportService {

    private final WithdrawalRepository withdrawalRepository;
    private final InvestorRepository investorRepository;
    private final CsvGenerator csvGenerator;

    /**
     * Generate CSV report of withdrawals for an investor
     *
     * @param investorId investor UUID
     * @param filterDTO filter criteria
     * @return CSV content as string
     * @throws ResourceNotFoundException if investor not found
     */
    @Transactional(readOnly = true)
    public String generateWithdrawalReport(UUID investorId, FilterRequestDTO filterDTO)
            throws ResourceNotFoundException {

        log.info("Generating withdrawal report for investor: {}", investorId);

        Investor investor = investorRepository.findById(investorId)
                .orElseThrow(() -> new ResourceNotFoundException("Investor", investorId.toString()));

        List<Withdrawal> withdrawals = withdrawalRepository.findByInvestorIdOrderByCreatedAtDesc(investorId);

        // Apply filters
        if (filterDTO != null) {
            withdrawals = applyFilters(withdrawals, filterDTO);
        }

        return csvGenerator.generateWithdrawalCsv(withdrawals, investor);
    }

    /**
     * Generate CSV report of all withdrawals (admin)
     *
     * @param filterDTO filter criteria
     * @return CSV content as string
     */
    @Transactional(readOnly = true)
    public String generateAllWithdrawalsReport(FilterRequestDTO filterDTO) {
        log.info("Generating all withdrawals report");

        List<Withdrawal> withdrawals = withdrawalRepository.findAll();

        if (filterDTO != null) {
            withdrawals = applyFilters(withdrawals, filterDTO);
        }

        return csvGenerator.generateAllWithdrawalsCsv(withdrawals);
    }

    /**
     * Apply filters to withdrawal list
     *
     * @param withdrawals list of withdrawals
     * @param filterDTO filter criteria
     * @return filtered list
     */
    private List<Withdrawal> applyFilters(List<Withdrawal> withdrawals, FilterRequestDTO filterDTO) {
        return withdrawals.stream()
                .filter(w -> filterDTO.getStartDate() == null ||
                        w.getCreatedAt().isAfter(filterDTO.getStartDate()))
                .filter(w -> filterDTO.getEndDate() == null ||
                        w.getCreatedAt().isBefore(filterDTO.getEndDate()))
                .filter(w -> filterDTO.getStatus() == null ||
                        w.getStatus().name().equalsIgnoreCase(filterDTO.getStatus()))
                .filter(w -> filterDTO.getMinAmount() == null ||
                        w.getAmount().doubleValue() >= filterDTO.getMinAmount())
                .filter(w -> filterDTO.getMaxAmount() == null ||
                        w.getAmount().doubleValue() <= filterDTO.getMaxAmount())
                .collect(Collectors.toList());
    }
}