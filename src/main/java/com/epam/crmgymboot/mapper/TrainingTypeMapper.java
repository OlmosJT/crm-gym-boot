package com.epam.crmgymboot.mapper;

import com.epam.crmgymboot.dto.common.TrainingTypeDTO;
import com.epam.crmgymboot.model.TrainingType;

public class TrainingTypeMapper {
    public static TrainingTypeDTO toDTO(TrainingType trainingType) {
        return new TrainingTypeDTO(trainingType.getId(), trainingType.getName());
    }
}
