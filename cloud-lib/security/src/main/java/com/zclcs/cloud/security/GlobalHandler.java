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
import io.vertx.core.http.HttpServerRequest;
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
//        HttpServerRequest request = ctx.request();
//        HttpMethod method = request.method();
//        String path = request.path();
//
//        for (HttpBlackList blackList : blackList) {
//            if (CommonCore.HTTP_METHOD_ALL.equalsIgnoreCase(blackList.getMethod()) || method.name().equalsIgnoreCase(blackList.getMethod())) {
//                if (path.equals(blackList.getPath())) {
//                    LocalDateTime limitFrom = blackList.getLimitFrom();
//                    LocalDateTime limitTo = blackList.getLimitTo();
//                    if (limitFrom != null && limitTo != null) {
//                        LocalDateTime now = LocalDateTime.now();
//                        if (!limitFrom.isAfter(now) && !limitTo.isBefore(now)) {
//                            String ip = RoutingContextUtil.getHttpRequestIpAddress(ctx);
//                            if (StringsUtil.isBlank(blackList.getIp()) || ip.equals(blackList.getIp())) {
//                                RoutingContextUtil.error(ctx, HttpStatus.HTTP_FORBIDDEN, "限制访问");
//                            }
//                        }
//                    } else {
//                        String ip = RoutingContextUtil.getHttpRequestIpAddress(ctx);
//                        if (StringsUtil.isBlank(blackList.getIp()) || ip.equals(blackList.getIp())) {
//                            RoutingContextUtil.error(ctx, HttpStatus.HTTP_FORBIDDEN, "限制访问");
//                        }
//                    }
//                }
//            }
//        }
//
//        for (HttpRateLimitList rateLimit : rateLimitList) {
//            if (method.name().equalsIgnoreCase(rateLimit.getMethod()) && path.equals(rateLimit.getPath())) {
//                LocalDateTime limitFrom = rateLimit.getLimitFrom();
//                LocalDateTime limitTo = rateLimit.getLimitTo();
//                LocalDateTime now = LocalDateTime.now();
//                if (!limitFrom.isAfter(now) && !limitTo.isBefore(now)) {
//                    String ip = RoutingContextUtil.getHttpRequestIpAddress(ctx);
//                    String rateLimitKey = String.format(RedisPrefix.RATE_LIMIT_PREFIX, method.name(), path, ip);
//                    rateLimiterClient.isAllowed(rateLimitKey, rateLimit.getRateLimitCount(), rateLimit.getIntervalSec(), TimeUnit.SECONDS).onComplete((data) -> {
//                        if (!data.succeeded() || !data.result()) {
//                            RoutingContextUtil.error(ctx, HttpStatus.HTTP_TOO_MANY_REQUESTS, "请求频率过快");
//                        }
//                    });
//                }
//            }
//        }
//
//        for (HttpWhiteList whiteList : whiteList) {
//            if (method.name().equalsIgnoreCase(whiteList.getMethod()) && path.equals(whiteList.getPath())) {
//                ctx.next();
//                return;
//            }
//        }
//
//        String s = request.headers().get("Authorization");
//        if (StringsUtil.isBlank(s)) {
//            RoutingContextUtil.error(ctx, HttpStatus.HTTP_UNAUTHORIZED, "无权限");
//        } else {
//            String finalToken = s.replace("Bearer ", "");
//            tokenProvider.verifyToken(finalToken).onComplete((data) -> {
//                if (data != null) {
//                    ctx.put(SecurityContext.LOGIN_ID, data);
//                    ctx.put(SecurityContext.TOKEN, finalToken);
//                    ctx.next();
//                } else {
//                    RoutingContextUtil.error(ctx, HttpStatus.HTTP_FAILED_DEPENDENCY, "token过期");
//                }
//            }, e -> {
//                log.error("token验证失败", e);
//                RoutingContextUtil.error(ctx, HttpStatus.HTTP_FAILED_DEPENDENCY, "未登录");
//            });
//        }
        check(ctx).onComplete(r -> {
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

    private Future<Void> check(RoutingContext ctx) {
        HttpServerRequest request = ctx.request();
        HttpMethod method = request.method();
        String path = request.path();
        String ip = RoutingContextUtil.getHttpRequestIpAddress(ctx);
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
                                    return stintProvider.getRateLimitList(method.name(), path)
                                            .compose(rateLimit -> {
                                                if (rateLimit != null) {
                                                    LocalTime limitFrom = rateLimit.getLimitFrom();
                                                    LocalTime limitTo = rateLimit.getLimitTo();
                                                    if (limitFrom != null && limitTo != null) {
                                                        LocalTime now = LocalDateTime.now().toLocalTime();
                                                        if (!limitFrom.isAfter(now) && !limitTo.isBefore(now)) {
                                                            return rateLimiterClient.isAllowed(String.format(RedisPrefix.RATE_LIMIT_PREFIX, method.name(), path, ip), rateLimit.getRateLimitCount(), rateLimit.getIntervalSec(), TimeUnit.SECONDS).compose(data -> {
                                                                if (data) {
                                                                    return Future.failedFuture(new SecurityException(HttpStatus.HTTP_TOO_MANY_REQUESTS, "请求频率过快"));
                                                                }
                                                                return Future.succeededFuture();
                                                            });
                                                        }
                                                    }
                                                }
                                                return stintProvider.getWhiteList(method.name(), path)
                                                        .compose(whiteList -> {
                                                            if (whiteList != null) {
                                                                return Future.succeededFuture();
                                                            } else {
                                                                String s = request.headers().get("Authorization");
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
                                                        })
                                                        ;
                                            });
                                });
                    }
                })
                ;
    }

}
