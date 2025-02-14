package com.example.univeus.domain.auth.repository;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SmsCertificationDao {
    private static final String PREFIX = "phone:";
    private static final Integer TTL = 5 * 60;

    private final StringRedisTemplate redisTemplate;

    public void save(String phone, String code) {
        redisTemplate.opsForValue()
                .set(PREFIX + phone, code, Duration.ofSeconds(TTL));
    }

    public String getByKey(String phone) {
        return redisTemplate.opsForValue().get(PREFIX + phone);
    }

    public void deleteByKey(String phone) {
        redisTemplate.delete(PREFIX + phone);
    }
}
