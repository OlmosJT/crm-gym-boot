package com.epam.crmgymboot.dto.common;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public final class UserDTO {
    private @NotEmpty String firstname;
    private @NotEmpty String lastname;
    private @NotEmpty String username;
}
