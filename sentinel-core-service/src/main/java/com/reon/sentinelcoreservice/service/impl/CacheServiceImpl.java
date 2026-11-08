package com.reon.sentinelcoreservice.service.impl;

import com.reon.sentinelcoreservice.service.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@Slf4j
public class CacheServiceImpl implements CacheService {
    
    private final StringRedisTemplate redisTemplate;
    private final Duration HASHTTL;
    private static final String KEY_PREFIX = "fingerprint:";
    
    public CacheServiceImpl(StringRedisTemplate redisTemplate, 
                            @Value("${app.redis.hash-ttl}") Duration hashttl
    ) {
        this.redisTemplate = redisTemplate;
        HASHTTL = hashttl;
    }

    private String keyFor(String hash) {
        return KEY_PREFIX + hash;
    }

    @Override
    public boolean markIfNew(String hash) {
        if (hash == null) {
            return false;
        }
        Boolean isNew = redisTemplate.opsForValue()
                .setIfAbsent(keyFor(hash), "1", HASHTTL);
        return Boolean.TRUE.equals(isNew);
    }

    @Override
    public String getCachedKey(String hash) {
        return redisTemplate.opsForValue().get(keyFor(hash));
    }
}
