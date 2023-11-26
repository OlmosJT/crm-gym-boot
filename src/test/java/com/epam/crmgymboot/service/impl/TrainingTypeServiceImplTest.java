package com.epam.crmgymboot.service.impl;

import com.epam.crmgymboot.dto.common.TrainingTypeDTO;
import com.epam.crmgymboot.model.TrainingType;
import com.epam.crmgymboot.repository.TrainingTypeRepository;
import jakarta.persistence.EntityExistsException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class TrainingTypeServiceImplTest {

    private @Mock TrainingTypeRepository trainingTypeRepository;
    private @InjectMocks TrainingTypeServiceImpl trainingTypeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void addTrainingType_409Conflict_existException() {
        String trainingName = "Stretching";

        when(trainingTypeRepository.findTrainingTypeByName(anyString()))
                .thenReturn(Optional.of(new TrainingType(11L, trainingName)));
        Assertions.assertThrows(EntityExistsException.class, () -> {
            TrainingTypeDTO dto = trainingTypeService.addTrainingType(trainingName);
        });
    }

    @Test
    void addTrainingType_201Created() {
        String trainingName = "Stretching";
        TrainingType trainingType = new TrainingType(11L, trainingName);

        when(trainingTypeRepository.findTrainingTypeByName(anyString()))
                .thenReturn(Optional.empty());
        when(trainingTypeRepository.save(any(TrainingType.class)))
                .thenReturn(trainingType);

        TrainingTypeDTO dto = trainingTypeService.addTrainingType(trainingName);
        assertEquals(trainingName, dto.name());
    }
}