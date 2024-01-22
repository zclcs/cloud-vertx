package com.zclcs.common.redis.starter.rate.limit;

import io.vertx.core.Future;

import java.util.concurrent.TimeUnit;

/**
 * RedisRateLimiter 限流 Client
 *
 * @author zclcs
 */
public interface RateLimiterClient {

    /**
     * 服务是否被限流
     *
     * @param key      自定义的key，请保证唯一
     * @param max      支持的最大请求
     * @param ttl      时间
     * @param timeUnit 时间单位
     * @return 是否允许
     */
    Future<Boolean> isAllowed(String key, long max, long ttl, TimeUnit timeUnit);

}
