package com.reon.sentinelcoreservice.service;

import com.reon.dto.AiDiagnosis;
import com.reon.dto.RawLogEvent;
import com.reon.sentinelcoreservice.dto.response.IncidentResponse;

public interface IncidentService {
    IncidentResponse saveNewIncident(RawLogEvent logEvent, AiDiagnosis diagnosis, String fingerprint);
    boolean recordRepeat(String tenantId, String fingerprint);
}
