package com.reon.dto;

import java.time.Instant;

public record RawLogEvent(
        String tenantId,
        String service,
        String exceptionType,
        String stackTrace,
        String message,
        Instant occurredAt
) {}
