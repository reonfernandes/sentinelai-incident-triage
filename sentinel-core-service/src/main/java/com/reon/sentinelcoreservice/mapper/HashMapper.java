package com.reon.sentinelcoreservice.mapper;

import com.reon.dto.RawLogEvent;
import com.reon.sentinelcoreservice.dto.HashInput;
import org.springframework.stereotype.Component;

@Component
public class HashMapper {
    public HashInput mapRawEventToHashInput(RawLogEvent rawLogEvent) {
        return HashInput.builder()
                .tenantId(rawLogEvent.tenantId())
                .service(rawLogEvent.service())
                .exceptionType(rawLogEvent.exceptionType())
                .stackTrace(rawLogEvent.stackTrace())
                .build();
    }
}
