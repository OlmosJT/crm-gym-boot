package com.epam.crmgymboot.repository;

import com.epam.crmgymboot.model.Trainee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TraineeRepository extends JpaRepository<Trainee, Long> {
    Optional<Trainee> findTraineeByUserEntityUsername(String username);
    boolean existsTraineeByUserEntityUsername(String username);
    void deleteTraineeByUserEntityUsername(String username);

    List<Trainee> findDistinctByTrainingsTrainerId(Long trainerId);
}
