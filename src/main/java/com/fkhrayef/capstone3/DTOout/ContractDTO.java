package com.fkhrayef.capstone3.DTOout;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class ContractDTO {
    private Date effective_date;
    private String investor_name;
    private String startup_name;
    private Double investment_amount;
    private String payment_method;
    private Integer investment_period;
    private String exchange;
    private Integer recurring_years;
    private Double recurring_amount;
}
