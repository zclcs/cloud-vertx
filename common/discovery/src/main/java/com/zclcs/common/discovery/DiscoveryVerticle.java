package com.zclcs.common.discovery;

import io.vertx.circuitbreaker.CircuitBreaker;
import io.vertx.circuitbreaker.CircuitBreakerOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.impl.ConcurrentHashSet;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceDiscoveryOptions;
import io.vertx.servicediscovery.types.HttpEndpoint;

import java.util.Set;

import static io.vertx.core.Future.await;

public abstract class DiscoveryVerticle extends AbstractVerticle {

    private final Logger log = LoggerFactory.getLogger(DiscoveryVerticle.class);

    protected ServiceDiscovery serviceDiscovery;
    protected CircuitBreaker circuitBreaker;
    protected Set<Record> registeredRecords = new ConcurrentHashSet<>();

    @Override
    public void start() throws Exception {
        JsonObject config = config();
        config.put("backend-name", CloudBackendService.class.getName());
        serviceDiscovery = ServiceDiscovery.create(vertx, new ServiceDiscoveryOptions().setBackendConfiguration(config));
        String circuitBreakerName = config.getString("CIRCUIT_BREAKER_NAME", "my-circuit-breaker");
        Integer circuitBreakerMaxFailures = config.getInteger("CIRCUIT_BREAKER_MAX_FAILURES", 5);
        Long circuitBreakerTimeout = config.getLong("CIRCUIT_BREAKER_TIMEOUT", 10000L);
        Long circuitBreakerResetTimeout = config.getLong("CIRCUIT_BREAKER_RESET_TIMEOUT", 30000L);
        circuitBreaker = CircuitBreaker.create(circuitBreakerName, vertx,
                new CircuitBreakerOptions()
                        .setMaxFailures(circuitBreakerMaxFailures)
                        .setTimeout(circuitBreakerTimeout)
                        .setFallbackOnFailure(true)
                        .setResetTimeout(circuitBreakerResetTimeout)
        );
    }

    @Override
    public void stop() throws Exception {
        if (!registeredRecords.isEmpty()) {
            for (Record registeredRecord : registeredRecords) {
                await(serviceDiscovery.unpublish(registeredRecord.getRegistration()));
            }
        }
        serviceDiscovery.close();
    }

    protected void publishHttpEndpoint(String name, String host, int port, String apiName) {
        Record record = HttpEndpoint.createRecord(name, host, port, "/",
                new JsonObject().put("api.name", config().getString("api.name", apiName))
        );
        publish(record);
    }

    /**
     * Publish a service with record.
     *
     * @param record service record
     * @return async result
     */
    private void publish(Record record) {
        if (serviceDiscovery == null) {
            try {
                start();
            } catch (Exception e) {
                throw new IllegalStateException("Cannot create discovery service");
            }
        }

        record = await(serviceDiscovery.publish(record));
        registeredRecords.add(record);
    }
}
