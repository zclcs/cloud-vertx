package com.zclcs.common.local.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.vertx.core.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * @author zclcs
 */
public class LocalCache<K, V> {

    private final Logger log = LoggerFactory.getLogger(LocalCache.class);

    private int maximumSize = 5000;

    private int initialCapacity = 100;

    private Duration duration = Duration.ofSeconds(30);

    public LocalCache() {
        this.localCache = buildCache(maximumSize, initialCapacity, duration);
    }

    public LocalCache(int maximumSize, int initialCapacity, Duration duration) {
        this.maximumSize = maximumSize;
        this.initialCapacity = initialCapacity;
        this.duration = duration;
        this.localCache = buildCache(this.maximumSize, this.initialCapacity, this.duration);
    }

    private final Cache<K, V> localCache;

    private <K1, V1> Cache<K1, V1> buildCache(int maximumSize, int initialCapacity, Duration duration) {
        final Caffeine<Object, Object> builder = Caffeine.newBuilder();
        builder
                .maximumSize(maximumSize)
                .initialCapacity(initialCapacity)
                .expireAfterWrite(duration);
        return builder.build();
    }

    public Future<V> getIfPresent(K key) {
        log.info("getIfPresent: " + key);
        return Future.succeededFuture(localCache.getIfPresent(key));
    }

    public Future<Void> invalidate(K key) {
        localCache.invalidate(key);
        return Future.succeededFuture();
    }

    public Future<Void> put(K key, V value) {
        log.info("put: " + key);
        localCache.put(key, value);
        return Future.succeededFuture();
    }
}
