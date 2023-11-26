package com.epam.crmgymboot.service;

import com.epam.crmgymboot.dto.common.TraineeDTO;
import com.epam.crmgymboot.dto.request.SignUpTraineeRequest;
import com.epam.crmgymboot.dto.request.UpdateTraineeRequest;
import com.epam.crmgymboot.dto.response.SignUpResponse;

public interface TraineeService {

    SignUpResponse signUpTrainee(SignUpTraineeRequest request);
    TraineeDTO getTraineeProfile(String username);
    TraineeDTO updateTraineeProfile(String username, UpdateTraineeRequest request);

    void deleteTrainee(String username);

    void activateDeactivateTrainee(String username, boolean isActive);

}
