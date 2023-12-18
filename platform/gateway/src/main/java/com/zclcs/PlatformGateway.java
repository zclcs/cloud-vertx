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
public class PlatformGateway extends AbstractVerticle {

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
                    List<Record> await = await(serviceDiscovery.getRecords(r -> true));
                    req.response().end("Hello from Platform Gateway + " + Json.encodePrettily(await));
                });
        await(httpServer.listen(6001));
        record = HttpEndpoint.createRecord("platform-gateway", "localhost", 6001, "/api");
        await(serviceDiscovery.publish(record));
        log.info("Service published");
    }

    @Override
    public void stop() throws Exception {
        await(serviceDiscovery.unpublish(record.getRegistration()));
        serviceDiscovery.close();
    }
}
