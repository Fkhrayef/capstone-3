package com.fkhrayef.capstone3.DTOin;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class AdvisorDTO {


    @NotEmpty(message = "Name is required")
    @Size(min = 3, max = 200, message = "Name must be between 3 and 200 characters")
    private String name;

    @NotEmpty(message = "Email is required")
    @Size(min = 3, max = 255, message = "Email must be between 3 and 255 characters")
    @Email(message = "Email must be valid")
    private String email;

    @NotEmpty(message = "Phone cannot be null")
    @Pattern(regexp = "^\\+9665\\d{8}$", message = "Phone number must be a valid Saudi mobile in the format +9665XXXXXXXX")
    private String phone;

    // todo Adding expertiseAreas pattern
    @NotEmpty(message = "Expertise Area is required")
    private String expertiseArea;

    @NotNull(message = "Years Experience is required")
    @PositiveOrZero(message = "Years Experience must be greater than or equal to zero")
    @Max(value = 100, message = "Years Experience must be less than or equal to 100")
    private Integer yearsExperience;

    @NotNull(message = "Hourly Rate is required")
    @PositiveOrZero(message = "Hourly Rate must be greater than or equal to zero")
    private Double hourlyRate;

}
