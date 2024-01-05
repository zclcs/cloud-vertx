package com.zclcs.common.discovery.starter;

import com.zclcs.common.core.utils.StringsUtil;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceDiscoveryOptions;
import io.vertx.servicediscovery.types.EventBusService;
import io.vertx.servicediscovery.types.HttpEndpoint;
import io.vertx.servicediscovery.types.JDBCDataSource;
import io.vertx.servicediscovery.types.MessageSource;

/**
 * This verticle provides support for various microservice functionality
 * like service discovery, circuit breaker and simple log publisher.
 *
 * @author Eric Zhao
 */
public class DiscoveryStarter {

    private final Vertx vertx;

    private final JsonObject env;
    private final JsonObject config;
    private ServiceDiscovery discovery;

    public DiscoveryStarter(Vertx vertx, JsonObject env) {
        this.vertx = vertx;
        this.env = env;
        this.config = vertx.getOrCreateContext().config();
    }


    public void startDiscovery() {
        env.mergeIn(config);
        env.put("backend-name", StringsUtil.chooseOneIsNotBlank(env.getString("DISCOVERY_BACKEND_NAME"), config.getString("discoveryBackendName"), "com.zclcs.cloud.lib.discovery.backend.RedisDiscoveryBackendService"));
        discovery = ServiceDiscovery
                .create(vertx, new ServiceDiscoveryOptions()
                        .setBackendConfiguration(env)
                );
    }

    public Future<Record> publishHttpEndpoint(String name, String host, int port, String forward) {
        Record record = HttpEndpoint.createRecord(name, host, port, "/",
                new JsonObject().put("api.name", forward)
        );
        return discovery.publish(record);
    }

    public Future<Record> publishMessageSource(String name, String address) {
        Record record = MessageSource.createRecord(name, address);
        return discovery.publish(record);
    }

    public Future<Record> publishJDBCDataSource(String name, JsonObject location) {
        Record record = JDBCDataSource.createRecord(name, location, new JsonObject());
        return discovery.publish(record);
    }

    public Future<Record> publishEventBusService(String name, String address, String serviceClassName) {
        Record record = EventBusService.createRecord(name, address, serviceClassName);
        return discovery.publish(record);
    }

    public ServiceDiscovery getDiscovery() {
        return discovery;
    }
}
