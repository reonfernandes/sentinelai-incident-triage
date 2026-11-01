package com.reon.sentinelcoreservice.producer;

import com.reon.dto.RawLogEvent;

public interface IngestProducer {
    void ingest(RawLogEvent logEvent);
}
