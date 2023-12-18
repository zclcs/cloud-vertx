package com.zclcs;

import io.vertx.core.*;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceDiscoveryOptions;
import io.vertx.servicediscovery.types.HttpEndpoint;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static io.vertx.core.Future.await;

/**
 * @author zclcs
 */
@Slf4j
public class PlatformGatewayVerticle extends AbstractVerticle {

    public static void main(String[] args) {
        Launcher.executeCommand("run", PlatformGatewayVerticle.class.getName());
    }

    @Override
    public void start() throws Exception {
        vertx.deployVerticle(PlatformGateway.class, new DeploymentOptions()
                .setThreadingModel(ThreadingModel.VIRTUAL_THREAD));
    }

}
