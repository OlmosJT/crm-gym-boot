package com.epam.crmgymboot.repository;

import com.epam.crmgymboot.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findUserEntityByUsername(String username);
    boolean existsUserEntityByUsername(String username);
    void deleteUserEntityByUsername(String username);
}
