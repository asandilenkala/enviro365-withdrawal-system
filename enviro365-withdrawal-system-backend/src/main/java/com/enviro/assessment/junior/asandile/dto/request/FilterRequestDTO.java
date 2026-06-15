package com.enviro.assessment.junior.asandile.dto.request;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * DTO for filtering requests
 *
 * @author Asandile
 * @version 1.0
 */
@Data
public class FilterRequestDTO {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime endDate;

    private String status;
    private String productType;
    private Double minAmount;
    private Double maxAmount;
}