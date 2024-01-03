package com.zclcs.platform;

import com.zclcs.platform.system.PlatformSystemVerticle;
import io.vertx.core.AbstractVerticle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zclcs
 */
public class PlatformApplication extends AbstractVerticle {

    private static final Logger log = LoggerFactory.getLogger(PlatformApplication.class);

    @Override
    public void start() throws Exception {
        vertx.deployVerticle(PlatformSystemVerticle.class.getName())
                .onSuccess(s -> log.info("PlatformSystem deploy success deployId {}", s));
    }
}
