package com.zclcs.common.core.launcher;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Launcher;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;

public class CustomLauncher extends Launcher {

    private static final Logger log = LoggerFactory.getLogger(CustomLauncher.class);

    public static void main(String[] args) {
        new CustomLauncher().dispatch(args);
    }

    @Override
    public void beforeStoppingVertx(Vertx vertx) {
        log.info(" beforeStoppingVertx Called ===========");
    }

    @Override
    public void afterStoppingVertx() {
        log.info(" afterStoppingVertx Called ===========");
    }

    @Override
    public void beforeDeployingVerticle(DeploymentOptions deploymentOptions) {
        log.info(" beforeDeployingVerticle Called ===========");
    }

    @Override
    public void beforeStartingVertx(VertxOptions options) {
        log.info(" beforeStartingVertx Called ===========");
    }


    @Override
    public void afterStartingVertx(Vertx vertx) {
        log.info(" afterStartingVertx Called ===========");
    }

    @Override
    public void handleDeployFailed(Vertx vertx, String mainVerticle, DeploymentOptions deploymentOptions,
                                   Throwable cause) {
        log.info("handleDeployFailed *****************");
        vertx.close();
    }
}
