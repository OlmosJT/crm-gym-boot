package com.epam.crmgymboot.repository;

import com.epam.crmgymboot.model.Trainee;
import com.epam.crmgymboot.model.Trainer;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Sql(scripts = {"classpath:data/schema.sql", "classpath:data/data.sql" })
class TrainerRepositoryTest {

    @Autowired
    private TrainerRepository trainerRepository;

    @Test
    void findTrainerByUserEntityUsername() {
        String username = "sophie.clark";
        Optional<Trainer> trainer = trainerRepository.findTrainerByUserEntityUsername(username);

        Assertions.assertTrue(trainer.isPresent());
        Assertions.assertEquals(trainer.get().getSpecialization().getName(), "Cardio");
    }

    @Test
    void existsTrainerByUserEntityUsername() {
        String username = "daniel.johnson";
        boolean result = trainerRepository.existsTrainerByUserEntityUsername(username);

        Assertions.assertTrue(result);
    }

    @Test
    @Transactional
    void deleteTrainerByUserEntityUsername() {
        String username = "daniel.johnson";
        trainerRepository.deleteTrainerByUserEntityUsername(username);
        Assertions.assertFalse(trainerRepository.existsTrainerByUserEntityUsername(username));
    }

    @Test
    void findActiveTrainersNotAssociatedWithTrainee() {
        String traineeUsername = "john.doe";
        List<Trainer> notAssignedTrainers = trainerRepository
                .findActiveTrainersNotAssociatedWithTrainee(
                        traineeUsername
                );
        System.out.println(notAssignedTrainers);
        Assertions.assertEquals(2, notAssignedTrainers.size());
    }

    @Test
    void findDistinctByTrainingsTraineeId() {
        Long traineeId = 1L;
        List<Trainer> assignedTrainers = trainerRepository
                .findDistinctByTrainingsTraineeId(traineeId);
        Assertions.assertEquals(1, assignedTrainers.size());
    }
}