package com.fkhrayef.capstone3.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Check(constraints = "status IN ('pending', 'accepted', 'active', 'completed') and " +
        "total_amount >= 0 and " +
        "estimated_hours >= 0")
public class FreelancerProject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @Column(columnDefinition = "varchar(30) not null")
    private String projectName;


    @Column(columnDefinition = "varchar(100) not null")
    private String description;


    @Column(columnDefinition = "varchar(10)")
    private String status;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    // Pricing fields - only hourly pricing supported (validation moved to DTO)
    @Column(columnDefinition = "DECIMAL(10,2)")
    private Double totalAmount;

    @Column(columnDefinition = "DECIMAL(8,2)")
    private Double estimatedHours;

    // relations
    @ManyToOne
    @JsonIgnore
    private Freelancer freelancer;

    @ManyToOne
    @JsonIgnore
    private Startup startup;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
