package com.zclcs;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.Json;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.types.HttpEndpoint;

import java.util.List;

import static io.vertx.core.Future.await;

/**
 * @author zclcs
 */
public class PlatformGateway extends AbstractVerticle {

    private final Logger log = LoggerFactory.getLogger(PlatformGateway.class);

    private ServiceDiscovery serviceDiscovery;
    private Record record;

    @Override
    public void start() throws Exception {
        serviceDiscovery = ServiceDiscovery.create(vertx);
        // Customize the configuration
        HttpServer httpServer = vertx.createHttpServer();
        httpServer
                .requestHandler(req -> {
                    List<Record> await = await(serviceDiscovery.getRecords(r -> r.getType().equals(HttpEndpoint.TYPE)));
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
