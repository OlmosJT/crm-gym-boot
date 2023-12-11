package com.epam.crmgymboot.service;

import com.epam.crmgymboot.dto.common.TokenDto;
import com.epam.crmgymboot.dto.response.SignInResponse;

public interface AuthService {
    SignInResponse authenticateUser(String username, String password);
    TokenDto refreshAccessAndRefreshTokens(String refreshToken);

    void logOutUser(String username);
}
