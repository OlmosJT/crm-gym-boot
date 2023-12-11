package com.epam.crmgymboot.dto.common;

public record TokenDto(
        String accessToken,
        String refreshToken,
        String tokenType
) {
    public TokenDto(String accessToken, String refreshToken) {
        this(accessToken, refreshToken, "Bearer");
    }
}
