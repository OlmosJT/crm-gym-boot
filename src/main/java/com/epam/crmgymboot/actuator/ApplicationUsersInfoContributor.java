package com.epam.crmgymboot.actuator;

import com.epam.crmgymboot.repository.TraineeRepository;
import com.epam.crmgymboot.repository.TrainerRepository;
import com.epam.crmgymboot.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@AllArgsConstructor
public class ApplicationUsersInfoContributor implements InfoContributor {

    private final UserRepository userRepository;
    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;

    @Override
    public void contribute(Info.Builder builder) {
        long totalUserCount = userRepository.count();
        long totalTraineeCount = traineeRepository.count();
        long totalTrainerCount = trainerRepository.count();
        Map<String, Object> gymMap = new HashMap<>();
        gymMap.put("total users", totalUserCount);
        gymMap.put("total trainees", totalTraineeCount);
        gymMap.put("total trainers", totalTrainerCount);
        builder.withDetail("gym-stats", gymMap);
    }
}
