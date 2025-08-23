package com.fkhrayef.capstone3.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Check(constraints = "employee_count >= 0")
@Check(constraints = "valuation >= 0")
@Check(constraints = "status IN ('active', 'acquired', 'closed', 'ipo')")
public class Startup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String name;

    @Column(columnDefinition = "VARCHAR(2096) NOT NULL")
    private String description;

    @Column(columnDefinition = "VARCHAR(100) NOT NULL")
    // TODO Add Pattern
    private String industry;

    @Column(columnDefinition = "VARCHAR(50) NOT NULL")
    // TODO Add Pattern
    private String stage;

    @Column(columnDefinition = "DATE NOT NULL")
    private LocalDate foundedDate;

    @Column(columnDefinition = "INTEGER DEFAULT 1 NOT NULL")
    private Integer employeeCount;

    @Column(columnDefinition = "DOUBLE DEFAULT 0 NOT NULL")
    private Double valuation;

    @Column(columnDefinition = "VARCHAR(50) NOT NULL")
    private String status;

    // Relations
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "startup")
    private Set<Founder> founders;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "startup")
    private Set<Investment> investments;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "startup")
    private Set<AdvisorSession> advisorSessions;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "startup")
    private Set<FreelancerProject> freelancerProjects;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "startup")
    private Set<Payment> payments;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "startup")
    @PrimaryKeyJoinColumn
    private Subscription subscription;

    // AI usage tracking fields
    @Column(columnDefinition = "INTEGER DEFAULT 0")
    private Integer dailyAiUsageCount; // Current daily usage

    @Column(columnDefinition = "INTEGER DEFAULT 10")
    private Integer dailyAiLimit; // Default free tier limit (10 calls/day)

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // Optional stored card details for subscription renewals
    // These are optional at creation and can be provided when subscribing
    @Column(columnDefinition = "VARCHAR(100)")
    private String cardName;

    @Column(columnDefinition = "VARCHAR(32)")
    private String cardNumber;

    @Column(columnDefinition = "VARCHAR(8)")
    private String cardCvc;

    @Column(columnDefinition = "VARCHAR(4)")
    private String cardExpMonth;

    @Column(columnDefinition = "VARCHAR(6)")
    private String cardExpYear;
}
