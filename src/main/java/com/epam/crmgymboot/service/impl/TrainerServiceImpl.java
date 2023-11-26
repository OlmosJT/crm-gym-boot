package com.epam.crmgymboot.service.impl;

import com.epam.crmgymboot.dto.common.TrainerDTO;
import com.epam.crmgymboot.dto.request.SignUpTrainerRequest;
import com.epam.crmgymboot.dto.request.UpdateTrainerRequest;
import com.epam.crmgymboot.dto.response.SignUpResponse;
import com.epam.crmgymboot.dto.response.TrainerResponse;
import com.epam.crmgymboot.mapper.TrainerMapper;
import com.epam.crmgymboot.model.Trainee;
import com.epam.crmgymboot.model.Trainer;
import com.epam.crmgymboot.model.TrainingType;
import com.epam.crmgymboot.model.UserEntity;
import com.epam.crmgymboot.repository.TraineeRepository;
import com.epam.crmgymboot.repository.TrainerRepository;
import com.epam.crmgymboot.repository.TrainingTypeRepository;
import com.epam.crmgymboot.service.TrainerService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static com.epam.crmgymboot.service.UserGeneratorUtils.passwordGenerator;
import static com.epam.crmgymboot.service.UserGeneratorUtils.usernameGenerator;

@Service
@Transactional
@AllArgsConstructor
public class TrainerServiceImpl implements TrainerService {

    private final TrainerRepository trainerRepository;
    private final TraineeRepository traineeRepository;
    private final TrainingTypeRepository trainingTypeRepository;

    @Override
    public SignUpResponse signUpTrainer(SignUpTrainerRequest request) throws EntityNotFoundException {
        UserEntity userEntity = new UserEntity();
        userEntity.setFirstname(request.getFirstname());
        userEntity.setLastname(request.getLastname());
        userEntity.setUsername(usernameGenerator(request.getFirstname(),
                request.getLastname(),
                trainerRepository::existsTrainerByUserEntityUsername)
        );
        userEntity.setPassword(passwordGenerator(10));
        userEntity.setDateOfBirth(request.getDateOfBirth());
        userEntity.setAddress(request.getAddress());
        userEntity.setIsActive(true);

        Trainer trainer = new Trainer();
        trainer.setUserEntity(userEntity);
        TrainingType trainingType = trainingTypeRepository.findById(request.getSpecializationId())
                .orElseThrow(() -> new EntityNotFoundException("Training type not found"));
        trainer.setSpecialization(trainingType);
        trainer.setTrainings(Collections.emptyList());

        trainerRepository.save(trainer);
        return new SignUpResponse(userEntity.getUsername(), userEntity.getPassword());
    }

    @Override
    public List<TrainerResponse> findActiveTrainersNotAssignedOnTrainee(String traineeUsername) {
        List<Trainer> trainers = trainerRepository
                .findActiveTrainersNotAssociatedWithTrainee(traineeUsername);
        return TrainerMapper.toTrainerResponse(trainers);
    }

    @Override
    public TrainerDTO getTrainerProfile(String username) throws EntityNotFoundException {
        Trainer trainer = trainerRepository.findTrainerByUserEntityUsername(username).orElseThrow(() ->
                new EntityNotFoundException("Trainer not found"));

        List<Trainee> assignedTrainees = traineeRepository.findDistinctByTrainingsTrainerId(trainer.getId());
        return TrainerMapper.toDTO(trainer, assignedTrainees);
    }

    @Override
    public TrainerDTO updateTrainerProfile(UpdateTrainerRequest request) throws EntityNotFoundException {
        Trainer trainer = trainerRepository.findTrainerByUserEntityUsername(request.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("Trainer not foudn"));
        trainer.getUserEntity().setFirstname(request.getFirstname());
        trainer.getUserEntity().setLastname(request.getLastname());
        trainer.getUserEntity().setAddress(request.getAddress());
        trainer.getUserEntity().setDateOfBirth(request.getDateOfBirth());

        TrainingType trainingType = trainingTypeRepository.findTrainingTypeByName(request.getSpecialization())
                .orElseThrow(() -> new EntityNotFoundException("Training type not found"));
        trainer.setSpecialization(trainingType);

        trainerRepository.save(trainer);
        List<Trainee> assignedTrainees = traineeRepository.findDistinctByTrainingsTrainerId(trainer.getId());
        return TrainerMapper.toDTO(trainer, assignedTrainees);
    }

    @Override
    public void deleteTrainee(String username) throws EntityNotFoundException {
          if(!trainerRepository.existsTrainerByUserEntityUsername(username)) {
              throw new EntityNotFoundException("Trainer not found");
          }
          trainerRepository.deleteTrainerByUserEntityUsername(username);
    }

    @Override
    public void activateDeactivateTrainer(String username, boolean isActive) {
        Trainer trainer = trainerRepository.findTrainerByUserEntityUsername(username).orElseThrow(
                () -> new EntityNotFoundException("Trainer not found")
        );
        trainer.getUserEntity().setIsActive(isActive);
        trainerRepository.save(trainer);
    }
}
