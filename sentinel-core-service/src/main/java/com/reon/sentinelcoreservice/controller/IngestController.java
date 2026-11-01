package com.reon.sentinelcoreservice.controller;

import com.reon.dto.RawLogEvent;
import com.reon.sentinelcoreservice.dto.LogRequest;
import com.reon.sentinelcoreservice.mapper.LogMapper;
import com.reon.sentinelcoreservice.producer.IngestProducer;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/logs")
@Slf4j
public class IngestController {

    private final IngestProducer ingestProducer;
    private final LogMapper logMapper;

    public IngestController(IngestProducer ingestProducer, LogMapper logMapper) {
        this.ingestProducer = ingestProducer;
        this.logMapper = logMapper;
    }

    @PostMapping("/ingest")
    public ResponseEntity<Map<String, String>> ingestLog(
            @Valid @RequestBody LogRequest logRequest,
            @RequestHeader(name = "X-Tenant-Id") String tenantId
            ) {
        log.info("Received log event for service: {} for tenant: {}", logRequest.service(), tenantId);
        RawLogEvent logEvent = logMapper.mapToRawLogEvent(tenantId, logRequest);
        log.info("Received log from {} for tenant {}", logEvent.service(), tenantId);
        ingestProducer.ingest(logEvent);

        return ResponseEntity
                .accepted()
                .body(Map.of(
                        "status", "accepted"
                ));
    }
}
