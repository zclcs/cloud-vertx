package com.zclcs.cloud.security;

import com.zclcs.cloud.core.bean.HttpBlackList;
import com.zclcs.cloud.core.bean.HttpRateLimitList;
import com.zclcs.cloud.core.bean.HttpWhiteList;
import com.zclcs.cloud.core.constant.CommonCore;
import com.zclcs.cloud.core.constant.RedisPrefix;
import com.zclcs.cloud.core.exception.SecurityException;
import com.zclcs.cloud.lib.web.utils.RoutingContextUtil;
import com.zclcs.common.core.constant.HttpStatus;
import com.zclcs.common.core.utils.StringsUtil;
import com.zclcs.common.redis.starter.rate.limit.RateLimiterClient;
import com.zclcs.common.security.provider.TokenProvider;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

/**
 * @author zhouc
 */
public class GlobalHandler implements Handler<RoutingContext> {

    private final Logger log = LoggerFactory.getLogger(GlobalHandler.class.getName());
    private final TokenProvider tokenProvider;
    private final RateLimiterClient rateLimiterClient;
    private final StintProvider stintProvider;

    public GlobalHandler(TokenProvider tokenProvider, RateLimiterClient rateLimiterClient, StintProvider stintProvider) {
        this.tokenProvider = tokenProvider;
        this.rateLimiterClient = rateLimiterClient;
        this.stintProvider = stintProvider;
    }

    @Override
    public void handle(RoutingContext ctx) {
        doFilter(ctx).onComplete(r -> {
            ctx.next();
        }, e -> {
            log.error("GlobalHandler {}", e.getMessage());
            if (e instanceof SecurityException securityException) {
                RoutingContextUtil.error(ctx, securityException.getHttpStatus(), securityException.getMsg());
            } else {
                RoutingContextUtil.error(ctx, "系统异常");
            }
        });
    }

    private Future<Void> doFilter(RoutingContext ctx) {
        HttpMethod method = ctx.request().method();
        String path = ctx.request().path();
        String ip = RoutingContextUtil.getHttpRequestIpAddress(ctx);
        return filterBlackList(ip, method, path,
                filterRateLimit(ip, method, path,
                        filterWhiteList(ip, method, path, checkToken(ctx))
                )
        );
    }

    private Future<Void> filterBlackList(String ip, HttpMethod method, String path, Future<Void> next) {
        return stintProvider.getBlackList()
                .flatMap(blackList -> {
                    if (blackList != null && !blackList.isEmpty()) {
                        for (HttpBlackList black : blackList) {
                            if (StringsUtil.isBlank(black.getIp()) || ip.equals(black.getIp())) {
                                if (CommonCore.HTTP_METHOD_ALL.equalsIgnoreCase(black.getMethod()) && CommonCore.HTTP_PATH_ALL.equalsIgnoreCase(black.getPath())) {
                                    return Future.succeededFuture(black);
                                } else if (method.name().equalsIgnoreCase(black.getMethod()) && path.equals(black.getPath())) {
                                    return Future.succeededFuture(black);
                                }
                            }
                        }
                    }
                    return Future.succeededFuture(null);
                })
                .compose(black -> {
                    if (black != null) {
                        LocalTime limitFrom = black.getLimitFrom();
                        LocalTime limitTo = black.getLimitTo();
                        if (limitFrom != null && limitTo != null) {
                            LocalTime now = LocalDateTime.now().toLocalTime();
                            if (!limitFrom.isAfter(now) && !limitTo.isBefore(now)) {
                                return Future.failedFuture(new SecurityException(HttpStatus.HTTP_FORBIDDEN, "限制访问"));
                            }
                        }
                        return Future.failedFuture(new SecurityException(HttpStatus.HTTP_FORBIDDEN, "限制访问"));
                    }
                    return next;
                });
    }

    private Future<Void> filterRateLimit(String ip, HttpMethod method, String path, Future<Void> next) {
        return stintProvider.getRateLimitList()
                .flatMap(rateLimitLists -> {
                    for (HttpRateLimitList rateLimit : rateLimitLists) {
                        if (method.name().equalsIgnoreCase(rateLimit.getMethod()) && path.equals(rateLimit.getPath())) {
                            return Future.succeededFuture(rateLimit);
                        }
                    }
                    return Future.succeededFuture(null);
                })
                .compose(rateLimit -> {
                    if (rateLimit != null) {
                        if (rateLimit.getLimitFrom() != null && rateLimit.getLimitTo() != null) {
                            LocalTime limitFrom = rateLimit.getLimitFrom();
                            LocalTime limitTo = rateLimit.getLimitTo();
                            LocalTime now = LocalDateTime.now().toLocalTime();
                            if (!limitFrom.isAfter(now) && !limitTo.isBefore(now)) {
                                return executeRateLimit(ip, rateLimit, next);
                            }
                        } else {
                            return executeRateLimit(ip, rateLimit, next);
                        }
                    }
                    return next;
                });
    }

    private Future<Void> executeRateLimit(String ip, HttpRateLimitList rateLimit, Future<Void> next) {
        return rateLimiterClient.isAllowed(String.format(RedisPrefix.RATE_LIMIT_PREFIX, rateLimit.getMethod(), rateLimit.getPath(), ip),
                        rateLimit.getRateLimitCount(), rateLimit.getIntervalSec(), TimeUnit.SECONDS)
                .compose(data -> {
                    if (!data) {
                        return Future.failedFuture(new SecurityException(HttpStatus.HTTP_TOO_MANY_REQUESTS, "请求频率过快"));
                    }
                    return next;
                });
    }

    private Future<Void> filterWhiteList(String ip, HttpMethod method, String path, Future<Void> next) {
        return stintProvider.getWhiteList()
                .flatMap(whiteList -> {
                    if (whiteList != null && !whiteList.isEmpty()) {
                        for (HttpWhiteList white : whiteList) {
                            if (StringsUtil.isBlank(white.getIp()) || ip.equals(white.getIp())) {
                                if (CommonCore.HTTP_METHOD_ALL.equalsIgnoreCase(white.getMethod()) && CommonCore.HTTP_PATH_ALL.equalsIgnoreCase(white.getPath())) {
                                    return Future.succeededFuture(white);
                                } else if (method.name().equalsIgnoreCase(white.getMethod()) && path.equals(white.getPath())) {
                                    return Future.succeededFuture(white);
                                }
                            }
                        }
                    }
                    return Future.succeededFuture(null);
                })
                .compose(whiteList -> {
                    if (whiteList != null) {
                        return Future.succeededFuture();
                    }
                    return next;
                })
                ;
    }

    private Future<Void> checkToken(RoutingContext ctx) {
        String s = ctx.request().headers().get("Authorization");
        if (StringsUtil.isBlank(s)) {
            return Future.failedFuture(new SecurityException(HttpStatus.HTTP_UNAUTHORIZED, "无权限"));
        } else {
            String finalToken = s.replace("Bearer ", "");
            return tokenProvider.verifyToken(finalToken).compose(data -> {
                if (StringsUtil.isNotBlank(data)) {
                    ctx.put(SecurityContext.LOGIN_ID, data);
                    ctx.put(SecurityContext.TOKEN, finalToken);
                    return Future.succeededFuture();
                } else {
                    return Future.failedFuture(new SecurityException(HttpStatus.HTTP_FAILED_DEPENDENCY, "token过期"));
                }
            });
        }
    }

}
