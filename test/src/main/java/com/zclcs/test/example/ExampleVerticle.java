package com.zclcs.test.example;

import io.vertx.core.AbstractVerticle;

/**
 * @author zclcs
 */
public class ExampleVerticle extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        vertx.createHttpServer().requestHandler(req -> {
            req.response().end("hello world");
        }).listen(1200, "0.0.0.0");
    }

}
