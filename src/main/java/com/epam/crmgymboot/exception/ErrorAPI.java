package com.epam.crmgymboot.exception;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ErrorAPI {
    private int statusCode;
    private LocalDateTime timestamp;
    private String message;
    private String description;
}

