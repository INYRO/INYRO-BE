package com.inyro.api.global.infra.redis;

import com.inyro.api.domain.common.port.AuthVerificationPort;
import com.inyro.api.global.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisAuthVerificationAdapter implements AuthVerificationPort {

    private final RedisUtils<String, String> redisUtils;

    @Override
    public void saveVerification(String sno, String value, long ttlSeconds) {
        redisUtils.save(sno, value, ttlSeconds, TimeUnit.SECONDS);
    }

    @Override
    public boolean isVerificationExists(String sno) {
        return redisUtils.hasKey(sno);
    }

    @Override
    public void deleteVerification(String sno) {
        redisUtils.delete(sno);
    }
}
