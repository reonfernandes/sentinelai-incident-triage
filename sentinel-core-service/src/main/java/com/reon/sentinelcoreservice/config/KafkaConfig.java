package com.reon.sentinelcoreservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    private final String ingestTopic;

    public KafkaConfig(@Value("${app.kafka.topic.ingest}") String ingestTopic) {
        this.ingestTopic = ingestTopic;
    }

    @Bean
    public NewTopic ingestTopic() {
        return TopicBuilder
                .name(ingestTopic)
                .partitions(3)
                .replicas(1)
                .build();
    }
}
