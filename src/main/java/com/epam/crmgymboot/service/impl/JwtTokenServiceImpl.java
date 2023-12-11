package com.epam.crmgymboot.service.impl;

import com.epam.crmgymboot.repository.RefreshTokenRepository;
import com.epam.crmgymboot.repository.UserRepository;
import com.epam.crmgymboot.security.JwtProperties;
import com.epam.crmgymboot.service.JwtTokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class JwtTokenServiceImpl implements JwtTokenService {

    private final JwtProperties jwtProperties;

    private SecretKey secretKey() {
        return Keys.hmacShaKeyFor(jwtProperties.key().getBytes());
    }


    @Override
    public String generateJwtToken(UserDetails userDetails, Date expirationDate, Map<String, ?> additionalClaims) {
        return Jwts.builder()
                .claims()
                .subject(userDetails.getUsername())
                .issuedAt(Date.from(Instant.now()))
                .expiration(expirationDate)
                .add(additionalClaims)
                .and()
                .signWith(secretKey())
                .compact();
    }

    @Override
    public boolean isValidJwtToken(String token, UserDetails userDetails) {
        var username = extractUsername(token);
        return Objects.equals(userDetails.getUsername(), username) && !hasExpiredJwtToken(token);
    }

    @Override
    public String extractUsername(String token) {
        return getAllClaims(token).getSubject();
    }

    @Override
    public boolean hasExpiredJwtToken(String token) {
        return getAllClaims(token)
                .getExpiration()
                .before(new Date(System.currentTimeMillis()));
    }

    private Claims getAllClaims(String token) throws ExpiredJwtException {
        return Jwts.parser()
                .verifyWith(secretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
