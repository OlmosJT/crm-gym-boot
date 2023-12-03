package com.epam.crmgymboot.repository;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Sql(scripts = {"classpath:data/schema.sql", "classpath:data/data.sql" })
class TrainingTypeRepositoryTest {

    @Autowired
    private TrainingTypeRepository trainingTypeRepository;

    @Test
    void findTrainingTypeByName_exist() {
        String trainingTypeName = "Cardio";
        boolean result = trainingTypeRepository.findTrainingTypeByName(trainingTypeName).isPresent();
        Assertions.assertTrue(result);
    }

    @Test
    void findTrainingTypeByName_absent() {
        String trainingTypeName = "Unavailable training type";
        boolean result = trainingTypeRepository.findTrainingTypeByName(trainingTypeName).isPresent();
        Assertions.assertFalse(result);
    }

    @Test
    void existsByName_true() {
        String trainingTypeName = "Cardio";
        boolean result = trainingTypeRepository.existsByName(trainingTypeName);
        Assertions.assertTrue(result);
    }

    @Test
    void existsByName_false() {
        String trainingTypeName = "Unavailable training type";
        boolean result = trainingTypeRepository.existsByName(trainingTypeName);
        Assertions.assertFalse(result);
    }

    @Test
    @Transactional
    @Disabled("It has used as fk in Trainer table, therefore deleting causes throwing of exception. " +
            "Need to add flag to entity to keep trainers but remove training type for future new trainers")
    void deleteByName() {
        String trainingTypeName = "Cardio";
        trainingTypeRepository.deleteByName(trainingTypeName);
        boolean result = trainingTypeRepository.existsByName(trainingTypeName);
        Assertions.assertFalse(result);
    }
}