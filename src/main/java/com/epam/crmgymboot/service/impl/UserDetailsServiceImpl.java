package com.epam.crmgymboot.service.impl;

import com.epam.crmgymboot.model.UserEntity;
import com.epam.crmgymboot.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findUserEntityByUsername(username)
                .orElseThrow(() ->  new UsernameNotFoundException("User Not Found via username " + username));

        if (userEntity.getLockTime() != null && userEntity.getLockTime().isAfter(LocalDateTime.now())) {
            throw new LockedException("User account is locked! Please retry after 5 minutes.");
        }

        if (userEntity.getLockTime() != null && userEntity.getLockTime().isBefore(LocalDateTime.now())) {
            userEntity.setLockTime(null);
            userEntity.setFailedAttempt(0);
            userRepository.save(userEntity);
        }

        return User.builder()
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .authorities(userEntity.getRoles().stream().map(role -> role.getName().name()).toArray(String[]::new))
                .build();
    }
}
