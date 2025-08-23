package com.fkhrayef.capstone3.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Check(constraints = "plan_type IN ('free', 'pro', 'enterprise')")
@Check(constraints = "billing_cycle IN ('monthly', 'yearly')")
@Check(constraints = "status IN ('active', 'expired')")
@Check(constraints = "price >= 0")
public class Subscription {
    @Id
    private Integer id;

    @Column(columnDefinition = "VARCHAR(15) NOT NULL")
    private String planType;

    @Column(columnDefinition = "VARCHAR(15) NOT NULL")
    private String billingCycle;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @Column(columnDefinition = "VARCHAR(15)")
    private String status;

    // Pricing field
    @Column(columnDefinition = "DECIMAL(10,2)")
    private Double price; // Price for this subscription period

    // relations
    @OneToOne
    @MapsId
    @JsonIgnore
    private Startup startup;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
