package com.reon.sentinelcoreservice.consumer;

import com.reon.dto.RawLogEvent;
import com.reon.sentinelcoreservice.mapper.HashMapper;
import com.reon.sentinelcoreservice.service.CacheService;
import com.reon.sentinelcoreservice.service.HashService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class IngestConsumer {

    private final HashService hashService;
    private final CacheService cacheService;
    private final HashMapper hashMapper;

    public IngestConsumer(HashService hashService, CacheService cacheService, HashMapper hashMapper) {
        this.hashService = hashService;
        this.cacheService = cacheService;
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
            log.info("New error {}, send to AI later", hash);
        } else {
            log.info("Redis cache hit {}, skipping", hash);
        }
    }
}
