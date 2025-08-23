package com.fkhrayef.capstone3.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Check(constraints = "equity_percentage >= 0 AND equity_percentage <= 100")
public class Founder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String name;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL UNIQUE")
    private String email;

    @Column(columnDefinition = "VARCHAR(13) NOT NULL")
    private String phone;

    @Column(columnDefinition = "DOUBLE DEFAULT 0")
    private Double equityPercentage;

    @Column(columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean hasStartup;

    // relations
    @ManyToOne
    @JsonIgnore
    private Startup startup;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
