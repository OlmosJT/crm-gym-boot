package com.epam.crmgymboot.dto.common;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record TrainingTypeDTO(
        @NotNull
        Long id,
        @NotEmpty
        String name
) {
}
