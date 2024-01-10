package com.zclcs.platform.system.handler;

import com.zclcs.cloud.core.constant.RedisPrefix;
import com.zclcs.cloud.lib.web.utils.WebUtil;
import com.zclcs.common.core.security.BCrypt;
import com.zclcs.common.core.utils.AESUtil;
import com.zclcs.common.security.provider.TokenProvider;
import com.zclcs.common.web.utils.RoutingContextUtil;
import com.zclcs.platform.system.dao.cache.UserCacheVo;
import com.zclcs.platform.system.dao.vo.LoginVo;
import com.zclcs.platform.system.dao.vo.UserTokenVo;
import com.zclcs.platform.system.service.UserService;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.openapi.validation.RequestParameter;
import io.vertx.openapi.validation.ValidatedRequest;
import io.vertx.redis.client.RedisAPI;
import org.slf4j.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static io.vertx.ext.web.openapi.router.RouterBuilder.KEY_META_DATA_VALIDATED_REQUEST;

/**
 * @author zclcs
 */
public class LoginHandler implements Handler<RoutingContext> {

    private final Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

    private final RedisAPI redis;

    private final JsonObject config;

    private final UserService userService;

    private final TokenProvider tokenProvider;

    public LoginHandler(RedisAPI redis, JsonObject config, UserService userService, TokenProvider tokenProvider) {
        this.redis = redis;
        this.config = config;
        this.userService = userService;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void handle(RoutingContext ctx) {
        ValidatedRequest validatedRequest = ctx.get(KEY_META_DATA_VALIDATED_REQUEST);
        Map<String, RequestParameter> query = validatedRequest.getQuery();
        JsonObject jsonObject = validatedRequest.getBody().getJsonObject();
        String username = jsonObject.getString("username");
        String password = jsonObject.getString("password");
        String code = query.get("code").getString();
        String randomStr = query.get("randomStr").getString();
        String verifyCodeRedisPrefix = String.format(RedisPrefix.VERIFY_CODE_PREFIX, randomStr);
        redis.get(verifyCodeRedisPrefix)
                .andThen(redisAr -> {
                    if (redisAr.succeeded() && redisAr.result().toString().equalsIgnoreCase(code)) {
                        redis.del(Collections.singletonList(verifyCodeRedisPrefix));
                        userService.getUserCache(username).timeout(1, TimeUnit.SECONDS)
                                .andThen(ar -> {
                                    UserCacheVo user = ar.result();
                                    if (ar.succeeded() && user != null) {
                                        if ("0".equals(user.getStatus())) {
                                            RoutingContextUtil.error(ctx, WebUtil.msg("用户被禁用"));
                                            return;
                                        }
                                        try {
                                            String passwordPlainText = "";
                                            boolean isDecodePassword = config.getBoolean("decodePassword");
                                            if (isDecodePassword) {
                                                String key = config.getString("passwordKey");
                                                passwordPlainText = new String(AESUtil.decryptCFB(Base64.getDecoder().decode(password),
                                                        key.getBytes(StandardCharsets.UTF_8),
                                                        key.getBytes(StandardCharsets.UTF_8))
                                                );
                                            } else {
                                                passwordPlainText = password;
                                            }
                                            boolean checkPassword = BCrypt.checkPassword(passwordPlainText, user.getPassword());
                                            if (checkPassword) {
                                                tokenProvider.generateAndStoreToken(user.getUsername(), "PC")
                                                        .onComplete(r -> {
                                                            RoutingContextUtil.success(ctx, WebUtil.data(new UserTokenVo(r, tokenProvider.getRedisTokenExpire().getSeconds(), new LoginVo(user))));
                                                        }, e -> {
                                                            log.error("message {}", e.getMessage(), e);
                                                            RoutingContextUtil.error(ctx, "token生成失败");
                                                        });
                                            } else {
                                                RoutingContextUtil.error(ctx, "用户名或密码错误");
                                            }
                                        } catch (NoSuchAlgorithmException | NoSuchPaddingException |
                                                 IllegalBlockSizeException |
                                                 InvalidKeyException | BadPaddingException |
                                                 InvalidAlgorithmParameterException e) {
                                            log.error("message {}", e.getMessage(), e);
                                            RoutingContextUtil.error(ctx, "解密失败");
                                        }
                                    } else {
                                        RoutingContextUtil.error(ctx, "用户名或密码错误");
                                    }
                                });
                    } else {
                        RoutingContextUtil.error(ctx, WebUtil.msg("验证码错误"));
                    }
                });
    }

}
