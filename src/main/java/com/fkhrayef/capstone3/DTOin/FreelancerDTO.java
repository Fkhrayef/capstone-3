package com.fkhrayef.capstone3.DTOin;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FreelancerDTO {

    @NotEmpty(message = "name can't be empty")
    @Size(min = 4, max = 25, message = "name length should be between 4-25 ")
    private String name;

    @NotEmpty(message = "Email can't be empty")
    @Email(message = "Email should be valid")
    private String email;

    @NotEmpty(message = "phone can't be empty")
    @Pattern(regexp = "^\\+9665\\d{8}$", message = "Phone number must be a valid Saudi mobile in the format +9665XXXXXXXX")
    private String phone;

    //todo add pattern underneath:
    @NotEmpty(message = "specialization can't be empty")
    private String specialization;

    @NotNull(message = "hourly rate can't be empty")
    @PositiveOrZero(message = "hourly rate should not be negative")
    private Double hourlyRate;

    @NotNull(message = "years pf experience can't be empty")
    @PositiveOrZero(message = "years pf experience  should be Zero or Above")
    private Integer yearsExperience;


}
