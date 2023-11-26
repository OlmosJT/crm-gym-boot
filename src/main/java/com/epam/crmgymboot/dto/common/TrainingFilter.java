package com.epam.crmgymboot.dto.common;

import java.time.LocalDateTime;

public record TrainingFilter(
        String traineeUsername,
        String trainerUsername,
        LocalDateTime periodFrom,
        LocalDateTime periodTo,
        Long trainingTypeId

) {
}
