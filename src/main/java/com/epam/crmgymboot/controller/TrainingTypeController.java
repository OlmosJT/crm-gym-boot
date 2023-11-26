package com.epam.crmgymboot.controller;

import com.epam.crmgymboot.actuator.RequestsCounterMetrics;
import com.epam.crmgymboot.dto.common.TrainingTypeDTO;
import com.epam.crmgymboot.model.TrainingType;
import com.epam.crmgymboot.service.TrainingTypeService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/training-types")
@AllArgsConstructor
public class TrainingTypeController {

    private final TrainingTypeService trainingTypeService;
    private final RequestsCounterMetrics metrics;

    @GetMapping
    public List<TrainingTypeDTO> getTrainingTypes() {
        metrics.incrementGetRequests();
        return trainingTypeService.findAllTrainingTypes();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public String createTrainingType(@RequestParam String name) {
        metrics.incrementPostRequests();
        trainingTypeService.addTrainingType(name);
        return "Training type \"" + name + "\" created successfully!";
    }

    @DeleteMapping("/{id}")
    public void deleteTrainingType(@PathVariable Long id) {
        trainingTypeService.deleteTrainingType(id);
    }

    @PatchMapping("/{id}")
    public void changeTrainingTypeName(@PathVariable Long id, @RequestParam String name) {
        trainingTypeService.updateTrainingType(new TrainingTypeDTO(id, name));
    }
}
