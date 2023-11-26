package com.epam.crmgymboot.mapper;

import com.epam.crmgymboot.dto.common.TrainingDTO;
import com.epam.crmgymboot.model.Training;

public class TrainingMapper {
    public static TrainingDTO toDTO(Training training) {
        TrainingDTO dto = new TrainingDTO();

        dto.setTrainingName(training.getTrainingName());
        dto.setTrainer(TrainerMapper.toUserDTO(training.getTrainer()));
        dto.setTrainee(TraineeMapper.toUserDTO(training.getTrainee()));
        dto.setDurationInMinutes(training.getDurationInMinutes());
        dto.setTrainingDate(training.getTrainingDate());

        return dto;
    }
}
