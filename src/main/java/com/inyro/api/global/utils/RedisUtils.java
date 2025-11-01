package com.inyro.api.global.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisUtils<K, V> {

    private final RedisTemplate<K, V> redisTemplate;

    public void save(K key, V val, Long time, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, val, time, timeUnit);
    }

    public boolean hasKey(K key) {
        return Objects.equals(Boolean.TRUE, redisTemplate.hasKey(key));
    }

    public V get(K key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void delete(K key) {
        redisTemplate.delete(key);
    }

    public boolean lock(K key, V val, Long time, TimeUnit timeUnit) {
        return Objects.equals(Boolean.TRUE, redisTemplate.opsForValue().setIfAbsent(key, val, time, timeUnit));
    }
}
