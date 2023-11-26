package com.epam.crmgymboot.repository;

import com.epam.crmgymboot.model.Trainee;
import jakarta.transaction.Transactional;
import org.hibernate.annotations.Comment;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TraineeRepositoryTest {

    @Autowired
    private TraineeRepository traineeRepository;

    @Test
    void findTraineeByUserEntityUsername() {
        String username = "alice.smith";
        Optional<Trainee> trainee = traineeRepository.findTraineeByUserEntityUsername(username);

        Assertions.assertTrue(trainee.isPresent());
        Assertions.assertEquals(trainee.get().getUserEntity().getPassword(), "password3");
    }

    @Test
    void existsTraineeByUserEntityUsername() {
        String username = "michael.williams";
        boolean result = traineeRepository.existsTraineeByUserEntityUsername(username);

        Assertions.assertTrue(result);
    }

    @Test
    @Transactional
    @DisplayName(value = "Deletes Trainee and UserEntity at the same time only within @Transactional context")
    void deleteTraineeByUserEntityUsername() {
        String username = "david.davis";
        traineeRepository.deleteTraineeByUserEntityUsername(username);

        Assertions.assertFalse(traineeRepository.existsTraineeByUserEntityUsername(username));
    }

    @Test
    void findDistinctByTrainingsTrainerId() {
        List<Trainee> assignedTrainees = traineeRepository.findDistinctByTrainingsTrainerId(1L);
        Assertions.assertEquals(4, assignedTrainees.size());
    }
}