package com.epam.crmgymboot.repository;

import com.epam.crmgymboot.model.TrainingType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrainingTypeRepository extends JpaRepository<TrainingType, Long> {
    Optional<TrainingType> findTrainingTypeByName(String name);

    boolean existsByName(String name);
    void deleteByName(String name);
}
