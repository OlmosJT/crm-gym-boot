package com.epam.crmgymboot.service;

import com.epam.crmgymboot.dto.common.TrainingDTO;
import com.epam.crmgymboot.dto.request.AddTrainingRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface TrainingService {
    TrainingDTO createTraining(AddTrainingRequest request);
    TrainingDTO updateTraining(Long id, TrainingDTO dto);

    Page<TrainingDTO> getTrainingsOfTrainer(
            String trainerUsername,
            String traineeUsername,
            LocalDateTime periodFrom,
            LocalDateTime periodTo,
            Pageable pageable
    );
    Page<TrainingDTO> getTrainingsOfTrainee(
            String traineeUsername,
            String trainerUsername,
            LocalDateTime periodFrom,
            LocalDateTime periodTo,
            Long trainingTypeId,
            Pageable pageable
    );
}
