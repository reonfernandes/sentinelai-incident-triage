package com.reon.sentinelcoreservice.service;

import com.reon.sentinelcoreservice.dto.HashInput;

public interface HashService {
    String generateEventHash(HashInput hashInput);
}
