package com.epam.crmgymboot.controller;

import com.epam.crmgymboot.actuator.RequestsCounterMetrics;
import com.epam.crmgymboot.dto.common.TraineeDTO;
import com.epam.crmgymboot.dto.common.TrainerDTO;
import com.epam.crmgymboot.dto.common.TrainingDTO;
import com.epam.crmgymboot.dto.request.EnableDisableUserRequest;
import com.epam.crmgymboot.dto.request.SignUpTraineeRequest;
import com.epam.crmgymboot.dto.request.UpdateTraineeRequest;
import com.epam.crmgymboot.dto.response.SignUpResponse;
import com.epam.crmgymboot.dto.response.TrainerResponse;
import com.epam.crmgymboot.service.TraineeService;
import com.epam.crmgymboot.service.TrainerService;
import com.epam.crmgymboot.service.TrainingService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("api/v1/trainees")
@AllArgsConstructor
public class TraineeController {

    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;
    private final RequestsCounterMetrics metrics;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SignUpResponse signUpTrainee(@Valid @RequestBody SignUpTraineeRequest request) {
        metrics.incrementPostRequests();
        return traineeService.signUpTrainee(request);
    }

    @GetMapping("/{username}")
    public TraineeDTO getTrainee(@PathVariable(name = "username") String username) {
        metrics.incrementGetRequests();
        return traineeService.getTraineeProfile(username);
    }

    @PutMapping("/{username}")
    public TraineeDTO updateTrainee(@PathVariable(name = "username") String username,
                                    @Valid @RequestBody UpdateTraineeRequest request) {
        return traineeService.updateTraineeProfile(username, request);
    }

    @DeleteMapping("/{username}")
    public void deleteTraineeProfile(@PathVariable String username) {
        traineeService.deleteTrainee(username);
    }

    @PatchMapping("/{username}")
    public ResponseEntity<String> enableDisableTrainee(
            @PathVariable String username,
            @Valid @RequestBody EnableDisableUserRequest request
    ) {
        traineeService.activateDeactivateTrainee(username, request.isActive());
        return new ResponseEntity<>("updated", HttpStatus.OK);
    }

    @GetMapping("/{username}/trainings")
    public Page<TrainingDTO> findTrainings(
            @PathVariable String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String[] sort,
            @RequestParam(required = false) String periodFrom,
            @RequestParam(required = false) String periodTo,
            @RequestParam(required = false) String trainerUsername,
            @RequestParam(required = false) Long trainingTypeId
    ) {
        LocalDateTime localDateFrom = null;
        LocalDateTime localDateTo = null;

        if (periodFrom != null) {
            try {
                localDateFrom = LocalDateTime.parse(periodFrom, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Format must match to pattern `yyyy-MM-ddTHH:mm`. Invalid periodFrom format: " + periodFrom);
            }
        }

        if (periodTo != null) {
            try {
                localDateTo = LocalDateTime.parse(periodTo, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Format must match to pattern `yyyy-MM-ddTHH:mm`. Invalid periodFrom format: " + periodFrom);
            }
        }
        metrics.incrementGetRequests();
        return trainingService.getTrainingsOfTrainee(
                username,
                trainerUsername,
                localDateFrom,
                localDateTo,
                trainingTypeId,
                PageRequest.of(page, size, Sort.by(sort).descending())
        );
    }

    @GetMapping("/{username}/available-trainers")
    public List<TrainerResponse> getActiveTrainersNotAssignedOnTrainee(
            @PathVariable String username
    ) {
        metrics.incrementGetRequests();
        return trainerService.findActiveTrainersNotAssignedOnTrainee(username);
    }

}
