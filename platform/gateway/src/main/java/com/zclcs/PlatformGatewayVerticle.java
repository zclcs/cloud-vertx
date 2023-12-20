package com.zclcs;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Launcher;
import io.vertx.core.ThreadingModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zclcs
 */
public class PlatformGatewayVerticle extends AbstractVerticle {

    private final Logger log = LoggerFactory.getLogger(PlatformGatewayVerticle.class);

    public static void main(String[] args) {
        Launcher.executeCommand("run", PlatformGatewayVerticle.class.getName());
    }

    @Override
    public void start() throws Exception {
        vertx.deployVerticle(PlatformGateway.class, new DeploymentOptions()
                        .setThreadingModel(ThreadingModel.VIRTUAL_THREAD)).onSuccess(s -> {
                    log.info("PlatformGateway deploy success deployId {}", s);
                })
                .onFailure(event -> {
                    log.error("PlatformGateway deploy failure", event);
                });
    }

}
