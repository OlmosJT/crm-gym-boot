package com.epam.crmgymboot;

import com.epam.crmgymboot.model.Role;
import com.epam.crmgymboot.model.RoleEnum;
import com.epam.crmgymboot.repository.RoleRepository;
import com.epam.crmgymboot.security.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@RequiredArgsConstructor
public class CrmGymBootApplication implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public static void main(String[] args) {
        SpringApplication.run(CrmGymBootApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        for(RoleEnum roleEnum : RoleEnum.values()) {
           if(!roleRepository.existsByName(roleEnum)) {
               Role role = new Role();
               role.setName(roleEnum);
               roleRepository.save(role);
           }
        }
    }
}
