package com.reon.sentinelcoreservice.consumer;

import com.reon.dto.AiDiagnosis;
import com.reon.dto.RawLogEvent;
import com.reon.sentinelcoreservice.mapper.HashMapper;
import com.reon.sentinelcoreservice.service.AiTriageService;
import com.reon.sentinelcoreservice.service.CacheService;
import com.reon.sentinelcoreservice.service.HashService;
import com.reon.sentinelcoreservice.service.IncidentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class IngestConsumer {

    private final HashService hashService;
    private final CacheService cacheService;
    private final AiTriageService aiTriageService;
    private final IncidentService incidentService;
    private final HashMapper hashMapper;

    public IngestConsumer(HashService hashService, CacheService cacheService, AiTriageService aiTriageService,
                          IncidentService incidentService,
                          HashMapper hashMapper) {
        this.hashService = hashService;
        this.cacheService = cacheService;
        this.aiTriageService = aiTriageService;
        this.incidentService = incidentService;
        this.hashMapper = hashMapper;
    }

    @KafkaListener(
            topics = "${app.kafka.topic.ingest}",
            groupId = "sentinel-core-group"
    )
    public void consumeRawEvents(RawLogEvent event) {
        log.info("Consumed raw event: {}", event);
        String hash = hashService.generateEventHash(hashMapper.mapRawEventToHashInput(event));

        if (cacheService.markIfNew(hash)) {
            try {
                AiDiagnosis diagnose = aiTriageService.diagnose(event);
                log.info("AI diagnosis for {}: {}", hash, diagnose);
                incidentService.saveNewIncident(event, diagnose, hash);
            } catch (Exception exception) {
                log.error("AI diagnosis failed for {}, releasing fingerprint", hash, exception);
                cacheService.evictCache(hash);
            }
        } else {
            log.info("Redis cache hit {}, skipping", hash);
        }
    }
}
