package com.epam.crmgymboot.repository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RefreshTokenRepository {
    private final Map<String, UserDetails> tokens = new ConcurrentHashMap<>();

    public UserDetails findUserDetailsByToken(String token) {
        return tokens.get(token);
    }

    public void save(String token, UserDetails userDetails) {
        tokens.put(token, userDetails);
    }

}
