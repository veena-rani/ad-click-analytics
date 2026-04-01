package org.example.clickservice.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RedisDedupService {

    private final StringRedisTemplate redis;

    public RedisDedupService(StringRedisTemplate redis) {
        this.redis = redis;
    }

    public boolean alreadySeen(String impressionId) {
        Boolean exists = redis.hasKey("imp:" + impressionId);
        return Boolean.TRUE.equals(exists);
    }

    public void markSeen(String impressionId, long ttlSeconds) {
        redis.opsForValue().set("imp:" + impressionId, "1", Duration.ofSeconds(ttlSeconds));
    }
}