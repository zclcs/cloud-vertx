package com.zclcs.platform.system.handler;

import com.zclcs.cloud.core.constant.LoginType;
import com.zclcs.cloud.core.constant.RedisPrefix;
import com.zclcs.cloud.lib.web.utils.RoutingContextUtil;
import com.zclcs.common.core.security.BCrypt;
import com.zclcs.common.core.utils.AESUtil;
import com.zclcs.common.security.provider.TokenProvider;
import com.zclcs.platform.system.dao.vo.LoginVo;
import com.zclcs.platform.system.dao.vo.UserTokenVo;
import com.zclcs.platform.system.service.UserService;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RequestBody;
import io.vertx.ext.web.RoutingContext;
import io.vertx.redis.client.RedisAPI;
import io.vertx.redis.client.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * @author zclcs
 */
public class LoginByUsernameHandler implements Handler<RoutingContext> {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final RedisAPI redis;

    private final JsonObject config;

    private final UserService userService;

    private final TokenProvider tokenProvider;

    private final String decodePasswordKey;

    private final boolean checkVerifyCode;

    private final boolean decodePassword;

    public LoginByUsernameHandler(RedisAPI redis, JsonObject config, UserService userService, TokenProvider tokenProvider) {
        this.redis = redis;
        this.config = config;
        this.userService = userService;
        this.tokenProvider = tokenProvider;
        this.decodePasswordKey = config.getString("login.decode.password.key");
        this.checkVerifyCode = config.getBoolean("login.check.verify.code");
        this.decodePassword = config.getBoolean("login.decode.password");
    }

    @Override
    public void handle(RoutingContext ctx) {
        RequestBody body = ctx.body();
        JsonObject jsonObject = body.asJsonObject();
        String username = jsonObject.getString("username");
        String password = jsonObject.getString("password");
        String code = ctx.request().getParam("code");
        String randomStr = ctx.request().getParam("randomStr");
        String verifyCodeRedisPrefix = String.format(RedisPrefix.VERIFY_CODE_PREFIX, randomStr);
        if (checkVerifyCode) {
            checkVerifyCode(ctx, verifyCodeRedisPrefix, (r -> {
                if (r != null && r.toString().equalsIgnoreCase(code)) {
                    checkUser(ctx, username, password);
                } else {
                    RoutingContextUtil.error(ctx, "验证码错误");
                }
            }));
        } else {
            checkUser(ctx, username, password);
        }
    }

    private void checkVerifyCode(RoutingContext ctx, String key, Handler<Response> handler) {
        redis.get(key)
                .timeout(1, TimeUnit.SECONDS)
                .onComplete(handler, e -> RoutingContextUtil.error(ctx, "无法获取验证码"))
                .eventually(() -> redis.del(Collections.singletonList(key)))
        ;
    }

    private void checkUser(RoutingContext ctx, String username, String password) {
        userService.getUserCache(username)
                .onComplete(user -> {
                    if (user != null) {
                        if ("0".equals(user.getStatus())) {
                            RoutingContextUtil.error(ctx, "用户被禁用");
                            return;
                        }
                        try {
                            String passwordPlainText;
                            if (decodePassword) {
                                passwordPlainText = new String(AESUtil.decryptCFB(Base64.getDecoder().decode(password),
                                        decodePasswordKey.getBytes(StandardCharsets.UTF_8),
                                        decodePasswordKey.getBytes(StandardCharsets.UTF_8))
                                );
                            } else {
                                passwordPlainText = password;
                            }
                            boolean checkPassword = BCrypt.checkPassword(passwordPlainText, user.getPassword());
                            if (checkPassword) {
                                tokenProvider.generateAndStoreToken(user.getUsername(), LoginType.PC)
                                        .onComplete(r -> {
                                            RoutingContextUtil.success(ctx, new UserTokenVo(r, tokenProvider.getRedisTokenExpire().getSeconds(), new LoginVo(user)));
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
                            RoutingContextUtil.error(ctx, "解密失败");
                        }
                    } else {
                        RoutingContextUtil.error(ctx, "用户名或密码错误");
                    }
                }, e -> {
                    RoutingContextUtil.error(ctx, "无法获取用户信息");
                })
        ;
    }

}
