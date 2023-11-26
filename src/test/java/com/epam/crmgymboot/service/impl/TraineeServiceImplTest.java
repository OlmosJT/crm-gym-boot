package com.epam.crmgymboot.service.impl;

import com.epam.crmgymboot.dto.common.TraineeDTO;
import com.epam.crmgymboot.dto.request.SignUpTraineeRequest;
import com.epam.crmgymboot.dto.request.UpdateTraineeRequest;
import com.epam.crmgymboot.dto.response.SignUpResponse;
import com.epam.crmgymboot.model.Trainee;
import com.epam.crmgymboot.model.Trainer;
import com.epam.crmgymboot.model.UserEntity;
import com.epam.crmgymboot.repository.TraineeRepository;
import com.epam.crmgymboot.repository.TrainerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TraineeServiceImplTest {
    private @Mock TraineeRepository traineeRepository;
    private @Mock TrainerRepository trainerRepository;
    private @InjectMocks TraineeServiceImpl traineeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void signUpTrainee_201Created() {
        SignUpTraineeRequest request = new SignUpTraineeRequest();
        request.setFirstname("John");
        request.setLastname("Cena");
        request.setAddress("2345 Maple Street, Vancouver, BC, V6A 2B3, Canada");
        request.setDateOfBirth(LocalDate.parse("1977-04-23", DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        when(traineeRepository.existsTraineeByUserEntityUsername(anyString())).thenReturn(false);
        when(traineeRepository.save(any(Trainee.class))).thenReturn(null);

        SignUpResponse response = traineeService.signUpTrainee(request);
        System.out.println(response);
        Assertions.assertEquals("John.Cena", response.username());
        Assertions.assertEquals(10, response.password().length());


        verify(traineeRepository, times(1)).existsTraineeByUserEntityUsername(anyString());
        verify(traineeRepository, times(1)).save(any(Trainee.class));
    }

    @Test
    void getTraineeProfile_404NotFound() {
        String traineeUsername ="John.Cena";

        when(traineeRepository.findTraineeByUserEntityUsername(anyString())).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            traineeService.getTraineeProfile(traineeUsername);
        });

        verify(traineeRepository, times(1)).findTraineeByUserEntityUsername(anyString());
    }

    @Test
    public void updateTraineeProfile_shouldUpdateTraineeAndReturnTraineeDTO() {
        // Create mock trainee, userEntity, and request objects
        UserEntity userEntity = new UserEntity();
        userEntity.setId(16L);
        userEntity.setFirstname("John");
        userEntity.setLastname("Cena");
        userEntity.setUsername("John.Cena");
        userEntity.setPassword("uCan1Se2Me");
        userEntity.setAddress("2345 Maple Street, Vancouver, BC, V6A 2B3, Canada");
        userEntity.setDateOfBirth(LocalDate.parse("1977-04-23", DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        userEntity.setIsActive(true);

        Trainee trainee = new Trainee();
        trainee.setId(16L);
        trainee.setUserEntity(userEntity);

        Trainer trainer = new Trainer();
        trainer.setId(1L); // Initialize trainer's ID

        List<Trainer> assignedTrainers = new ArrayList<>();
        assignedTrainers.add(trainer);

        UpdateTraineeRequest request = new UpdateTraineeRequest();
        request.setFirstname("John");
        request.setLastname("Cena");
        request.setAddress("updated address");
        request.setDateOfBirth(LocalDate.parse("1977-04-23", DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        // Stub traineeRepository.findTraineeByUserEntityUsername() to return the mock trainee
        when(traineeRepository.findTraineeByUserEntityUsername(anyString())).thenReturn(Optional.of(trainee));

        // Stub trainerRepository.findDistinctByTrainingsTraineeId() to return a list of initialized trainers
        when(trainerRepository.findDistinctByTrainingsTraineeId(anyLong())).thenReturn(assignedTrainers);

        // Call the updateTraineeProfile method
        TraineeDTO traineeDTO = traineeService.updateTraineeProfile("John.Cena", request);

        // Verify that the trainee's user entity was updated
        Assertions.assertEquals("John", trainee.getUserEntity().getFirstname());
        Assertions.assertEquals("Cena", trainee.getUserEntity().getLastname());
        Assertions.assertEquals("updated address", trainee.getUserEntity().getAddress());
        Assertions.assertEquals(LocalDate.parse("1977-04-23", DateTimeFormatter.ofPattern("yyyy-MM-dd")), trainee.getUserEntity().getDateOfBirth());

        // Verify that the trainee was saved
        verify(traineeRepository, times(1)).save(trainee);

        // Verify that the traineeDTO was created with the updated trainee and assigned trainers list
        Assertions.assertEquals("John.Cena", traineeDTO.getUsername());
        Assertions.assertEquals(assignedTrainers, traineeDTO.getAssignedTrainers());
    }


    @Test
    void deleteTrainee() {
        String traineeUsername = "john.doe";

        when(traineeRepository.findTraineeByUserEntityUsername(anyString())).thenReturn(Optional.of(new Trainee()));
        doNothing().when(traineeRepository).deleteTraineeByUserEntityUsername(anyString());

        traineeService.deleteTrainee(traineeUsername);

        verify(traineeRepository, times(1)).deleteTraineeByUserEntityUsername(anyString());
        verify(traineeRepository, times(1)).findTraineeByUserEntityUsername(anyString());
    }

    @Test
    void activateDeactivateTrainee() {
        // Create mock trainee, userEntity, and request objects
        UserEntity userEntity = new UserEntity();
        userEntity.setId(16L);
        userEntity.setFirstname("John");
        userEntity.setLastname("Cena");
        userEntity.setUsername("John.Cena");
        userEntity.setPassword("uCan1Se2Me");
        userEntity.setAddress("2345 Maple Street, Vancouver, BC, V6A 2B3, Canada");
        userEntity.setDateOfBirth(LocalDate.parse("1977-04-23", DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        userEntity.setIsActive(true);

        Trainee trainee = new Trainee();
        trainee.setId(16L);
        trainee.setUserEntity(userEntity);

        Trainer trainer = new Trainer();
        trainer.setId(1L); // Initialize trainer's ID

        when(traineeRepository.findTraineeByUserEntityUsername(anyString())).thenReturn(Optional.of(trainee));

        traineeService.activateDeactivateTrainee("john.doe", false);


        verify(traineeRepository, times(1)).findTraineeByUserEntityUsername(anyString());
        verify(traineeRepository, times(1)).save(any());

    }
}