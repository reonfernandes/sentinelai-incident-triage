package com.reon.sentinelcoreservice.consumer;

import com.reon.dto.RawLogEvent;
import com.reon.sentinelcoreservice.mapper.HashMapper;
import com.reon.sentinelcoreservice.service.HashService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class IngestConsumer {

    private final HashService hashService;
    private final HashMapper hashMapper;

    public IngestConsumer(HashService hashService, HashMapper hashMapper) {
        this.hashService = hashService;
        this.hashMapper = hashMapper;
    }

    @KafkaListener(
            topics = "${app.kafka.topic.ingest}",
            groupId = "sentinel-core-group"
    )
    public void consumeRawEvents(RawLogEvent event) {
        log.info("Consumed raw event: {}", event);
        hashService.generateEventHash(hashMapper.mapRawEventToHashInput(event));
    }
}
