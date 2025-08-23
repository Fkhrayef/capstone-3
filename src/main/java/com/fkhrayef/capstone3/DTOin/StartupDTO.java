package com.fkhrayef.capstone3.DTOin;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class StartupDTO {

    @NotEmpty(message = "Name cannot be null")
    private String name;

    @NotEmpty(message = "Description cannot be null")
    private String description;

    @NotEmpty(message = "Industry cannot be null")
    // TODO Add Pattern
    private String industry;

    @NotEmpty(message = "Stage cannot be null")
    @Pattern(regexp = "^(?i)(pre_seed|seed|series_a|series_b|series_c)$")
    private String stage;

    @NotNull(message = "Founded Date cannot be null")
    private LocalDate foundedDate;

    @NotNull(message = "Employee Count cannot be null")
    @PositiveOrZero(message = "Employee Count cannot be negative")
    private Integer employeeCount;

    @NotNull(message = "Valuation cannot be null")
    @PositiveOrZero(message = "Valuation cannot be negative")
    private Double valuation;

}
