package com.epam.crmgymboot.dto.request;

import jakarta.validation.constraints.NotNull;

public record EnableDisableUserRequest(
        @NotNull
        Boolean isActive
) {
}
