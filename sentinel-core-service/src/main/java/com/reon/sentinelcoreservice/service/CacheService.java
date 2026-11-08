package com.reon.sentinelcoreservice.service;

public interface CacheService {
    boolean markIfNew(String hash);
    String getCachedKey(String hash);
    void evictCache(String hash);
}
