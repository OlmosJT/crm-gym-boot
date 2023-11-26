package com.epam.crmgymboot.repository;

import com.epam.crmgymboot.model.Training;
import com.epam.crmgymboot.model.TrainingType;
import io.micrometer.common.lang.NonNull;
import jakarta.annotation.Nullable;
import org.hibernate.annotations.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public interface TrainingRepository extends JpaRepository<Training, Long>, JpaSpecificationExecutor<Training> {

    @Query("SELECT t FROM Training t " +
            "WHERE (t.trainee.userEntity.username = :traineeUsername)" +
            "AND (:periodFrom IS NULL OR t.trainingDate >= :periodFrom) " +
            "AND (:periodTo IS NULL OR t.trainingDate <= :periodTo) " +
            "AND (:trainerUsername IS NULL OR t.trainer.userEntity.username = :trainerUsername) " +
            "AND (:trainingType IS NULL OR t.trainer.specialization = :trainingType)"
    )
    Page<Training> findTrainingsOfTrainee(
            @Param(value = "traineeUsername") String traineeUsername,
            @Param(value = "periodFrom") LocalDateTime periodFrom,
            @Param(value = "periodTo") LocalDateTime periodTo,
            @Param(value = "trainerUsername") String trainerUsername,
            @Param(value = "trainingType") TrainingType trainingType,
            Pageable pageable
    );

    @Override
    Page<Training> findAll(@Nullable Specification<Training> spec, @NonNull Pageable pageable);

    @Query("SELECT t FROM Training t " +
            "WHERE (t.trainer.userEntity.username = ?1)" +
            "AND (?2 IS NULL OR t.trainingDate >= ?2) " +
            "AND (?3 IS NULL OR t.trainingDate <= ?3) " +
            "AND (?4 IS NULL OR t.trainee.userEntity.username = ?4)")
    Page<Training> findTrainingsOfTrainer(
            String trainerUsername,
            LocalDateTime periodFrom,
            LocalDateTime periodTo,
            String traineeUsername,
            Pageable pageable
    );


    @Comment(value = "returns page of trainings associated with trainee")
    Page<Training> findAllByTraineeUserEntityUsername(String traineeUsername, Pageable pageable);
    @Comment(value = "returns list of all trainings of trainee")
    List<Training> findAllByTraineeIdOrderByIdDesc(Long traineeId);

    @Comment(value = "returns page of trainings associated with trainer")
    Page<Training> findAllByTrainerUserEntityUsername(String trainerUsername, Pageable pageable);
    @Comment(value = "returns list of all trainings of trainer")
    List<Training> findAllByTrainerIdOrderByIdDesc(Long trainerId);


}
