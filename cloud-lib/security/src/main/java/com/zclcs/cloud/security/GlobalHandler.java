package com.zclcs.cloud.security;

import com.zclcs.cloud.core.bean.HttpRateLimitList;
import com.zclcs.cloud.core.bean.HttpWhiteList;
import com.zclcs.cloud.core.constant.RedisPrefix;
import com.zclcs.cloud.lib.web.utils.RoutingContextUtil;
import com.zclcs.common.core.constant.DatePattern;
import com.zclcs.common.core.constant.HttpStatus;
import com.zclcs.common.core.utils.StringsUtil;
import com.zclcs.common.redis.starter.rate.limit.RateLimiterClient;
import com.zclcs.common.security.provider.TokenProvider;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author zhouc
 */
public class GlobalHandler implements Handler<RoutingContext> {

    private final Logger log = LoggerFactory.getLogger(GlobalHandler.class.getName());
    private final List<HttpWhiteList> whiteList;
    private final List<HttpRateLimitList> rateLimitList;
    private final TokenProvider tokenProvider;
    private final RateLimiterClient rateLimiterClient;

    public GlobalHandler(List<HttpWhiteList> whiteList, List<HttpRateLimitList> rateLimitList, TokenProvider tokenProvider, RateLimiterClient rateLimiterClient) {
        this.whiteList = whiteList;
        this.rateLimitList = rateLimitList;
        this.tokenProvider = tokenProvider;
        this.rateLimiterClient = rateLimiterClient;
    }

    @Override
    public void handle(RoutingContext ctx) {
        HttpServerRequest request = ctx.request();
        HttpMethod method = request.method();
        String path = request.path();

        for (HttpRateLimitList rateLimit : rateLimitList) {
            if (method.name().equalsIgnoreCase(method.name()) && path.equals(rateLimit.getPath())) {
                LocalDateTime limitFrom = LocalDateTime.parse(rateLimit.getLimitFrom(), DatePattern.TIME_FORMATTER);
                LocalDateTime limitTo = LocalDateTime.parse(rateLimit.getLimitTo(), DatePattern.TIME_FORMATTER);
                LocalDateTime now = LocalDateTime.now();
                if (!limitFrom.isAfter(now) && !limitTo.isBefore(now)) {
                    String ip = RoutingContextUtil.getHttpRequestIpAddress(ctx);
                    String rateLimitKey = String.format(RedisPrefix.RATE_LIMIT_PREFIX, method.name(), path, ip);
                    rateLimiterClient.isAllowed(rateLimitKey, rateLimit.getRateLimitCount(), rateLimit.getIntervalSec(), TimeUnit.SECONDS).onComplete((data) -> {
                        if (!data.succeeded() || !data.result()) {
                            RoutingContextUtil.error(ctx, HttpStatus.HTTP_TOO_MANY_REQUESTS, "请求频率过快");
                        }
                    });
                }
            }
        }
        for (HttpWhiteList whiteList : whiteList) {
            if (method.name().equalsIgnoreCase(method.name()) && path.equals(whiteList.getPath())) {
                ctx.next();
                return;
            }
        }
        String s = request.headers().get("Authorization");
        if (StringsUtil.isBlank(s)) {
            RoutingContextUtil.error(ctx, HttpStatus.HTTP_UNAUTHORIZED, "无权限");
        } else {
            String finalToken = s.replace("Bearer ", "");
            tokenProvider.verifyToken(finalToken).onComplete((data) -> {
                if (data != null) {
                    ctx.put(SecurityContext.LOGIN_ID, data);
                    ctx.put(SecurityContext.TOKEN, finalToken);
                    ctx.next();
                } else {
                    RoutingContextUtil.error(ctx, HttpStatus.HTTP_FAILED_DEPENDENCY, "token过期");
                }
            }, e -> {
                log.error("token验证失败", e);
                RoutingContextUtil.error(ctx, HttpStatus.HTTP_FAILED_DEPENDENCY, "未登录");
            });
        }
    }
}
