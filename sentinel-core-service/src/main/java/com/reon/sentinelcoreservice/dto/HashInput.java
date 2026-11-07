package com.reon.sentinelcoreservice.dto;

import lombok.Builder;

@Builder
public record HashInput(
        String tenantId,
        String service,
        String exceptionType,
        String stackTrace
) {
}
