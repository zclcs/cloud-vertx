package com.zclcs.cloud.core.verticle;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;

import static io.vertx.core.Future.await;

/**
 * @author zclcs
 */
public abstract class BaseVerticle extends AbstractVerticle {

    protected JsonObject config;

    @Override
    public void start() throws Exception {
        ConfigStoreOptions store = new ConfigStoreOptions()
                .setType("env");
        ConfigRetriever retriever = ConfigRetriever.create(vertx,
                new ConfigRetrieverOptions().addStore(store));
        config = await(retriever.getConfig());
    }
}
