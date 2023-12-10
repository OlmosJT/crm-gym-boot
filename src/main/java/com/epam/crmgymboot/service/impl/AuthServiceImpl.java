package com.epam.crmgymboot.service.impl;

import com.epam.crmgymboot.dto.response.SignInResponse;
import com.epam.crmgymboot.repository.RefreshTokenRepository;
import com.epam.crmgymboot.security.JwtProperties;
import com.epam.crmgymboot.service.AuthService;
import com.epam.crmgymboot.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authManager;
    private final UserDetailsService userDetailsService;
    private final TokenService tokenService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProperties jwtProperties;

    @Override
    public SignInResponse authenticateUser(String username, String password) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        UserDetails user = userDetailsService.loadUserByUsername(username);

        String accessToken = createAccessToken(user);
        String refreshToken = createRefreshToken(user);

        refreshTokenRepository.save(refreshToken, user);

        return new SignInResponse(
                user.getUsername(),
                user.getAuthorities().stream().map(Object::toString).toList(),
                accessToken,
                refreshToken
        );
    }

    @Override
    public String refreshAccessToken(String refreshToken) {
        String extractedUsername = tokenService.extractUsername(refreshToken);
        if (extractedUsername == null) {
            return null;
        }
        UserDetails currentUserDetails = userDetailsService.loadUserByUsername(extractedUsername);
        UserDetails refreshTokenUserDetails = refreshTokenRepository.findUserDetailsByToken(refreshToken);
        if (!tokenService.hasExpiredJwtToken(refreshToken) && refreshTokenUserDetails != null && refreshTokenUserDetails.getUsername().equals(currentUserDetails.getUsername())) {
            return createAccessToken(currentUserDetails);
        } else {
            return null;
        }
    }


    private String createRefreshToken(UserDetails user) {
        return tokenService.generateJwtToken(user, getRefreshTokenExpiration(), Collections.emptyMap());
    }

    private String createAccessToken(UserDetails userDetails) {
        return tokenService.generateJwtToken(
                userDetails,
                getAccessTokenExpiration(),
                Collections.emptyMap()
        );
    }

    private Date getAccessTokenExpiration() {
        return new Date(System.currentTimeMillis() + jwtProperties.accessTokenExpiration());
    }

    private Date getRefreshTokenExpiration() {
        return new Date(System.currentTimeMillis() + jwtProperties.refreshTokenExpiration());
    }
}
