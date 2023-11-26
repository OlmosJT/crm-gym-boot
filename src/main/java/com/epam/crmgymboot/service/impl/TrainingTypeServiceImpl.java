package com.epam.crmgymboot.service.impl;

import com.epam.crmgymboot.dto.common.TrainingTypeDTO;
import com.epam.crmgymboot.mapper.TrainingTypeMapper;
import com.epam.crmgymboot.model.TrainingType;
import com.epam.crmgymboot.repository.TrainingTypeRepository;
import com.epam.crmgymboot.service.TrainingTypeService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class TrainingTypeServiceImpl implements TrainingTypeService {

    private final TrainingTypeRepository trainingTypeRepository;

    @Override
    public List<TrainingTypeDTO> findAllTrainingTypes() {
        return trainingTypeRepository.findAll().stream().map(trainingType ->
                new TrainingTypeDTO(trainingType.getId(), trainingType.getName())
        ).toList();
    }

    @Override
    public TrainingTypeDTO addTrainingType(String name) throws EntityExistsException {
        trainingTypeRepository.findTrainingTypeByName(name).ifPresent(trainingType -> {
            throw new EntityExistsException("Training type name exist");
        });

        TrainingType trainingType = new TrainingType();
        trainingType.setName(name);
        trainingTypeRepository.save(trainingType);
        return new TrainingTypeDTO(trainingType.getId(),trainingType.getName());
    }

    @Override
    public TrainingTypeDTO updateTrainingType(TrainingTypeDTO dto) throws EntityExistsException {
        TrainingType trainingType = trainingTypeRepository.findById(dto.id()).orElseThrow(() -> new EntityNotFoundException("Training type not found by id"));
        if(!trainingType.getName().equals(dto.name()) && !trainingTypeRepository.existsByName(dto.name())) {
            trainingType.setName(dto.name());
            trainingTypeRepository.save(trainingType);
        }
        return TrainingTypeMapper.toDTO(trainingType);
    }

    @Override
    public void deleteTrainingType(Long id) {
        trainingTypeRepository.deleteById(id);
    }

    @Override
    public void deleteTrainingType(String name) {
        trainingTypeRepository.deleteByName(name);
    }
}
