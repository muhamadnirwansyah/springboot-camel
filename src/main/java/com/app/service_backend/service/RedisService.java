package com.app.service_backend.service;

import com.app.service_backend.constant.RedisConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisService<T> {

    private final RedisTemplate<String,Object> redisTemplate;

    public void set(String key, Object value){
        log.info("save key : {} - value : {}",key,value);
        redisTemplate.opsForValue().set(key, value, Duration.ofHours(RedisConstant.TTL_CACHE));
        log.info("save key - value {} - {} successfully !", key, value);
    }

    @SuppressWarnings("unchecked")
    public List<T> cacheData(String key){
        log.info("getting cache for key : {}", key);
        Object objectCache = redisTemplate.opsForValue().get(key);
        if (!Objects.isNull(objectCache) && objectCache instanceof List<?>) {
            try{
                return (List<T>) objectCache;
            }catch (ClassCastException e){
                log.error("Failed to Cast cache object to List !");
            }
        }
        log.warn("No cache found for key : {}",key);
        return null;
    }
}
