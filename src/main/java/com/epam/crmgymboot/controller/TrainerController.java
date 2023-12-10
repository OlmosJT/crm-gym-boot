package com.epam.crmgymboot.controller;

import com.epam.crmgymboot.actuator.RequestsCounterMetrics;
import com.epam.crmgymboot.dto.common.TrainerDTO;
import com.epam.crmgymboot.dto.common.TrainingDTO;
import com.epam.crmgymboot.dto.request.SignUpTrainerRequest;
import com.epam.crmgymboot.dto.request.UpdateTrainerRequest;
import com.epam.crmgymboot.dto.response.SignUpResponse;
import com.epam.crmgymboot.service.TraineeService;
import com.epam.crmgymboot.service.TrainerService;
import com.epam.crmgymboot.service.TrainingService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("api/v1/trainers")
@AllArgsConstructor
public class TrainerController {
    private final TrainerService trainerService;
    private final TrainingService trainingService;
    private final RequestsCounterMetrics metrics;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SignUpResponse signUpTrainer(@Valid @RequestBody SignUpTrainerRequest request) {
        metrics.incrementPostRequests();
        return trainerService.signUpTrainer(request);
    }

    @GetMapping("/{username}")
    public TrainerDTO getTrainer(@PathVariable(name = "username") String username) {
        metrics.incrementGetRequests();
        return trainerService.getTrainerProfile(username);
    }

    @PutMapping("/{username}")
    public TrainerDTO updateTrainer(@Valid @RequestBody UpdateTrainerRequest request) {
        return trainerService.updateTrainerProfile(request);
    }

    @DeleteMapping("/{username}")
    public void deleteTrainerProfile(@PathVariable String username) {
        trainerService.deleteTrainee(username);
    }

    @PatchMapping("/{username}")
    public void enableDisableTrainer(
            @PathVariable String username,
            @RequestParam(value = "isActive") boolean isActive
    ) {
        trainerService.activateDeactivateTrainer(username, isActive);
    }

    @GetMapping("/{username}/trainings")
    public Page<TrainingDTO> findTrainings(
            @PathVariable String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String[] sort,
            @RequestParam(required = false) String periodFrom,
            @RequestParam(required = false) String periodTo,
            @RequestParam(required = false) String traineeUsername
    ) {
        LocalDateTime localDateFrom = null;
        LocalDateTime localDateTo = null;

        if (periodFrom != null) {
            try {
                localDateFrom = LocalDateTime.parse(periodFrom, DateTimeFormatter.ofPattern("yyyy-MM-ddTHH:mm"));
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Format must match to pattern `yyyy-MM-ddTHH:mm`. Invalid periodFrom format: " + periodFrom);
            }
        }

        if (periodTo != null) {
            try {
                localDateTo = LocalDateTime.parse(periodTo, DateTimeFormatter.ofPattern("yyyy-MM-ddTHH:mm"));
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Format must match to pattern `yyyy-MM-ddTHH:mm`. Invalid periodFrom format: " + periodFrom);
            }
        }
        metrics.incrementGetRequests();
        return trainingService.getTrainingsOfTrainer(
                username,
                traineeUsername,
                localDateFrom,
                localDateTo,
                PageRequest.of(page, size, Sort.by(sort).descending())
        );
    }
}
