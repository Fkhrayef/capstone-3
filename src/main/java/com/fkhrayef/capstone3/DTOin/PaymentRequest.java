package com.fkhrayef.capstone3.DTOin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {
    
    @NotBlank(message = "Cardholder name is required")
    private String name;

    @Pattern(regexp = "\\d{15,19}", message = "Card number must be 15-19 digits")
    private String number;

    @Pattern(regexp = "\\d{3,4}", message = "CVC must be 3 or 4 digits")
    private String cvc;

    @Pattern(regexp = "^(0?[1-9]|1[0-2])$", message = "Month must be 1-12")
    private String month;

    @Pattern(regexp = "^\\d{2,4}$", message = "Year must be 2-4 digits")
    private String year;

    // Optional for flows where server computes amount
    private Double amount;

    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency must be a 3-letter code (e.g., SAR)")
    private String currency;

    @Size(max = 255, message = "Description max length is 255")
    private String description;

    private String callbackUrl;
}
