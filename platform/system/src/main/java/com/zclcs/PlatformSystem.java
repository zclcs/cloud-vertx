package com.zclcs;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.Json;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceDiscoveryOptions;
import io.vertx.servicediscovery.types.HttpEndpoint;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static io.vertx.core.Future.await;

/**
 * @author zclcs
 */
public class PlatformSystem extends AbstractVerticle {

    private final Logger log = LoggerFactory.getLogger(PlatformSystem.class);

    private ServiceDiscovery serviceDiscovery;
    private Record record;

    @Override
    public void start() throws Exception {
        serviceDiscovery = ServiceDiscovery.create(vertx, new ServiceDiscoveryOptions().setBackendConfiguration(config()));
        // Customize the configuration
        HttpServer httpServer = vertx.createHttpServer();
        httpServer
                .requestHandler(req -> {
                    List<Record> await = await(serviceDiscovery.getRecords(r -> true));
                    req.response().end("Hello from Platform System + " + Json.encodePrettily(await));
                });
        await(httpServer.listen(6002).timeout(1, TimeUnit.SECONDS));
        record = HttpEndpoint.createRecord("platform-system", "localhost", 6002, "/system");
        await(serviceDiscovery.publish(record));
        log.info("Service published");
    }

    @Override
    public void stop() throws Exception {
        await(serviceDiscovery.unpublish(record.getRegistration()));
        serviceDiscovery.close();
    }
}
