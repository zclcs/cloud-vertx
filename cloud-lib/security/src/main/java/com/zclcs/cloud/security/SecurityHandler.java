package com.zclcs.cloud.security;

import com.zclcs.cloud.core.bean.HttpWhiteList;
import com.zclcs.cloud.lib.web.utils.WebUtil;
import com.zclcs.common.core.constant.HttpStatus;
import com.zclcs.common.core.utils.StringsUtil;
import com.zclcs.common.security.provider.TokenProvider;
import com.zclcs.common.web.utils.RoutingContextUtil;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.RoutingContext;

import java.util.List;

/**
 * @author zhouc
 */
public class SecurityHandler implements Handler<RoutingContext> {

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
            if (method.name().equals(method.name()) && path.equals(whiteList.getPath())) {
                ctx.next();
            }
        }
        String s = request.headers().get("token");
        if (StringsUtil.isBlank(s)) {
            RoutingContextUtil.error(ctx, HttpStatus.HTTP_UNAUTHORIZED, WebUtil.msg("未鉴权"));
        } else {
            tokenProvider.verifyToken(s).onComplete((data) -> {
                String result = data.result();
                if (result != null) {
                    ctx.next();
                } else {
                    RoutingContextUtil.error(ctx, HttpStatus.HTTP_FAILED_DEPENDENCY, WebUtil.msg("未鉴权"));
                }
            });
        }
    }
}
