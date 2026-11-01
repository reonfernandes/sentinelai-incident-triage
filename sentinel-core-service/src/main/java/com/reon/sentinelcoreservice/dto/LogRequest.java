package com.reon.sentinelcoreservice.dto;

import jakarta.validation.constraints.NotBlank;

public record LogRequest(
        @NotBlank(message = "service name cannot be blank")
        String service,

        @NotBlank(message = "mention the exception type")
        String exceptionType,
        String stackTrace,
        String message
) {
}
