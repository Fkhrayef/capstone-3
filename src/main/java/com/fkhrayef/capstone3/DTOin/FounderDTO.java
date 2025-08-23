package com.fkhrayef.capstone3.DTOin;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FounderDTO {
    @NotEmpty(message = "Name cannot be null")
    private String name;

    @NotEmpty(message = "Email cannot be null")
    @Email(message = "Email is not valid")
    private String email;

    @NotEmpty(message = "Phone cannot be null")
    @Pattern(regexp = "^\\+9665\\d{8}$", message = "Phone number is not valid")
    private String phone;
}
