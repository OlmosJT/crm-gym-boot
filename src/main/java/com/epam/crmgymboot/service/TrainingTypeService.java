package com.epam.crmgymboot.service;

import com.epam.crmgymboot.dto.common.TrainingTypeDTO;

import java.util.List;

public interface TrainingTypeService {
    List<TrainingTypeDTO> findAllTrainingTypes();

    TrainingTypeDTO addTrainingType(String name);
    TrainingTypeDTO updateTrainingType(TrainingTypeDTO dto);
    void deleteTrainingType(Long id);
    void deleteTrainingType(String name);

}
