package com.epam.crmgymboot.controller;

import com.epam.crmgymboot.actuator.RequestsCounterMetrics;
import com.epam.crmgymboot.dto.common.TrainingDTO;
import com.epam.crmgymboot.dto.request.AddTrainingRequest;
import com.epam.crmgymboot.service.TrainingService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/trainings")
@AllArgsConstructor
public class TrainingController {

    private final TrainingService trainingService;
    private final RequestsCounterMetrics metrics;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TrainingDTO addTraining(@Valid @RequestBody AddTrainingRequest request) {
        metrics.incrementPostRequests();
        return trainingService.createTraining(request);
    }

    @PatchMapping("/{id}")
    public void updateTraining(@Valid @RequestBody TrainingDTO dto, @PathVariable Long id) {
        trainingService.updateTraining(id, dto);
    }
}
