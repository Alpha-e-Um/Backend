package com.example.eumserver.global.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisUtil {
    private final RedisTemplate<String, Object> redisTemplate;

    public Boolean lock(String key) {
        return redisTemplate
                .opsForValue()
                .setIfAbsent(key, "lock", Duration.ofMillis(3000L));
    }

    public Boolean unlock(String key) {
        return redisTemplate.delete(key);
    }

    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    public void increaseValue(String key) {
        redisTemplate.opsForValue().increment(key, 1);
    }

    public void setValue(String key, Object data) {
        redisTemplate.opsForValue().set(key, data);
    }

    public void setValue(String key, Object data, Long time, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, data, time, timeUnit);
    }

    public void setValuesList(String key, String data) {
        redisTemplate.opsForList().rightPushAll(key, data);
    }

    public void createValueSet(String key) {
        redisTemplate.opsForSet().add(key);
    }

    public void createValueSet(String key, Long time, TimeUnit timeUnit) {
        redisTemplate.opsForSet().add(key);
        redisTemplate.expire(key, time, timeUnit);
    }

    public void addValuesSet(String key, String data) {
        redisTemplate.opsForSet().add(key, data);
    }

    public Object getValues(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public List<Object> getValuesList(String key) {
        List<Object> list = redisTemplate.opsForList().range(key, 0, -1);
        return list == null ? new ArrayList<>() : list;
    }

    public Set<Object> getValuesSet(String key) {
        Set<Object> set = redisTemplate.opsForSet().members(key);
        return set == null ? new HashSet<>() : set;
    }

    public boolean checkValuesList(String key, String data) {
        List<Object> list = getValuesList(key);
        return list.contains(data);
    }

    public boolean checkValuesSet(String key, String data) {
        return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(key, data));
    }

    public void deleteValues(String key) {
        redisTemplate.delete(key);
    }
}
