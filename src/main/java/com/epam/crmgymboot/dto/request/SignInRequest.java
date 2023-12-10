package com.epam.crmgymboot.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SignInRequest {
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
}
