package com.epam.crmgymboot.repository;

import com.epam.crmgymboot.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByUserUsername(String username);

    @Modifying
    void deleteByUserUsername(String username);

}
