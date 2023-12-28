package com.zclcs.platform;

import com.zclcs.platform.system.PlatformSystemVerticle;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Launcher;
import io.vertx.core.ThreadingModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zclcs
 */
public class PlatformApplication extends AbstractVerticle {

    private final Logger log = LoggerFactory.getLogger(PlatformApplication.class);

    public static void main(String[] args) {
        Launcher.executeCommand("run", PlatformApplication.class.getName());
    }

    @Override
    public void start() throws Exception {
        vertx.deployVerticle(PlatformSystemVerticle.class, new DeploymentOptions()
                        .setThreadingModel(ThreadingModel.VIRTUAL_THREAD)).onSuccess(s -> {
                    log.info("PlatformSystem deploy success deployId {}", s);
                })
                .onFailure(event -> {
                    log.error("PlatformSystem deploy failure", event);
                })
        ;
    }
}
