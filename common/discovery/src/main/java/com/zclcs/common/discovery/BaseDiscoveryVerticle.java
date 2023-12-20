package com.zclcs.common.discovery;

import io.vertx.circuitbreaker.CircuitBreaker;
import io.vertx.circuitbreaker.CircuitBreakerOptions;
import io.vertx.core.*;
import io.vertx.core.impl.ConcurrentHashSet;
import io.vertx.core.impl.VertxInternal;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceDiscoveryOptions;
import io.vertx.servicediscovery.types.EventBusService;
import io.vertx.servicediscovery.types.HttpEndpoint;
import io.vertx.servicediscovery.types.JDBCDataSource;
import io.vertx.servicediscovery.types.MessageSource;

import java.util.Set;

import static io.vertx.core.Future.await;


/**
 * This verticle provides support for various microservice functionality
 * like service discovery, circuit breaker and simple log publisher.
 *
 * @author Eric Zhao
 */
public abstract class BaseDiscoveryVerticle extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(BaseDiscoveryVerticle.class);

    protected ServiceDiscovery discovery;
    protected CircuitBreaker circuitBreaker;
    protected Set<Record> registeredRecords = new ConcurrentHashSet<>();
    protected VertxInternal vertxInternal;

    @Override
    public void start() throws Exception {
        vertxInternal = (VertxInternal) vertx;
        // init service discovery instance
        JsonObject config = config();
        discovery = ServiceDiscovery.create(vertx, new ServiceDiscoveryOptions().setBackendConfiguration(new JsonObject().put("backend-name", config.getString("DISCOVERY_BACKEND_NAME", CloudBackendService.class.getName()))));

        // init circuit breaker instance
        String circuitBreakerName = config.getString("CIRCUIT_BREAKER_NAME", "circuit-breaker");
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

    protected Future<Record> publishHttpEndpoint(String name, String host, int port) {
        Promise<Record> promise = vertxInternal.promise();
        Record record = HttpEndpoint.createRecord(name, host, port, "/",
                new JsonObject().put("api.name", config().getString("api.name", ""))
        );
        return publish(record, promise);
    }

    protected Future<Record> publishMessageSource(String name, String address) {
        Promise<Record> promise = vertxInternal.promise();
        Record record = MessageSource.createRecord(name, address);
        return publish(record, promise);
    }

    protected Future<Record> publishJDBCDataSource(String name, JsonObject location) {
        Promise<Record> promise = vertxInternal.promise();
        Record record = JDBCDataSource.createRecord(name, location, new JsonObject());
        return publish(record, promise);
    }

    protected Future<Record> publishEventBusService(String name, String address, String serviceClassName) {
        Promise<Record> promise = vertxInternal.promise();
        Record record = EventBusService.createRecord(name, address, serviceClassName);
        return publish(record, promise);
    }

    /**
     * Publish a service with record.
     *
     * @param record service record
     * @return async result
     */
    private Future<Record> publish(Record record, Handler<AsyncResult<Record>> handler) {
        if (discovery == null) {
            try {
                start();
            } catch (Exception e) {
                throw new IllegalStateException("Cannot create discovery service");
            }
        }
        // publish the service
        return discovery.publish(record).onComplete(handler);
    }

    @Override
    public void stop() throws Exception {
        for (Record registeredRecord : registeredRecords) {
            await(discovery.unpublish(registeredRecord.getRegistration()));
        }
        discovery.close();
    }
}
