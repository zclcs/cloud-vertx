package com.zclcs.cloud.security;

import com.zclcs.cloud.core.bean.HttpBlackList;
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
    //    private final List<HttpWhiteList> whiteList;
//    private final List<HttpRateLimitList> rateLimitList;
//    private final List<HttpBlackList> blackList;
    private final TokenProvider tokenProvider;
    private final RateLimiterClient rateLimiterClient;
    private final StintProvider stintProvider;

    public GlobalHandler(TokenProvider tokenProvider, RateLimiterClient rateLimiterClient, StintProvider stintProvider) {
//        this.whiteList = whiteList;
//        this.rateLimitList = rateLimitList;
//        this.blackList = blackList;
        this.tokenProvider = tokenProvider;
        this.rateLimiterClient = rateLimiterClient;
        this.stintProvider = stintProvider;
    }

    @Override
    public void handle(RoutingContext ctx) {
        doFilter(ctx).onComplete(r -> {
            ctx.next();
        }, e -> {
            log.error("GlobalHandler", e);
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
                        filterWhiteList(method, path, checkToken(ctx))
                )
        );
    }

    private Future<Void> filterBlackList(String ip, HttpMethod method, String path, Future<Void> next) {
        return stintProvider.getBlackList(ip, method.name(), path)
                .compose(blackListByIp -> {
                    if (blackListByIp != null) {
                        return Future.failedFuture(new SecurityException(HttpStatus.HTTP_FORBIDDEN, "限制访问"));
                    } else {
                        return stintProvider.getBlackList(method.name(), path)
                                .compose(blackList -> {
                                    for (HttpBlackList black : blackList) {
                                        LocalTime limitFrom = black.getLimitFrom();
                                        LocalTime limitTo = black.getLimitTo();
                                        if (limitFrom != null && limitTo != null) {
                                            LocalTime now = LocalDateTime.now().toLocalTime();
                                            if (!limitFrom.isAfter(now) && !limitTo.isBefore(now)) {
                                                return Future.failedFuture(new SecurityException(HttpStatus.HTTP_FORBIDDEN, "限制访问"));
                                            }
                                        } else {
                                            return Future.failedFuture(new SecurityException(HttpStatus.HTTP_FORBIDDEN, "限制访问"));
                                        }
                                    }
                                    return next;
                                });
                    }
                });
    }

    private Future<Void> filterRateLimit(String ip, HttpMethod method, String path, Future<Void> next) {
        return stintProvider.getRateLimitList(method.name(), path)
                .compose(rateLimit -> {
                    if (rateLimit != null) {
                        LocalTime limitFrom = rateLimit.getLimitFrom();
                        LocalTime limitTo = rateLimit.getLimitTo();
                        if (limitFrom != null && limitTo != null) {
                            LocalTime now = LocalDateTime.now().toLocalTime();
                            if (!limitFrom.isAfter(now) && !limitTo.isBefore(now)) {
                                return rateLimiterClient.isAllowed(String.format(RedisPrefix.RATE_LIMIT_PREFIX, method.name(), path, ip), rateLimit.getRateLimitCount(), rateLimit.getIntervalSec(), TimeUnit.SECONDS).compose(data -> {
                                    if (!data) {
                                        return Future.failedFuture(new SecurityException(HttpStatus.HTTP_TOO_MANY_REQUESTS, "请求频率过快"));
                                    }
                                    return next;
                                });
                            }
                        }
                    }
                    return next;
                });
    }

    private Future<Void> filterWhiteList(HttpMethod method, String path, Future<Void> next) {
        return stintProvider.getWhiteList(method.name(), path)
                .compose(whiteList -> {
                    if (whiteList != null) {
                        return Future.succeededFuture();
                    } else {
                        return next;
                    }
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
                if (data != null) {
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
