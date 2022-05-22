package com.skye.cache.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.skye.cache.config.DoubleCacheConfig;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * <p></p>
 * <p>
 * <PRE>
 * <BR>    修改记录
 * <BR>-----------------------------------------------
 * <BR>    修改日期         修改人          修改内容
 * </PRE>
 *
 * @author skye
 * @version 1.0
 * @date Created in 2022年04月24日 14:35
 * @since 1.0
 */
public class DoubleCacheManager implements CacheManager {
    Map<String, Cache> cacheMap = new ConcurrentHashMap<>();
    private RedisTemplate<Object, Object> redisTemplate;
    private DoubleCacheConfig dcConfig;

    public DoubleCacheManager(RedisTemplate<Object, Object> redisTemplate,
                              DoubleCacheConfig doubleCacheConfig) {
        this.redisTemplate = redisTemplate;
        this.dcConfig = doubleCacheConfig;
    }

    @Override
    public Cache getCache(String name) {
        Cache cache = cacheMap.get(name);
        if (Objects.nonNull(cache)) {
            return cache;
        }

        cache = new DoubleCache(name, redisTemplate, createCaffeineCache(), dcConfig);
        Cache oldCache = cacheMap.putIfAbsent(name, cache);
        return oldCache == null ? cache : oldCache;
    }

    @Override
    public Collection<String> getCacheNames() {
        return cacheMap.keySet();
    }

    private com.github.benmanes.caffeine.cache.Cache createCaffeineCache(){
        Caffeine<Object, Object> caffeineBuilder = Caffeine.newBuilder();
        Optional<DoubleCacheConfig> dcConfigOpt = Optional.ofNullable(this.dcConfig);
        dcConfigOpt.map(DoubleCacheConfig::getInit)
                .ifPresent(init->caffeineBuilder.initialCapacity(init));
        dcConfigOpt.map(DoubleCacheConfig::getMax)
                .ifPresent(max->caffeineBuilder.maximumSize(max));
        dcConfigOpt.map(DoubleCacheConfig::getExpireAfterWrite)
                .ifPresent(eaw->caffeineBuilder.expireAfterWrite(eaw,TimeUnit.SECONDS));
        dcConfigOpt.map(DoubleCacheConfig::getExpireAfterAccess)
                .ifPresent(eaa->caffeineBuilder.expireAfterAccess(eaa,TimeUnit.SECONDS));
        dcConfigOpt.map(DoubleCacheConfig::getRefreshAfterWrite)
                .ifPresent(raw->caffeineBuilder.refreshAfterWrite(raw,TimeUnit.SECONDS));
        return caffeineBuilder.build();
    }
}
