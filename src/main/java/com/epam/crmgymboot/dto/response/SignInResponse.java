package com.epam.crmgymboot.dto.response;

import lombok.*;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SignInResponse {
    private String username;
    private List<String> roles;
    private String accessToken;
    private String refreshToken;
}
