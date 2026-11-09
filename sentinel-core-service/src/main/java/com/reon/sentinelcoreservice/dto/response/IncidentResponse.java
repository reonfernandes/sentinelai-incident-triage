package com.reon.sentinelcoreservice.dto.response;

import lombok.Builder;

import java.time.Instant;
@Builder
public record IncidentResponse(
        String tenantId,
        String service,
        Instant occurredAt
) {
}
