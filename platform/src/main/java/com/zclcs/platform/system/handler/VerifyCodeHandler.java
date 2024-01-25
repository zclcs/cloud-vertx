package com.zclcs.platform.system.handler;

import com.zclcs.cloud.core.constant.RedisPrefix;
import com.zclcs.cloud.lib.web.utils.RoutingContextUtil;
import com.zclcs.verify.code.starter.SpecVerifyCode;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.validation.RequestParameters;
import io.vertx.ext.web.validation.ValidationHandler;
import io.vertx.redis.client.RedisAPI;
import org.slf4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Random;

/**
 * @author zclcs
 */
public class VerifyCodeHandler implements Handler<RoutingContext> {

    private final Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

    private final RedisAPI redis;

    public VerifyCodeHandler(RedisAPI redis) {
        this.redis = redis;
    }

    @Override
    public void handle(RoutingContext ctx) {
        RequestParameters parameters = ctx.get(ValidationHandler.REQUEST_CONTEXT_KEY);
        String randomStr = parameters.queryParameter("randomStr").getString();
        SpecVerifyCode specVerifyCode = new SpecVerifyCode(120, 40, 4);
        String code = specVerifyCode.text();
        String expireTime = String.valueOf(120 + new Random().nextLong(10) + 1L);
        redis.set(List.of(String.format(RedisPrefix.VERIFY_CODE_PREFIX, randomStr), code, "EX", expireTime)).onComplete(ar -> {
            try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
                specVerifyCode.out(output);
                Buffer buffer = Buffer.buffer(output.toByteArray());
                ctx.response()
                        .putHeader("Content-Type", "image/png")
                        .putHeader("Content-Length", String.valueOf(buffer.length()))
                        .write(buffer).onComplete(arr -> {
                            if (arr.succeeded()) {
                                ctx.response().end();
                            } else {
                                RoutingContextUtil.error(ctx, "验证码生成失败");
                            }
                        });
            } catch (IOException e) {
                RoutingContextUtil.error(ctx, "验证码生成失败");
            }
        }, e -> {
            log.error("验证码生成失败", e);
            RoutingContextUtil.error(ctx, "验证码生成失败");
        });

    }

}
