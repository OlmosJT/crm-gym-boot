package com.epam.crmgymboot.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("jwt")
public record JwtProperties(
    String key,
    Long accessTokenExpiration,
    Long refreshTokenExpiration
) {

}
