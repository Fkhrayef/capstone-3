package com.fkhrayef.capstone3.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Check(constraints = "hourly_rate >= 0 and " +
        "LENGTH(name) >= 3 and " +
        "LENGTH(email) >= 3 and " +
        "total_earnings >= 0")
public class Advisor {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Integer id;


    @Column(columnDefinition = "VARCHAR(200)")
    private String name;


    @Column(columnDefinition = "VARCHAR(255) unique")
    private String email;


    @Column(columnDefinition = "VARCHAR(13)")
    private String phone;

    // todo Adding expertiseAreas As a different entity
    @Column(columnDefinition = "VARCHAR(200)")
    private String expertiseArea;


    @Column(columnDefinition = "INTEGER")
    private Integer yearsExperience;


    @Column(columnDefinition = "DECIMAL(8,2)")
    private Double hourlyRate;

    @Column(columnDefinition = "BOOLEAN")
    private Boolean isAvailable;

    // earnings tracking
    @Column(columnDefinition = "DECIMAL(10,2) DEFAULT 0")
    private Double totalEarnings = 0.0;

    // relation
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "advisor")
    private Set<AdvisorSession> advisorSessions;


    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
