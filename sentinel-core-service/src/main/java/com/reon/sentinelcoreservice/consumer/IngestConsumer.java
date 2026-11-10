package com.reon.sentinelcoreservice.consumer;

import com.reon.dto.AiDiagnosis;
import com.reon.dto.RawLogEvent;
import com.reon.sentinelcoreservice.mapper.HashMapper;
import com.reon.sentinelcoreservice.service.AiTriageService;
import com.reon.sentinelcoreservice.service.CacheService;
import com.reon.sentinelcoreservice.service.HashService;
import com.reon.sentinelcoreservice.service.IncidentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
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

        if (!cacheService.markIfNew(hash)) {
            incidentService.recordRepeat(event.tenantId(), hash);
            log.info("Redis cache hit: {}, repeat counted", hash);
            return;
        }

        try {
            if (incidentService.recordRepeat(event.tenantId(), hash)) {
                log.info("Known incident {}, repeat counted, AI skipped", hash);
                return;
            }

            AiDiagnosis diagnosis = aiTriageService.diagnose(event);
            incidentService.saveNewIncident(event, diagnosis, hash);
            log.info("New incident {} saved: {}", hash, diagnosis);

        } catch (DataIntegrityViolationException race) {
            incidentService.recordRepeat(event.tenantId(), hash);
            log.info("Incident {} was saved by another thread, repeat counted", hash);

        } catch (Exception exception) {
            log.error("Processing failed for {}, releasing fingerprint", hash, exception);
            cacheService.evictCache(hash);
        }
    }
}
