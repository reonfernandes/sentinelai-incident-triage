package com.reon.sentinelcoreservice.mapper;

import com.reon.dto.RawLogEvent;
import com.reon.sentinelcoreservice.dto.LogRequest;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class LogMapper {
    public RawLogEvent mapToRawLogEvent(String tenantId, LogRequest logRequest) {
        RawLogEvent log = new RawLogEvent(
                tenantId,
                logRequest.service(),
                logRequest.exceptionType(),
                logRequest.stackTrace(),
                logRequest.message(),
                Instant.now()
        );
        return log;
    }
}
