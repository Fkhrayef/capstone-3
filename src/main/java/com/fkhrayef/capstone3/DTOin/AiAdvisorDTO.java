package com.fkhrayef.capstone3.DTOin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AiAdvisorDTO {

    @NotNull(message = "Startup ID is required")
    private Integer startupId;

    @NotBlank(message = "prompt can't be empty")
    @Size(max = 4000, message = "prompt must not exceed 4000 characters")
    private String prompt;
}
