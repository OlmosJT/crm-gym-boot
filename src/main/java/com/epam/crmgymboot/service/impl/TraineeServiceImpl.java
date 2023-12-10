package com.epam.crmgymboot.service.impl;

import com.epam.crmgymboot.dto.common.TraineeDTO;
import com.epam.crmgymboot.dto.request.SignUpTraineeRequest;
import com.epam.crmgymboot.dto.request.UpdateTraineeRequest;
import com.epam.crmgymboot.dto.response.SignUpResponse;
import com.epam.crmgymboot.mapper.TraineeMapper;
import com.epam.crmgymboot.model.*;
import com.epam.crmgymboot.repository.RoleRepository;
import com.epam.crmgymboot.repository.TraineeRepository;
import com.epam.crmgymboot.repository.TrainerRepository;
import com.epam.crmgymboot.service.TraineeService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.epam.crmgymboot.service.UserGeneratorUtils.passwordGenerator;
import static com.epam.crmgymboot.service.UserGeneratorUtils.usernameGenerator;

@Service
@AllArgsConstructor
public class TraineeServiceImpl implements TraineeService {

    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public SignUpResponse signUpTrainee(SignUpTraineeRequest request) throws EntityExistsException {
        UserEntity userEntity = new UserEntity();
        userEntity.setFirstname(request.getFirstname());
        userEntity.setLastname(request.getLastname());
        userEntity.setUsername(usernameGenerator(request.getFirstname(),
                request.getLastname(),
                traineeRepository::existsTraineeByUserEntityUsername)
        );
        String generatedPassword = passwordGenerator(10);
        userEntity.setPassword(passwordEncoder.encode(generatedPassword));
        userEntity.setDateOfBirth(request.getDateOfBirth());
        userEntity.setAddress(request.getAddress());

        Role role_user = roleRepository.findByName(RoleEnum.ROLE_USER)
                .orElseThrow(() -> new EntityNotFoundException("Role USER not found in database"));
        Role role_trainee = roleRepository.findByName(RoleEnum.ROLE_TRAINEE)
                .orElseThrow(() -> new EntityNotFoundException("Role TRAINEE not found in database"));
        userEntity.setRoles(Set.of(role_user, role_trainee));
        userEntity.setIsActive(true);
        Trainee trainee = new Trainee();
        trainee.setUserEntity(userEntity);
        trainee.setTrainings(Collections.emptyList());

        traineeRepository.save(trainee);

        return new SignUpResponse(userEntity.getUsername(), generatedPassword);
    }

    @Override
    @Transactional
    public TraineeDTO getTraineeProfile(String username) throws EntityNotFoundException {
        Trainee trainee = traineeRepository.findTraineeByUserEntityUsername(username).orElseThrow(
                () -> new EntityNotFoundException("Trainee not found")
        );
        List<Trainer> assignedTrainers = trainerRepository
                .findDistinctByTrainingsTraineeId(trainee.getId());
        return TraineeMapper.toDTO(trainee, assignedTrainers);
    }

    @Override
    @Transactional
    public TraineeDTO updateTraineeProfile(String username, UpdateTraineeRequest request) throws EntityNotFoundException {
        Trainee trainee = traineeRepository.findTraineeByUserEntityUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Trainee not found"));
        trainee.getUserEntity().setFirstname(request.getFirstname());
        trainee.getUserEntity().setLastname(request.getLastname());
        trainee.getUserEntity().setAddress(request.getAddress());
        trainee.getUserEntity().setDateOfBirth(request.getDateOfBirth());

        trainee = traineeRepository.save(trainee);

        List<Trainer> assignedTrainers = trainerRepository
                .findDistinctByTrainingsTraineeId(trainee.getId());

        return TraineeMapper.toDTO(trainee, assignedTrainers);
    }

    @Override
    @Transactional
    public void deleteTrainee(String username) throws EntityNotFoundException {
        traineeRepository.findTraineeByUserEntityUsername(username).ifPresentOrElse(trainee -> {
            traineeRepository.deleteTraineeByUserEntityUsername(username);
        }, () -> {
            throw new EntityNotFoundException("Trainee not found");
        });
    }

    @Override
    @Transactional
    public void activateDeactivateTrainee(String username, boolean isActive) throws EntityNotFoundException {
        Trainee trainee = traineeRepository.findTraineeByUserEntityUsername(username).orElseThrow(
                () -> new EntityNotFoundException("Trainee not found")
        );
        trainee.getUserEntity().setIsActive(isActive);
        traineeRepository.save(trainee);
    }
}
