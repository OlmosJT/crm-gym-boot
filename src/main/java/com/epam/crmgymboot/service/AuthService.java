package com.epam.crmgymboot.service;

import com.epam.crmgymboot.dto.response.SignInResponse;

public interface AuthService {
    SignInResponse authenticateUser(String username, String password);
    String refreshAccessToken(String refreshToken);
}
