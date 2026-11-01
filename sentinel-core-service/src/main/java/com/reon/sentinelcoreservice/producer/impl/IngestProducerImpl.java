package com.reon.sentinelcoreservice.producer.impl;

import com.reon.dto.RawLogEvent;
import com.reon.sentinelcoreservice.producer.IngestProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
public class IngestProducerImpl implements IngestProducer {

    private final String ingestTopic;
    private final KafkaTemplate<String, RawLogEvent> kafkaTemplate;

    public IngestProducerImpl(
            @Value("${app.kafka.topic.ingest}") String ingestTopic,
            KafkaTemplate<String, RawLogEvent> kafkaTemplate
    ) {
        this.ingestTopic = ingestTopic;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void ingest(RawLogEvent logEvent) {
        Objects.requireNonNull(logEvent, "logEvent must not be null");
        log.info("Received log event for service: {}", logEvent.service());

        // topic - key - payload
        kafkaTemplate.send(ingestTopic, logEvent.tenantId(), logEvent)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to send log from {}", logEvent.service(), ex);
                    } else {
                        log.info("Sent log from {} to partition {}",
                                logEvent.service(), result.getRecordMetadata().partition());
                    }
                });
    }
}
