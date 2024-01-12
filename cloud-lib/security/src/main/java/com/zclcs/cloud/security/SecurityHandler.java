package com.zclcs.cloud.security;

import com.zclcs.cloud.core.bean.HttpWhiteList;
import com.zclcs.cloud.lib.web.utils.RoutingContextUtil;
import com.zclcs.common.core.constant.HttpStatus;
import com.zclcs.common.core.utils.StringsUtil;
import com.zclcs.common.security.provider.TokenProvider;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author zhouc
 */
public class SecurityHandler implements Handler<RoutingContext> {

    private final Logger log = LoggerFactory.getLogger(SecurityHandler.class.getName());
    private final List<HttpWhiteList> whiteList;
    private final TokenProvider tokenProvider;

    public SecurityHandler(List<HttpWhiteList> whiteList, TokenProvider tokenProvider) {
        this.whiteList = whiteList;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void handle(RoutingContext ctx) {
        HttpServerRequest request = ctx.request();
        HttpMethod method = request.method();
        String path = request.path();
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
                    RoutingContextUtil.error(ctx, HttpStatus.HTTP_FAILED_DEPENDENCY, "未登录");
                }
            }, e -> {
                log.error("token验证失败", e);
                RoutingContextUtil.error(ctx, HttpStatus.HTTP_UNAUTHORIZED, "无权限");
            });
        }
    }
}
