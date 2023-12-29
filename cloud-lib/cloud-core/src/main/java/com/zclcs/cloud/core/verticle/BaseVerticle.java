package com.zclcs.cloud.core.verticle;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zclcs
 */
public abstract class BaseVerticle extends AbstractVerticle {

    private final Logger log = LoggerFactory.getLogger(BaseVerticle.class);

    protected JsonObject config;

    @Override
    public void start() throws Exception {
        log.info("nativeTransport {}", vertx.isNativeTransportEnabled());
        ConfigStoreOptions store = new ConfigStoreOptions()
                .setType("env");
        ConfigRetriever retriever = ConfigRetriever.create(vertx,
                new ConfigRetrieverOptions().addStore(store));
        retriever.getConfig().onComplete(ar -> {
            if (ar.succeeded()) {
                config = ar.result();
            } else {

            }
        });
    }
}
