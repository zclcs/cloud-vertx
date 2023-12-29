package com.zclcs.platform;

import com.zclcs.platform.system.PlatformSystemVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zclcs
 */
public class PlatformApplication {

    private static final Logger log = LoggerFactory.getLogger(PlatformApplication.class);

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx(new VertxOptions().setPreferNativeTransport(true));
        try {
            vertx.deployVerticle(PlatformSystemVerticle.class, new DeploymentOptions()).onSuccess(s -> {
                log.info("PlatformSystem deploy success deployId {}", s);
            });
        } catch (Exception e) {
            log.error("PlatformSystem deploy error", e);
        }
    }
}
