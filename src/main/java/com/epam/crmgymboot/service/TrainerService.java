package com.epam.crmgymboot.service;

import com.epam.crmgymboot.dto.common.TraineeDTO;
import com.epam.crmgymboot.dto.common.TrainerDTO;
import com.epam.crmgymboot.dto.request.SignUpTraineeRequest;
import com.epam.crmgymboot.dto.request.SignUpTrainerRequest;
import com.epam.crmgymboot.dto.request.UpdateTraineeRequest;
import com.epam.crmgymboot.dto.request.UpdateTrainerRequest;
import com.epam.crmgymboot.dto.response.SignUpResponse;
import com.epam.crmgymboot.dto.response.TrainerResponse;

import java.util.List;

public interface TrainerService {
    SignUpResponse signUpTrainer(SignUpTrainerRequest request);
    List<TrainerResponse> findActiveTrainersNotAssignedOnTrainee(String traineeUsername);

    TrainerDTO getTrainerProfile(String username);
    TrainerDTO updateTrainerProfile(UpdateTrainerRequest request);

    void deleteTrainee(String username);
    void activateDeactivateTrainer(String username, boolean isActive);
}
