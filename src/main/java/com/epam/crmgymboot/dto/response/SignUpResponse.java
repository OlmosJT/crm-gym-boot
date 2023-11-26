package com.epam.crmgymboot.dto.response;

import jakarta.validation.constraints.NotEmpty;

public record SignUpResponse(
        @NotEmpty
        String username,
        @NotEmpty
        String password
) {
}
