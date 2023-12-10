package com.epam.crmgymboot.repository;

import com.epam.crmgymboot.model.Role;
import com.epam.crmgymboot.model.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleEnum name);
    boolean existsByName(RoleEnum name);
}
