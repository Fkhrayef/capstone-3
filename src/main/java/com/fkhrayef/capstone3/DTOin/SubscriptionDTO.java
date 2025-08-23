package com.fkhrayef.capstone3.DTOin;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SubscriptionDTO {

    private Integer founderId;

    @NotEmpty(message = "Plan Type cannot be null")
    @Pattern(regexp = "^(free|pro|enterprise)$", message = "Plan Type must be either free, pro or enterprise")
    private String planType;

    @NotEmpty(message = "Billing Cycle cannot be null")
    @Pattern(regexp = "^(monthly|yearly)$", message = "Billing Cycle must be either monthly or yearly")
    private String billingCycle;

}
