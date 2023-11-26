package com.epam.crmgymboot.mapper;

import com.epam.crmgymboot.dto.common.TraineeDTO;
import com.epam.crmgymboot.dto.common.UserDTO;
import com.epam.crmgymboot.model.Trainee;
import com.epam.crmgymboot.model.Trainer;

import java.util.List;

public class TraineeMapper {
    public static TraineeDTO toDTO(Trainee trainee, List<Trainer> assignedTrainers) {
        TraineeDTO dto = new TraineeDTO();
        dto.setFirstname(trainee.getUserEntity().getFirstname());
        dto.setLastname(trainee.getUserEntity().getLastname());
        dto.setUsername(trainee.getUserEntity().getUsername());
        dto.setAddress(trainee.getUserEntity().getAddress());
        dto.setDateOfBirth(trainee.getUserEntity().getDateOfBirth());
        dto.setIsActive(trainee.getUserEntity().getIsActive());

        dto.setAssignedTrainers(assignedTrainers.stream().map(trainer -> new UserDTO(
                trainer.getUserEntity().getFirstname(),
                trainer.getUserEntity().getLastname(),
                trainer.getUserEntity().getUsername()
        )).toList());
        return dto;
    }

    public static UserDTO toUserDTO(Trainee trainee) {
        UserDTO dto = new UserDTO();
        dto.setFirstname(trainee.getUserEntity().getFirstname());
        dto.setLastname(trainee.getUserEntity().getLastname());
        dto.setUsername(trainee.getUserEntity().getUsername());
        return dto;
    }

    public static List<UserDTO> toUserDTOs(List<Trainee> trainees) {
        return trainees.stream().map(TraineeMapper::toUserDTO).toList();
    }
}
