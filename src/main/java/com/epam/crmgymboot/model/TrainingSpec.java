package com.epam.crmgymboot.model;

import com.epam.crmgymboot.dto.common.TrainingFilter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;


public class TrainingSpec {
    public static Specification<Training> filterBy(TrainingFilter trainingFilter) {
        return Specification
                .where(hasTraineeUsername(trainingFilter.traineeUsername()))
                .and(hasTrainerUsername(trainingFilter.trainerUsername()))
                .and(hasPeriodFrom(trainingFilter.periodFrom()))
                .and(hasPeriodTo(trainingFilter.periodTo()))
                .and(hasTrainingTypeId(trainingFilter.trainingTypeId()));
    }

    public static Specification<Training> hasTraineeUsername(String traineeUsername) {
        return (root, query, builder) ->
                traineeUsername == null ?
                        builder.isTrue(builder.literal(true)) :
                        builder.equal(root.get("trainee").get("userEntity").get("username"), traineeUsername);
    }

    public static Specification<Training> hasTrainerUsername(String trainerUsername) {
        return (root, query, builder) ->
                trainerUsername == null ?
                        builder.isTrue(builder.literal(true)) :
                        builder.equal(root.get("trainer").get("userEntity").get("username"), trainerUsername);
    }

    public static Specification<Training> hasPeriodFrom(LocalDateTime periodFrom) {
        return (root, query, builder) ->
                periodFrom == null ?
                        builder.isTrue(builder.literal(true)) :
                        builder.greaterThanOrEqualTo(root.get("trainingDate"), periodFrom);
    }

    public static Specification<Training> hasPeriodTo(LocalDateTime periodTo) {
        return (root, query, builder) ->
                periodTo == null ?
                        builder.isTrue(builder.literal(true)) :
                        builder.lessThanOrEqualTo(root.get("trainingDate"), periodTo);
    }

    public static Specification<Training> hasTrainingTypeId(Long trainingTypeId) {
        return (root, query, builder) ->
                trainingTypeId == null ?
                        builder.isTrue(builder.literal(true)) :
                        builder.equal(root.get("trainer").get("specialization").get("id"), trainingTypeId);
    }
}
