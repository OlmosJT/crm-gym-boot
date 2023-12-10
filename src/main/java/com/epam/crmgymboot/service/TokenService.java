package com.epam.crmgymboot.service;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.Map;

public interface TokenService {

    String generateJwtToken(
            UserDetails userDetails,
            Date expirationDate,
            Map<String, ?> additionalClaims
    );

    boolean isValidJwtToken(String token, UserDetails userDetails);
    boolean hasExpiredJwtToken(String token);

    String extractUsername(String jwtToken);
}
