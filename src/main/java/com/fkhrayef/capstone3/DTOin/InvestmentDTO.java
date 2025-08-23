package com.fkhrayef.capstone3.DTOin;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InvestmentDTO {

    @NotNull
    private Integer investor_id;
    @NotNull
    private Integer startup_id;

    @NotEmpty(message = "Round Type is required")
    @Pattern(regexp = "^(?i)(pre_seed|seed|series_a|series_b|series_c)$")
    private String roundType;

    @NotNull(message = "Effective Date is required")
    @FutureOrPresent(message = "Effective Date must be in the future or present")
    private Date effectiveDate;

    @NotNull(message = "Investment Amount is required")
    @Positive(message = "Investment Amount must be greater than zero")
    private Double investment_amount;

    @NotEmpty(message = "Payment Method is required")
    @Pattern(regexp = "^(?i)(check|transfer)$")
    private String paymentMethod;

    @NotNull(message = "Is Recurring is required")
    private Boolean isRecurring;

    @Positive(message = "Recurring Amount must be greater than zero")
    private Double recurringAmount;

    @Positive(message = "Recurring Years must be greater than zero")
    private Integer recurringYears;

    @NotNull(message = "Minimum Investment Period is required")
    @Positive(message = "Minimum Investment Period in years must be greater than zero")
    private Integer minimumInvestmentPeriod;
}
