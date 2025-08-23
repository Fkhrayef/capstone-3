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
@Check(constraints = "amount > 0")
@Check(constraints = "payment_type IN ('freelancer_project', 'advisor_session', 'subscription')")
@Check(constraints = "status IN ('initiated','pending','paid','captured','failed','expired','refunded','partially_refunded')")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = true)
    private String moyasarPaymentId; // From Moyasar API response

    @Column(columnDefinition = "DECIMAL(10,2)")
    private Double amount;

    @Column(columnDefinition = "VARCHAR(20)")
    private String paymentType;

    @Column(columnDefinition = "VARCHAR(20)")
    private String status;

    @Column(columnDefinition = "VARCHAR(3) DEFAULT 'SAR'")
    private String currency = "SAR";

    @Column(columnDefinition = "VARCHAR(500)")
    private String description;

    // Reference IDs for different payment types (only one will be filled based on paymentType)
    @Column(columnDefinition = "INTEGER")
    private Integer freelancerProjectId;

    @Column(columnDefinition = "INTEGER")
    private Integer advisorSessionId;

    @Column(columnDefinition = "INTEGER")
    private Integer subscriptionId;

    // Recipient IDs (only one will be filled for freelancer/advisor payments)
    @Column(columnDefinition = "INTEGER")
    private Integer freelancerId;

    @Column(columnDefinition = "INTEGER")
    private Integer advisorId;

    // Relations
    @ManyToOne
    @JoinColumn(name = "startup_id")
    @JsonIgnore
    private Startup startup; // Always the payer

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
