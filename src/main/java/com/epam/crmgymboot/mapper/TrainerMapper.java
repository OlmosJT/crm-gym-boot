package com.epam.crmgymboot.mapper;

import com.epam.crmgymboot.dto.common.TrainerDTO;
import com.epam.crmgymboot.dto.common.UserDTO;
import com.epam.crmgymboot.dto.response.TrainerResponse;
import com.epam.crmgymboot.model.Trainee;
import com.epam.crmgymboot.model.Trainer;

import java.util.List;

public class TrainerMapper {

    public static List<TrainerResponse> toTrainerResponse(List<Trainer> trainers) {
        return trainers.stream().map(trainer -> new TrainerResponse(
                        trainer.getUserEntity().getFirstname(),
                        trainer.getUserEntity().getLastname(),
                        trainer.getUserEntity().getUsername(),
                        TrainingTypeMapper.toDTO(trainer.getSpecialization())
                )
        ).toList();
    }

    public static TrainerDTO toDTO(Trainer trainer, List<Trainee> assignedTrainees) {
        TrainerDTO dto = new TrainerDTO();
        dto.setFirstname(trainer.getUserEntity().getFirstname());
        dto.setLastname(trainer.getUserEntity().getLastname());
        dto.setUsername(trainer.getUserEntity().getUsername());
        dto.setAddress(trainer.getUserEntity().getAddress());
        dto.setDateOfBirth(trainer.getUserEntity().getDateOfBirth());
        dto.setIsActive(trainer.getUserEntity().getIsActive());
        dto.setSpecialization(TrainingTypeMapper.toDTO(trainer.getSpecialization()));

        dto.setAssignedTrainees(assignedTrainees.stream().map(trainee -> new UserDTO(
                trainee.getUserEntity().getFirstname(),
                trainee.getUserEntity().getLastname(),
                trainee.getUserEntity().getUsername()
        )).toList());

        return dto;
    }

    public static UserDTO toUserDTO(Trainer trainer) {
        UserDTO dto = new UserDTO();
        dto.setFirstname(trainer.getUserEntity().getFirstname());
        dto.setLastname(trainer.getUserEntity().getLastname());
        dto.setUsername(trainer.getUserEntity().getUsername());
        return dto;
    }

    public static List<UserDTO> toUserDTOs(List<Trainer> trainers) {
        return trainers.stream().map(TrainerMapper::toUserDTO).toList();
    }
}
