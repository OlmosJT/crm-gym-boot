package com.epam.crmgymboot.repository;

import com.epam.crmgymboot.model.Trainer;
import com.epam.crmgymboot.model.Training;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Sql(scripts = {"classpath:data/schema.sql", "classpath:data/data.sql" })
class TrainingRepositoryTest {

    @Autowired
    private TrainingRepository trainingRepository;

    @Test
    void findTrainingsOfTrainee() {

    }

    @Test
    void findTrainingsOfTrainer() {
    }

    @Test
    @DisplayName("Trainee's trainings list as page")
    void findAllByTraineeUserEntityUsername() {
        String traineeUsername ="jane.doe"; // id: 2L
        Page<Training> trainingPage = trainingRepository.findAllByTraineeUserEntityUsername(
                traineeUsername,
                PageRequest.of(0, 2, Sort.by("trainingDate").descending())
        );

        Assertions.assertEquals(2, trainingPage.getTotalElements());
        Assertions.assertEquals(1, trainingPage.getTotalPages());
    }

    @Test
    void findAllByTraineeIdOrderByIdDesc() {
        Long traineeId = 2L;
        List<Training>  trainingList = trainingRepository.findAllByTraineeIdOrderByIdDesc(traineeId);
        Assertions.assertEquals(2, trainingList.size());
    }

    @Test
    void findAllByTrainerUserEntityUsername() {
        String trainerUsername = "ava.taylor"; // id: 3L
        Page<Training> trainingPage = trainingRepository.findAllByTrainerUserEntityUsername(
                trainerUsername,
                PageRequest.of(0, 2, Sort.by("trainingDate").descending())
        );
        Assertions.assertEquals(6, trainingPage.getTotalElements());
        Assertions.assertEquals(3, trainingPage.getTotalPages());
    }

    @Test
    void findAllByTrainerIdOrderByIdDesc() {
        Long trainerId = 3L;
        List<Training> trainingList = trainingRepository.findAllByTrainerIdOrderByIdDesc(trainerId);
        Assertions.assertEquals(6, trainingList.size());
    }
}