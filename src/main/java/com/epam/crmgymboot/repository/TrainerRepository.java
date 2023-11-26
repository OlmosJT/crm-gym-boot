package com.epam.crmgymboot.repository;

import com.epam.crmgymboot.model.Trainer;
import org.hibernate.annotations.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TrainerRepository extends JpaRepository<Trainer, Long> {
    Optional<Trainer> findTrainerByUserEntityUsername(String username);

    boolean existsTrainerByUserEntityUsername(String username);
    void deleteTrainerByUserEntityUsername(String username);
    @Comment(value = "Returns list of active trainers who are not assigned on trainee username")
    List<Trainer> findAllByUserEntityIsActiveTrueAndTrainingsTraineeUserEntityUsernameNot(String traineeUsername);

    @Query("SELECT DISTINCT t.trainer FROM Training t WHERE t.trainer.userEntity.isActive = true " +
            "AND NOT EXISTS (SELECT t2 FROM Training t2 WHERE t2.trainer.userEntity.username = t.trainer.userEntity.username " +
            "AND t2.trainee.userEntity.username = :traineeUsername)")
    List<Trainer> findActiveTrainersNotAssociatedWithTrainee(String traineeUsername);

    List<Trainer> findDistinctByTrainingsTraineeId(Long traineeId);
}
