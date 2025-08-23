package com.fkhrayef.capstone3.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Investor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "name can't be empty")
    @Size(min = 4, max = 25, message = "name length should be between 4-25 ")
    @Column(columnDefinition = "varchar(25) not null")
    private String name;

    @NotEmpty(message = "Email can't be empty")
    @Email(message = "Email should be valid")
    @Column(nullable = false, unique = true)
    private String email;

    @NotEmpty(message = "name can't be empty")
    @Size(min = 4, max = 25, message = "organization name length should be between 4-25 ")
    @Column(columnDefinition = "varchar(25) not null")
    private String organizationName;

    // todo (could add pattern for it)
    @NotEmpty(message = "name can't be empty")
    @Column(columnDefinition = "varchar(255) not null")
    private String investmentFocus;

    // relations
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "investor")
    private Set<Investment> investments;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
