package com.reon.sentinelcoreservice.mapper;

import com.reon.dto.AiDiagnosis;
import com.reon.dto.RawLogEvent;
import com.reon.sentinelcoreservice.dto.response.IncidentResponse;
import com.reon.sentinelcoreservice.entity.Incident;
import org.springframework.stereotype.Component;

@Component
public class IncidentMapper {
    public Incident mapToEntity(RawLogEvent logEvent, AiDiagnosis diagnosis, String fingerprint) {
        return Incident.builder()
                .tenantId(logEvent.tenantId())
                .service(logEvent.service())
                .fingerprint(fingerprint)
                .exceptionType(logEvent.exceptionType())
                .stackTrace(logEvent.stackTrace())
                .message(logEvent.message())
                .severity(diagnosis.severity())
                .probableCause(diagnosis.probableCause())
                .suggestedFix(diagnosis.suggestedFix())
                .build();
    }

    public IncidentResponse responseToUser(Incident incident) {
        return IncidentResponse.builder()
                .tenantId(incident.getTenantId())
                .service(incident.getService())
                .occurredAt(incident.getFirstSeen())
                .build();
    }
}
