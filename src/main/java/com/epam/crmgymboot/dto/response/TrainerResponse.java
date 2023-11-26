package com.epam.crmgymboot.dto.response;

import com.epam.crmgymboot.dto.common.TrainingTypeDTO;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TrainerResponse {
    private @NotEmpty String firstname;
    private @NotEmpty String lastname;
    private @NotEmpty String username;
    private @NotNull TrainingTypeDTO specialization;
}
