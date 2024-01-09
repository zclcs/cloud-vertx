package com.zclcs.platform.system.handler;

import com.zclcs.cloud.lib.web.utils.WebUtil;
import com.zclcs.common.core.security.BCrypt;
import com.zclcs.common.security.provider.TokenProvider;
import com.zclcs.common.web.utils.RoutingContextUtil;
import com.zclcs.platform.system.dao.entity.User;
import com.zclcs.platform.system.dao.vo.UserTokenVo;
import com.zclcs.platform.system.service.UserService;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.openapi.validation.ValidatedRequest;
import org.slf4j.Logger;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

import static io.vertx.ext.web.openapi.router.RouterBuilder.KEY_META_DATA_VALIDATED_REQUEST;

/**
 * @author zclcs
 */
public class LoginHandler implements Handler<RoutingContext> {

    private final Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

    private final UserService userService;

    private final TokenProvider tokenProvider;

    public LoginHandler(UserService userService, TokenProvider tokenProvider) {
        this.userService = userService;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void handle(RoutingContext ctx) {
        // Get the validated request
        ValidatedRequest validatedRequest = ctx.get(KEY_META_DATA_VALIDATED_REQUEST);
        // Get the parameter value
        JsonObject jsonObject = validatedRequest.getBody().getJsonObject();
        String username = jsonObject.getString("username");
        String password = jsonObject.getString("password");
        userService.getUser(username).timeout(1, TimeUnit.SECONDS)
                .andThen(ar -> {
                    User user = ar.result();
                    if (user != null) {
                        if ("0".equals(user.getStatus())) {
                            RoutingContextUtil.error(ctx, WebUtil.msg("用户被禁用"));
                            return;
                        }
                        try {
                            String passwordPlainText = "";
                            boolean isDecodePassword = true;
                            if (isDecodePassword) {
                                Cipher cipher = Cipher.getInstance("AES/CFB/NoPadding");
                                SecretKey keySpec = new SecretKeySpec("123456".getBytes(StandardCharsets.UTF_8), "AES");
                                cipher.init(Cipher.DECRYPT_MODE, keySpec);
                                passwordPlainText = new String(cipher.doFinal(password.getBytes(StandardCharsets.UTF_8)));
                            } else {
                                passwordPlainText = password;
                            }
                            boolean checkPassword = BCrypt.checkPassword(passwordPlainText, user.getPassword());
                            if (checkPassword) {
                                tokenProvider.generateAndStoreToken(user.getUsername(), "PC")
                                        .onComplete(r -> {
                                            RoutingContextUtil.success(ctx, WebUtil.data(new UserTokenVo(r, tokenProvider.getRedisTokenExpire().getSeconds(), user)));
                                        }, e -> {
                                            log.error("message {}", e.getMessage(), e);
                                            RoutingContextUtil.error(ctx, "token生成失败");
                                        });
                            } else {
                                RoutingContextUtil.error(ctx, "用户名或密码错误");
                            }
                        } catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException |
                                 InvalidKeyException | BadPaddingException e) {
                            log.error("message {}", e.getMessage(), e);
                            RoutingContextUtil.error(ctx, "用户名或密码错误");
                        }
                    } else {
                        RoutingContextUtil.error(ctx, "用户名或密码错误");
                    }
                });
    }

}
