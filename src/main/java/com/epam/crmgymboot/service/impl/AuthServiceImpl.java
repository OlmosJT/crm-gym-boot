package com.epam.crmgymboot.service.impl;

import com.epam.crmgymboot.dto.common.TokenDto;
import com.epam.crmgymboot.dto.response.SignInResponse;
import com.epam.crmgymboot.exception.UserLockedException;
import com.epam.crmgymboot.model.RefreshToken;
import com.epam.crmgymboot.repository.RefreshTokenRepository;
import com.epam.crmgymboot.repository.UserRepository;
import com.epam.crmgymboot.security.JwtProperties;
import com.epam.crmgymboot.security.UserDetailsAdapter;
import com.epam.crmgymboot.service.AuthService;
import com.epam.crmgymboot.service.JwtTokenService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private static final int MAX_ATTEMPT = 3;
    private final AuthenticationManager authManager;
    private final UserDetailsService userDetailsService;
    private final JwtTokenService jwtTokenService;
    private final JwtProperties jwtProperties;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    @Transactional(dontRollbackOn = {BadCredentialsException.class, UserLockedException.class})
    public SignInResponse authenticateUser(String username, String password) {
        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
            UserDetails user = userDetailsService.loadUserByUsername(username);
            String accessToken = createAccessToken(user);
            String refreshToken = createRefreshToken(user);

            // If authentication is successful, reset failedAttempt and lockTime
            userRepository.findUserEntityByUsername(user.getUsername())
                    .ifPresent(userEntity -> {
                        userEntity.setFailedAttempt(0);
                        userEntity.setLockTime(null);
                        userRepository.save(userEntity);
                    });

            return new SignInResponse(
                    user.getUsername(),
                    user.getAuthorities().stream().map(Object::toString).toList(),
                    accessToken,
                    refreshToken
            );
        } catch (BadCredentialsException exception) {
            userRepository.findUserEntityByUsername(username).ifPresent(userEntity -> {
                int newFailedAttempt = userEntity.getFailedAttempt() + 1;
                // if the number of attempts is bigger than max attempts block the user
                if(newFailedAttempt > MAX_ATTEMPT) {
                    userEntity.setLockTime(LocalDateTime.now().plusMinutes(5));
                    userEntity.setFailedAttempt(newFailedAttempt);
                } else {
                    userEntity.setFailedAttempt(newFailedAttempt);
                }
                userRepository.save(userEntity);
            });
            throw  new UserLockedException("Invalid Credentials. Please, try again");
        }
    }

    @Override
    @Transactional
    public TokenDto refreshAccessAndRefreshTokens(String refreshToken) {
        RefreshToken refreshTokenEntity = refreshTokenRepository
                .findByToken(refreshToken)
                .orElseThrow(
                        () -> new EntityNotFoundException("Invalid refresh token credentials.")
                );

        if(refreshTokenEntity.getExpiryDate().compareTo(Instant.now()) <= 0) {
            throw new BadCredentialsException("Refresh token has expired. Please sign in.");
        }

        UserDetails userDetails = new UserDetailsAdapter(refreshTokenEntity.getUser());
        String newAccessToken = createAccessToken(userDetails);
        String newRefreshToken = createRefreshToken(userDetails);

        return new TokenDto(newAccessToken, newRefreshToken);
    }

    @Override
    public void logOutUser(String username) {
        refreshTokenRepository.deleteByUserUsername(username);
    }


    @Transactional
    public String createRefreshToken(UserDetails userDetails) throws UsernameNotFoundException {
        var user = userRepository.findUserEntityByUsername(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found by username " + userDetails.getUsername()));

        Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository
                .findByUserUsername(user.getUsername());

        if(optionalRefreshToken.isPresent()) {
            // RefreshToken exist in database
            RefreshToken refreshToken = optionalRefreshToken.get();
            refreshToken.setToken(UUID.randomUUID().toString());
            refreshToken.setExpiryDate(Instant.now().plusMillis(jwtProperties.refreshTokenExpiration()));
            refreshTokenRepository.save(refreshToken);
            return refreshToken.getToken();
        } else {
            RefreshToken newToken = RefreshToken.builder()
                    .user(user)
                    .token(UUID.randomUUID().toString())
                    .expiryDate(Instant.now().plusMillis(jwtProperties.refreshTokenExpiration()))
                    .build();

            refreshTokenRepository.save(newToken);
            return newToken.getToken();
        }
    }

    private String createAccessToken(UserDetails userDetails) {
        return jwtTokenService.generateJwtToken(
                userDetails,
                getAccessTokenExpiration(),
                Collections.emptyMap()
        );
    }

    private Date getAccessTokenExpiration() {
        return new Date(System.currentTimeMillis() + jwtProperties.accessTokenExpiration());
    }
}
