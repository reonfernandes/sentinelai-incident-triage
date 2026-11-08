package com.reon.sentinelcoreservice.service;

import com.reon.dto.AiDiagnosis;
import com.reon.dto.RawLogEvent;

public interface AiTriageService {
    AiDiagnosis diagnose(RawLogEvent event);
}