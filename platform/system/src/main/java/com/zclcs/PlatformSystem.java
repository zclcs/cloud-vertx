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

import static io.vertx.core.Future.await;

/**
 * @author zclcs
 */
@Slf4j
public class PlatformSystem extends AbstractVerticle {

    public static void main(String[] args) {
        var vertx = Vertx.vertx();
        try {
            vertx.deployVerticle(PlatformSystem.class, new DeploymentOptions()
                            .setThreadingModel(ThreadingModel.VIRTUAL_THREAD))
                    .toCompletionStage()
                    .toCompletableFuture()
                    .get();
        } catch (Throwable e) {
            log.error("start up error", e);
        }
    }

    private ServiceDiscovery serviceDiscovery;
    private Record record;

    @Override
    public void start() throws Exception {
        serviceDiscovery = ServiceDiscovery.create(vertx, new ServiceDiscoveryOptions()
                .setBackendConfiguration(
                        new JsonObject()
                                .put("connectionString", "redis://:123456@192.168.33.10:6379")
                                .put("key", "records")
                ));
        // Customize the configuration
        HttpServer httpServer = vertx.createHttpServer();
        httpServer
                .requestHandler(req -> {
                    Record await = await(serviceDiscovery.getRecord(r -> true));
                    req.response().end("Hello from Platform System + " + Json.encodePrettily(await));
                });
        await(httpServer.listen(6002));
        record = HttpEndpoint.createRecord("platform-system", "localhost", 6002, "/system");
        await(serviceDiscovery.publish(record));
        log.info("Service published");
    }

    @Override
    public void stop(Promise<Void> stopPromise) throws Exception {
        log.info("Service published");
        await(serviceDiscovery.unpublish(record.getRegistration()));
        serviceDiscovery.close();
        super.stop(stopPromise);
    }
}
