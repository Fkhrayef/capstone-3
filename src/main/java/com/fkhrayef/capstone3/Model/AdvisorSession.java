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
@Check(constraints = "duration_minutes > 0 and " +
        "status IN ('pending', 'scheduled', 'confirmed', 'in_progress', 'completed') and " +
        "session_cost >= 0")
public class AdvisorSession {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "varchar(50)")
    private String title;

    // todo this is session_date + start_time, and duration_minutes is the session duration in mins
    @Column(columnDefinition = "DATETIME")
    private LocalDateTime startDate;


    @Column(columnDefinition = "INTEGER")
    private Integer duration_minutes;

    @Column(columnDefinition = "VARCHAR(15)")
    private String status;

    @Column(columnDefinition = "VARCHAR(2000)")
    private String notes;

    // Pricing field
    @Column(columnDefinition = "DECIMAL(10,2)")
    private Double sessionCost; // Calculated from advisor hourly rate × duration

    @Column(columnDefinition = "VARCHAR(255)")
    private String meeting_url;

    @Column(columnDefinition = "VARCHAR(255)")
    private String meeting_id;

    // relations
    @ManyToOne
    @JsonIgnore
    private Advisor advisor;

    @ManyToOne
    @JsonIgnore
    private Startup startup;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
