package com.reon.sentinelcoreservice.consumer;

import com.reon.dto.RawLogEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class IngestConsumer {
    @KafkaListener(topics = "${app.kafka.topic.ingest}", groupId = "sentinel-core-group")
    public void consumeRawEvents(RawLogEvent event) {
        log.info("Consumed raw event: {}", event);
    }
}
