package com.zclcs;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Launcher;
import io.vertx.core.ThreadingModel;

/**
 * @author zclcs
 */
public class RegisterVerticle extends AbstractVerticle {

    public static void main(String[] args) {
        Launcher.executeCommand("run", RegisterVerticle.class.getName());
    }

    @Override
    public void start() throws Exception {
        vertx.deployVerticle(Register.class, new DeploymentOptions()
                .setThreadingModel(ThreadingModel.VIRTUAL_THREAD));
    }

}