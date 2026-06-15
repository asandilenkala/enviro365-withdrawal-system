package com.enviro.assessment.junior.asandile.mapper;

import com.enviro.assessment.junior.asandile.dto.request.WithdrawalRequestDTO;
import com.enviro.assessment.junior.asandile.dto.response.WithdrawalResponseDTO;
import com.enviro.assessment.junior.asandile.model.Withdrawal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * MapStruct mapper for Withdrawal entity to/from DTO conversion
 *
 * @author Asandile
 * @version 1.0
 */
@Mapper(componentModel = "spring")
public interface WithdrawalMapper {

    WithdrawalMapper INSTANCE = Mappers.getMapper(WithdrawalMapper.class);

    /**
     * Convert WithdrawalRequestDTO to Withdrawal entity
     *
     * @param requestDTO withdrawal request DTO
     * @return withdrawal entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", constant = "PENDING")
    @Mapping(target = "rejectionReason", constant = "")
    @Mapping(target = "investor", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "processedAt", ignore = true)
    Withdrawal toEntity(WithdrawalRequestDTO requestDTO);

    /**
     * Convert Withdrawal entity to WithdrawalResponseDTO
     *
     * @param withdrawal withdrawal entity
     * @return withdrawal response DTO
     */
    @Mapping(target = "withdrawalId", source = "id")
    @Mapping(target = "investorName", expression = "java(withdrawal.getInvestor().getFirstName() + \" \" + withdrawal.getInvestor().getLastName())")
    @Mapping(target = "formattedAmount", expression = "java(String.format(\"$%.2f\", withdrawal.getAmount()))")
    @Mapping(target = "statusDescription", expression = "java(withdrawal.getStatus().getDescription())")
    @Mapping(target = "canBeProcessed", expression = "java(withdrawal.getStatus().equals(com.enviro.assessment.junior.asandile.model.enums.WithdrawalStatus.PENDING))")
    WithdrawalResponseDTO toDto(Withdrawal withdrawal);

    /**
     * Convert list of Withdrawal entities to list of DTOs
     *
     * @param withdrawals list of withdrawals
     * @return list of response DTOs
     */
    List<WithdrawalResponseDTO> toDtoList(List<Withdrawal> withdrawals);
}