package com.epam.crmgymboot.service.impl;

import com.epam.crmgymboot.dto.common.TrainingDTO;
import com.epam.crmgymboot.dto.common.TrainingFilter;
import com.epam.crmgymboot.dto.request.AddTrainingRequest;
import com.epam.crmgymboot.mapper.TraineeMapper;
import com.epam.crmgymboot.mapper.TrainerMapper;
import com.epam.crmgymboot.mapper.TrainingMapper;
import com.epam.crmgymboot.model.Training;
import com.epam.crmgymboot.model.TrainingSpec;
import com.epam.crmgymboot.model.TrainingType;
import com.epam.crmgymboot.repository.TraineeRepository;
import com.epam.crmgymboot.repository.TrainerRepository;
import com.epam.crmgymboot.repository.TrainingRepository;
import com.epam.crmgymboot.repository.TrainingTypeRepository;
import com.epam.crmgymboot.service.TrainingService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
@Transactional
@AllArgsConstructor
public class TrainingServiceImpl implements TrainingService {

    private final TrainingRepository trainingRepository;
    private final TrainerRepository trainerRepository;
    private final TraineeRepository traineeRepository;
    private final TrainingTypeRepository trainingTypeRepository;

    @Override
    public TrainingDTO createTraining(AddTrainingRequest request) throws EntityNotFoundException {
        Training training = new Training();

        traineeRepository.findTraineeByUserEntityUsername(request.getTraineeUsername()).ifPresentOrElse(
                training::setTrainee, () -> {
                    throw new EntityNotFoundException("Trainee not found");
                }
        );

        trainerRepository.findTrainerByUserEntityUsername(request.getTrainerUsername())
                .ifPresentOrElse(training::setTrainer, () -> {
                    throw new EntityNotFoundException("Trainer not found");
                });

        training.setTrainingName(request.getTrainingName());
        training.setTrainingDate(request.getTrainingDate());
        training.setDurationInMinutes(request.getDurationInMinutes());

        trainingRepository.save(training);

        return TrainingMapper.toDTO(training);
    }

    @Override
    public TrainingDTO updateTraining(Long id, TrainingDTO dto) throws EntityNotFoundException {
        Training training = trainingRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Training not found")
        );
        training.setDurationInMinutes(dto.getDurationInMinutes());
        training.setTrainingName(dto.getTrainingName());
        training.setTrainingDate(dto.getTrainingDate());
        trainingRepository.save(training);
        return dto;
    }

    @Override
    public Page<TrainingDTO> getTrainingsOfTrainer(
            String trainerUsername,
            String traineeUsername,
            LocalDateTime periodFrom,
            LocalDateTime periodTo,
            Pageable pageable
    ) {
        Page<Training> trainings = trainingRepository.findTrainingsOfTrainer(
                trainerUsername,
                periodFrom,
                periodTo,
                traineeUsername,
                pageable
        );
        return trainings.map(training -> new TrainingDTO(
                training.getTrainingName(),
                TrainerMapper.toUserDTO(training.getTrainer()),
                TraineeMapper.toUserDTO(training.getTrainee()),
                training.getDurationInMinutes(),
                training.getTrainingDate()
        ));
    }

    @Override
    public Page<TrainingDTO> getTrainingsOfTrainee(
            String traineeUsername,
            String trainerUsername,
            LocalDateTime periodFrom,
            LocalDateTime periodTo,
            Long trainingTypeId,
            Pageable pageable
    ) throws EntityNotFoundException {
        TrainingType trainingTypeEntity = null;

        if(!traineeRepository.existsTraineeByUserEntityUsername(traineeUsername)) {
            throw new EntityNotFoundException("Trainee not found");
        }

        if(trainingTypeId != null) {
            trainingTypeEntity = trainingTypeRepository.findById(trainingTypeId).orElseThrow(
                    () -> new EntityNotFoundException("Training type is not found")
            );
        }

        TrainingFilter trainingFilter = new TrainingFilter(
                traineeUsername, trainerUsername, periodFrom, periodTo, trainingTypeId
        );

        Page<Training> trainings = trainingRepository.findAll(
                TrainingSpec.filterBy(trainingFilter),
                pageable
        );

        return trainings.map(training -> new TrainingDTO(
                training.getTrainingName(),
                TrainerMapper.toUserDTO(training.getTrainer()),
                TraineeMapper.toUserDTO(training.getTrainee()),
                training.getDurationInMinutes(),
                training.getTrainingDate()
        ));
    }
}
