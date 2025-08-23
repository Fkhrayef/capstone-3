package com.fkhrayef.capstone3.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Check(constraints = "round_type IN ('pre_seed', 'seed', 'series_a', 'series_b', 'series_c') and " +
        "payment_method IN('check', 'transfer') and " +
        "investment_amount > 0 and " +
        "recurring_amount > 0 and " +
        "recurring_years > 0 and " +
        "minimum_investment_period > 0")
public class Investment {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Integer id;


    @Column(columnDefinition = "VARCHAR(8)")
    private String roundType;

    @Column(columnDefinition = "Date")
    private Date effectiveDate;


    @Column(columnDefinition = "DECIMAL(15,2)")
    private Double investment_amount;

    @Column(columnDefinition = "VARCHAR(8)")
    private String paymentMethod;


    @Column(columnDefinition = "BOOLEAN")
    private Boolean isRecurring;

    @Column(columnDefinition = "DECIMAL(15,2)")
    private Double recurringAmount;

    @Column(columnDefinition = "INTEGER")
    private Integer recurringYears;

    @Column(columnDefinition = "INTEGER")
    private Integer minimumInvestmentPeriod;

    // relations
    @ManyToOne
    @JsonIgnore
    private Investor investor;

    @ManyToOne
    @JsonIgnore
    private Startup startup;


    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
